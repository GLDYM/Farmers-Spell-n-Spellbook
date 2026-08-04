package com.chenjdy.farmers_spell.effects;

import com.chenjdy.farmers_spell.FarmersSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ClawBreakEffect extends MobEffect {
    
    private static final String ATTACK_DAMAGE_MODIFIER_ID = "1a2b3c4d-e5f6-7890-abcd-ef1234567890";

    @SuppressWarnings("this-escape")
    public ClawBreakEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "claw_break_attack_damage"), -0.015, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }
}
