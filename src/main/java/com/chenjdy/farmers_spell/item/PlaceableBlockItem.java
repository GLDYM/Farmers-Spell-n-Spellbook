package com.chenjdy.farmers_spell.item;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;

public class PlaceableBlockItem extends BlockItem {

    private static final Component PLACEABLE = Component.translatable(FARMERSSPELL.MODID + ".tooltip.placeable")
            .withStyle(ChatFormatting.DARK_GRAY)
            .withStyle(ChatFormatting.ITALIC);

    public PlaceableBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable net.minecraft.world.level.Level level,
                                List<Component> tooltipComponents, TooltipFlag flag) {
        tooltipComponents.add(PLACEABLE);
    }
}
