package com.chenjdy.farmers_spell.event;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.entity.BadAppleEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FARMERSSPELL.MODID)
public class BadAppleEventHandler {
    
    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!event.getEntity().level().isClientSide) {
            var entities = event.getEntity().level().getEntitiesOfClass(BadAppleEntity.class, 
                event.getEntity().getBoundingBox().inflate(200));
            for (BadAppleEntity entity : entities) {
                entity.discard();
            }
        }
    }
}