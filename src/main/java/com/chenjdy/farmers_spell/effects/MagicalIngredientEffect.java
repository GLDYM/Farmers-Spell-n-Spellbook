package com.chenjdy.farmers_spell.effects;

import com.chenjdy.farmers_spell.FarmersSpell;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class MagicalIngredientEffect extends MobEffect {

    public MagicalIngredientEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "a7b8c9d0_e1f2_4a5b_9c0d_1e2f3a4b5c6d"), -0.02, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
