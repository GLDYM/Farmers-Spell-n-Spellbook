package com.chenjdy.farmers_spell.integration.jei;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.block.entity.container.AlchemistPotMenu;
import com.chenjdy.farmers_spell.client.AlchemistPotScreen;
import com.chenjdy.farmers_spell.init.ModBlocks;
import com.chenjdy.farmers_spell.init.ModItems;
import com.chenjdy.farmers_spell.init.ModMenuTypes;
import com.chenjdy.farmers_spell.init.ModRecipeTypes;
import com.chenjdy.farmers_spell.recipe.AlchemistCookingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import net.minecraft.world.item.crafting.RecipeHolder;

@JeiPlugin
public class Plugin implements IModPlugin {

    private static final ResourceLocation PLUGIN_ID = ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "jei_plugin");

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
        registration.addRecipes(RecipeTypes.ANVIL, VanillaAnvilRecipeMaker.getAnvilRepairRecipes(registration.getVanillaRecipeFactory()));
        registration.addIngredientInfo(
                new ItemStack(ModItems.FOODGEIST_SEASONING.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.item.farmers_spell.foodgeist_seasoning.info")
        );
        registration.addIngredientInfo(
                new ItemStack(ModItems.FOODGEIST_CHEESE.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.item.farmers_spell.foodgeist_cheese.info")
        );
        registration.addIngredientInfo(
                new ItemStack(ModItems.CINDEROUS_HAM.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.item.farmers_spell.cinderoous_ham.info")
        );
        registration.addIngredientInfo(
                new ItemStack(ModItems.AMETHYST_BEETROOT_SEEDS.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.item.farmers_spell.amethyst_beetroot_seeds.info")
        );
        registration.addIngredientInfo(
                new ItemStack(ModItems.ICY_EGG.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.item.farmers_spell.icy_egg.info")
        );
    }

    private static List<AlchemistCookingRecipe> getRecipes() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return List.of();
        }
        return minecraft.level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.ALCHEMIST_COOKING.get()).stream().map(RecipeHolder::value).toList();
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
                AlchemistPotMenu.class,
                ModMenuTypes.ALCHEMIST_POT.get(),
                AlchemistPotRecipeCategory.RECIPE_TYPE,
                0, 6,
                9, 36
        );
    }
}
