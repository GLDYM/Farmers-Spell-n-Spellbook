package com.chenjdy.farmers_spell.block;

import com.chenjdy.farmers_spell.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.ForgeEventFactory;
import vectorwing.farmersdelight.common.utility.ItemUtils;

public class RedVelvetCakeBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 7);

    protected static final VoxelShape[] SHAPES_NORTH = new VoxelShape[]{
            Block.box(1.0, 0.0, 1.0, 15.0, 6.0, 15.0),
            Block.box(1.0, 0.0, 1.0, 15.0, 6.0, 15.0),
            Block.box(1.0, 0.0, 1.0, 15.0, 6.0, 13.0),
            Block.box(1.0, 0.0, 1.0, 15.0, 6.0, 11.0),
            Block.box(1.0, 0.0, 1.0, 15.0, 6.0, 9.0),
            Block.box(1.0, 0.0, 1.0, 15.0, 6.0, 7.0),
            Block.box(1.0, 0.0, 1.0, 15.0, 6.0, 5.0),
            Block.box(1.0, 0.0, 1.0, 15.0, 6.0, 3.0)
    };

    protected static final VoxelShape[] SHAPES_SOUTH = new VoxelShape[]{
            Block.box(1.0, 0.0, 1.0, 15.0, 6.0, 15.0),
            Block.box(1.0, 0.0, 1.0, 15.0, 6.0, 15.0),
            Block.box(1.0, 0.0, 3.0, 15.0, 6.0, 15.0),
            Block.box(1.0, 0.0, 5.0, 15.0, 6.0, 15.0),
            Block.box(1.0, 0.0, 7.0, 15.0, 6.0, 15.0),
            Block.box(1.0, 0.0, 9.0, 15.0, 6.0, 15.0),
            Block.box(1.0, 0.0, 11.0, 15.0, 6.0, 15.0),
            Block.box(1.0, 0.0, 13.0, 15.0, 6.0, 15.0)
    };

    protected static final VoxelShape[] SHAPES_WEST = new VoxelShape[]{
            Block.box(1.0, 0.0, 1.0, 15.0, 6.0, 15.0),
            Block.box(1.0, 0.0, 1.0, 15.0, 6.0, 15.0),
            Block.box(1.0, 0.0, 1.0, 13.0, 6.0, 15.0),
            Block.box(1.0, 0.0, 1.0, 11.0, 6.0, 15.0),
            Block.box(1.0, 0.0, 1.0, 9.0, 6.0, 15.0),
            Block.box(1.0, 0.0, 1.0, 7.0, 6.0, 15.0),
            Block.box(1.0, 0.0, 1.0, 5.0, 6.0, 15.0),
            Block.box(1.0, 0.0, 1.0, 3.0, 6.0, 15.0)
    };

    protected static final VoxelShape[] SHAPES_EAST = new VoxelShape[]{
            Block.box(1.0, 0.0, 1.0, 15.0, 6.0, 15.0),
            Block.box(1.0, 0.0, 1.0, 15.0, 6.0, 15.0),
            Block.box(3.0, 0.0, 1.0, 15.0, 6.0, 15.0),
            Block.box(5.0, 0.0, 1.0, 15.0, 6.0, 15.0),
            Block.box(7.0, 0.0, 1.0, 15.0, 6.0, 15.0),
            Block.box(9.0, 0.0, 1.0, 15.0, 6.0, 15.0),
            Block.box(11.0, 0.0, 1.0, 15.0, 6.0, 15.0),
            Block.box(13.0, 0.0, 1.0, 15.0, 6.0, 15.0)
    };

    public RedVelvetCakeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(BITES, 0));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        int bites = state.getValue(BITES);

        switch (facing) {
            case SOUTH:
                return SHAPES_SOUTH[bites];
            case WEST:
                return SHAPES_WEST[bites];
            case EAST:
                return SHAPES_EAST[bites];
            case NORTH:
            default:
                return SHAPES_NORTH[bites];
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getItemInHand(hand);

        if (ItemUtils.isKnife(heldStack)) {
            return cutSlice(level, pos, state, player, heldStack.getItem());
        }

        return this.consumeBite(level, pos, state, player);
    }

    protected InteractionResult consumeBite(Level level, BlockPos pos, BlockState state, Player player) {
        if (!player.canEat(false)) {
            return InteractionResult.PASS;
        }

        ItemStack sliceStack = new ItemStack(ModItems.RED_VELVET_CAKE_SLICE.get());
        ItemStack sliceCopy = sliceStack.copy();

        player.getFoodData().eat(sliceStack.getItem(), sliceStack);

        if (!level.isClientSide()) {
            sliceStack.getItem().getFoodProperties().getEffects().forEach(effectPair -> {
                MobEffectInstance effectInstance = effectPair.getFirst();
                float chance = effectPair.getSecond();
                if (level.getRandom().nextFloat() < chance) {
                    player.addEffect(new MobEffectInstance(effectInstance));
                }
            });

            level.playSound(null, pos, SoundEvents.GENERIC_EAT,
                    SoundSource.PLAYERS, 0.8f, 0.8f);
        }

        ForgeEventFactory.onItemUseFinish(player, sliceCopy, 0, ItemStack.EMPTY);

        int bites = state.getValue(BITES);
        if (bites < getMaxBites() - 1) {
            level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
        } else {
            level.removeBlock(pos, false);
        }

        return InteractionResult.SUCCESS;
    }

    protected InteractionResult cutSlice(Level level, BlockPos pos, BlockState state, Player player, Item knife) {
        int bites = state.getValue(BITES);
        if (bites < getMaxBites() - 1) {
            level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
        } else {
            level.removeBlock(pos, false);
        }

        Direction direction = player.getDirection().getOpposite();
        ItemUtils.spawnItemEntity(level, new ItemStack(ModItems.RED_VELVET_CAKE_SLICE.get()),
                pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5,
                direction.getStepX() * 0.15, 0.05, direction.getStepZ() * 0.15);

        return InteractionResult.SUCCESS;
    }

    public int getMaxBites() {
        return 8;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.DOWN && !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canSupportRigidBlock(level, pos.below());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, BITES);
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return getMaxBites() - state.getValue(BITES);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }
}
