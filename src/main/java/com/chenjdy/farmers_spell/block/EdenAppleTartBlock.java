package com.chenjdy.farmers_spell.block;

import com.chenjdy.farmers_spell.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.block.PieBlock;
import java.util.function.Supplier;

public class EdenAppleTartBlock extends PieBlock {

    public EdenAppleTartBlock(Properties properties, Supplier<Item> pieSlice) {
        super(properties, pieSlice);
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
