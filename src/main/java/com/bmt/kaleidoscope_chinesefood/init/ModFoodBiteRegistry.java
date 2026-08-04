package com.bmt.kaleidoscope_chinesefood.init;

import com.bmt.kaleidoscope_chinesefood.KaleidoscopeChineseFood;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import net.minecraft.resources.ResourceLocation;

public class ModFoodBiteRegistry {
    public static ResourceLocation SICHUAN_BOILED_FISH;
    public static ResourceLocation SICHUAN_BOILED_PORK_SLICES;

    public static void init() {
        FoodBiteRegistry registry = new FoodBiteRegistry();

        SICHUAN_BOILED_FISH = registry.registerFoodData(KaleidoscopeChineseFood.id("sichuan_boiled_fish"), FoodBiteRegistry.FoodData
                .create(4, ModFoods.SICHUAN_BOILED_FISH_BLOCK, ModFoods.SICHUAN_BOILED_FISH_ITEM)
                .bowlAABB());


        SICHUAN_BOILED_PORK_SLICES = registry.registerFoodData(KaleidoscopeChineseFood.id("sichuan_boiled_pork_slices"), FoodBiteRegistry.FoodData
                .create(3, ModFoods.SICHUAN_BOILED_PORK_SLICES_BLOCK, ModFoods.SICHUAN_BOILED_PORK_SLICES_ITEM)
                .bowlAABB());
    }
}
