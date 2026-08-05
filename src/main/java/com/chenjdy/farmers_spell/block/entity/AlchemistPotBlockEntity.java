package com.chenjdy.farmers_spell.block.entity;

import com.google.common.collect.Lists;
import com.chenjdy.farmers_spell.init.ModTriggers;
import com.chenjdy.farmers_spell.init.ModBlockEntities;
import com.chenjdy.farmers_spell.init.ModRecipeTypes;
import com.chenjdy.farmers_spell.recipe.AlchemistCookingRecipe;
import io.redspace.ironsspellbooks.api.item.IScroll;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.mixin.accessor.RecipeManagerAccessor;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Map.entry;

public class AlchemistPotBlockEntity extends BlockEntity implements MenuProvider, HeatableBlockEntity, Nameable, RecipeHolder, Clearable
{
    public static final int MEAL_DISPLAY_SLOT = 6;
    public static final int CONTAINER_SLOT = 7;
    public static final int OUTPUT_SLOT = 8;
    public static final int SCROLL_SLOT = 9;
    public static final int INVENTORY_SIZE = SCROLL_SLOT + 1;

    public static final Map<Item, Item> INGREDIENT_REMAINDER_OVERRIDES = Map.ofEntries(
            entry(Items.POWDER_SNOW_BUCKET, Items.BUCKET),
            entry(Items.AXOLOTL_BUCKET, Items.BUCKET),
            entry(Items.COD_BUCKET, Items.BUCKET),
            entry(Items.PUFFERFISH_BUCKET, Items.BUCKET),
            entry(Items.SALMON_BUCKET, Items.BUCKET),
            entry(Items.TROPICAL_FISH_BUCKET, Items.BUCKET),
            entry(Items.SUSPICIOUS_STEW, Items.BOWL),
            entry(Items.MUSHROOM_STEW, Items.BOWL),
            entry(Items.RABBIT_STEW, Items.BOWL),
            entry(Items.BEETROOT_SOUP, Items.BOWL),
            entry(Items.POTION, Items.GLASS_BOTTLE),
            entry(Items.SPLASH_POTION, Items.GLASS_BOTTLE),
            entry(Items.LINGERING_POTION, Items.GLASS_BOTTLE),
            entry(Items.EXPERIENCE_BOTTLE, Items.GLASS_BOTTLE),
            entry(com.chenjdy.farmers_spell.init.ModItems.ORIGINAL_NECTAR.get(), Items.GLASS_BOTTLE)
    );

    private final ItemStackHandler inventory;
    private final ItemStackHandler scrollHandler;
    private final LazyOptional<IItemHandler> inputHandler;
    private final LazyOptional<IItemHandler> outputHandler;

    private int cookTime;
    private int cookTimeTotal;
    private ItemStack mealContainerStack;
    private Component customName;

    protected final ContainerData cookingPotData;
    private final Map<ResourceLocation, Integer> usedRecipeTracker;

    private ResourceLocation lastRecipeID;
    private boolean checkNewRecipe;

