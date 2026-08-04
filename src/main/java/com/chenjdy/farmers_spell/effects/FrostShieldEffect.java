package com.chenjdy.farmers_spell.effects;

import com.chenjdy.farmers_spell.FarmersSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class FrostShieldEffect extends MobEffect {

    private static final String KNOCKBACK_RESISTANCE_MODIFIER_ID = "b3c4d5e6-f7a8-9012-3456-789012345678";

    @SuppressWarnings("this-escape")
    public FrostShieldEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        this.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "frost_shield_knockback_resistance"), 0.25, AttributeModifier.Operation.ADD_VALUE);
    }
}
