package com.chenjdy.farmers_spell.item.weapons;

import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

public class GospelButterKnife extends MagicSwordItem {
    public GospelButterKnife() {
        super(
            WeaponRepairTiers.GOSPEL,
            new Item.Properties()
                .durability(1561)
                .rarity(Rarity.RARE)
                .attributes(SwordItem.createAttributes(Tiers.IRON, 1.5F, -2F)),
            SpellDataRegistryHolder.of(new SpellDataRegistryHolder(SpellRegistry.WISP_SPELL, 8))
        );
    }

    @Override
    public float getAttackDamageBonus(Entity target, float damage, DamageSource damageSource) {
        return target.getType().is(EntityTypeTags.UNDEAD) ? 2.5F : 0.0F;
    }
}
