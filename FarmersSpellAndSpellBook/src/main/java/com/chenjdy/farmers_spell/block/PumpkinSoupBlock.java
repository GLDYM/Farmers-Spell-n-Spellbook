package com.chenjdy.farmers_spell.block;

import com.chenjdy.farmers_spell.init.ModItems;
import net.minecraft.world.level.block.state.BlockBehaviour;
import vectorwing.farmersdelight.common.block.FeastBlock;

public class PumpkinSoupBlock extends FeastBlock {

    public PumpkinSoupBlock(BlockBehaviour.Properties properties) {
        super(properties, () -> ModItems.BOWL_OF_PUMPKIN_SOUP.get(), false);
    }
}
