package com.chenjdy.farmers_spell.item.curios;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.init.ModItems;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.Map;
import java.util.WeakHashMap;

public final class RingManaBonusHelper {
    private static final ResourceLocation FOODGEIST_BONUS_ID = ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "spirit_ring_mana_bonus");
    private static final ResourceLocation AFFINITY_BONUS_ID = ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "glutton_ring_mana_bonus");
    private static final Map<Player, Boolean> NOURISHMENT_STATE = new WeakHashMap<>();

    private RingManaBonusHelper() {
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
        boolean hasNourishment = player.hasEffect(ModEffects.NOURISHMENT);
        updateBonus(player, FOODGEIST_BONUS_ID, 75.0D, hasFoodgeistRing && hasNourishment);
        updateBonus(player, AFFINITY_BONUS_ID, 150.0D, hasAffinityRing && hasNourishment);
    }

    public static void updateFoodgeistRing(Player player, boolean equipped) {
        updateBonus(player, FOODGEIST_BONUS_ID, 75.0D, equipped && player.hasEffect(ModEffects.NOURISHMENT));
    }

    public static void updateAffinityRing(Player player, boolean equipped) {
        updateBonus(player, AFFINITY_BONUS_ID, 150.0D, equipped && player.hasEffect(ModEffects.NOURISHMENT));
    }

    public static void clearAll(Player player) {
        updateBonus(player, FOODGEIST_BONUS_ID, 75.0D, false);
        updateBonus(player, AFFINITY_BONUS_ID, 150.0D, false);
        NOURISHMENT_STATE.put(player, false);
    }

    public static void syncNourishmentTransition(Player player) {
        boolean hasNourishment = player.hasEffect(ModEffects.NOURISHMENT);
        Boolean previous = NOURISHMENT_STATE.put(player, hasNourishment);
        if (previous == null || previous != hasNourishment) {
            syncAll(player);
        }
    }

    private static void updateBonus(Player player, ResourceLocation id, double amount, boolean shouldApply) {
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        var attribute = player.getAttributes().getInstance(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.MAX_MANA.get()));
        if (attribute == null) {
            return;
        }
        boolean isApplied = attribute.getModifier(id) != null;
        if (shouldApply && !isApplied) {
            attribute.addPermanentModifier(new AttributeModifier(id, amount, AttributeModifier.Operation.ADD_VALUE));
        } else if (!shouldApply && isApplied) {
            attribute.removeModifier(id);
        }
    }
}
