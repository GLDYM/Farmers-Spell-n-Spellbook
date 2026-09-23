package com.chenjdy.farmers_spell.block;

import com.chenjdy.farmers_spell.init.ModBlocks;
import com.chenjdy.farmers_spell.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class IcebreakerBreadBlock extends Block {

    public static final IntegerProperty POSITION = IntegerProperty.create("position", 0, 1);
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 1, 9);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public IcebreakerBreadBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(POSITION, 0)
                .setValue(STAGE, 1)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int position = state.getValue(POSITION);
        int stage = state.getValue(STAGE);
        Direction facing = state.getValue(FACING);
        int length = getLength(position, stage);
        if (length <= 0) {
            return Shapes.empty();
        }
        return getShapeForLength(facing, length);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }

    private int getLength(int position, int stage) {
        if (position == 0) {
            switch (stage) {
                case 1: case 2: case 3: return 16;
                case 4: return 10;
                case 5: return 6;
                case 6: return 2;
                default: return 0;
            }
        } else {
            switch (stage) {
                case 1: case 2: case 3: case 4: case 5: case 6: return 16;
                case 7: return 14;
                case 8: return 10;
                case 9: return 6;
                default: return 16;
            }
        }
    }

    private VoxelShape getShapeForLength(Direction facing, int length) {
        int start = 16 - length;
        return switch (facing) {
            case NORTH -> Block.box(0, 0, start, 16, 7, 16);
            case SOUTH -> Block.box(0, 0, 0, 16, 7, length);
            case EAST -> Block.box(0, 0, 0, length, 7, 16);
            case WEST -> Block.box(start, 0, 0, 16, 7, 16);
            default -> Block.box(0, 0, 0, 16, 7, 16);
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(POSITION, 0)
                .setValue(STAGE, 1)
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getItemInHand(hand);
        int stage = state.getValue(STAGE);

        if (!heldStack.isEmpty()) {
            return InteractionResult.PASS;
        }

        if (stage < 9) {
            if (!level.isClientSide) {
                int newStage = stage + 1;
                int position = state.getValue(POSITION);
                Direction facing = state.getValue(FACING);

                ItemStack sandwich = new ItemStack(ModItems.ICEBERGCREAM_SANDWICH.get());
                if (!player.getInventory().add(sandwich)) {
                    Block.popResource(level, pos, sandwich);
                }

                if (newStage == 9) {
                    destroyStructure(level, pos, state);
                } else {
                    updateAllBlocksStage(level, pos, state, newStage);
                    if (newStage == 7) {
                        BlockPos originPos = getOriginPos(pos, position, facing);
                        BlockPos block1Pos = getBlockPos(originPos, 0, facing);
                        if (level.getBlockState(block1Pos).is(this)) {
                            level.removeBlock(block1Pos, false);
                        }
                    }
                }

                level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_IRON,
                        SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    private void updateAllBlocksStage(Level level, BlockPos pos, BlockState state, int newStage) {
        int position = state.getValue(POSITION);
        Direction facing = state.getValue(FACING);
        BlockPos originPos = getOriginPos(pos, position, facing);

        for (int i = 0; i < 2; i++) {
            BlockPos blockPos = getBlockPos(originPos, i, facing);
            BlockState blockState = level.getBlockState(blockPos);
            if (blockState.is(this)) {
                level.setBlock(blockPos, blockState.setValue(STAGE, newStage), 3);
            }
        }
    }

    private void destroyStructure(Level level, BlockPos pos, BlockState state) {
        if (!state.is(this)) return;
        int position = state.getValue(POSITION);
        Direction facing = state.getValue(FACING);
        BlockPos originPos = getOriginPos(pos, position, facing);

        for (int i = 0; i < 2; i++) {
            BlockPos blockPos = getBlockPos(originPos, i, facing);
            if (level.getBlockState(blockPos).is(this)) {
                level.removeBlock(blockPos, false);
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.is(newState.getBlock())) {
            return;
        }

        if (!level.isClientSide() && state.is(this) && !newState.is(this)) {
            if (state.getValue(POSITION) == 0 && state.getValue(STAGE) == 7) {
                super.onRemove(state, level, pos, newState, isMoving);
                return;
            }

            int position = state.getValue(POSITION);
            Direction facing = state.getValue(FACING);
            BlockPos originPos = getOriginPos(pos, position, facing);

            for (int i = 0; i < 2; i++) {
                BlockPos blockPos = getBlockPos(originPos, i, facing);
                if (!blockPos.equals(pos) && level.getBlockState(blockPos).is(this)) {
                    level.removeBlock(blockPos, false);
                }
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && state.is(this)) {
            int stage = state.getValue(STAGE);
            if (stage == 1) {
                Block.popResource(level, pos, new ItemStack(ModBlocks.ICEBREAKER_BREAD.get().asItem()));
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState facingState,
                                   LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (direction == Direction.DOWN && !canSurvive(state, level, currentPos)) {
            int position = state.getValue(POSITION);
            Direction facing = state.getValue(FACING);
            BlockPos originPos = getOriginPos(currentPos, position, facing);
            BlockPos partnerPos = getBlockPos(originPos, 1 - position, facing);
            BlockState partnerState = level.getBlockState(partnerPos);

            boolean partnerHasSupport = false;
            if (partnerState.is(this)) {
                BlockPos partnerBelow = partnerPos.below();
                partnerHasSupport = level.getBlockState(partnerBelow).isFaceSturdy(level, partnerBelow, Direction.UP);
            }

            if (partnerHasSupport) {
                return state;
            }

            if (!level.isClientSide() && level instanceof Level levelEx) {
                int stage = state.getValue(STAGE);
                if (stage == 1) {
                    Block.popResource(levelEx, currentPos, new ItemStack(ModBlocks.ICEBREAKER_BREAD.get().asItem()));
                }
            }
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, facingState, level, currentPos, facingPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canSupportRigidBlock(level, pos.below());
    }

    private boolean canSupportRigidBlock(LevelReader level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.isFaceSturdy(level, pos, Direction.UP);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POSITION, STAGE, FACING);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    public static BlockPos getOriginPos(BlockPos pos, int position, Direction facing) {
        if (position == 0) {
            return pos;
        }
        return pos.relative(facing);
    }

    public static BlockPos getBlockPos(BlockPos originPos, int position, Direction facing) {
        if (position == 0) {
            return originPos;
        }
        return originPos.relative(facing.getOpposite());
    }
}
