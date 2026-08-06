package com.chenjdy.farmers_spell.block;

import com.chenjdy.farmers_spell.init.ModBlocks;
import com.chenjdy.farmers_spell.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, 
                                   InteractionHand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getItemInHand(hand);
        int stage = state.getValue(STAGE);

        if (stage < 5) {
            if (heldStack.is(Items.BOWL)) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) {
                        heldStack.shrink(1);
                    }
                    ItemStack bowlStack = new ItemStack(ModItems.BOWL_OF_SAINGEZI_CHICKEN.get());
                    
                    if (!player.getInventory().add(bowlStack)) {
                        Block.popResource(level, pos, bowlStack);
                    }
                    
                    int newStage = stage + 1;
                    updateAllBlocksStage(level, pos, state, newStage);
                    level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_IRON, 
                            SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                if (!level.isClientSide) {
                    player.displayClientMessage(Component.translatable(
                            "item.farmers_spell.saingezi_chicken.serve", 
                            Component.translatable("item.minecraft.bowl")), true);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        } else {
            if (!level.isClientSide) {
                Block.popResource(level, pos, new ItemStack(Items.BOWL));
                destroyStructure(level, pos, state);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }
    
    private void updateAllBlocksStage(Level level, BlockPos pos, BlockState state, int newStage) {
        int position = state.getValue(POSITION);
        Direction facing = state.getValue(FACING);
        BlockPos originPos = getOriginPos(pos, position);
        
        for (int i = 0; i < 4; i++) {
            BlockPos blockPos = getBlockPos(originPos, i);
            BlockState blockState = level.getBlockState(blockPos);
            if (blockState.is(this)) {
                level.setBlock(blockPos, blockState.setValue(STAGE, newStage), 3);
            }
        }
    }
    
    private void destroyStructure(Level level, BlockPos pos, BlockState state) {
        if (!state.is(this)) return;
        
        int position = state.getValue(POSITION);
        BlockPos originPos = getOriginPos(pos, position);

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
            int position = state.getValue(POSITION);
            BlockPos originPos = getOriginPos(pos, position);

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
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide() && state.is(this)) {
            int stage = state.getValue(STAGE);
            int position = state.getValue(POSITION);
            BlockPos originPos = getOriginPos(pos, position);

            if (stage == 1) {
                Block.popResource(level, pos, new ItemStack(ModBlocks.SAINGEZI_CHICKEN.get()));
            } else {
                Block.popResource(level, pos, new ItemStack(Items.BOWL));
            }

            for (int i = 0; i < 4; i++) {
                BlockPos blockPos = getBlockPos(originPos, i);
                if (!blockPos.equals(pos) && level.getBlockState(blockPos).is(this)) {
                    level.removeBlock(blockPos, false);
                }
            }
        }
        
        super.playerWillDestroy(level, pos, state, player);
    }
    
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState facingState, 
                                   LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (direction == Direction.DOWN && !canSurvive(state, level, currentPos)) {
            if (!level.isClientSide()) {
                int position = state.getValue(POSITION);
                BlockPos originPos = getOriginPos(currentPos, position);
                
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
                    int stage = state.getValue(STAGE);

                    BlockPos lowestPos = originPos;
                    for (int i = 1; i < 4; i++) {
                        BlockPos blockPos = getBlockPos(originPos, i);
                        if (blockPos.getY() < lowestPos.getY() || 
                            (blockPos.getY() == lowestPos.getY() && blockPos.compareTo(lowestPos) < 0)) {
                            lowestPos = blockPos;
                        }
                    }

                    if (level instanceof Level) {
                        Level levelEx = (Level) level;
                        if (stage == 1) {
                            Block.popResource(levelEx, lowestPos, new ItemStack(ModBlocks.SAINGEZI_CHICKEN.get()));
                        } else {
                            Block.popResource(levelEx, lowestPos, new ItemStack(Items.BOWL));
                        }
                    }

                    return Blocks.AIR.defaultBlockState();
                }
            }
        }
        return super.updateShape(state, direction, facingState, level, currentPos, facingPos);
    }
    
    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canSupportRigidBlock(level, pos.below());
    }
    
    protected boolean canSupportRigidBlock(LevelReader level, BlockPos pos) {
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

    
    public static BlockPos getOriginPos(BlockPos pos, int position) {
        if (position == 0) {
            return pos;
        }

        switch (position) {
            case 1: return pos.west();
            case 2: return pos.north();
            case 3: return pos.west().north();
            default: return pos;
        }
    }
    
    public static BlockPos getBlockPos(BlockPos originPos, int position) {
        switch (position) {
            case 0: return originPos;
            case 1: return originPos.east();
            case 2: return originPos.south();
            case 3: return originPos.east().south();
            default: return originPos;
        }
    }

    public static int calculatePosition(BlockPos originPos, BlockPos clickedPos) {
        if (clickedPos.equals(originPos)) {
            return 0;
        } else if (clickedPos.equals(originPos.east())) {
            return 1;
        } else if (clickedPos.equals(originPos.south())) {
            return 2;
        } else if (clickedPos.equals(originPos.east().south())) {
            return 3;
        }
        return 0;
    }
}
