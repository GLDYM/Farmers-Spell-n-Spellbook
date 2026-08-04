package com.chenjdy.farmers_spell.client;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.block.entity.CinderousStoveBlockEntity;
import com.chenjdy.farmers_spell.init.ModBlockEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import vectorwing.farmersdelight.client.renderer.DefaultStoveRenderer;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = FARMERSSPELL.MODID, bus = Bus.MOD , value = Dist.CLIENT)
public class ClientEventHandlers {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.CINDEROUS_STOVE.get(), DefaultStoveRenderer<CinderousStoveBlockEntity>::new);
    }
}
