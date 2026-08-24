package com.chenjdy.farmers_spell.item;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.block.IcebreakerBreadBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;
import java.util.List;

public class IcebreakerBreadItem extends BlockItem {

    private static final Component PLACEABLE = Component.translatable(FARMERSSPELL.MODID + ".tooltip.placeable")
            .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC);

    public IcebreakerBreadItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltipComponents, TooltipFlag flag) {
        tooltipComponents.add(PLACEABLE);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Direction facing = context.getHorizontalDirection().getOpposite();

        BlockPos[][] options = {
                { clickedPos, clickedPos.relative(facing.getOpposite()) },
                { clickedPos.relative(facing), clickedPos }
        };

        for (BlockPos[] option : options) {
            BlockPos block1Pos = option[0];
            BlockPos block2Pos = option[1];

            if (canPlaceAt(level, block1Pos) && canPlaceAt(level, block2Pos)) {
                BlockState state1 = state
                        .setValue(IcebreakerBreadBlock.POSITION, 0)
                        .setValue(IcebreakerBreadBlock.STAGE, 1)
                        .setValue(IcebreakerBreadBlock.FACING, facing);
                BlockState state2 = state
                        .setValue(IcebreakerBreadBlock.POSITION, 1)
                        .setValue(IcebreakerBreadBlock.STAGE, 1)
                        .setValue(IcebreakerBreadBlock.FACING, facing);

                level.setBlock(block1Pos, state1, 3);
                level.setBlock(block2Pos, state2, 3);

                context.getItemInHand().shrink(1);
                return true;
            }
        }

        return false;
    }

    private boolean canPlaceAt(Level level, BlockPos pos) {
        if (!level.isEmptyBlock(pos)) {
            return false;
        }
        return canSupportRigidBlock(level, pos.below());
    }

    private boolean canSupportRigidBlock(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.isFaceSturdy(level, pos, Direction.UP);
    }
}
