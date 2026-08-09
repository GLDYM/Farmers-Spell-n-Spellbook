package com.chenjdy.farmers_spell.item.irons;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.network.SyncManaPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import vectorwing.farmersdelight.common.item.DrinkableItem;

import java.util.ArrayList;
import java.util.List;

public class DrinkItem extends DrinkableItem {
    private final int manaRecoveryPercent;
    private final boolean clearNegativeEffects;

    public DrinkItem(Item.Properties properties, int manaRecoveryPercent, boolean clearNegativeEffects) {
        super(properties);
        this.manaRecoveryPercent = manaRecoveryPercent;
        this.clearNegativeEffects = clearNegativeEffects;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        if (!level.isClientSide && entityLiving instanceof ServerPlayer player) {
            if (clearNegativeEffects) {
                List<MobEffectInstance> effectsToRemove = new ArrayList<>();
                for (MobEffectInstance effect : player.getActiveEffects()) {
                    if (effect.getEffect().value().isInstantenous()) {
                        continue;
                    }
                    if (effect.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
                        effectsToRemove.add(effect);
                    }
                }
                for (MobEffectInstance effect : effectsToRemove) {
                    player.removeEffect(effect.getEffect());
                }
            }

            if (manaRecoveryPercent > 0) {
                double maxMana = player.getAttributeValue(AttributeRegistry.MAX_MANA);
                double manaRecovery = maxMana * manaRecoveryPercent / 100.0;
                MagicData magicData = MagicData.getPlayerMagicData(player);
                float currentMana = magicData.getMana();
                float newMana = (float) Math.min(currentMana + manaRecovery, maxMana);
                magicData.setMana(newMana);
                if (player instanceof ServerPlayer serverPlayer) {
                    PacketDistributor.sendToPlayer(serverPlayer, new SyncManaPacket(magicData));
                }
            }

            level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        return super.finishUsingItem(stack, level, entityLiving);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        if (manaRecoveryPercent > 0) {
            tooltipComponents.add(Component.literal("- ")
                    .append(Component.translatable("item.farmers_spell.tooltip.mana_recovery", manaRecoveryPercent).withStyle(ChatFormatting.DARK_AQUA))
                    .withStyle(ChatFormatting.GRAY));
        }

        if (clearNegativeEffects) {
            tooltipComponents.add(Component.literal("- ")
                    .append(Component.translatable("item.farmers_spell.tooltip.clear_effects").withStyle(ChatFormatting.GOLD))
                    .withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }
}
