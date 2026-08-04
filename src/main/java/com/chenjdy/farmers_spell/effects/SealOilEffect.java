package com.chenjdy.farmers_spell.effects;

import com.chenjdy.farmers_spell.FarmersSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SealOilEffect extends MobEffect {

    private static final String ATTACK_SPEED_MODIFIER_ID = "a1b2c3d4-e5f6-7890-abcd-ef1234567890";

    @SuppressWarnings("this-escape")
    public SealOilEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        this.addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "seal_oil_attack_speed"), -0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }
}
