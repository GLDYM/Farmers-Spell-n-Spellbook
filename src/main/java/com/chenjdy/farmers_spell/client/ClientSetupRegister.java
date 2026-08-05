package com.chenjdy.farmers_spell.client;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.client.renderer.BadAppleRender;
import com.chenjdy.farmers_spell.client.renderer.ChaosSlashRender;
import com.chenjdy.farmers_spell.client.renderer.FoodgeistRender;
import com.chenjdy.farmers_spell.client.renderer.SpellBookCurioRender;
import com.chenjdy.farmers_spell.init.ModEntities;
import com.chenjdy.farmers_spell.init.ModFluids;
import com.chenjdy.farmers_spell.init.ModItems;
import com.chenjdy.farmers_spell.init.ModMenuTypes;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.bus.api.SubscribeEvent;
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

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(new ColoredClientExtensions(0xFFFFB347), ModFluids.ORIGINAL_NECTAR_TYPE.get());
        event.registerFluidType(new ColoredClientExtensions(0xFF8E7CC3), ModFluids.AMETHYST_TEQUILA_TYPE.get());
        event.registerFluidType(new ColoredClientExtensions(0xFF8B0000), ModFluids.CATACOMBS_WINE_TYPE.get());
        event.registerFluidType(new ColoredClientExtensions(0xFF00BFFF), ModFluids.ICE_VENOM_WINE_TYPE.get());
    }

    private static class ColoredClientExtensions implements IClientFluidTypeExtensions {
        private static final ResourceLocation stillTexture = ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_still");;
        private static final ResourceLocation flowingTexture = ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_flow");

        private final int tintColor;

        ColoredClientExtensions(int tintColor) {
            this.tintColor = tintColor;
        }

        @Override
        public ResourceLocation getStillTexture() {
            return stillTexture;
        }

        @Override
        public ResourceLocation getFlowingTexture() {
            return flowingTexture;
        }

        @Override
        public int getTintColor() {
            return tintColor;
        }
    }
}
