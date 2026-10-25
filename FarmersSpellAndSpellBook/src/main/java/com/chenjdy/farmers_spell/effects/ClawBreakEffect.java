package com.chenjdy.farmers_spell.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ClawBreakEffect extends MobEffect {

    private static final String ATTACK_DAMAGE_MODIFIER_ID = "1a2b3c4d-e5f6-7890-abcd-ef1234567890";
    private static final double ATTACK_DAMAGE_PER_LEVEL = -3.0D;

    public ClawBreakEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE_MODIFIER_ID, ATTACK_DAMAGE_PER_LEVEL, AttributeModifier.Operation.ADDITION);
    }
}