package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.recipe.AlchemistCookingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeTypes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, FARMERSSPELL.MODID);

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, FARMERSSPELL.MODID);

    public static final RegistryObject<RecipeSerializer<AlchemistCookingRecipe>> ALCHEMIST_COOKING_SERIALIZER =
            RECIPE_SERIALIZERS.register("alchemist_cooking", () -> AlchemistCookingRecipe.AlchemistRecipeSerializer.INSTANCE);

    public static final RegistryObject<RecipeType<AlchemistCookingRecipe>> ALCHEMIST_COOKING =
            RECIPE_TYPES.register("alchemist_cooking", () -> AlchemistCookingRecipe.AlchemistRecipeType.INSTANCE);

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
    }
}