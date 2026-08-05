package com.chenjdy.farmers_spell.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ClawBreakEffect extends MobEffect {
    
    private static final String ATTACK_DAMAGE_MODIFIER_ID = "1a2b3c4d-e5f6-7890-abcd-ef1234567890";

    public ClawBreakEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE_MODIFIER_ID, -0.015, AttributeModifier.Operation.MULTIPLY_BASE);
    }
}