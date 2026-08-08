package com.chenjdy.farmers_spell.integration.emi;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.init.ModMenuTypes;
import com.chenjdy.farmers_spell.init.ModBlocks;
import com.chenjdy.farmers_spell.init.ModItems;
import com.chenjdy.farmers_spell.init.ModRecipeTypes;
import com.chenjdy.farmers_spell.recipe.AlchemistCookingRecipe;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.TieredItem;

import java.util.Arrays;
import java.util.List;
import vectorwing.farmersdelight.integration.emi.FDRecipeCategories;

@EmiEntrypoint
public class EmiPlugin implements dev.emi.emi.api.EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }

        EmiAlchemistPotRecipe.register(registry);
        EmiAlchemistPotScreenHandler.register();
        registry.addRecipeHandler(ModMenuTypes.ALCHEMIST_POT.get(), new AlchemistPotEmiRecipeHandler());

        for (RecipeHolder<AlchemistCookingRecipe> holder : registry.getRecipeManager()
                .getAllRecipesFor(ModRecipeTypes.ALCHEMIST_COOKING.get())) {
            registry.addRecipe(EmiAlchemistPotRecipe.of(holder.id(), holder.value()));
        }

        addIngredientInfo(registry,
                new ItemStack(ModItems.FOODGEIST_SEASONING.get()),
                "emi.item.farmers_spell.foodgeist_seasoning.info",
                "foodgeist_seasoning");
        addIngredientInfo(registry,
                new ItemStack(ModItems.FOODGEIST_CHEESE.get()),
                "emi.item.farmers_spell.foodgeist_cheese.info",
                "foodgeist_cheese");
        addIngredientInfo(registry,
                new ItemStack(ModItems.CINDEROUS_HAM.get()),
                "emi.item.farmers_spell.cinderoous_ham.info",
                "cinderoous_ham");
        addIngredientInfo(registry,
                new ItemStack(ModItems.AMETHYST_BEETROOT_SEEDS.get()),
                "emi.item.farmers_spell.amethyst_beetroot_seeds.info",
                "amethyst_beetroot_seeds");
        addIngredientInfo(registry,
                new ItemStack(ModItems.ICY_EGG.get()),
                "emi.item.farmers_spell.icy_egg.info",
                "icy_egg");

        addAnvilRecipes(registry);
        registry.addWorkstation(EmiAlchemistPotRecipe.CATEGORY, EmiStack.of(ModBlocks.ALCHEMIST_POT.get()));
        registry.addWorkstation(FDRecipeCategories.COOKING, EmiStack.of(ModBlocks.ALCHEMIST_POT.get()));
    }

    private static void addIngredientInfo(EmiRegistry registry, ItemStack stack, String translationKey, String path) {
        registry.addRecipe(new EmiInfoRecipe(
                List.of(EmiStack.of(stack)),
                List.of(Component.translatable(translationKey)),
                ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "/emi/info/" + path)));
    }

    private static void addAnvilRecipes(EmiRegistry registry) {
        for (Item item : getTieredItems()) {
            if (item instanceof TieredItem tieredItem) {
                addSelfRepairAnvilRecipe(registry, item, "repair_item");
                List<ItemStack> repairMaterials = Arrays.stream(tieredItem.getTier().getRepairIngredient().getItems()).toList();
                addMaterialRepairAnvilRecipes(registry, item, repairMaterials, "repair_material");
            }
        }

        for (Item item : getArmorItems()) {
            if (item instanceof ArmorItem armorItem) {
                addSelfRepairAnvilRecipe(registry, item, "repair_item");
                List<ItemStack> repairMaterials = Arrays.stream(armorItem.getMaterial().value().repairIngredient().get().getItems()).toList();
                addMaterialRepairAnvilRecipes(registry, item, repairMaterials, "repair_material");
            }
        }
    }

    private static void addSelfRepairAnvilRecipe(EmiRegistry registry, Item item, String suffix) {
        ItemStack stack = new ItemStack(item);
        registry.addRecipe(new EmiAnvilRecipe(
                EmiStack.of(stack),
                EmiStack.of(stack.copy()),
                ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID,
                        "/emi/anvil/" + BuiltInRegistries.ITEM.getKey(item).getPath() + "/" + suffix)));
    }

    private static void addMaterialRepairAnvilRecipes(EmiRegistry registry, Item item, List<ItemStack> repairMaterials, String suffix) {
        if (repairMaterials.isEmpty()) {
            return;
        }
        registry.addRecipe(new EmiAnvilRecipe(
                EmiStack.of(new ItemStack(item)),
                dev.emi.emi.api.stack.EmiIngredient.of(repairMaterials.stream().map(EmiStack::of).toList()),
                ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID,
                        "/emi/anvil/" + BuiltInRegistries.ITEM.getKey(item).getPath() + "/" + suffix)));
    }

    private static List<Item> getTieredItems() {
        return List.of(
                ModItems.GOSPEL.get(),
                ModItems.HELL_KNIFE.get(),
                ModItems.CHERRY_SPOON.get(),
                ModItems.IRIS_FORK.get(),
                ModItems.GROW_KNIFE.get(),
                ModItems.TWILIGHT_BLADE.get(),
                ModItems.BOREAL_KNIFE.get(),
                ModItems.ECHOING_KNIFE.get());
    }

    private static List<Item> getArmorItems() {
        return List.of(
                ModItems.GLUTTONY_CHEF_HAT.get(),
                ModItems.GLUTTONY_CHEF_APRON.get(),
                ModItems.GLUTTONY_CHEF_LEGGINGS.get(),
                ModItems.GLUTTONY_CHEF_BOOTS.get());
    }
}
