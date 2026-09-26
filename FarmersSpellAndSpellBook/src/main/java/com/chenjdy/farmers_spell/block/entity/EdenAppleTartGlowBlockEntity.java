package com.chenjdy.farmers_spell.block.entity;

import com.chenjdy.farmers_spell.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class EdenAppleTartGlowBlockEntity extends GlowOverlayBlockEntity {

    public EdenAppleTartGlowBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.EDEN_APPLE_TART_GLOW.get(), pos, state);
    }
}
