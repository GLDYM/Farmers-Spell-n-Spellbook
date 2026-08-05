package com.chenjdy.farmers_spell.event;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.entity.BadAppleEntity;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = FarmersSpell.MODID)
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