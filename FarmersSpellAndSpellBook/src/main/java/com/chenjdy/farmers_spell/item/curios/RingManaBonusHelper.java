package com.chenjdy.farmers_spell.item.curios;

import com.chenjdy.farmers_spell.init.ModItems;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public final class RingManaBonusHelper {

    private static final UUID FOODGEIST_MANA_UUID = UUID.fromString("F4A5B6C7-D8E9-4F0A-1B2C-3D4E5F6A7B8C");
    private static final UUID AFFINITY_MANA_UUID = UUID.fromString("A5B6C7D8-E9F0-4A5B-8C9D-0E1F2A3B4C5D");
    private static final UUID FOODGEIST_MOVE_UUID = UUID.fromString("1A2B3C4D-5E6F-4A5B-9C8D-7E8F9A0B1C2D");
    private static final UUID AFFINITY_MOVE_UUID = UUID.fromString("2B3C4D5E-6F7A-4B5C-AD9E-8F9A0B1C2D3E");
    private static final UUID FOODGEIST_ATTACK_SPEED_UUID = UUID.fromString("3C4D5E6F-7A8B-4C6D-BEAF-9A0B1C2D3E4F");
    private static final UUID AFFINITY_ATTACK_SPEED_UUID = UUID.fromString("4D5E6F7A-8B9C-4D7E-CFB0-A0B1C2D3E4F5");
    private static final UUID FOODGEIST_COOLDOWN_UUID = UUID.fromString("5E6F7A8B-9C0D-4E7F-A1B2-C3D4E5F6A7B8");
    private static final UUID AFFINITY_COOLDOWN_UUID = UUID.fromString("6F7A8B9C-0D1E-4F8A-B2C3-D4E5F6A7B8C9");
    private static final UUID FOODGEIST_CAST_TIME_UUID = UUID.fromString("7A8B9C0D-1E2F-4A9B-C3D4-E5F6A7B8C9D0");
    private static final UUID AFFINITY_CAST_TIME_UUID = UUID.fromString("8B9C0D1E-2F3A-4B0C-D4E5-F6A7B8C9D0E1");

    private static final double FOODGEIST_MANA_BONUS = 75.0D;
    private static final double AFFINITY_MANA_BONUS = 150.0D;
    private static final double FOODGEIST_SPEED_BONUS = 0.10D;
    private static final double AFFINITY_SPEED_BONUS = 0.20D;
    private static final double FOODGEIST_CLEANSE_BONUS = 0.10D;
    private static final double AFFINITY_CLEANSE_BONUS = 0.15D;

    private static final Map<Player, Boolean> FOOD_BLESSING_STATE = new WeakHashMap<>();
    private static final Map<Player, Boolean> CLEANSE_STATE = new WeakHashMap<>();

    private RingManaBonusHelper() {
    }

    public static void syncAll(Player player) {
        if (!(player instanceof net.minecraft.server.level.ServerPlayer)) {
            return;
        }
        boolean hasFoodgeistRing = isRingEquipped(player, ModItems.FOODGEIST_RING.get());
        boolean hasAffinityRing = isRingEquipped(player, ModItems.AFFINITY_RING_GLUTTON.get());
        boolean hasFoodBlessing = hasNourishmentOrComfort(player);
        boolean hasCleanse = player.hasEffect(com.chenjdy.farmers_spell.init.ModEffects.CLEANSE.get());

        updateModifier(player, AttributeRegistry.MAX_MANA.get(), FOODGEIST_MANA_UUID,
                "Foodgeist Ring Mana Bonus", FOODGEIST_MANA_BONUS,
                AttributeModifier.Operation.ADDITION, hasFoodgeistRing && hasFoodBlessing);
        updateModifier(player, AttributeRegistry.MAX_MANA.get(), AFFINITY_MANA_UUID,
                "Glutton Ring Mana Bonus", AFFINITY_MANA_BONUS,
                AttributeModifier.Operation.ADDITION, hasAffinityRing && hasFoodBlessing);

        updateModifier(player, Attributes.MOVEMENT_SPEED, FOODGEIST_MOVE_UUID,
                "Foodgeist Ring Move Bonus", FOODGEIST_SPEED_BONUS,
                AttributeModifier.Operation.MULTIPLY_BASE, hasFoodgeistRing && hasFoodBlessing);
        updateModifier(player, Attributes.MOVEMENT_SPEED, AFFINITY_MOVE_UUID,
                "Glutton Ring Move Bonus", AFFINITY_SPEED_BONUS,
                AttributeModifier.Operation.MULTIPLY_BASE, hasAffinityRing && hasFoodBlessing);
        updateModifier(player, Attributes.ATTACK_SPEED, FOODGEIST_ATTACK_SPEED_UUID,
                "Foodgeist Ring Attack Speed Bonus", FOODGEIST_SPEED_BONUS,
                AttributeModifier.Operation.MULTIPLY_BASE, hasFoodgeistRing && hasFoodBlessing);
        updateModifier(player, Attributes.ATTACK_SPEED, AFFINITY_ATTACK_SPEED_UUID,
                "Glutton Ring Attack Speed Bonus", AFFINITY_SPEED_BONUS,
                AttributeModifier.Operation.MULTIPLY_BASE, hasAffinityRing && hasFoodBlessing);

        updateModifier(player, AttributeRegistry.COOLDOWN_REDUCTION.get(), FOODGEIST_COOLDOWN_UUID,
                "Foodgeist Ring Cleanse Cooldown", FOODGEIST_CLEANSE_BONUS,
                AttributeModifier.Operation.MULTIPLY_BASE, hasFoodgeistRing && hasCleanse);
        updateModifier(player, AttributeRegistry.COOLDOWN_REDUCTION.get(), AFFINITY_COOLDOWN_UUID,
                "Glutton Ring Cleanse Cooldown", AFFINITY_CLEANSE_BONUS,
                AttributeModifier.Operation.MULTIPLY_BASE, hasAffinityRing && hasCleanse);
        updateModifier(player, AttributeRegistry.CAST_TIME_REDUCTION.get(), FOODGEIST_CAST_TIME_UUID,
                "Foodgeist Ring Cleanse Cast Time", FOODGEIST_CLEANSE_BONUS,
                AttributeModifier.Operation.MULTIPLY_BASE, hasFoodgeistRing && hasCleanse);
        updateModifier(player, AttributeRegistry.CAST_TIME_REDUCTION.get(), AFFINITY_CAST_TIME_UUID,
                "Glutton Ring Cleanse Cast Time", AFFINITY_CLEANSE_BONUS,
                AttributeModifier.Operation.MULTIPLY_BASE, hasAffinityRing && hasCleanse);
    }

    public static void updateFoodgeistRing(Player player, boolean equipped) {
        syncAll(player);
    }

    public static void updateAffinityRing(Player player, boolean equipped) {
        syncAll(player);
    }

    public static void syncFoodBlessingTransition(Player player) {
        boolean hasFoodBlessing = hasNourishmentOrComfort(player);
        boolean hasCleanse = player.hasEffect(com.chenjdy.farmers_spell.init.ModEffects.CLEANSE.get());
        Boolean prevBlessing = FOOD_BLESSING_STATE.put(player, hasFoodBlessing);
        Boolean prevCleanse = CLEANSE_STATE.put(player, hasCleanse);
        if (prevBlessing == null || prevBlessing != hasFoodBlessing
                || prevCleanse == null || prevCleanse != hasCleanse) {
            syncAll(player);
        }
    }

    public static void resetFoodBlessingState(Player player) {
        FOOD_BLESSING_STATE.remove(player);
        CLEANSE_STATE.remove(player);
    }

    public static boolean isFoodgeistRingEquipped(Player player) {
        return isRingEquipped(player, ModItems.FOODGEIST_RING.get());
    }

    public static boolean isAffinityRingEquipped(Player player) {
        return isRingEquipped(player, ModItems.AFFINITY_RING_GLUTTON.get());
    }

    public static boolean isAnyRingEquipped(Player player) {
        return isFoodgeistRingEquipped(player) || isAffinityRingEquipped(player);
    }

    public static boolean hasNourishmentOrComfort(Player player) {
        return player.hasEffect(ModEffects.NOURISHMENT.get()) || player.hasEffect(ModEffects.COMFORT.get());
    }

    private static boolean isRingEquipped(Player player, net.minecraft.world.item.Item ring) {
        return CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.isEquipped(ring))
                .orElse(false);
    }

    private static void updateModifier(Player player, Attribute attribute, UUID id, String name,
                                       double amount, AttributeModifier.Operation operation, boolean shouldApply) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance == null) {
            return;
        }
        boolean isApplied = instance.getModifier(id) != null;
        if (shouldApply && !isApplied) {
            instance.addPermanentModifier(new AttributeModifier(id, name, amount, operation));
        } else if (!shouldApply && isApplied) {
            instance.removeModifier(id);
        }
    }
}
