package com.chenjdy.farmers_spell.block.entity;

import com.chenjdy.farmers_spell.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class RedVelvetCakeGlowBlockEntity extends GlowOverlayBlockEntity {

    public RedVelvetCakeGlowBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RED_VELVET_CAKE_GLOW.get(), pos, state);
    }
}
