package com.chenjdy.farmers_spell.item;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.block.SaingeziChickenBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class SaingeziChickenItem extends BlockItem {
    private static final Component PLACEABLE = Component.translatable(FarmersSpell.MODID + ".tooltip.placeable")
            .withStyle(ChatFormatting.DARK_GRAY)
            .withStyle(ChatFormatting.ITALIC);

    public SaingeziChickenItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext level, List<Component> tooltipComponents, TooltipFlag flag) {
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

        for (int optionIndex : getPriorityOrder(facing)) {
            BlockPos[] positions = options[optionIndex];
            boolean allValid = true;
            for (BlockPos pos : positions) {
                if (!canPlaceAt(level, pos)) {
                    allValid = false;
                    break;
                }
            }

            if (allValid) {
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
        return switch (facing) {
            case NORTH -> new int[]{2, 3, 1, 0};
            case SOUTH -> new int[]{1, 0, 3, 2};
            case EAST -> new int[]{1, 3, 0, 2};
            case WEST -> new int[]{0, 2, 3, 1};
            default -> new int[]{0, 1, 2, 3};
        };
    }

    private boolean canPlaceAt(Level level, BlockPos pos) {
        if (!level.isEmptyBlock(pos)) {
            return false;
        }
        BlockPos belowPos = pos.below();
        return level.getBlockState(belowPos).isFaceSturdy(level, belowPos, Direction.UP);
    }
}
