package com.chenjdy.farmers_spell.client;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.client.renderer.BadAppleRender;
import com.chenjdy.farmers_spell.client.renderer.ChaosSlashRender;
import com.chenjdy.farmers_spell.client.renderer.FoodgeistRender;
import com.chenjdy.farmers_spell.client.renderer.SpellBookCurioRender;
import com.chenjdy.farmers_spell.init.ModEntities;
import com.chenjdy.farmers_spell.init.ModItems;
import com.chenjdy.farmers_spell.init.ModMenuTypes;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;


@EventBusSubscriber(modid = FarmersSpell.MODID, value = Dist.CLIENT)
public class ClientSetupRegister {

    @SubscribeEvent
    public static void clientSetup(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.ALCHEMIST_POT.get(), AlchemistPotScreen::new);
            
            CuriosRendererRegistry.register(ModItems.TIRAMISU.get(), SpellBookCurioRender::new);
            CuriosRendererRegistry.register(ModItems.LASAGNOWLEDGE.get(), SpellBookCurioRender::new);
            CuriosRendererRegistry.register(ModItems.WHEAT_BOOK.get(), SpellBookCurioRender::new);
    }
    
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.BAD_APPLE_ENTITY.get(), BadAppleRender::new);
        event.registerEntityRenderer(ModEntities.CHAOS_SLASH_PROJECTILE.get(), ChaosSlashRender::new);
        event.registerEntityRenderer(ModEntities.BUTTER_PROJECTILE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntities.PRESERVE_CIRCLE_AOE.get(), NoopRenderer::new);
        event.registerEntityRenderer(ModEntities.FOODGEIST.get(), FoodgeistRender::new);
    }
}