    public AlchemistPotBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ALCHEMIST_POT.get(), pos, state);
        this.inventory = createInventoryHandler();
        this.scrollHandler = createScrollHandler();
        this.inputHandler = LazyOptional.of(() -> this.inventory);
        this.outputHandler = LazyOptional.of(() -> this.inventory);
        this.mealContainerStack = ItemStack.EMPTY;
        this.cookingPotData = createIntArray();
        this.usedRecipeTracker = new HashMap<>();
        this.checkNewRecipe = true;
    }

    private ItemStackHandler createInventoryHandler() {
        return new ItemStackHandler(OUTPUT_SLOT + 1) {
            @Override
            protected void onContentsChanged(int slot) {
                if (slot >= 0 && slot < MEAL_DISPLAY_SLOT) {
                    checkNewRecipe = true;
                }
                inventoryChanged();
            }
        };
    }

    private ItemStackHandler createScrollHandler() {
        return new ItemStackHandler(1) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return stack.getItem() instanceof IScroll;
            }

            @Override
            protected void onContentsChanged(int slot) {
                inventoryChanged();
                checkBlazeScrollAdvancement();
            }
        };
    }

    private void checkBlazeScrollAdvancement() {
        if (level == null || level.isClientSide()) return;
        if (!(level instanceof ServerLevel serverLevel)) return;
        
        if (isBlazeScroll()) {
            for (ServerPlayer player : serverLevel.players()) {
                if (player.distanceToSqr(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()) < 64.0D) {
                    ModTriggers.BLAZE_SCROLL_TRIGGER.trigger(player);
                }
            }
        }
    }

    private ContainerData createIntArray() {
        return new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> AlchemistPotBlockEntity.this.cookTime;
                    case 1 -> AlchemistPotBlockEntity.this.cookTimeTotal;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> AlchemistPotBlockEntity.this.cookTime = value;
                    case 1 -> AlchemistPotBlockEntity.this.cookTimeTotal = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public static void cookingTick(Level level, BlockPos pos, BlockState state, AlchemistPotBlockEntity cookingPot) {
        boolean isHeated = cookingPot.isPotHeated();
        boolean hasBlazeScroll = cookingPot.isBlazeScroll();
        boolean canCook = isHeated || hasBlazeScroll;
        boolean didInventoryChange = false;

        if (canCook && cookingPot.hasInput()) {
            RecipeWrapper inventoryWrapper = new RecipeWrapper(cookingPot.inventory);

            Optional<AlchemistCookingRecipe> magicRecipe = cookingPot.getMatchingMagicRecipe(inventoryWrapper);
            if (magicRecipe.isPresent() && cookingPot.canCook(magicRecipe.get())) {
                didInventoryChange = cookingPot.processCooking(magicRecipe.get(), cookingPot);
            } else {
                Optional<CookingPotRecipe> recipe = cookingPot.getMatchingRecipe(inventoryWrapper);
                if (recipe.isPresent() && cookingPot.canCook(recipe.get())) {
                    didInventoryChange = cookingPot.processCooking(recipe.get(), cookingPot);
                } else {
                    cookingPot.cookTime = Mth.clamp(cookingPot.cookTime - 2, 0, cookingPot.cookTimeTotal);
                }
            }
        } else if (cookingPot.cookTime > 0) {
            cookingPot.cookTime = Mth.clamp(cookingPot.cookTime - 2, 0, cookingPot.cookTimeTotal);
        }

        cookingPot.cookingPotData.set(0, cookingPot.cookTime);
        cookingPot.cookingPotData.set(1, cookingPot.cookTimeTotal);

        ItemStack mealStack = cookingPot.getMeal();
        if (!mealStack.isEmpty()) {
            if (!cookingPot.doesMealHaveContainer(mealStack)) {
                cookingPot.moveMealToOutput();
                didInventoryChange = true;
            } else if (!cookingPot.inventory.getStackInSlot(CONTAINER_SLOT).isEmpty()) {
                cookingPot.useStoredContainersOnMeal();
                didInventoryChange = true;
            }
        }

        if (didInventoryChange) {
            cookingPot.inventoryChanged();
        }
    }

    public static void animationTick(Level level, BlockPos pos, BlockState state, AlchemistPotBlockEntity cookingPot) {
        if (cookingPot.isPotHeated()) {
            RandomSource random = level.random;
            if (random.nextFloat() < 0.2F) {
                double x = (double) pos.getX() + 0.5D + (random.nextDouble() * 0.6D - 0.3D);
                double y = (double) pos.getY() + 0.7D;
                double z = (double) pos.getZ() + 0.5D + (random.nextDouble() * 0.6D - 0.3D);
                level.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, 0.0D, 0.0D, 0.0D);
            }
            if (random.nextFloat() < 0.05F) {
                double x = (double) pos.getX() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
                double y = (double) pos.getY() + 0.5D;
                double z = (double) pos.getZ() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
                double motionY = random.nextBoolean() ? 0.015D : 0.005D;
                level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 0.0D, motionY, 0.0D);
            }
        }
    }

    private Optional<CookingPotRecipe> getMatchingRecipe(RecipeWrapper inventoryWrapper) {
        if (level == null) return Optional.empty();

        if (lastRecipeID != null) {
            Recipe<RecipeWrapper> recipe = ((RecipeManagerAccessor) level.getRecipeManager())
                    .getRecipeMap(vectorwing.farmersdelight.common.registry.ModRecipeTypes.COOKING.get())
                    .get(lastRecipeID);
            if (recipe instanceof CookingPotRecipe) {
                if (recipe.matches(inventoryWrapper, level)) {
                    return Optional.of((CookingPotRecipe) recipe);
                }
                if (ItemStack.isSameItem(recipe.getResultItem(this.level.registryAccess()), getMeal())) {
                    return Optional.empty();
                }
            }
        }

        if (checkNewRecipe) {
            Optional<CookingPotRecipe> recipe = level.getRecipeManager().getRecipeFor(
                    vectorwing.farmersdelight.common.registry.ModRecipeTypes.COOKING.get(), inventoryWrapper, level);
            if (recipe.isPresent()) {
                ResourceLocation newRecipeID = recipe.get().getId();
                if (lastRecipeID != null && !lastRecipeID.equals(newRecipeID)) {
                    cookTime = 0;
                }
                lastRecipeID = newRecipeID;
                return recipe;
            }
        }

        checkNewRecipe = false;
        return Optional.empty();
    }

    private Optional<AlchemistCookingRecipe> getMatchingMagicRecipe(RecipeWrapper inventoryWrapper) {
        if (level == null) return Optional.empty();

        Optional<AlchemistCookingRecipe> recipe = level.getRecipeManager().getRecipeFor(
                ModRecipeTypes.ALCHEMIST_COOKING.get(), inventoryWrapper, level);

        if (recipe.isPresent()) {
            AlchemistCookingRecipe magicRecipe = recipe.get();
            SchoolType scrollSchool = getScrollSchool();

            if (magicRecipe.getRequiredSchool() == null) {
                return Optional.of(magicRecipe);
            } else if (scrollSchool != null && magicRecipe.getRequiredSchool().equals(scrollSchool.getId())) {
                return Optional.of(magicRecipe);
            }
        }
        return Optional.empty();
    }

    public ItemStack getContainer() {
        ItemStack mealStack = getMeal();
        if (mealStack.isEmpty() || mealContainerStack.isEmpty()) return mealStack.getCraftingRemainingItem();
        return mealContainerStack;
    }

    private boolean hasInput() {
        for (int i = 0; i < MEAL_DISPLAY_SLOT; ++i) {
            if (!inventory.getStackInSlot(i).isEmpty()) return true;
        }
        return false;
    }

    public boolean canCook(CookingPotRecipe recipe) {
        if (level == null) return false;

        if (hasInput()) {
            ItemStack resultStack = recipe.assemble(new RecipeWrapper(inventory), this.level.registryAccess());
            if (resultStack.isEmpty()) {
                return false;
            } else {
                ItemStack storedMealStack = inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
                if (storedMealStack.isEmpty()) {
                    return true;
                } else if (!ItemStack.isSameItemSameTags(storedMealStack, resultStack)) {
                    return false;
                } else if (storedMealStack.getCount() + resultStack.getCount() <= inventory.getSlotLimit(MEAL_DISPLAY_SLOT)) {
                    return true;
                } else {
                    return storedMealStack.getCount() + resultStack.getCount() <= resultStack.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }

    public boolean canCook(AlchemistCookingRecipe recipe) {
        return canCook((CookingPotRecipe) recipe);
    }

    private boolean processCooking(CookingPotRecipe recipe, AlchemistPotBlockEntity cookingPot) {
        if (cookingPot.level == null) return false;

        float speedMultiplier = cookingPot.isGluttonyScroll() ? 1.5F : 1.0F;
        cookingPot.cookTimeTotal = (int) (recipe.getCookTime() / speedMultiplier);
        cookingPot.cookTime += (int) (1 * speedMultiplier);

        if (cookingPot.cookTime < cookingPot.cookTimeTotal) {
            return false;
        }

        cookingPot.cookTime = 0;
        cookingPot.cookTimeTotal = recipe.getCookTime();
        cookingPot.mealContainerStack = recipe.getOutputContainer();

        cookingPot.usedRecipeTracker.merge(recipe.getId(), 1, Integer::sum);

        ItemStack resultStack = recipe.assemble(new RecipeWrapper(cookingPot.inventory), this.level.registryAccess());
        ItemStack storedMealStack = cookingPot.inventory.getStackInSlot(MEAL_DISPLAY_SLOT);

        if (storedMealStack.isEmpty()) {
            cookingPot.inventory.setStackInSlot(MEAL_DISPLAY_SLOT, resultStack.copy());
        } else if (ItemStack.isSameItemSameTags(storedMealStack, resultStack)) {
            storedMealStack.grow(resultStack.getCount());
        }

        for (int i = 0; i < MEAL_DISPLAY_SLOT; ++i) {
            ItemStack slotStack = cookingPot.inventory.getStackInSlot(i);
            if (slotStack.hasCraftingRemainingItem()) {
                cookingPot.ejectIngredientRemainder(slotStack.getCraftingRemainingItem());
            } else if (INGREDIENT_REMAINDER_OVERRIDES.containsKey(slotStack.getItem())) {
                cookingPot.ejectIngredientRemainder(INGREDIENT_REMAINDER_OVERRIDES.get(slotStack.getItem()).getDefaultInstance());
            }
            if (!slotStack.isEmpty())
                slotStack.shrink(1);
        }

        float experience = recipe.getExperience();
        if (cookingPot.isGluttonyScroll()) {
            experience *= 3.0F;
        }

        cookingPot.splitAndSpawnExperience(experience);
        return true;
    }

    protected void ejectIngredientRemainder(ItemStack remainderStack) {
        Direction direction = getBlockState().getValue(CookingPotBlock.FACING).getCounterClockWise();
        double x = worldPosition.getX() + 0.5 + (direction.getStepX() * 0.25);
        double y = worldPosition.getY() + 0.7;
        double z = worldPosition.getZ() + 0.5 + (direction.getStepZ() * 0.25);
        ItemUtils.spawnItemEntity(level, remainderStack, x, y, z,
                direction.getStepX() * 0.08F, 0.25F, direction.getStepZ() * 0.08F);
    }

    private void splitAndSpawnExperience(float experience) {
        int expTotal = Mth.floor(experience);
        float expFraction = Mth.frac(experience);
        if (expFraction != 0.0F && Math.random() < (double) expFraction) {
            ++expTotal;
        }

        if (expTotal > 0 && level instanceof ServerLevel serverLevel) {
            ExperienceOrb.award(serverLevel, Vec3.atCenterOf(worldPosition), expTotal);
        }
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation recipeID = recipe.getId();
            usedRecipeTracker.merge(recipeID, 1, Integer::sum);
        }
    }

    @Nullable
    @Override
    public Recipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void awardUsedRecipes(Player player, List<ItemStack> items) {
        List<Recipe<?>> usedRecipes = getUsedRecipesAndPopExperience(player.level(), player.position());
        player.awardRecipes(usedRecipes);
        usedRecipeTracker.clear();
    }

    public List<Recipe<?>> getUsedRecipesAndPopExperience(Level level, Vec3 pos) {
        List<Recipe<?>> list = Lists.newArrayList();

        for (Map.Entry<ResourceLocation, Integer> entry : usedRecipeTracker.entrySet()) {
            level.getRecipeManager().byKey(entry.getKey()).ifPresent((recipe) -> {
                list.add(recipe);
                if (level instanceof ServerLevel serverLevel) {
                    float exp = ((CookingPotRecipe) recipe).getExperience() * entry.getValue();
                    splitAndSpawnExperience(exp);
                }
            });
        }

        usedRecipeTracker.clear();
        return list;
    }

    public boolean isPotHeated() {
        if (level == null) return false;
        if (isBlazeScroll()) return true;
        return isHeated(level, worldPosition);
    }

    @Override
    public boolean isHeated(Level level, BlockPos pos) {
        BlockState stateBelow = level.getBlockState(pos.below());

        if (stateBelow.is(ModTags.Blocks.HEAT_SOURCES)) {
            if (stateBelow.hasProperty(BlockStateProperties.LIT))
                return stateBelow.getValue(BlockStateProperties.LIT);
            return true;
        }

        if (!this.requiresDirectHeat() && stateBelow.is(ModTags.Blocks.HEAT_CONDUCTORS)) {
            BlockState stateFurtherBelow = level.getBlockState(pos.below(2));
            if (stateFurtherBelow.is(ModTags.Blocks.HEAT_SOURCES)) {
                if (stateFurtherBelow.hasProperty(BlockStateProperties.LIT))
                    return stateFurtherBelow.getValue(BlockStateProperties.LIT);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean requiresDirectHeat() {
        return false;
    }

    public boolean isHeated() {
        return isPotHeated();
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public ItemStackHandler getScrollHandler() {
        return scrollHandler;
    }

    public ItemStack getScroll() {
        return scrollHandler.getStackInSlot(0);
    }

    public boolean hasScroll() {
        return !scrollHandler.getStackInSlot(0).isEmpty();
    }

    @Nullable
    public SchoolType getScrollSchool() {
        ItemStack scroll = scrollHandler.getStackInSlot(0);
        if (scroll.isEmpty()) return null;

        ISpellContainer spellContainer = ISpellContainer.get(scroll);
        if (spellContainer == null) return null;

        SpellData spellData = spellContainer.getSpellAtIndex(0);
        if (spellData == null) return null;

        return spellData.getSpell().getSchoolType();
    }

    public boolean isBlazeScroll() {
        SchoolType school = getScrollSchool();
        if (school == null) return false;
        ResourceLocation schoolId = school.getId();
        return schoolId.getNamespace().equals("irons_spellbooks") && schoolId.getPath().equals("fire");
    }

    public boolean isGluttonyScroll() {
        SchoolType school = getScrollSchool();
        if (school == null) return false;
        ResourceLocation schoolId = school.getId();
        return schoolId.getNamespace().equals("irons_spellbooks") && schoolId.getPath().equals("holy");
    }

    public ItemStack getMeal() {
        return inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
    }

    public NonNullList<ItemStack> getDroppableInventory() {
        NonNullList<ItemStack> drops = NonNullList.create();
        for (int i = 0; i < OUTPUT_SLOT + 1; ++i) {
            if (i != MEAL_DISPLAY_SLOT) {
                drops.add(inventory.getStackInSlot(i));
            }
        }
        if (hasScroll()) {
            drops.add(scrollHandler.getStackInSlot(0));
        }
        return drops;
    }

    private void moveMealToOutput() {
        ItemStack mealStack = inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
        ItemStack outputStack = inventory.getStackInSlot(OUTPUT_SLOT);
        int mealCount = Math.min(mealStack.getCount(), mealStack.getMaxStackSize() - outputStack.getCount());
        if (outputStack.isEmpty()) {
            inventory.setStackInSlot(OUTPUT_SLOT, mealStack.split(mealCount));
        } else if (ItemStack.isSameItemSameTags(mealStack, outputStack)) {
            mealStack.shrink(mealCount);
            outputStack.grow(mealCount);
        }
    }

    private void useStoredContainersOnMeal() {
        ItemStack mealStack = inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
        ItemStack containerInputStack = inventory.getStackInSlot(CONTAINER_SLOT);
        ItemStack outputStack = inventory.getStackInSlot(OUTPUT_SLOT);

        if (isContainerValid(containerInputStack) && outputStack.getCount() < outputStack.getMaxStackSize()) {
            int smallerStackCount = Math.min(mealStack.getCount(), containerInputStack.getCount());
            int mealCount = Math.min(smallerStackCount, mealStack.getMaxStackSize() - outputStack.getCount());
            if (outputStack.isEmpty()) {
                containerInputStack.shrink(mealCount);
                inventory.setStackInSlot(OUTPUT_SLOT, mealStack.split(mealCount));
            } else if (ItemStack.isSameItemSameTags(outputStack, mealStack)) {
                mealStack.shrink(mealCount);
                containerInputStack.shrink(mealCount);
                outputStack.grow(mealCount);
            }
        }
    }

    public ItemStack useHeldItemOnMeal(ItemStack container) {
        if (isContainerValid(container) && !getMeal().isEmpty()) {
            container.shrink(1);
            inventoryChanged();
            return getMeal().split(1);
        }
        return ItemStack.EMPTY;
    }

    private boolean doesMealHaveContainer(ItemStack meal) {
        return !mealContainerStack.isEmpty() || meal.hasCraftingRemainingItem();
    }

    public boolean isContainerValid(ItemStack containerItem) {
        if (containerItem.isEmpty()) return false;
        if (!mealContainerStack.isEmpty()) return ItemStack.isSameItem(mealContainerStack, containerItem);
        return false;
    }

    @Override
    public Component getName() {
        return customName != null ? customName : Component.translatable("container.farmers_spell.alchemist_pot");
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Override
    @Nullable
    public Component getCustomName() {
        return customName;
    }

    public void setCustomName(Component name) {
        customName = name;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player, Player entity) {
        return new com.chenjdy.farmers_spell.block.entity.container.AlchemistPotMenu(id, player, this, cookingPotData);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap.equals(ForgeCapabilities.ITEM_HANDLER)) {
            if (side == null || side.equals(Direction.UP)) {
                return inputHandler.cast();
            } else {
                return outputHandler.cast();
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        inputHandler.invalidate();
        outputHandler.invalidate();
    }

    @Override
    public CompoundTag getUpdateTag() {
        return writeItems(new CompoundTag());
    }

    private CompoundTag writeItems(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put("Container", mealContainerStack.serializeNBT());
        compound.put("Inventory", inventory.serializeNBT());
        compound.put("Scroll", scrollHandler.serializeNBT());
        return compound;
    }

    public CompoundTag writeMeal(CompoundTag compound) {
        if (getMeal().isEmpty()) return compound;

        ItemStackHandler drops = new ItemStackHandler(OUTPUT_SLOT + 1);
        for (int i = 0; i < OUTPUT_SLOT + 1; ++i) {
            drops.setStackInSlot(i, i == MEAL_DISPLAY_SLOT ? inventory.getStackInSlot(i) : ItemStack.EMPTY);
        }
        if (customName != null) {
            compound.putString("CustomName", Component.Serializer.toJson(customName));
        }
        compound.put("Container", mealContainerStack.serializeNBT());
        compound.put("Inventory", drops.serializeNBT());
        compound.put("Scroll", scrollHandler.serializeNBT());
        return compound;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        inventory.deserializeNBT(compound.getCompound("Inventory"));
        scrollHandler.deserializeNBT(compound.getCompound("Scroll"));
        cookTime = compound.getInt("CookTime");
        cookTimeTotal = compound.getInt("CookTimeTotal");
        mealContainerStack = ItemStack.of(compound.getCompound("Container"));
        if (compound.contains("CustomName", 8)) {
            customName = Component.Serializer.fromJson(compound.getString("CustomName"));
        }
        CompoundTag compoundRecipes = compound.getCompound("RecipesUsed");
        for (String key : compoundRecipes.getAllKeys()) {
            usedRecipeTracker.put(ResourceLocation.parse(key), compoundRecipes.getInt(key));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("CookTime", cookTime);
        compound.putInt("CookTimeTotal", cookTimeTotal);
        compound.put("Container", mealContainerStack.serializeNBT());
        if (customName != null) {
            compound.putString("CustomName", Component.Serializer.toJson(customName));
        }
        compound.put("Inventory", inventory.serializeNBT());
        compound.put("Scroll", scrollHandler.serializeNBT());
        CompoundTag compoundRecipes = new CompoundTag();
        usedRecipeTracker.forEach((recipeId, craftedAmount) -> compoundRecipes.putInt(recipeId.toString(), craftedAmount));
        compound.put("RecipesUsed", compoundRecipes);
    }

    @Override
    public void clearContent() {
        ItemUtils.clearItems(inventory);
        ItemUtils.clearItems(scrollHandler);
    }

    private void inventoryChanged() {
        super.setChanged();
        if (level != null)
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }
}
