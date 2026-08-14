package com.chenjdy.farmers_spell.integration.jei;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.init.ModBlocks;
import com.chenjdy.farmers_spell.recipe.AlchemistCookingRecipe;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AlchemistPotRecipeCategory implements IRecipeCategory<AlchemistCookingRecipe>
{
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "alchemist_cooking");
    public static final RecipeType<AlchemistCookingRecipe> RECIPE_TYPE = RecipeType.create(FARMERSSPELL.MODID, "alchemist_cooking", AlchemistCookingRecipe.class);
    private static final ResourceLocation JEI_TEXTURE = ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "textures/gui/arc_cooking_pot_jei.png");
    private static final int BACKGROUND_WIDTH = 165;
    private static final int BACKGROUND_HEIGHT = 68;
    private static final int INPUT_START_X = 26;
    private static final int INPUT_START_Y = 13;
    private static final int SLOT_SPACING = 18;
    private static final int DISPLAY_X = 119;
    private static final int DISPLAY_Y = 21;
    private static final int CONTAINER_X = 88;
    private static final int CONTAINER_Y = 51;
    private static final int OUTPUT_X = 120;
    private static final int OUTPUT_Y = 51;
    private static final int SCHOOL_X = 148;
    private static final int SCHOOL_Y = 51;
    private static final int ARROW_X = 85;
    private static final int ARROW_Y = 21;
    private static final int ARROW_ORIGIN_X = 165;
    private static final int ARROW_ORIGIN_Y = 15;
    private static final int ARROW_WIDTH = 26;
    private static final int ARROW_HEIGHT = 17;
    private static final int FIRE_X = 43;
    private static final int FIRE_Y = 51;
    private static final int FIRE_ORIGIN_X = 165;
    private static final int FIRE_ORIGIN_Y = 0;
    private static final int FIRE_WIDTH = 17;
    private static final int FIRE_HEIGHT = 15;
    private static final int INFO_X = 85;
    private static final int INFO_Y = 21;
    private static final int INFO_WIDTH = 26;
    private static final int INFO_HEIGHT = 17;

    private final Component title;
    private final IDrawable icon;
    private final IDrawable heatIndicator;
    private final IDrawableAnimated arrow;

    public AlchemistPotRecipeCategory(IGuiHelper helper) {
        this.title = Component.translatable("jei." + FARMERSSPELL.MODID + ".alchemist_cooking");
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.ALCHEMIST_POT.get()));
        this.heatIndicator = helper.drawableBuilder(
                JEI_TEXTURE,
                FIRE_ORIGIN_X,
                FIRE_ORIGIN_Y,
                FIRE_WIDTH,
                FIRE_HEIGHT
        ).setTextureSize(256, 256)
         .build();
        this.arrow = helper.drawableBuilder(
                JEI_TEXTURE,
                ARROW_ORIGIN_X,
                ARROW_ORIGIN_Y,
                ARROW_WIDTH,
                ARROW_HEIGHT
        ).setTextureSize(256, 256)
         .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<AlchemistCookingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    @Override
    public int getWidth() {
        return BACKGROUND_WIDTH;
    }

    @Override
    public int getHeight() {
        return BACKGROUND_HEIGHT;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void draw(AlchemistCookingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(JEI_TEXTURE, 0, 0, 0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, 256, 256);
        this.arrow.draw(guiGraphics, ARROW_X, ARROW_Y);
        this.heatIndicator.draw(guiGraphics, FIRE_X, FIRE_Y);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AlchemistCookingRecipe recipe, IFocusGroup focusGroup) {
        NonNullList<Ingredient> recipeIngredients = recipe.getIngredients();

        Minecraft minecraft = Minecraft.getInstance();
        RegistryAccess registryAccess = minecraft.level != null ? minecraft.level.registryAccess() : RegistryAccess.EMPTY;
        ItemStack resultStack = recipe.getResultItem(registryAccess);
        ItemStack containerStack = recipe.getOutputContainer();

        for (int row = 0; row < 2; ++row) {
            for (int column = 0; column < 3; ++column) {
                int inputIndex = row * 3 + column;
                if (inputIndex < recipeIngredients.size()) {
                    builder.addSlot(RecipeIngredientRole.INPUT, INPUT_START_X + column * SLOT_SPACING, INPUT_START_Y + row * SLOT_SPACING)
                            .addItemStacks(Arrays.asList(recipeIngredients.get(inputIndex).getItems()));
                }
            }
        }

        builder.addSlot(RecipeIngredientRole.CATALYST, DISPLAY_X, DISPLAY_Y).addItemStack(resultStack);

        if (!containerStack.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.CATALYST, CONTAINER_X, CONTAINER_Y).addItemStack(containerStack);
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, OUTPUT_Y).addItemStack(resultStack);

        if (recipe.getRequiredSchool() != null) {
            List<ItemStack> scrollStacks = createScrollItemStacks(recipe.getRequiredSchool());
            if (!scrollStacks.isEmpty()) {
                builder.addSlot(RecipeIngredientRole.CATALYST, SCHOOL_X, SCHOOL_Y).addItemStacks(scrollStacks);
            }
        }
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, AlchemistCookingRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (isCursorInsideBounds(INFO_X, INFO_Y, INFO_WIDTH, INFO_HEIGHT, mouseX, mouseY)) {
            int cookTime = recipe.getCookTime();
            if (cookTime > 0) {
                int cookTimeSeconds = cookTime / 20;
                tooltip.add(Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds));
            }
            float experience = recipe.getExperience();
            if (experience > 0) {
                tooltip.add(Component.translatable("gui.jei.category.smelting.experience", experience));
            }
        }
    }

    private static boolean isCursorInsideBounds(int x, int y, int width, int height, double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    private static List<ItemStack> createScrollItemStacks(ResourceLocation schoolId) {
        List<ItemStack> scrollStacks = new ArrayList<>();

        Item scrollItem = ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "scroll"));
        if (scrollItem == null) {
            return scrollStacks;
        }

        SchoolType schoolType = SchoolRegistry.getSchool(schoolId);
        if (schoolType == null) {
            return scrollStacks;
        }

        List<AbstractSpell> schoolSpells = SpellRegistry.getSpellsForSchool(schoolType);
        if (schoolSpells.isEmpty()) {
            return scrollStacks;
        }

        for (AbstractSpell spell : schoolSpells) {
            ItemStack scrollStack = new ItemStack(scrollItem);
            ISpellContainer.createScrollContainer(spell, spell.getMinLevel(), scrollStack);
            scrollStacks.add(scrollStack);
        }

        return scrollStacks;
    }
}
