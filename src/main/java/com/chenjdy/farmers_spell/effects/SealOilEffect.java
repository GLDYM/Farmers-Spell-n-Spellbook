package com.chenjdy.farmers_spell.effects;

import com.chenjdy.farmers_spell.FarmersSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SealOilEffect extends MobEffect {

    @SuppressWarnings("this-escape")
    public SealOilEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        this.addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "seal_oil_attack_speed"), -0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }
}
