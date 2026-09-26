package com.chenjdy.farmers_spell.block;

import com.chenjdy.farmers_spell.block.entity.EdenAppleTartGlowBlockEntity;
import com.chenjdy.farmers_spell.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.block.PieBlock;
import java.util.function.Supplier;

import javax.annotation.Nullable;

public class EdenAppleTartBlock extends PieBlock implements EntityBlock {

    public EdenAppleTartBlock(Properties properties, Supplier<Item> pieSlice) {
        super(properties, pieSlice);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EdenAppleTartGlowBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(2) == 0) {
            double x = pos.getX() + 0.2 + random.nextDouble() * 0.6;
            double y = pos.getY() + 0.1 + random.nextDouble() * 0.3;
            double z = pos.getZ() + 0.2 + random.nextDouble() * 0.6;
            level.addParticle(ModParticles.GOLDEN_SPARKLE.get(), x, y, z, 0.0, 0.0, 0.0);
        }
    }
}
