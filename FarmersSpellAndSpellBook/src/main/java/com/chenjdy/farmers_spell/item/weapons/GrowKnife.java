package com.chenjdy.farmers_spell.item.weapons;

import com.chenjdy.farmers_spell.init.ModItems;
import com.chenjdy.farmers_spell.init.ModSpells;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class GrowKnife extends MagicSwordItem {

    public GrowKnife() {
        super(Tiers.IRON, 0.0f, -1.0f,
                SpellDataRegistryHolder.of(new SpellDataRegistryHolder(ModSpells.GOODBERRY_SPELL, 1)),
                Map.of(),
                new Item.Properties().durability(250).rarity(Rarity.COMMON));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return super.useOn(context);

        Level level = context.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }

        BlockPos clickPos = context.getClickedPos();
        ItemStack heldStack = context.getItemInHand();
        int harvestCount = 0;

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos scanPos = clickPos.offset(x, 0, z);
                BlockState state = level.getBlockState(scanPos);

                if (state.isAir() || !level.mayInteract(player, scanPos)) continue;
                if (!isValidHarvestPlant(state)) continue;

                level.destroyBlock(scanPos, true);
                level.levelEvent(null, LevelEvent.PARTICLES_DESTROY_BLOCK, scanPos, Block.getId(state));
                harvestCount++;
                tryDropSpecialSeed(level, scanPos, state);
            }
        }

        if (harvestCount > 0) {
            serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1F, 1F);
            player.sweepAttack();
            heldStack.hurtAndBreak(harvestCount, player,
                    p -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            player.getCooldowns().addCooldown(this, 10);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide) {
            pStack.setDamageValue(pStack.getDamageValue() + 1);
            tryDropSpecialSeed(pLevel, pPos, pState);
        }
        return true;
    }

    private boolean isValidHarvestPlant(BlockState state) {
        Block block = state.getBlock();
        if (block instanceof CropBlock || block instanceof BushBlock) {
            return true;
        }
        return state.is(Blocks.SUGAR_CANE)
                || state.is(Blocks.BAMBOO)
                || state.is(Blocks.CACTUS)
                || state.is(Blocks.VINE)
                || state.is(Blocks.KELP)
                || state.is(Blocks.SEAGRASS);
    }

    private void tryDropSpecialSeed(Level level, BlockPos pos, BlockState state) {
        if (state.is(Blocks.GRASS) || state.is(Blocks.TALL_GRASS)) {
            if (level.getRandom().nextFloat() < 0.02F) {
                dropAmethystBeetSeed(level, pos);
            }
        }
        if (state.is(Blocks.BEETROOTS)) {
            if (level.getRandom().nextFloat() < 0.10F) {
                dropAmethystBeetSeed(level, pos);
            }
        }
    }

    private void dropAmethystBeetSeed(Level level, BlockPos pos) {
        ItemStack seedStack = new ItemStack(ModItems.AMETHYST_BEETROOT_SEEDS.get());
        ItemEntity entity = new ItemEntity(
                level,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                seedStack
        );
        level.addFreshEntity(entity);
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState pBlock) {
        return isValidHarvestPlant(pBlock);
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        return isValidHarvestPlant(pState) ? 4.0F : super.getDestroySpeed(pStack, pState);
    }
}
