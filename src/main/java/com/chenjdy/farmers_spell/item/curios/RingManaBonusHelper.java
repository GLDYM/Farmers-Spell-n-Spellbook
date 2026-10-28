package com.chenjdy.farmers_spell.item.curios;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.init.ModEffects;
import com.chenjdy.farmers_spell.init.ModItems;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;

import static vectorwing.farmersdelight.common.registry.ModEffects.COMFORT;
import static vectorwing.farmersdelight.common.registry.ModEffects.NOURISHMENT;

import java.util.Map;
import java.util.WeakHashMap;

public final class RingManaBonusHelper {
    private static final double FOODGEIST_MANA_BONUS = 75.0D;
    private static final double AFFINITY_MANA_BONUS = 150.0D;
    private static final ResourceLocation FOODGEIST_MANA_ID = id("foodgeist_ring_mana");
    private static final ResourceLocation AFFINITY_MANA_ID = id("glutton_ring_mana");
    private static final ResourceLocation FOODGEIST_SPEED_ID = id("foodgeist_ring_speed");
    private static final ResourceLocation FOODGEIST_ATTACK_SPEED_ID = id("foodgeist_ring_attack_speed");
    private static final ResourceLocation FOODGEIST_MINING_SPEED_ID = id("foodgeist_ring_mining_speed");
    private static final ResourceLocation FOODGEIST_COOLDOWN_ID = id("foodgeist_ring_cooldown");
    private static final ResourceLocation FOODGEIST_CAST_TIME_ID = id("foodgeist_ring_cast_time");
    private static final ResourceLocation AFFINITY_SPEED_ID = id("glutton_ring_speed");
    private static final ResourceLocation AFFINITY_ATTACK_SPEED_ID = id("glutton_ring_attack_speed");
    private static final ResourceLocation AFFINITY_MINING_SPEED_ID = id("glutton_ring_mining_speed");
    private static final ResourceLocation AFFINITY_COOLDOWN_ID = id("glutton_ring_cooldown");
    private static final ResourceLocation AFFINITY_CAST_TIME_ID = id("glutton_ring_cast_time");

    // Lao Wu~
    private static final ResourceLocation OLD_FOODGEIST_MANA_ID = id("spirit_ring_mana_bonus");
    private static final ResourceLocation OLD_AFFINITY_MANA_ID = id("glutton_ring_mana_bonus");
    private static final ResourceLocation OLD_AFFINITY_SPELL_POWER_ID = id("glutton_ring_spell_power");

    private static final Map<Player, Boolean> NOURISHMENT_STATE = new WeakHashMap<>();

