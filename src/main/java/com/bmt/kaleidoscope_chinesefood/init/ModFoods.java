package com.bmt.kaleidoscope_chinesefood.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

import static com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects.*;
import static net.minecraft.world.effect.MobEffects.FIRE_RESISTANCE;

public class ModFoods {
    // 麻辣抄手
    public static final FoodProperties SICHUAN_WONTON = new FoodProperties.Builder()
            .nutrition(14).saturationMod(0.64f)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 8 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 云吞面
    public static final FoodProperties WONTON_NOODLES = new FoodProperties.Builder()
            .nutrition(14).saturationMod(0.66f)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 8 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 羊肉泡馍
    public static final FoodProperties YANGROU_PAOMO = new FoodProperties.Builder()
            .nutrition(14).saturationMod(0.64f)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 8 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 冒菜
    public static final FoodProperties MAOCAI = new FoodProperties.Builder()
            .nutrition(6).saturationMod(0.64f)
            .effect(() -> new MobEffectInstance(FIRE_RESISTANCE, 2 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 紫菜蛋花汤
    public static final FoodProperties SEAWEED_EGG_DROP_SOUP = new FoodProperties.Builder()
            .nutrition(6).saturationMod(0.64f)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 8 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 番茄鸡蛋汤
    public static final FoodProperties TOMATO_EGG_DROP_SOUP = new FoodProperties.Builder()
            .nutrition(6).saturationMod(0.64f)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 8 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 皮蛋瘦肉粥
    public static final FoodProperties CENTURY_EGG_CONGEE = new FoodProperties.Builder()
            .nutrition(6).saturationMod(0.64f)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 8 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 南瓜粥
    public static final FoodProperties PUMPKIN_PORRIDGE = new FoodProperties.Builder()
            .nutrition(6).saturationMod(0.64f)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 8 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 水煮肉片
    public static final FoodProperties SICHUAN_BOILED_PORK_SLICES_ITEM = new FoodProperties.Builder()
            .nutrition(13).saturationMod(0.61f)
            .effect(() -> new MobEffectInstance(FIRE_RESISTANCE, 2 * 60 * 20), 1.0F)
            .alwaysEat().build();
    public static final FoodProperties SICHUAN_BOILED_PORK_SLICES_BLOCK = new FoodProperties.Builder()
            .nutrition(4).saturationMod(0.61f)
            .effect(() -> new MobEffectInstance(FIRE_RESISTANCE, 2 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 水煮鱼
    public static final FoodProperties SICHUAN_BOILED_FISH_ITEM = new FoodProperties.Builder()
            .nutrition(13).saturationMod(0.61f)
            .effect(() -> new MobEffectInstance(FIRE_RESISTANCE, 2 * 60 * 20), 1.0F)
            .alwaysEat().build();
    public static final FoodProperties SICHUAN_BOILED_FISH_BLOCK = new FoodProperties.Builder()
            .nutrition(3).saturationMod(0.61f)
            .effect(() -> new MobEffectInstance(FIRE_RESISTANCE, 2 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 回锅肉
    public static final FoodProperties TWICE_COOKED_PORK = new FoodProperties.Builder()
            .nutrition(9).saturationMod(0.61f)
            .effect(() -> new MobEffectInstance(VIGOR.get(), 90 * 20), 1.0F)
            .alwaysEat().build();

    // 回锅肉盖饭
    public static final FoodProperties TWICE_COOKED_PORK_RICE = new FoodProperties.Builder()
            .nutrition(14).saturationMod(0.64f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 3 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 小炒黄牛肉
    public static final FoodProperties STIR_FRIED_YELLOW_BEEF = new FoodProperties.Builder()
            .nutrition(9).saturationMod(0.61f)
            .effect(() -> new MobEffectInstance(VIGOR.get(), 90 * 20), 1.0F)
            .alwaysEat().build();

    // 小炒黄牛肉盖饭
    public static final FoodProperties STIR_FRIED_YELLOW_BEEF_RICE = new FoodProperties.Builder()
            .nutrition(14).saturationMod(0.64f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 3 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 滑蛋牛肉
    public static final FoodProperties BEEF_WITH_SCRAMBLED_EGGS = new FoodProperties.Builder()
            .nutrition(9).saturationMod(0.61f)
            .effect(() -> new MobEffectInstance(VIGOR.get(), 90 * 20), 1.0F)
            .alwaysEat().build();

    // 滑蛋牛肉盖饭
    public static final FoodProperties BEEF_WITH_SCRAMBLED_EGGS_RICE = new FoodProperties.Builder()
            .nutrition(14).saturationMod(0.64f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 3 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 地三鲜
    public static final FoodProperties STIR_FRIED_THREE_FRESH_VEGETABLES = new FoodProperties.Builder()
            .nutrition(9).saturationMod(0.61f)
            .effect(() -> new MobEffectInstance(VIGOR.get(), 90 * 20), 1.0F)
            .alwaysEat().build();

    // 地三鲜盖饭
    public static final FoodProperties STIR_FRIED_THREE_FRESH_VEGETABLES_RICE = new FoodProperties.Builder()
            .nutrition(14).saturationMod(0.7f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 3 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 大盘鸡
    public static final FoodProperties BIG_PLATE_CHICKEN = new FoodProperties.Builder()
            .nutrition(9).saturationMod(0.61f)
            .effect(() -> new MobEffectInstance(VIGOR.get(), 90 * 20), 1.0F)
            .alwaysEat().build();

    // 大盘鸡拌面
    public static final FoodProperties BIG_PLATE_CHICKEN_NOODLES = new FoodProperties.Builder()
            .nutrition(14).saturationMod(0.7f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 3 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 西红柿鸡蛋拌面
    public static final FoodProperties TOMATO_EGG_NOODLES = new FoodProperties.Builder()
            .nutrition(14).saturationMod(0.7f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 3 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 辣椒炒肉拌面
    public static final FoodProperties PORK_CHILI_NOODLES = new FoodProperties.Builder()
            .nutrition(14).saturationMod(0.7f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 3 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 四喜丸子
    public static final FoodProperties FOUR_JOY_MEATBALLS = new FoodProperties.Builder()
            .nutrition(13).saturationMod(0.61f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD.get(), 3 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 干锅土豆片
    public static final FoodProperties DRY_POT_POTATOES = new FoodProperties.Builder()
            .nutrition(13).saturationMod(0.61f)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 8 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 干锅鸡
    public static final FoodProperties DRY_POT_CHICKEN = new FoodProperties.Builder()
            .nutrition(13).saturationMod(0.61f)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 8 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 干锅排骨
    public static final FoodProperties DRY_POT_SPARE_RIBS = new FoodProperties.Builder()
            .nutrition(13).saturationMod(0.61f)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 8 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 扬州炒饭
    public static final FoodProperties YANGZHOU_FRIED_RICE = new FoodProperties.Builder()
            .nutrition(12).saturationMod(0.66f)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 8 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 羊肉抓饭
    public static final FoodProperties LAMB_PILAF = new FoodProperties.Builder()
            .nutrition(14).saturationMod(0.66f)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 8 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 肠粉
    public static final FoodProperties STEAMED_RICE_ROLLS = new FoodProperties.Builder()
            .nutrition(14).saturationMod(0.64f)
            .effect(() -> new MobEffectInstance(WARMTH.get(), 8 * 60 * 20), 1.0F)
            .alwaysEat().build();

    // 咸鸭蛋
    public static final FoodProperties SALTED_EGG = new FoodProperties.Builder()
            .nutrition(4).saturationMod(0.5f)
            .alwaysEat().build();

    // 皮蛋
    public static final FoodProperties CENTURY_EGG = new FoodProperties.Builder()
            .nutrition(4).saturationMod(0.5f)
            .alwaysEat().build();
}