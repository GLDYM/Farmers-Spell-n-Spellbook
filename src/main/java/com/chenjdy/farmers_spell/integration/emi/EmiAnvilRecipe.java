package com.chenjdy.farmers_spell.integration.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Random;

public class EmiAnvilRecipe implements EmiRecipe {
    private final EmiStack tool;
    private final EmiIngredient resource;
    private final ResourceLocation id;
    private final int unique;

    public EmiAnvilRecipe(EmiStack tool, EmiIngredient resource, ResourceLocation id) {
        this.tool = tool;
        this.resource = resource;
        this.id = id;
        this.unique = idHash();
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return VanillaEmiRecipeCategories.ANVIL_REPAIRING;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(tool, resource);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(tool);
    }

    @Override
    public boolean supportsRecipeTree() {
        return false;
    }

    @Override
    public int getDisplayWidth() {
        return 125;
    }

    @Override
    public int getDisplayHeight() {
        return 18;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.PLUS, 27, 3);
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 75, 1);
        widgets.addGeneratedSlot(random -> getTool(random, false), unique, 0, 0);
        widgets.addSlot(resource, 49, 0);
        widgets.addGeneratedSlot(random -> getTool(random, true), unique, 107, 0).recipeContext(this);
    }

    private EmiStack getTool(Random random, boolean repaired) {
        ItemStack stack = tool.getItemStack().copy();
        if (stack.getMaxDamage() <= 0) {
            return tool;
        }
        int damage = random.nextInt(stack.getMaxDamage());
        if (repaired) {
            damage -= stack.getMaxDamage() / 4;
            if (damage <= 0) {
                return tool;
            }
        }
        stack.setDamageValue(damage);
        return EmiStack.of(stack);
    }

    private int idHash() {
        return id.hashCode();
    }
}
