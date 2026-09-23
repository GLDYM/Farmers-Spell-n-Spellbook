package com.chenjdy.farmers_spell.block;

import com.chenjdy.farmers_spell.init.ModItems;
import net.minecraft.world.level.block.state.BlockBehaviour;
import vectorwing.farmersdelight.common.block.FeastBlock;

public class GluttonHotchpotchBlock extends FeastBlock {

    public GluttonHotchpotchBlock(BlockBehaviour.Properties properties) {
        super(properties, () -> ModItems.BOWL_OF_GLUTTON_HOTCHPOTCH.get(), false);
    }
}
