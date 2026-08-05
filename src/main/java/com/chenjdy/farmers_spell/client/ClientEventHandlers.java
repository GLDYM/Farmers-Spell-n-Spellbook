package com.chenjdy.farmers_spell.client;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.block.entity.CinderousStoveBlockEntity;
import com.chenjdy.farmers_spell.init.ModBlockEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import vectorwing.farmersdelight.client.renderer.DefaultStoveRenderer;

@Mod.EventBusSubscriber(modid = FARMERSSPELL.MODID, bus = Bus.MOD , value = Dist.CLIENT)
public class ClientEventHandlers {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.CINDEROUS_STOVE.get(), DefaultStoveRenderer<CinderousStoveBlockEntity>::new);
    }
}
