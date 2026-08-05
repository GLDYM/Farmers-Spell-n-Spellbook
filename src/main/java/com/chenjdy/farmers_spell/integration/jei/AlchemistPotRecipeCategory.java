package com.chenjdy.farmers_spell.integration.jei;

import com.chenjdy.farmers_spell.FarmersSpell;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.core.registries.BuiltInRegistries;
import vectorwing.farmersdelight.FarmersDelight;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.core.RegistryAccess;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AlchemistPotRecipeCategory implements IRecipeCategory<AlchemistCookingRecipe>
{
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "alchemist_cooking");
    public static final RecipeType<AlchemistCookingRecipe> RECIPE_TYPE = RecipeType.create(FarmersSpell.MODID, "alchemist_cooking", AlchemistCookingRecipe.class);

    private final IDrawable heatIndicator;
    private final IDrawable timeIcon;
    private final IDrawable expIcon;
    private final IDrawableAnimated arrow;
    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;

    public AlchemistPotRecipeCategory(IGuiHelper helper) {
        this.title = Component.translatable("jei." + FarmersSpell.MODID + ".alchemist_cooking");

        ResourceLocation widgetBackgroundImage = ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "textures/gui/jei/cooking_pot.png");
        ResourceLocation interfaceImage = ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "textures/gui/cooking_pot.png");

        this.background = helper.createDrawable(widgetBackgroundImage, 0, 0, 116, 56);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.ALCHEMIST_POT.get()));

        this.heatIndicator = helper.createDrawable(interfaceImage, 176, 0, 17, 15);
        this.timeIcon = helper.createDrawable(interfaceImage, 176, 32, 8, 11);
        this.expIcon = helper.createDrawable(interfaceImage, 176, 43, 9, 9);
        this.arrow = helper.drawableBuilder(interfaceImage, 176, 15, 24, 17)
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

    public int getWidth() {
        return 116;
    }

    public int getHeight() {
        return 56;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AlchemistCookingRecipe recipe, IFocusGroup focusGroup) {
        NonNullList<Ingredient> recipeIngredients = recipe.getIngredients();

        Minecraft minecraft = Minecraft.getInstance();
        RegistryAccess registryAccess = minecraft.level != null ? minecraft.level.registryAccess() : RegistryAccess.EMPTY;
        ItemStack resultStack = recipe.getResultItem(registryAccess);
        ItemStack containerStack = recipe.getOutputContainer();

        int borderSlotSize = 18;
        for (int row = 0; row < 2; ++row) {
            for (int column = 0; column < 3; ++column) {
                int inputIndex = row * 3 + column;
                if (inputIndex < recipeIngredients.size()) {
                    builder.addSlot(RecipeIngredientRole.INPUT, column * borderSlotSize + 1, row * borderSlotSize + 1)
                            .addItemStacks(Arrays.asList(recipeIngredients.get(inputIndex).getItems()));
                }
            }
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 10).addItemStack(resultStack);

        if (!containerStack.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.CATALYST, 63, 39).addItemStack(containerStack);
        }

        if (recipe.getRequiredSchool() != null) {
            List<ItemStack> scrollStacks = createScrollItemStacks(recipe.getRequiredSchool());
            if (!scrollStacks.isEmpty()) {
                builder.addSlot(RecipeIngredientRole.CATALYST, 95, 39).addItemStacks(scrollStacks);
            }
        } else {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 39).addItemStack(resultStack);
        }
    }

    @Override
    public void draw(AlchemistCookingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics, 0, 0);
        arrow.draw(guiGraphics, 60, 9);
        heatIndicator.draw(guiGraphics, 18, 39);
        timeIcon.draw(guiGraphics, 64, 2);

        if (recipe.getExperience() > 0) {
            expIcon.draw(guiGraphics, 63, 21);
        }
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, AlchemistCookingRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (isCursorInsideBounds(61, 2, 22, 28, mouseX, mouseY)) {
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
        
        Item scrollItem = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "scroll"));
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
