package com.chenjdy.farmers_spell.block;

import com.mojang.serialization.MapCodec;
import com.chenjdy.farmers_spell.init.ModItems;
import com.chenjdy.farmers_spell.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class IcebreakerBreadBlock extends Block {
    public static final MapCodec<IcebreakerBreadBlock> CODEC = Block.simpleCodec(IcebreakerBreadBlock::new);
    public static final IntegerProperty POSITION = IntegerProperty.create("position", 0, 1);
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 8);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public IcebreakerBreadBlock(Properties p) {
        super(p);
        registerDefaultState(
                stateDefinition.any().setValue(POSITION, 0).setValue(STAGE, 0).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int position = state.getValue(POSITION), stage = state.getValue(STAGE);
        int length = position == 0 ? switch (stage) {
            case 0, 1, 2 -> 16;
            case 3 -> 10;
            case 4 -> 6;
            case 5 -> 2;
            default -> 0;
        }
                : switch (stage) {
                    case 0, 1, 2, 3, 4, 5 -> 16;
                    case 6 -> 14;
                    case 7 -> 10;
                    case 8 -> 6;
                    default -> 16;
                };
        if (length <= 0)
            return Shapes.empty();
        int start = 16 - length;
        return switch (state.getValue(FACING)) {
            case NORTH -> Block.box(0, 0, start, 16, 7, 16);
            case SOUTH -> Block.box(0, 0, 0, 16, 7, length);
            case EAST -> Block.box(0, 0, 0, length, 7, 16);
            case WEST -> Block.box(start, 0, 0, 16, 7, 16);
            default -> Shapes.block();
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(POSITION, STAGE, FACING);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hit) {
        if (!stack.isEmpty())
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        return eat(state, level, pos, player);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hit) {
        eat(state, level, pos, player);
        return level.isClientSide ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
    }

    private ItemInteractionResult eat(BlockState state, Level level, BlockPos pos, Player player) {
        int stage = state.getValue(STAGE);
        if (stage <= 8 && !level.isClientSide) {
            int newStage = stage + 1;
            int position = state.getValue(POSITION);
            Direction facing = state.getValue(FACING);

            ItemStack reward = new ItemStack(stage <= 1
                    ? ModItems.ICEBERGCREAM.get()
                    : ModItems.ICEBERGCREAM_SANDWICH.get());
            if (!player.getInventory().add(reward)) {
                Block.popResource(level, pos, reward);
            }

            if (newStage == 9) {
                destroyStructure(level, pos, state);
            } else {
                updateAllBlocksStage(level, pos, state, newStage);
                if (newStage == 6) {
                    BlockPos originPos = getOriginPos(pos, position, facing);
                    BlockPos firstHalf = getBlockPos(originPos, 0, facing);
                    if (level.getBlockState(firstHalf).is(this)) {
                        level.removeBlock(firstHalf, false);
                    }
                }
            }
            level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_IRON.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    private void updateAllBlocksStage(Level level, BlockPos pos, BlockState state, int newStage) {
        BlockPos originPos = getOriginPos(pos, state.getValue(POSITION), state.getValue(FACING));
        for (int position = 0; position < 2; position++) {
            BlockPos blockPos = getBlockPos(originPos, position, state.getValue(FACING));
            BlockState blockState = level.getBlockState(blockPos);
            if (blockState.is(this))
                level.setBlock(blockPos, blockState.setValue(STAGE, newStage), 3);
        }
    }

    private void destroyStructure(Level level, BlockPos pos, BlockState state) {
        BlockPos originPos = getOriginPos(pos, state.getValue(POSITION), state.getValue(FACING));
        for (int position = 0; position < 2; position++) {
            BlockPos blockPos = getBlockPos(originPos, position, state.getValue(FACING));
            if (level.getBlockState(blockPos).is(this))
                level.removeBlock(blockPos, false);
        }
    }



    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(POSITION, 0).setValue(STAGE, 0)
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return below.isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState facingState,
            LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (direction == Direction.DOWN && !canSurvive(state, level, currentPos)) {
            BlockPos originPos = getOriginPos(currentPos, state.getValue(POSITION), state.getValue(FACING));
            BlockPos partner = getBlockPos(originPos, 1 - state.getValue(POSITION), state.getValue(FACING));
            BlockState partnerState = level.getBlockState(partner);
            if (partnerState.is(this)
                    && level.getBlockState(partner.below()).isFaceSturdy(level, partner.below(), Direction.UP))
                return state;
            if (!level.isClientSide() && level instanceof Level actualLevel && state.getValue(STAGE) == 0) {
                Block.popResource(actualLevel, currentPos, new ItemStack(ModBlocks.ICEBREAKER_BREAD.get().asItem()));
            }
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, facingState, level, currentPos, facingPos);
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.is(newState.getBlock())) {
            return;
        }
        if (!level.isClientSide && state.is(this)) {
            if (!(state.getValue(POSITION) == 0 && state.getValue(STAGE) == 6)) {
                BlockPos originPos = getOriginPos(pos, state.getValue(POSITION), state.getValue(FACING));
                for (int position = 0; position < 2; position++) {
                    BlockPos blockPos = getBlockPos(originPos, position, state.getValue(FACING));
                    if (!blockPos.equals(pos) && level.getBlockState(blockPos).is(this)) {
                        level.removeBlock(blockPos, false);
                    }
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    public static BlockPos getOriginPos(BlockPos pos, int position, Direction facing) {
        return position == 0 ? pos : pos.relative(facing);
    }

    public static BlockPos getBlockPos(BlockPos originPos, int position, Direction facing) {
        return position == 0 ? originPos : originPos.relative(facing.getOpposite());
    }
}
