package com.chenjdy.farmers_spell.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class MagicalIngredientEffect extends MobEffect {

    public MagicalIngredientEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "a7b8c9d0-e1f2-4a5b-9c0d-1e2f3a4b5c6d", -0.02, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
