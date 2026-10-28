package com.chenjdy.farmers_spell.block;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.init.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class IceEggBushBlock extends CropBlock {
    public static final MapCodec<IceEggBushBlock> CODEC = simpleCodec(IceEggBushBlock::new);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public static final TagKey<Block> PLANTABLE = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "ice_egg_bush_plantable"));

    private static final VoxelShape SMALL_SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 4.0D, 13.0D);
    private static final VoxelShape BIG_SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D);

    public IceEggBushBlock() {
        this(BlockBehaviour.Properties.of()
                .mapColor(MapColor.ICE)
                .randomTicks()
                .noCollission()
                .instabreak()
                .sound(SoundType.SNOW)
                .pushReaction(PushReaction.DESTROY));
    }

    public IceEggBushBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    public MapCodec<IceEggBushBlock> codec() {
        return CODEC;
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
        return ModItems.ICY_EGG.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) {
            return;
        }

        int age = this.getAge(state);
        if (age < this.getMaxAge()) {
            float speed = getGrowthSpeed(state, level, pos);
            if (random.nextInt((int) (25.0F / speed) + 1) == 0) {
                level.setBlock(pos, this.getStateForAge(age + 1), 2);
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(AGE) < 2 ? SMALL_SHAPE : BIG_SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(PLANTABLE);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (state.getValue(AGE) < this.getMaxAge()) {
            return;
        }
        entity.makeStuckInBlock(state, new Vec3(0.9D, 1.5D, 0.9D));
        entity.setIsInPowderSnow(true);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(AGE) < this.getMaxAge()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (!level.isClientSide) {
            this.pickEggs(state, level, pos, player);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                            BlockHitResult hit) {
        if (state.getValue(AGE) < this.getMaxAge()) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            this.pickEggs(state, level, pos, player);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private void pickEggs(BlockState state, Level level, BlockPos pos, Player player) {
        popResource(level, pos, new ItemStack(ModItems.ICY_EGG.get(), 3));
        popResource(level, pos, new ItemStack(Items.COBWEB, 1 + level.random.nextInt(3)));
        level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS,
                1.0F, 0.8F + level.random.nextFloat() * 0.4F);

        BlockState picked = state.setValue(AGE, 1);
        level.setBlock(pos, picked, 2);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, picked));
    }
}
