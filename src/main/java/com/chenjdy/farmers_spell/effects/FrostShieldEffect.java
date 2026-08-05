package com.chenjdy.farmers_spell.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class FrostShieldEffect extends MobEffect {

    private static final String KNOCKBACK_RESISTANCE_MODIFIER_ID = "b3c4d5e6-f7a8-9012-3456-789012345678";

    public FrostShieldEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        this.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_RESISTANCE_MODIFIER_ID, 0.25, AttributeModifier.Operation.ADDITION);
    }
}
