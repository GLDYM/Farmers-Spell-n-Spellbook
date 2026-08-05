package com.chenjdy.farmers_spell.item.irons;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffectInstance;
import vectorwing.farmersdelight.common.item.DrinkableItem;

import javax.annotation.Nullable;
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
    public ItemStack finishUsingItem(ItemStack stack, Level level, net.minecraft.world.entity.LivingEntity entityLiving) {
        if (!level.isClientSide && entityLiving instanceof ServerPlayer player) {
            if (clearNegativeEffects) {
                List<MobEffectInstance> effectsToRemove = new ArrayList<>();
                for (MobEffectInstance effect : player.getActiveEffects()) {
                    if (effect.getEffect().isInstantenous()) continue;
                    if (effect.getEffect().getCategory() == net.minecraft.world.effect.MobEffectCategory.HARMFUL) {
                        effectsToRemove.add(effect);
                    }
                }
                for (MobEffectInstance effect : effectsToRemove) {
                    player.removeEffect(effect.getEffect());
                }
            }
            
            if (manaRecoveryPercent > 0) {
                double maxMana = player.getAttributeValue(AttributeRegistry.MAX_MANA.get());
                double manaRecovery = maxMana * manaRecoveryPercent / 100.0;
                
                MagicData magicData = MagicData.getPlayerMagicData(player);
                float currentMana = magicData.getMana();
                float newMana = (float) Math.min(currentMana + manaRecovery, maxMana);
                magicData.setMana(newMana);
            }
            
            level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        
        return super.finishUsingItem(stack, level, entityLiving);
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        if (manaRecoveryPercent > 0) {
            tooltipComponents.add(Component.literal("- ").append(Component.literal("恢复 " + manaRecoveryPercent + "% 最大法力").withStyle(ChatFormatting.DARK_AQUA)).withStyle(ChatFormatting.GRAY));
        }
        
        if (clearNegativeEffects) {
            tooltipComponents.add(Component.literal("- ").append(Component.literal("清除所有负面效果").withStyle(ChatFormatting.GOLD)).withStyle(ChatFormatting.GRAY));
        }
        
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }
}
