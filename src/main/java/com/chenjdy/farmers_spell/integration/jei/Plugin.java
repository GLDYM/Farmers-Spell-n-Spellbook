package com.chenjdy.farmers_spell.integration.jei;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.client.AlchemistPotScreen;
import com.chenjdy.farmers_spell.init.ModBlocks;
import com.chenjdy.farmers_spell.init.ModMenuTypes;
import com.chenjdy.farmers_spell.init.ModRecipeTypes;
import com.chenjdy.farmers_spell.recipe.AlchemistCookingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

@JeiPlugin
public class Plugin implements IModPlugin {

    private static final ResourceLocation PLUGIN_ID = ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new AlchemistPotRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<AlchemistCookingRecipe> recipes = getRecipes();
        registration.addRecipes(AlchemistPotRecipeCategory.RECIPE_TYPE, recipes);
    }

    private static List<AlchemistCookingRecipe> getRecipes() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return List.of();
        }
        return minecraft.level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.ALCHEMIST_COOKING.get());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        ItemStack catalyst = new ItemStack(ModBlocks.ALCHEMIST_POT.get());
        registration.addRecipeCatalyst(catalyst, AlchemistPotRecipeCategory.RECIPE_TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(AlchemistPotScreen.class, 89, 25, 24, 17, AlchemistPotRecipeCategory.RECIPE_TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(
                com.chenjdy.farmers_spell.block.entity.container.AlchemistPotMenu.class,
                ModMenuTypes.ALCHEMIST_POT.get(),
                AlchemistPotRecipeCategory.RECIPE_TYPE,
                0, 6,
                9, 36
        );
    }
}
