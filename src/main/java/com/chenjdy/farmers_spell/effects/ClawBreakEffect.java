package com.chenjdy.farmers_spell.effects;

import com.chenjdy.farmers_spell.FarmersSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ClawBreakEffect extends MobEffect {
    private static final double ATTACK_DAMAGE_PER_LEVEL = -3.0D;
    
    public ClawBreakEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE,
                ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "claw_break_attack_damage"),
                ATTACK_DAMAGE_PER_LEVEL, AttributeModifier.Operation.ADD_VALUE);
    }
}
