package com.chenjdy.farmers_spell.integration.jei;

import com.chenjdy.farmers_spell.init.ModItems;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class VanillaAnvilRecipeMaker {

    public static List<IJeiAnvilRecipe> getAnvilRepairRecipes(IVanillaRecipeFactory vanillaRecipeFactory) {
        return Stream.concat(
                getArmorRepairRecipes(vanillaRecipeFactory),
                getTieredItemRepairRecipes(vanillaRecipeFactory)
        ).toList();
    }

    public static Stream<IJeiAnvilRecipe> getTieredItemRepairRecipes(IVanillaRecipeFactory vanillaRecipeFactory) {
        var repairableItems = getTieredItems();
        return repairableItems.stream()
                .filter(item -> item instanceof TieredItem)
                .map(item -> (TieredItem) item)
                .mapMulti((item, consumer) -> {
                    ItemStack damagedThreeQuarters = new ItemStack(item);
                    damagedThreeQuarters.setDamageValue(damagedThreeQuarters.getMaxDamage() * 3 / 4);
                    ItemStack damagedHalf = new ItemStack(item);
                    damagedHalf.setDamageValue(damagedHalf.getMaxDamage() / 2);

                    IJeiAnvilRecipe repairWithSame = vanillaRecipeFactory.createAnvilRecipe(List.of(damagedThreeQuarters), List.of(damagedThreeQuarters), List.of(damagedHalf));
                    consumer.accept(repairWithSame);

                    List<ItemStack> repairMaterials = Arrays.stream(item.getTier().getRepairIngredient().getItems()).toList();
                    ItemStack damagedFully = new ItemStack(item);
                    damagedFully.setDamageValue(damagedFully.getMaxDamage());
                    IJeiAnvilRecipe repairWithMaterial = vanillaRecipeFactory.createAnvilRecipe(List.of(damagedFully), repairMaterials, List.of(damagedThreeQuarters));
                    consumer.accept(repairWithMaterial);
                });
    }

    public static Stream<IJeiAnvilRecipe> getArmorRepairRecipes(IVanillaRecipeFactory vanillaRecipeFactory) {
        var repairableItems = getArmorItems();
        return repairableItems.stream()
                .filter(item -> item instanceof ArmorItem)
                .map(item -> (ArmorItem) item)
                .mapMulti((item, consumer) -> {
                    ItemStack damagedThreeQuarters = new ItemStack(item);
                    damagedThreeQuarters.setDamageValue(damagedThreeQuarters.getMaxDamage() * 3 / 4);
                    ItemStack damagedHalf = new ItemStack(item);
                    damagedHalf.setDamageValue(damagedHalf.getMaxDamage() / 2);

                    IJeiAnvilRecipe repairWithSame = vanillaRecipeFactory.createAnvilRecipe(List.of(damagedThreeQuarters), List.of(damagedThreeQuarters), List.of(damagedHalf));
                    consumer.accept(repairWithSame);

                    List<ItemStack> repairMaterials = Arrays.stream(item.getMaterial().getRepairIngredient().getItems()).toList();
                    ItemStack damagedFully = new ItemStack(item);
                    damagedFully.setDamageValue(damagedFully.getMaxDamage());
                    IJeiAnvilRecipe repairWithMaterial = vanillaRecipeFactory.createAnvilRecipe(List.of(damagedFully), repairMaterials, List.of(damagedThreeQuarters));
                    consumer.accept(repairWithMaterial);
                });
    }

    public static List<Item> getTieredItems() {
        List<Item> items = new ArrayList<>();
        items.add(ModItems.GOSPEL.get());
        items.add(ModItems.HELL_KNIFE.get());
        items.add(ModItems.CHERRY_SPOON.get());
        items.add(ModItems.IRIS_FORK.get());
        items.add(ModItems.GROW_KNIFE.get());
        items.add(ModItems.TWILIGHT_BLADE.get());
        items.add(ModItems.BOREAL_KNIFE.get());
        items.add(ModItems.ECHOING_KNIFE.get());
        return items;
    }

    public static List<Item> getArmorItems() {
        List<Item> items = new ArrayList<>();
        items.add(ModItems.GLUTTONY_CHEF_HAT.get());
        items.add(ModItems.GLUTTONY_CHEF_APRON.get());
        items.add(ModItems.GLUTTONY_CHEF_LEGGINGS.get());
        items.add(ModItems.GLUTTONY_CHEF_BOOTS.get());
        return items;
    }
}
