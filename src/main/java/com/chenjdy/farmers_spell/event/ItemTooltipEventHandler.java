package com.chenjdy.farmers_spell.event;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.entity.FoodgeistEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(modid = FarmersSpell.MODID)
public final class ItemTooltipEventHandler {
    private ItemTooltipEventHandler() {
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().is(FoodgeistEntity.FOODGEIST_FOOD)) {
            event.getToolTip().add(Component.translatable(FarmersSpell.MODID + ".tooltip.foodgeist_food")
                    .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
    }
}
