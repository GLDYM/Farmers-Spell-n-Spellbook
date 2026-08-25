package com.chenjdy.farmers_spell.item;

import com.chenjdy.farmers_spell.block.IcebreakerBreadBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;


public class IcebreakerBreadItem extends PlaceableBlockItem {
    public IcebreakerBreadItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        Level level = context.getLevel();
        Direction facing = context.getHorizontalDirection().getOpposite();
        BlockPos first = context.getClickedPos();
        BlockPos second = first.relative(facing.getOpposite());
        if (!canPlace(level, first) || !canPlace(level, second))
            return false;
        level.setBlock(first, state.setValue(IcebreakerBreadBlock.POSITION, 0).setValue(IcebreakerBreadBlock.STAGE, 0)
                .setValue(IcebreakerBreadBlock.FACING, facing), 3);
        level.setBlock(second, state.setValue(IcebreakerBreadBlock.POSITION, 1).setValue(IcebreakerBreadBlock.STAGE, 0)
                .setValue(IcebreakerBreadBlock.FACING, facing), 3);
        context.getItemInHand().shrink(1);
        return true;
    }

    private boolean canPlace(Level level, BlockPos pos) {
        return level.isEmptyBlock(pos)
                && level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
    }
}
