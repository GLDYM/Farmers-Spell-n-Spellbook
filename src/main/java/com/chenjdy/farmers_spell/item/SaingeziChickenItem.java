package com.chenjdy.farmers_spell.item;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.block.SaingeziChickenBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class SaingeziChickenItem extends BlockItem {

    private static final Component PLACEABLE = Component.translatable(FARMERSSPELL.MODID + ".tooltip.placeable")
            .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC);
        

    public SaingeziChickenItem(net.minecraft.world.level.block.Block block, Properties properties) {
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

        BlockPos[] positionsOption1 = {
            clickedPos,
            clickedPos.east(),
            clickedPos.south(),
            clickedPos.east().south()
        };

        BlockPos[] positionsOption2 = {
            clickedPos.west(),
            clickedPos,
            clickedPos.west().south(),
            clickedPos.south()
        };

        BlockPos[] positionsOption3 = {
            clickedPos.north(),
            clickedPos.north().east(),
            clickedPos,
            clickedPos.east()
        };

        BlockPos[] positionsOption4 = {
            clickedPos.north().west(),
            clickedPos.north(),
            clickedPos.west(),
            clickedPos
        };

        BlockPos[][] options = {positionsOption1, positionsOption2, positionsOption3, positionsOption4};

        int[] priorityOrder = getPriorityOrder(facing);
        
        for (int optionIndex : priorityOrder) {
            BlockPos[] positions = options[optionIndex];
            int positionValue = optionIndex;

            boolean allValid = true;
            for (int i = 0; i < 4; i++) {
                if (!canPlaceAt(level, positions[i])) {
                    allValid = false;
                    break;
                }
            }
            
            if (allValid) {
                SaingeziChickenBlock block = (SaingeziChickenBlock) this.getBlock();
                
                for (int i = 0; i < 4; i++) {
                    BlockState blockState = state
                        .setValue(SaingeziChickenBlock.POSITION, i)
                        .setValue(SaingeziChickenBlock.STAGE, 1)
                        .setValue(SaingeziChickenBlock.FACING, facing);
                    level.setBlock(positions[i], blockState, 3);
                }

                context.getItemInHand().shrink(1);
                
                return true;
            }
        }
        
        return false;
    }
    
    private int[] getPriorityOrder(Direction facing) {
        switch (facing) {
            case NORTH:
                return new int[]{2, 3, 1, 0};
            case SOUTH:
                return new int[]{1, 0, 3, 2};
            case EAST:
                return new int[]{1, 3, 0, 2};
            case WEST:
                return new int[]{0, 2, 3, 1};
            default:
                return new int[]{0, 1, 2, 3};
        }
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
