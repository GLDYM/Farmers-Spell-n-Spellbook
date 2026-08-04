package com.chenjdy.farmers_spell.block;

import com.chenjdy.farmers_spell.block.entity.CinderousStoveBlockEntity;
import com.chenjdy.farmers_spell.init.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import vectorwing.farmersdelight.common.block.StoveBlock;

import javax.annotation.Nullable;

public class CinderousStoveBlock extends StoveBlock {
    public static final MapCodec<CinderousStoveBlock> CODEC = simpleCodec(CinderousStoveBlock::new);

    @Override
    public MapCodec<StoveBlock> codec() {
        return (MapCodec<StoveBlock>) (MapCodec<?>) CODEC;
    }

    @SuppressWarnings("this-escape")
    public CinderousStoveBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.TRUE));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CinderousStoveBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide && state.getValue(LIT)) {
            return createTickerHelper(blockEntityType, ModBlockEntities.CINDEROUS_STOVE.get(), CinderousStoveBlockEntity::particleTick);
        }
        return createStoveTicker(level, blockEntityType, ModBlockEntities.CINDEROUS_STOVE.get());
    }
}
