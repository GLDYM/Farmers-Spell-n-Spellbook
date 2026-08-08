package com.chenjdy.farmers_spell.integration.emi;

import com.chenjdy.farmers_spell.block.entity.container.AlchemistPotMenu;
import net.minecraft.world.inventory.Slot;
import java.util.ArrayList;
import java.util.List;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import vectorwing.farmersdelight.integration.emi.FDRecipeCategories;

public class AlchemistPotEmiRecipeHandler implements StandardRecipeHandler<AlchemistPotMenu> {

    @Override
    public List<Slot> getInputSources(AlchemistPotMenu handler) {
        List<Slot> slots = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            slots.add(handler.getSlot(i));
        }
        for (int i = 10; i < 46; i++) {
            slots.add(handler.getSlot(i));
        }
        return slots;
    }

    @Override
    public List<Slot> getCraftingSlots(AlchemistPotMenu handler) {
        List<Slot> slots = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            slots.add(handler.getSlot(i));
        }
        return slots;
    }

    @Override
    public Slot getOutputSlot(AlchemistPotMenu handler) {
        return handler.slots.get(AlchemistPotMenu.INDEX_OUTPUT);
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.supportsRecipeTree() && (recipe.getCategory() == EmiAlchemistPotRecipe.CATEGORY
                || recipe.getCategory() == FDRecipeCategories.COOKING);
    }
}
