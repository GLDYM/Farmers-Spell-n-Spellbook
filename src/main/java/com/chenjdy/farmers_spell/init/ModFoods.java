package com.chenjdy.farmers_spell.init;

import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import vectorwing.farmersdelight.common.FoodValues;
import static vectorwing.farmersdelight.common.FoodValues.nourishment;

public class ModFoods {
    // 疣猪皮小香肠
    public static final FoodProperties HOGSKIN_SAUSAGE = (new FoodProperties.Builder())
            .nutrition(20)
            .saturationMod(0.75f)
            .effect(() -> nourishment(FoodValues.MEDIUM_DURATION ), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 3 * 60 * 20, 0), 1.0F)
            .build();
    // 血酒炖牛肉
    public static final FoodProperties WINE_BEEF_STEW = (new FoodProperties.Builder())
            .nutrition(24)
            .saturationMod(0.75f)
            .effect(() -> nourishment(FoodValues.MEDIUM_DURATION ), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffectRegistry.VIGOR.get(), 5 * 60 * 20, 4), 1.0F)
            .build();
    // 红酒血汁烩饭
    public static final FoodProperties WINE_RICE = (new FoodProperties.Builder())
            .nutrition(24)
            .saturationMod(0.75f)
            .effect(() -> nourishment(FoodValues.MEDIUM_DURATION ), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffectRegistry.VIGOR.get(), 5 * 60 * 20, 4), 1.0F)
            .build();
    // 炽血麻辣烫
    public static final FoodProperties CINDEROUS_HOTPOT = (new FoodProperties.Builder())
            .nutrition(24)
            .saturationMod(0.75f)
            .effect(() -> nourishment(FoodValues.MEDIUM_DURATION ), 1.0F)
            .effect(() -> new MobEffectInstance(ModEffects.GOLDEN_ARMOR.get(), 5 * 60 * 20, 2), 1.0F)
            .build();
    // 神圣黄油鸡
    public static final FoodProperties BUTTER_CHICKEN = (new FoodProperties.Builder())          
            .nutrition(6)
            .saturationMod(0.8f)
            .effect(() -> nourishment(FoodValues.MEDIUM_DURATION ), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffectRegistry.HASTENED.get(), 2 * 60 * 20, 4), 1.0F)
            .build();
    // 碗装龙鳞冻
    public static final FoodProperties BOWL_OF_DRAGON_SKIN_ASPIC = (new FoodProperties.Builder())
            .nutrition(32)
            .saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(MobEffectRegistry.EVASION.get(), 1 * 60 * 20, 4), 1.0F)
            .build();
    // 冰山奶霜
    public static final FoodProperties ICEBERGCREAM = (new FoodProperties.Builder())
            .nutrition(6)
            .saturationMod(0.1f)
            .effect(() -> new MobEffectInstance(ModEffects.FROST_SHIELD.get(), 5 * 60 * 20, 0), 1.0F)
            .build();
    // 神圣奶酪焗土豆
    public static final FoodProperties BUTTER_POTATO = (new FoodProperties.Builder())
            .nutrition(16)
            .saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(MobEffectRegistry.HASTENED.get(), 3 * 60 * 20, 4), 1.0F)
            .build();
    // 宝石汉堡
    public static final FoodProperties AMETHYST_BURGER = (new FoodProperties.Builder())
            .nutrition(20)
            .saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(MobEffectRegistry.HASTENED.get(), 3 * 60 * 20, 2), 1.0F)
            .effect(() -> new MobEffectInstance(ModEffects.CLEANSE.get(), 1 * 60 * 20, 0), 1.0F)
            .build();
    // 闪烁十字面包
    public static final FoodProperties CERIC_CROSS_BUN = (new FoodProperties.Builder())
            .nutrition(12)
            .saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(MobEffectRegistry.HASTENED.get(), 3 * 60 * 20, 2), 1.0F)
            .effect(() -> new MobEffectInstance(ModEffects.CLEANSE.get(), 3 * 60 * 20, 0), 1.0F)
            .build();
    // 神莓玛芬
    public static final FoodProperties GOODBERRY_MUFFIN = (new FoodProperties.Builder())
            .nutrition(10)
            .saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(ModEffects.DRUID_HEAL.get(), 3 * 60 * 20, 0), 1.0F)
            .effect(() -> new MobEffectInstance(ModEffects.CLEANSE.get(), 3 * 60 * 20, 0), 1.0F)
            .build();
    // 冰挞
    public static final FoodProperties SNOWY_TART = (new FoodProperties.Builder())
            .nutrition(10)
            .saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(ModEffects.CLEANSE.get(), 3 * 60 * 20, 0), 1.0F)
            .effect(() -> new MobEffectInstance(ModEffects.FROST_SHIELD.get(), 3 * 60 * 20, 0), 1.0F)
            .build();
    // 月饼
    public static final FoodProperties AMETHYST_MOONCAKE = (new FoodProperties.Builder())
            .nutrition(10)
            .saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(ModEffects.CLEANSE.get(), 3 * 60 * 20, 0), 1.0F)
            .build();
    // 龙肉冻
    public static final FoodProperties DRAGONSKIN_ASPIC = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(0.75f)
            .build();
    // 披萨
    public static final FoodProperties DRAGON_PIZZA = new FoodProperties.Builder()
            .nutrition(32)
            .saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(MobEffectRegistry.EVASION.get(), 1 * 60 * 20, 4), 1.0F)
            .build();
    // 过载焦糖
    public static final FoodProperties ENERGIZED_CANDY = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(0.6f)
            .effect(() -> new MobEffectInstance(MobEffectRegistry.CHARGED.get(), 2 * 60 * 20, 2), 1.0F)
            .effect(() -> new MobEffectInstance(ModEffects.CLEANSE.get(), 3 * 60 * 20, 0), 1.0F)
            .build();
    // 紫晶糖
    public static final FoodProperties AMETHYST_SUGAR = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(0.6f)
            .effect(() -> new MobEffectInstance(ModEffects.CLEANSE.get(), 3 * 60 * 20, 0), 1.0F)
            .build();
    // 泡芙
    public static final FoodProperties PAOFU = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(MobEffectRegistry.CHARGED.get(), 3000, 2), 1.0F)
            .build();
    // 破冰面包
    public static final FoodProperties ICEBREAKER_BREAD = new FoodProperties.Builder()
            .nutrition(16)
            .saturationMod(0.75f)
            .effect(() -> nourishment(FoodValues.MEDIUM_DURATION ), 1.0F)
            .effect(() -> new MobEffectInstance(ModEffects.FROST_SHIELD.get(), 5 * 60 * 20, 2), 1.0F)
            .build();
    // 火腿
    public static final FoodProperties CINDEROUS_HAM = new FoodProperties.Builder()
            .nutrition(24)
            .saturationMod(0.75f)
            .effect(() -> new MobEffectInstance(ModEffects.GOLDEN_ARMOR.get(), 8 * 60 * 20, 2), 1.0F)
            .build();
    // 北冰鲑鱼堡
    public static final FoodProperties SALMON_BURGER = new FoodProperties.Builder()
            .nutrition(10)
            .saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(ModEffects.FROST_SHIELD.get(), 3 * 60 * 20, 0), 1.0F)
            .build();
    // 雷云棉花糖
    public static final FoodProperties THUNDER_COTTON_CANDY = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(MobEffectRegistry.CHARGED.get(), 2 * 60 * 20, 4), 1.0F)
            .build();
    // 血豆腐
    public static final FoodProperties BLOOD_TOFU = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(0.6f)
            .effect(() -> new MobEffectInstance(MobEffectRegistry.VIGOR.get(), 8 * 60 * 20, 1), 1.0F)
            .build();
    // 芝士
    public static final FoodProperties FOODGEIST_CHEESE = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(0.6f)
            .build();
    // 黄油
    public static final FoodProperties WSIP_BUTTER = new FoodProperties.Builder()
            .nutrition(4)
            .saturationMod(0.6f)
            .build();
    // 过载焦糖
    public static final FoodProperties ENERGIZED_CARAMEL = new FoodProperties.Builder()
            .nutrition(4)
            .saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(MobEffectRegistry.CHARGED.get(), 600, 2), 1.0F)
            .effect(() -> new MobEffectInstance(ModEffects.CLEANSE.get(), 3 * 60 * 20, 0), 1.0F)
            .build();
    // 橡肤南瓜浓汤
    public static final FoodProperties PUMPKIN_SOUP = new FoodProperties.Builder()
            .nutrition(20)
            .saturationMod(0.6f)
            .effect(() -> nourishment(FoodValues.MEDIUM_DURATION ), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffectRegistry.OAKSKIN.get(), 5 * 60 * 20, 3), 1.0F)
            .build();
    // 神莓
    public static final FoodProperties GOODBERRY = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(0.2f)
            .fast()
            .alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 1, 0), 1.0F)
            .effect(() -> new MobEffectInstance(ModEffects.DRUID_HEAL.get(), 100, 0), 1.0F)
            .build();
    // 霜皮蛋
    public static final FoodProperties ICY_EGG = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(0.2f)
            .build();
    // 紫晶甜菜
    public static final FoodProperties AMETHYST_BEETROOT = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(0.2f)
            .build();
    // 神莓汁
    public static final FoodProperties GOODBERRY_JUICE = (new FoodProperties.Builder())
            .alwaysEat()
            .effect(() -> new MobEffectInstance(ModEffects.DRUID_HEAL.get(), 3 * 60 * 20, 0), 1.0F)
            .build();
    // 南瓜汁
    public static final FoodProperties PUMPKIN_JUICE = (new FoodProperties.Builder())
            .alwaysEat()
            .effect(() -> nourishment(FoodValues.MEDIUM_DURATION ), 1.0F)
            .build();
    // 紫水晶龙舌兰
    public static final FoodProperties AMETHYST_TEQUILA = new FoodProperties.Builder()
            .alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 100, 4), 1.0F)
            .effect(() -> new MobEffectInstance(ModEffects.CLEANSE.get(), 3 * 60 * 20, 0), 1.0F)
            .build();
    // 奥术热可可
    public static final FoodProperties ARCANE_COCOA = new FoodProperties.Builder()
            .alwaysEat()
            .build();
    // 黄油啤酒
    public static final FoodProperties BUTTERBEER = new FoodProperties.Builder()
            .alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffectRegistry.FORTIFY.get(), 3 * 60 * 20, 4), 1.0F)
            .build();
    // 墓穴红酒
    public static final FoodProperties CATACOMBS_WINE = new FoodProperties.Builder()
            .alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 100, 3), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffectRegistry.VIGOR.get(), 3 * 60 * 20, 2), 1.0F)
            .build();
    // 蛛牙冰酒
    public static final FoodProperties ICE_VENOM_WINE = new FoodProperties.Builder()
            .alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 100, 4), 1.0F)
            .effect(() -> new MobEffectInstance(ModEffects.FROST_SHIELD.get(), 8 * 60 * 20, 2), 1.0F)
            .build();
    // 墨水啤酒
    public static final FoodProperties INK_BEER = new FoodProperties.Builder()
            .alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.DARKNESS, 3 * 60 * 20, 0), 1.0F)
            .build();
    // 雷爪
    public static final FoodProperties MOZHAO = new FoodProperties.Builder()
            .alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffectRegistry.CHARGED.get(), 600, 4), 1.0F)
            .build();
    // 闪避拿铁
    public static final FoodProperties EVASION_MILK = new FoodProperties.Builder()
            .alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffectRegistry.EVASION.get(), 1 * 60 * 20, 4), 1.0F)
            .build();
    // 红丝绒蛋糕
    public static final FoodProperties RED_VELVET_CAKE = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(0.15f)
            .effect(() -> new MobEffectInstance(MobEffectRegistry.VIGOR.get(), 5 * 60 * 20, 5), 1.0F)
            .build();
    // 红丝绒蛋糕切片
    public static final FoodProperties RED_VELVET_CAKE_SLICE = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(0.15f)
            .effect(() -> new MobEffectInstance(MobEffectRegistry.VIGOR.get(), 5 * 60 * 20, 4), 1.0F)
            .build();
    // 神莓派
    public static final FoodProperties GOODBERRY_PIE = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 0), 1.0F)
            .effect(() -> new MobEffectInstance(ModEffects.DRUID_HEAL.get(), 3 * 60 * 20, 0), 1.0F)
            .build();
    // 神莓派片
    public static final FoodProperties GOODBERRY_PIE_SLICE = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(ModEffects.DRUID_HEAL.get(), 3 * 60 * 20, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 0), 1.0F)
            .build();
    // 黄油金苹果
    public static final FoodProperties EDEN_APPLE_TART = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(MobEffectRegistry.FORTIFY.get(), 2 * 60 * 20, 5), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 1), 1.0F)
            .build();
    // 黄油金苹果片
    public static final FoodProperties EDEN_APPLE_TART_SLICE = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(MobEffectRegistry.FORTIFY.get(), 2 * 60 * 20, 5), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 1), 1.0F)
            .build();
    // 永冻冰棍
    public static final FoodProperties PERMAFROST_POPSICLE = new FoodProperties.Builder()
            .alwaysEat()
            .effect(() -> new MobEffectInstance(ModEffects.FROST_SHIELD.get(), 45 * 20, 1), 1.0F)
            .build();
    // 碗装饕餮乱炖
    public static final FoodProperties BOWL_OF_GLUTTON_HOTCHPOTCH = new FoodProperties.Builder()
            .nutrition(32)
            .saturationMod(1f)
            .effect(() -> nourishment(FoodValues.MEDIUM_DURATION), 1.0F)
            .effect(() -> new MobEffectInstance(ModEffects.GOLDEN_ARMOR.get(), 5 * 60 * 20, 2), 1.0F)
            .build();
}