    private RingManaBonusHelper() {
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, path);
    }

    public static void syncAll(Player player) {
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        boolean hasFoodgeistRing = CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.isEquipped(ModItems.FOODGEIST_RING.get()))
                .orElse(false);
        boolean hasAffinityRing = CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.isEquipped(ModItems.AFFINITY_RING_GLUTTON.get()))
                .orElse(false);
        boolean hasFoodBlessing = hasFoodBlessing(player);
        removeLegacyModifier(player, BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.MAX_MANA.get()), OLD_FOODGEIST_MANA_ID);
        removeLegacyModifier(player, BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.MAX_MANA.get()), OLD_AFFINITY_MANA_ID);
        removeLegacyModifier(player, BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.SPELL_POWER.get()), OLD_AFFINITY_SPELL_POWER_ID);
        updateBonus(player, BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.MAX_MANA.get()), FOODGEIST_MANA_ID,
                FOODGEIST_MANA_BONUS, AttributeModifier.Operation.ADD_VALUE, hasFoodgeistRing && hasFoodBlessing);
        updateBonus(player, Attributes.MOVEMENT_SPEED, FOODGEIST_SPEED_ID, 0.10D, hasFoodgeistRing && hasFoodBlessing);
        updateBonus(player, Attributes.ATTACK_SPEED, FOODGEIST_ATTACK_SPEED_ID, 0.10D, hasFoodgeistRing && hasFoodBlessing);
        updateBonus(player, Attributes.BLOCK_BREAK_SPEED, FOODGEIST_MINING_SPEED_ID, 0.10D, hasFoodgeistRing && hasFoodBlessing);
        updateBonus(player, BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.COOLDOWN_REDUCTION.get()), FOODGEIST_COOLDOWN_ID, 0.10D, hasFoodgeistRing && player.hasEffect(ModEffects.CLEANSE));
        updateBonus(player, BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.CAST_TIME_REDUCTION.get()), FOODGEIST_CAST_TIME_ID, 0.10D, hasFoodgeistRing && player.hasEffect(ModEffects.CLEANSE));
        updateBonus(player, BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.MAX_MANA.get()), AFFINITY_MANA_ID,
                AFFINITY_MANA_BONUS, AttributeModifier.Operation.ADD_VALUE, hasAffinityRing && hasFoodBlessing);
        updateBonus(player, Attributes.MOVEMENT_SPEED, AFFINITY_SPEED_ID, 0.20D, hasAffinityRing && hasFoodBlessing);
        updateBonus(player, Attributes.ATTACK_SPEED, AFFINITY_ATTACK_SPEED_ID, 0.20D, hasAffinityRing && hasFoodBlessing);
        updateBonus(player, Attributes.BLOCK_BREAK_SPEED, AFFINITY_MINING_SPEED_ID, 0.20D, hasAffinityRing && hasFoodBlessing);
        updateBonus(player, BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.COOLDOWN_REDUCTION.get()), AFFINITY_COOLDOWN_ID, 0.15D, hasAffinityRing && player.hasEffect(ModEffects.CLEANSE));
        updateBonus(player, BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.CAST_TIME_REDUCTION.get()), AFFINITY_CAST_TIME_ID, 0.15D, hasAffinityRing && player.hasEffect(ModEffects.CLEANSE));
    }

    public static void updateFoodgeistRing(Player player, boolean equipped) {
        syncAll(player);
    }

    public static void updateAffinityRing(Player player, boolean equipped) {
        syncAll(player);
    }

    public static void clearAll(Player player) {
        updateBonus(player, BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.MAX_MANA.get()), FOODGEIST_MANA_ID,
                FOODGEIST_MANA_BONUS, AttributeModifier.Operation.ADD_VALUE, false);
        updateBonus(player, Attributes.MOVEMENT_SPEED, FOODGEIST_SPEED_ID, 0.10D, false);
        updateBonus(player, Attributes.ATTACK_SPEED, FOODGEIST_ATTACK_SPEED_ID, 0.10D, false);
        updateBonus(player, Attributes.BLOCK_BREAK_SPEED, FOODGEIST_MINING_SPEED_ID, 0.10D, false);
        updateBonus(player, BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.COOLDOWN_REDUCTION.get()), FOODGEIST_COOLDOWN_ID, 0.10D, false);
        updateBonus(player, BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.CAST_TIME_REDUCTION.get()), FOODGEIST_CAST_TIME_ID, 0.10D, false);
        updateBonus(player, BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.MAX_MANA.get()), AFFINITY_MANA_ID,
                AFFINITY_MANA_BONUS, AttributeModifier.Operation.ADD_VALUE, false);
        updateBonus(player, Attributes.MOVEMENT_SPEED, AFFINITY_SPEED_ID, 0.20D, false);
        updateBonus(player, Attributes.ATTACK_SPEED, AFFINITY_ATTACK_SPEED_ID, 0.20D, false);
        updateBonus(player, Attributes.BLOCK_BREAK_SPEED, AFFINITY_MINING_SPEED_ID, 0.20D, false);
        updateBonus(player, BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.COOLDOWN_REDUCTION.get()), AFFINITY_COOLDOWN_ID, 0.15D, false);
        updateBonus(player, BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.CAST_TIME_REDUCTION.get()), AFFINITY_CAST_TIME_ID, 0.15D, false);
        NOURISHMENT_STATE.put(player, false);
    }

    public static void syncNourishmentTransition(Player player) {
        boolean hasFoodBlessing = hasFoodBlessing(player);
        Boolean previous = NOURISHMENT_STATE.put(player, hasFoodBlessing);
        if (previous == null || previous != hasFoodBlessing) {
            syncAll(player);
        }
    }

    private static boolean hasFoodBlessing(Player player) {
        return player.hasEffect(NOURISHMENT) || player.hasEffect(COMFORT);
    }

    private static void updateBonus(Player player, Holder<Attribute> attributeHolder,
                                    ResourceLocation id, double amount, boolean shouldApply) {
        updateBonus(player, attributeHolder, id, amount,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE, shouldApply);
    }

    private static void updateBonus(Player player, Holder<Attribute> attributeHolder,
                                    ResourceLocation id, double amount,
                                    AttributeModifier.Operation operation, boolean shouldApply) {
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        var attribute = player.getAttributes().getInstance(attributeHolder);
        if (attribute == null) {
            return;
        }
        AttributeModifier existing = attribute.getModifier(id);
        boolean needsUpdate = existing == null
                || Double.compare(existing.amount(), amount) != 0
                || existing.operation() != operation;
        if (shouldApply && needsUpdate) {
            if (existing != null) {
                attribute.removeModifier(id);
            }
            attribute.addPermanentModifier(new AttributeModifier(id, amount, operation));
        } else if (!shouldApply && existing != null) {
            attribute.removeModifier(id);
        }
    }

    private static void removeLegacyModifier(Player player,
                                              Holder<Attribute> attributeHolder,
                                              ResourceLocation id) {
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        var attribute = player.getAttributes().getInstance(attributeHolder);
        if (attribute != null) {
            attribute.removeModifier(id);
        }
    }
}
