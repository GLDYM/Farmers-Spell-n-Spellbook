package com.chenjdy.farmers_spell.block;

import com.chenjdy.farmers_spell.init.ModBlocks;
import com.chenjdy.farmers_spell.init.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.phys.shapes.VoxelShape;

public class SaingeziChickenBlock extends Block {
    public static final MapCodec<SaingeziChickenBlock> CODEC = simpleCodec(SaingeziChickenBlock::new);
    public static final IntegerProperty POSITION = IntegerProperty.create("position", 0, 3);
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 1, 5);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);

    public SaingeziChickenBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(POSITION, 0)
                .setValue(STAGE, 1)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(POSITION, 0)
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemstack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        int stage = state.getValue(STAGE);
        if (stage < 5) {
            if (itemstack.is(Items.BOWL)) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    ItemStack bowlStack = new ItemStack(ModItems.BOWL_OF_SAINGEZI_CHICKEN.get());
                    if (!player.getInventory().add(bowlStack)) {
                        Block.popResource(level, pos, bowlStack);
                    }
                    updateAllBlocksStage(level, pos, state, stage + 1);
                    level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_IRON.value(), SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }

            if (!level.isClientSide) {
                player.displayClientMessage(Component.translatable(
                        "item.farmers_spell.saingezi_chicken.serve",
                        Component.translatable("item.minecraft.bowl")), true);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (!level.isClientSide) {
            Block.popResource(level, pos, new ItemStack(Items.BOWL));
            destroyStructure(level, pos, state);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    private void updateAllBlocksStage(Level level, BlockPos pos, BlockState state, int newStage) {
        BlockPos originPos = getOriginPos(pos, state.getValue(POSITION));
        for (int i = 0; i < 4; i++) {
            BlockPos blockPos = getBlockPos(originPos, i);
            BlockState blockState = level.getBlockState(blockPos);
            if (blockState.is(this)) {
                level.setBlock(blockPos, blockState.setValue(STAGE, newStage), 3);
            }
        }
    }

    private void destroyStructure(Level level, BlockPos pos, BlockState state) {
        if (!state.is(this)) {
            return;
        }
        BlockPos originPos = getOriginPos(pos, state.getValue(POSITION));
        for (int i = 0; i < 4; i++) {
            BlockPos blockPos = getBlockPos(originPos, i);
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
            BlockPos originPos = getOriginPos(pos, state.getValue(POSITION));
            boolean otherBlocksExist = false;
            for (int i = 0; i < 4; i++) {
                BlockPos blockPos = getBlockPos(originPos, i);
                if (!blockPos.equals(pos) && level.getBlockState(blockPos).is(this)) {
                    otherBlocksExist = true;
                    break;
                }
            }

            if (otherBlocksExist) {
                for (int i = 0; i < 4; i++) {
                    BlockPos blockPos = getBlockPos(originPos, i);
                    if (!blockPos.equals(pos) && level.getBlockState(blockPos).is(this)) {
                        level.removeBlock(blockPos, false);
                    }
                }
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (direction == Direction.DOWN && !canSurvive(state, level, currentPos) && !level.isClientSide()) {
            BlockPos originPos = getOriginPos(currentPos, state.getValue(POSITION));
            boolean anyBlockHasSupport = false;
            for (int i = 0; i < 4; i++) {
                BlockPos blockPos = getBlockPos(originPos, i);
                BlockState blockState = level.getBlockState(blockPos);
                if (blockState.is(this)) {
                    BlockPos belowPos = blockPos.below();
                    if (level.getBlockState(belowPos).isFaceSturdy(level, belowPos, Direction.UP)) {
                        anyBlockHasSupport = true;
                        break;
                    }
                }
            }

            if (!anyBlockHasSupport) {
                if (level instanceof Level actualLevel) {
                    ItemStack drop = state.getValue(STAGE) == 1
                            ? new ItemStack(ModBlocks.SAINGEZI_CHICKEN.get().asItem())
                            : new ItemStack(Items.BOWL);
                    Block.popResource(actualLevel, originPos, drop);
                }
                return Blocks.AIR.defaultBlockState();
            }
        }
        return super.updateShape(state, direction, facingState, level, currentPos, facingPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        return level.getBlockState(belowPos).isFaceSturdy(level, belowPos, Direction.UP);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POSITION, STAGE, FACING);
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    public static BlockPos getOriginPos(BlockPos pos, int position) {
        return switch (position) {
            case 1 -> pos.west();
            case 2 -> pos.north();
            case 3 -> pos.west().north();
            default -> pos;
        };
    }

    public static BlockPos getBlockPos(BlockPos originPos, int position) {
        return switch (position) {
            case 1 -> originPos.east();
            case 2 -> originPos.south();
            case 3 -> originPos.east().south();
            default -> originPos;
        };
    }
}
