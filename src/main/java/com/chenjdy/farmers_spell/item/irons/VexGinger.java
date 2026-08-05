package com.chenjdy.farmers_spell.item.irons;

import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.entity.mobs.SummonedVex;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class VexGinger extends Item {

    private static final int VEX_COUNT = 3;
    private static final int VEX_LIFETIME = 5 * 60 * 20;

    public VexGinger(Properties properties) {
        super(properties.stacksTo(64));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getMainHandItem();
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        if (!level.isClientSide && entityLiving instanceof ServerPlayer player) {
            for (int i = 0; i < VEX_COUNT; i++) {
                SummonedVex vex = EntityRegistry.SUMMONED_VEX.get().create(level);
                if (vex != null) {
                    Vec3 offset = new Vec3(
                            Utils.getRandomScaled(2),
                            0.5,
                            Utils.getRandomScaled(2)
                    );
                    vex.moveTo(player.getEyePosition().add(offset));

                    vex.finalizeSpawn(
                            (ServerLevel) level,
                            level.getCurrentDifficultyAt(vex.blockPosition()),
                            MobSpawnType.MOB_SUMMONED,
                            null
                    );

                    SummonManager.setOwner(vex, player);
                    SummonManager.setDuration(vex, VEX_LIFETIME);
                    level.addFreshEntity(vex);
                }
            }

            level.playSound(null, player.blockPosition(), SoundEvents.EVOKER_PREPARE_SUMMON, SoundSource.PLAYERS, 1.0F, 1.0F);
            level.playSound(null, player.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 1.0F, 1.0F);

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.literal("- ").append(Component.translatable("entity.minecraft.vex").withStyle(ChatFormatting.GOLD)).append(Component.literal(" x" + VEX_COUNT)).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }
}
