package com.chenjdy.farmers_spell.block;

import com.chenjdy.farmers_spell.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AmethystBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AmethystBeetrootBlock extends CropBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    private static final VoxelShape[] UP_SHAPES = new VoxelShape[]{
        Block.box(3, 0, 3, 13, 2, 13),
        Block.box(3, 0, 3, 13, 4, 13),
        Block.box(3, 0, 3, 13, 6, 13),
        Block.box(3, 0, 3, 13, 8, 13)
    };

    private static final VoxelShape[] DOWN_SHAPES = new VoxelShape[]{
        Block.box(3, 14, 3, 13, 16, 13),
        Block.box(3, 12, 3, 13, 16, 13),
        Block.box(3, 10, 3, 13, 16, 13),
        Block.box(3, 8, 3, 13, 16, 13)
    };

    private static final VoxelShape[] NORTH_SHAPES = new VoxelShape[]{
        Block.box(3, 3, 14, 13, 13, 16),
        Block.box(3, 3, 12, 13, 13, 16),
        Block.box(3, 3, 10, 13, 13, 16),
        Block.box(3, 3, 8, 13, 13, 16)
    };

    private static final VoxelShape[] SOUTH_SHAPES = new VoxelShape[]{
        Block.box(3, 3, 0, 13, 13, 2),
        Block.box(3, 3, 0, 13, 13, 4),
        Block.box(3, 3, 0, 13, 13, 6),
        Block.box(3, 3, 0, 13, 13, 8)
    };

    private static final VoxelShape[] WEST_SHAPES = new VoxelShape[]{
        Block.box(14, 3, 3, 16, 13, 13),
        Block.box(12, 3, 3, 16, 13, 13),
        Block.box(10, 3, 3, 16, 13, 13),
        Block.box(8, 3, 3, 16, 13, 13)
    };

    private static final VoxelShape[] EAST_SHAPES = new VoxelShape[]{
        Block.box(0, 3, 3, 2, 13, 13),
        Block.box(0, 3, 3, 4, 13, 13),
        Block.box(0, 3, 3, 6, 13, 13),
        Block.box(0, 3, 3, 8, 13, 13)
    };

    public AmethystBeetrootBlock() {
        super(Block.Properties.of().mapColor(MapColor.COLOR_PURPLE).sound(SoundType.AMETHYST).noCollission().instabreak().randomTicks().lightLevel(state -> state.getValue(AGE) >= 3 ? 2 : 0));
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(FACING, Direction.UP));
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(AGE) >= 3 ? 2 : 0;
    }

    @Override
    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

    @Override
    protected Item getBaseSeedId() {
        return ModItems.AMETHYST_BEETROOT_SEEDS.get();
    }

    @Override
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        int age = Math.min(this.getMaxAge(), this.getAge(state) + this.getBonemealAgeIncrease(level));
        level.setBlock(pos, state.setValue(AGE, age), 2);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int age = state.getValue(AGE);
        Direction facing = state.getValue(FACING);
        switch (facing) {
            case DOWN: return DOWN_SHAPES[age];
            case NORTH: return NORTH_SHAPES[age];
            case SOUTH: return SOUTH_SHAPES[age];
            case WEST: return WEST_SHAPES[age];
            case EAST: return EAST_SHAPES[age];
            case UP:
            default: return UP_SHAPES[age];
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, FACING);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getBlock() instanceof AmethystBlock 
            || state.getBlock() instanceof BuddingAmethystBlock 
            || state.is(Blocks.CALCITE);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos supportPos = pos.relative(direction.getOpposite());
        BlockState supportState = level.getBlockState(supportPos);

        return this.mayPlaceOn(supportState, level, supportPos) 
            && supportState.isFaceSturdy(level, supportPos, direction);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        return this.defaultBlockState().setValue(FACING, direction);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        Direction facing = state.getValue(FACING);
        if (direction == facing.getOpposite()) {
            if (!canSurvive(state, level, pos)) {
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.destroyBlock(pos, true);
                }
                return Blocks.AIR.defaultBlockState();
            } else {
                int age = state.getValue(AGE);
                if (age >= getMaxAge() && level instanceof ServerLevel serverLevel) {
                    BlockPos supportPos = pos.relative(facing.getOpposite());
                    BlockState supportState = serverLevel.getBlockState(supportPos);
                    if (supportState.is(Blocks.AMETHYST_BLOCK) && serverLevel.getRandom().nextFloat() < 0.05f) {
                        serverLevel.setBlock(supportPos, Blocks.BUDDING_AMETHYST.defaultBlockState(), 2);
                    }
                }
            }
        }
        return state;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) return;

        int age = this.getAge(state);
        if (age < this.getMaxAge()) {
            float f = this.getGrowthSpeed(state, level, pos);
            if (random.nextInt((int)(25.0F / f) + 1) == 0) {
                level.setBlock(pos, state.setValue(AGE, age + 1), 2);
            }
        }
    }

    private float getGrowthSpeed(BlockState state, BlockGetter level, BlockPos pos) {
        float speed = 1.0F;
        Direction facing = state.getValue(FACING);
        BlockPos supportPos = pos.relative(facing.getOpposite());

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos checkPos = supportPos.offset(x, y, z);
                    if (!checkPos.equals(supportPos)) {
                        BlockState checkState = level.getBlockState(checkPos);
                        if (checkState.getBlock() instanceof AmethystBlock 
                            || checkState.getBlock() instanceof BuddingAmethystBlock
                            || checkState.is(Blocks.CALCITE)) {
                            speed += 0.5F;
                        }
                    }
                }
            }
        }
        return speed;
    }
}
