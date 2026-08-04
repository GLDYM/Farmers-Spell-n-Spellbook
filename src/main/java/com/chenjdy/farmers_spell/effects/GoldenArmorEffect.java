package com.chenjdy.farmers_spell.effects;

import com.chenjdy.farmers_spell.FarmersSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class GoldenArmorEffect extends MobEffect {

    private static final String ARMOR_MODIFIER_ID = "56a77a0e-7f37-4326-b13d-456af5392a80";

    @SuppressWarnings("this-escape")
    public GoldenArmorEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        this.addAttributeModifier(Attributes.ARMOR, ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "golden_armor_bonus"), 2.0, AttributeModifier.Operation.ADD_VALUE);
    }
}
