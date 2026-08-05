package com.chenjdy.farmers_spell.item.irons;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.item.ConsumableItem;

import java.util.List;

public class PermafrostPopsicle extends ConsumableItem {
    private static final int MANA_RECOVERY_PERCENT = 10;
    private static final int COOLDOWN_SECONDS = 20;

    public PermafrostPopsicle(Item.Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getMainHandItem();
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 20;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        super.finishUsingItem(stack, level, entityLiving);

        if (!level.isClientSide && entityLiving instanceof ServerPlayer serverPlayer) {
            applyEffects(serverPlayer);
        }

        if (entityLiving instanceof Player player && player.getAbilities().instabuild) {
            return stack;
        }

        return new ItemStack(this);
    }

    private void applyEffects(ServerPlayer player) {
        double maxMana = player.getAttributeValue(AttributeRegistry.MAX_MANA);
        double manaRecovery = maxMana * MANA_RECOVERY_PERCENT / 100.0;
        MagicData magicData = MagicData.getPlayerMagicData(player);
        float currentMana = magicData.getMana();
        float newMana = (float) Math.min(currentMana + manaRecovery, maxMana);
        magicData.setMana(newMana);
        player.level().playSound(null, player.blockPosition(), SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 1.0F, 1.0F);
        player.getCooldowns().addCooldown(this, COOLDOWN_SECONDS * 20);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.literal("- ")
                .append(Component.translatable("item.farmers_spell.permafrost_popsicle.mana_recovery", MANA_RECOVERY_PERCENT).withStyle(ChatFormatting.DARK_AQUA))
                .withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.literal("- ")
                .append(Component.translatable("item.farmers_spell.permafrost_popsicle.infinite").withStyle(ChatFormatting.GOLD))
                .withStyle(ChatFormatting.GRAY));

        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }
}
