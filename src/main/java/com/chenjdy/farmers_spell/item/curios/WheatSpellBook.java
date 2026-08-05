package com.chenjdy.farmers_spell.item.curios;

import io.redspace.ironsspellbooks.item.SpellBook;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WheatSpellBook extends SpellBook {
    public WheatSpellBook(int maxSpellSlots, Item.Properties properties) {
        super(maxSpellSlots, properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> lines, TooltipFlag flag) {
        super.appendHoverText(itemStack, level, lines, flag);
    }
}
