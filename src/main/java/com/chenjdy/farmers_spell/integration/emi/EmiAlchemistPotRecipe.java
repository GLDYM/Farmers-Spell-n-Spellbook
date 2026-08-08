package com.chenjdy.farmers_spell.integration.emi;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.init.ModBlocks;
import com.chenjdy.farmers_spell.recipe.AlchemistCookingRecipe;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.emi.emi.api.widget.SlotWidget;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EmiAlchemistPotRecipe extends BasicEmiRecipe {
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
            ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "alchemist_cooking"),
            EmiStack.of(ModBlocks.ALCHEMIST_POT.get()));

    private static final ResourceLocation BG = ResourceLocation.fromNamespaceAndPath(
            FarmersSpell.MODID, "textures/gui/arc_cooking_pot_jei.png");

    private static final int WIDTH = 165;
    private static final int HEIGHT = 68;
    private static final int INPUT_START_X = 25;
    private static final int INPUT_START_Y = 12;
    private static final int SLOT_SPACING = 18;
    private static final int DISPLAY_X = 118;
    private static final int DISPLAY_Y = 20;
    private static final int CONTAINER_X = 87;
    private static final int CONTAINER_Y = 50;
    private static final int OUTPUT_X = 119;
    private static final int OUTPUT_Y = 50;
    private static final int SCHOOL_X = 147;
    private static final int SCHOOL_Y = 50;
    private static final int ARROW_X = 85;
    private static final int ARROW_Y = 21;
    private static final int ARROW_U = 165;
    private static final int ARROW_V = 15;
    private static final int ARROW_WIDTH = 26;
    private static final int ARROW_HEIGHT = 17;
    private static final int FIRE_X = 43;
    private static final int FIRE_Y = 51;
    private static final int FIRE_U = 165;
    private static final int FIRE_V = 0;
    private static final int FIRE_WIDTH = 17;
    private static final int FIRE_HEIGHT = 15;

    private final AlchemistCookingRecipe recipe;
    private final ItemStack resultStack;
    private final ItemStack containerStack;
    private final List<EmiStack> scrollStacks;

    private EmiAlchemistPotRecipe(ResourceLocation id, AlchemistCookingRecipe recipe, List<EmiIngredient> inputs,
            List<EmiStack> outputs, ItemStack resultStack, ItemStack containerStack, List<EmiStack> scrollStacks) {
        super(CATEGORY, id, WIDTH, HEIGHT);
        this.recipe = recipe;
        this.inputs = inputs;
        this.outputs = outputs;
        this.resultStack = resultStack;
        this.containerStack = containerStack;
        this.scrollStacks = scrollStacks;
    }

    public static void register(EmiRegistry registry) {
        registry.addCategory(CATEGORY);
    }

    public static EmiAlchemistPotRecipe of(ResourceLocation id, AlchemistCookingRecipe recipe) {
        List<EmiIngredient> inputList = new ArrayList<>();
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        for (Ingredient ingredient : ingredients) {
            inputList.add(EmiIngredient.of(ingredient));
        }

        RegistryAccess access = Minecraft.getInstance().level == null
                ? RegistryAccess.EMPTY
                : Minecraft.getInstance().level.registryAccess();
        ItemStack resultStack = recipe.getResultItem(access);
        ItemStack containerStack = recipe.getOutputContainer();

        List<EmiStack> outputList = new ArrayList<>();
        outputList.add(EmiStack.of(resultStack));
        if (!containerStack.isEmpty()) {
            outputList.add(EmiStack.of(containerStack));
        }

        return new EmiAlchemistPotRecipe(
                id,
                recipe,
                inputList,
                outputList,
                resultStack,
                containerStack,
                createScrollStacks(recipe.getRequiredSchool()));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BG, 0, 0, WIDTH, HEIGHT, 0, 0);
        widgets.addTexture(BG, FIRE_X, FIRE_Y, FIRE_WIDTH, FIRE_HEIGHT, FIRE_U, FIRE_V);
        widgets.addAnimatedTexture(BG, ARROW_X, ARROW_Y, ARROW_WIDTH, ARROW_HEIGHT, ARROW_U, ARROW_V, 10000, true,
                false, false);

        for (int row = 0; row < 2; ++row) {
            for (int column = 0; column < 3; ++column) {
                int inputIndex = row * 3 + column;
                if (inputIndex < inputs.size()) {
                    widgets.addSlot(inputs.get(inputIndex), INPUT_START_X + column * SLOT_SPACING,
                            INPUT_START_Y + row * SLOT_SPACING).drawBack(false);
                }
            }
        }

        widgets.addSlot(EmiStack.of(resultStack), DISPLAY_X, DISPLAY_Y).drawBack(false);
        SlotWidget outputSlot = widgets.addSlot(EmiStack.of(resultStack), OUTPUT_X, OUTPUT_Y).drawBack(false).recipeContext(this);
        if (recipe.getRequiredSchool() != null) {
            outputSlot.appendTooltip(Component.translatable("emi.category.farmers_spell.scroll_required",
                    Component.translatable("school.farmers_spell." + recipe.getRequiredSchool().getPath())));
        }
        if (recipe.getCookTime() > 0) {
            outputSlot.appendTooltip(Component.translatable("emi.category.farmers_spell.cook_time",
                    recipe.getCookTime() / 20));
        }

        if (!containerStack.isEmpty()) {
            widgets.addSlot(EmiStack.of(containerStack), CONTAINER_X, CONTAINER_Y).drawBack(false);
        }

        if (!scrollStacks.isEmpty()) {
            widgets.addSlot(EmiIngredient.of(scrollStacks), SCHOOL_X, SCHOOL_Y).drawBack(false);
        }
    }

    private static List<EmiStack> createScrollStacks(@Nullable ResourceLocation schoolId) {
        List<EmiStack> scrollStacks = new ArrayList<>();
        if (schoolId == null) {
            return scrollStacks;
        }

        Item scrollItem = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "scroll"));
        if (scrollItem == null) {
            return scrollStacks;
        }

        SchoolType schoolType = SchoolRegistry.getSchool(schoolId);
        if (schoolType == null) {
            return scrollStacks;
        }

        List<AbstractSpell> spells = SpellRegistry.getSpellsForSchool(schoolType);
        for (AbstractSpell spell : spells) {
            ItemStack scrollStack = new ItemStack(scrollItem);
            ISpellContainer.createScrollContainer(spell, spell.getMinLevel(), scrollStack);
            scrollStacks.add(EmiStack.of(scrollStack));
        }
        return scrollStacks;
    }
}
