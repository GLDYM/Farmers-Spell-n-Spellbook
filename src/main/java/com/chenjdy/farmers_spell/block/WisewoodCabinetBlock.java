package com.chenjdy.farmers_spell.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.BaseEntityBlock;
import vectorwing.farmersdelight.common.block.CabinetBlock;

public class WisewoodCabinetBlock extends CabinetBlock {
    public static final MapCodec<WisewoodCabinetBlock> CODEC = simpleCodec(WisewoodCabinetBlock::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return (MapCodec<? extends BaseEntityBlock>) (MapCodec<?>) CODEC;
    }

    public WisewoodCabinetBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
}
