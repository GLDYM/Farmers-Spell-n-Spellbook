package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.recipe.AlchemistCookingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
public class ModRecipeTypes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, FARMERSSPELL.MODID);

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, FARMERSSPELL.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<AlchemistCookingRecipe>> ALCHEMIST_COOKING_SERIALIZER =
            RECIPE_SERIALIZERS.register("alchemist_cooking", () -> AlchemistCookingRecipe.AlchemistRecipeSerializer.INSTANCE);

    public static final DeferredHolder<RecipeType<?>, RecipeType<AlchemistCookingRecipe>> ALCHEMIST_COOKING =
            RECIPE_TYPES.register("alchemist_cooking", () -> AlchemistCookingRecipe.AlchemistRecipeType.INSTANCE);

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
    }
}