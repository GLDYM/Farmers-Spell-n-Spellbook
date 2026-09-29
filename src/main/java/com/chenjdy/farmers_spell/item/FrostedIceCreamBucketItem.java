package com.chenjdy.farmers_spell.item;

import com.chenjdy.farmers_spell.block.FufuBlock;
import com.chenjdy.farmers_spell.init.ModBlocks;
import com.chenjdy.farmers_spell.init.ModTriggers;
import net.minecraft.core.BlockPos;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class FrostedIceCreamBucketItem extends Item {
    public FrostedIceCreamBucketItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Player player = context.getPlayer();
        BlockPos targetPos = canPlaceAt(level.getBlockState(clickedPos)) ? clickedPos
                : canPlaceAt(level.getBlockState(clickedPos.relative(context.getClickedFace())))
                        ? clickedPos.relative(context.getClickedFace())
                        : null;
        if (targetPos == null)
            return InteractionResult.PASS;

        BlockState fufuState = ModBlocks.FUFU.get().defaultBlockState()
                .setValue(FufuBlock.FACING, player.getDirection().getOpposite());
        if (!level.setBlock(targetPos, fufuState, 11))
            return InteractionResult.PASS;
        var sound = fufuState.getSoundType(level, targetPos, player);
        level.playSound(player, targetPos, sound.getPlaceSound(), SoundSource.BLOCKS,
                (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
        consumeBucket(context);
        if (player instanceof ServerPlayer serverPlayer)
            ModTriggers.MIKU_FUFU_TRIGGER.get().trigger(serverPlayer);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        super.finishUsingItem(stack, level, entity);
        if (!level.isClientSide) {
            entity.setTicksFrozen(Math.max(entity.getTicksFrozen(), entity.getTicksRequiredToFreeze() / 2));
        }
        if (stack.isEmpty())
            return new ItemStack(Items.BUCKET);
        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            ItemStack bucket = new ItemStack(Items.BUCKET);
            if (!player.getInventory().add(bucket))
                player.drop(bucket, false);
        }
        return stack;
    }

    private boolean canPlaceAt(BlockState state) {
        return state.canBeReplaced() && state.getFluidState().isEmpty();
    }

    private void consumeBucket(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null || player.getAbilities().instabuild)
            return;
        ItemStack stack = context.getItemInHand();
        InteractionHand hand = context.getHand();
        stack.shrink(1);
        ItemStack emptyBucket = new ItemStack(Items.BUCKET);
        if (stack.isEmpty())
            player.setItemInHand(hand, emptyBucket);
        else if (!player.getInventory().add(emptyBucket))
            player.drop(emptyBucket, false);
    }
}
