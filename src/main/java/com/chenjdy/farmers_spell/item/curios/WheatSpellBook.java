package com.chenjdy.farmers_spell.item.curios;

import io.redspace.ironsspellbooks.item.SpellBook;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class WheatSpellBook extends SpellBook {
    public WheatSpellBook(int maxSpellSlots, Item.Properties properties) {
        super(maxSpellSlots, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context,
                                List<Component> tooltipComponents, TooltipFlag flag) {
        tooltipComponents.add(Component.translatable(this.getDescriptionId() + ".tooltip")
                .withStyle(ChatFormatting.GRAY)
                .withStyle(ChatFormatting.ITALIC));
        super.appendHoverText(stack, context, tooltipComponents, flag);
    }
}
