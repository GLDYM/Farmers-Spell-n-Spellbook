package com.chenjdy.farmers_spell.client;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.client.renderer.BadAppleRender;
import com.chenjdy.farmers_spell.client.renderer.ChaosSlashRender;
import com.chenjdy.farmers_spell.client.renderer.FoodgeistRender;
import com.chenjdy.farmers_spell.init.ModEntities;
import com.chenjdy.farmers_spell.init.ModItems;
import com.chenjdy.farmers_spell.init.ModMenuTypes;
import io.redspace.ironsspellbooks.render.SpellBookCurioRenderer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod.EventBusSubscriber(modid = FARMERSSPELL.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientSetupRegister {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenuTypes.ALCHEMIST_POT.get(), AlchemistPotScreen::new);
            
            CuriosRendererRegistry.register(ModItems.TIRAMISU.get(), SpellBookCurioRenderer::new);
            CuriosRendererRegistry.register(ModItems.LASAGNOWLEDGE.get(), SpellBookCurioRenderer::new);
            CuriosRendererRegistry.register(ModItems.WHEAT_BOOK.get(), SpellBookCurioRenderer::new);
        });
    }
    
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.BAD_APPLE_ENTITY.get(), BadAppleRender::new);
        event.registerEntityRenderer(ModEntities.CHAOS_SLASH_PROJECTILE.get(), ChaosSlashRender::new);
        event.registerEntityRenderer(ModEntities.BUTTER_PROJECTILE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntities.PRESERVE_CIRCLE_AOE.get(), NoopRenderer::new);
        event.registerEntityRenderer(ModEntities.FOODGEIST.get(), FoodgeistRender::new);
        //event.registerEntityRenderer(ModEntities.PAN.get(), PanRenderer::new);
    }
}
