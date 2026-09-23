package com.chenjdy.farmers_spell.event;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.item.curios.RingManaBonusHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.common.registry.ModEffects;

@Mod.EventBusSubscriber(modid = FARMERSSPELL.MODID)
public class RingEventHandler {

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;
        if (!(entity instanceof Player player)) return;

        RingManaBonusHelper.syncFoodBlessingTransition(player);
    }

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;
        if (!(entity instanceof Player player)) return;
        if (isFoodBlessingEffect(event.getEffectInstance())) {
            RingManaBonusHelper.syncAll(player);
        }
    }

    @SubscribeEvent
    public static void onEffectRemove(MobEffectEvent.Remove event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;
        if (!(entity instanceof Player player)) return;
        if (isFoodBlessingEffect(event.getEffectInstance())) {
            RingManaBonusHelper.syncAll(player);
        }
    }

    @SubscribeEvent
    public static void onEffectExpire(MobEffectEvent.Expired event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;
        if (!(entity instanceof Player player)) return;
        if (isFoodBlessingEffect(event.getEffectInstance())) {
            RingManaBonusHelper.syncAll(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;
        RingManaBonusHelper.resetFoodBlessingState(player);
        RingManaBonusHelper.syncAll(player);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;
        RingManaBonusHelper.resetFoodBlessingState(player);
        RingManaBonusHelper.syncAll(player);
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;
        RingManaBonusHelper.resetFoodBlessingState(player);
        RingManaBonusHelper.syncAll(player);
    }

    private static boolean isFoodBlessingEffect(MobEffectInstance effect) {
        if (effect == null) return false;
        MobEffect mobEffect = effect.getEffect();
        return mobEffect == ModEffects.NOURISHMENT.get()
                || mobEffect == ModEffects.COMFORT.get()
                || mobEffect == com.chenjdy.farmers_spell.init.ModEffects.CLEANSE.get();
    }
}
