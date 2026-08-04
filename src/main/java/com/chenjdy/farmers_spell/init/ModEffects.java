package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.effects.CleanseEffect;
import com.chenjdy.farmers_spell.effects.ClawBreakEffect;
import com.chenjdy.farmers_spell.effects.DruidHealEffect;
import com.chenjdy.farmers_spell.effects.FrostShieldEffect;
import com.chenjdy.farmers_spell.effects.SealOilEffect;
import com.chenjdy.farmers_spell.effects.GoldenArmorEffect;
import com.chenjdy.farmers_spell.effects.MagicalIngredientEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
public class ModEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, FARMERSSPELL.MODID);

    // 德鲁医
    public static final DeferredHolder<MobEffect, MobEffect> DRUID_HEAL = MOB_EFFECTS.register("druid_heal",
            () -> new DruidHealEffect(MobEffectCategory.BENEFICIAL, 0x00FF00));
    // 油封
    public static final DeferredHolder<MobEffect, MobEffect> SEAL_OIL = MOB_EFFECTS.register("seal_oil",
            () -> new SealOilEffect(MobEffectCategory.BENEFICIAL, 0xFFD700));
    // 澄净
    public static final DeferredHolder<MobEffect, MobEffect> CLEANSE = MOB_EFFECTS.register("cleanse",
            () -> new CleanseEffect(MobEffectCategory.BENEFICIAL, 0x87CEEB));
    // 霜衣
    public static final DeferredHolder<MobEffect, MobEffect> FROST_SHIELD = MOB_EFFECTS.register("frost_shield",
            () -> new FrostShieldEffect(MobEffectCategory.BENEFICIAL, 0xB0E0E6));
    // 断螯
    public static final DeferredHolder<MobEffect, MobEffect> CLAW_BREAK = MOB_EFFECTS.register("claw_break",
            () -> new ClawBreakEffect(MobEffectCategory.HARMFUL, 0xFF6B6B));
    // 金甲
    public static final DeferredHolder<MobEffect, MobEffect> GOLDEN_ARMOR = MOB_EFFECTS.register("golden_armor",
            () -> new GoldenArmorEffect(MobEffectCategory.BENEFICIAL, 0xFFD700));
    // 魔法食材
    public static final DeferredHolder<MobEffect, MobEffect> MAGICAL_INGREDIENT = MOB_EFFECTS.register("magical_ingredient",
            () -> new MagicalIngredientEffect(MobEffectCategory.HARMFUL, 0x9966CC));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
