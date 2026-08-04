package com.chenjdy.farmers_spell.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import vectorwing.farmersdelight.common.block.CabinetBlock;

public class WisewoodCabinetBlock extends CabinetBlock {
    public static final com.mojang.serialization.MapCodec<WisewoodCabinetBlock> CODEC = simpleCodec(WisewoodCabinetBlock::new);

    @Override
    protected com.mojang.serialization.MapCodec<? extends net.minecraft.world.level.block.Block> codec() {
        return CODEC;
    }

    public WisewoodCabinetBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
}