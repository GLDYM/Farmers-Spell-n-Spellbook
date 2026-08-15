package com.chenjdy.farmers_spell.item.curios;

import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class AffinityRingGlutton extends CurioBaseItem {

    public AffinityRingGlutton(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatable("item.farmers_spell.affinity_ring_glutton.tooltip"));
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack previousStack, ItemStack currentStack) {
        if (slotContext.entity() instanceof Player player) {
            RingManaBonusHelper.updateAffinityRing(player, true);
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack currentStack) {
        if (slotContext.entity() instanceof Player player) {
            RingManaBonusHelper.updateAffinityRing(player, false);
        }
    }
}
