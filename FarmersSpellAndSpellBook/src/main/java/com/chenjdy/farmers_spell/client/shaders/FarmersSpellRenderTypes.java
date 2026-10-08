package com.chenjdy.farmers_spell.client.shaders;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard.CullStateShard;
import net.minecraft.client.renderer.RenderStateShard.DepthTestStateShard;
import net.minecraft.client.renderer.RenderStateShard.LightmapStateShard;
import net.minecraft.client.renderer.RenderStateShard.OverlayStateShard;
import net.minecraft.client.renderer.RenderStateShard.ShaderStateShard;
import net.minecraft.client.renderer.RenderStateShard.TextureStateShard;
import net.minecraft.client.renderer.RenderStateShard.TransparencyStateShard;
import net.minecraft.client.renderer.RenderStateShard.WriteMaskStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

public final class FarmersSpellRenderTypes {
    private FarmersSpellRenderTypes() {
    }

    public static ShaderInstance chaosSlashShader;

    private static final ResourceLocation CHAOS_SLASH_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "textures/entity/chaos_slash.png");

    private static final int GL_LEQUAL = 515;

    public static final RenderType CHAOS_SLASH = RenderType.create(
            FARMERSSPELL.MODID + ":chaos_slash",
            DefaultVertexFormat.NEW_ENTITY,
            VertexFormat.Mode.QUADS,
            1536,
            true,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(new ShaderStateShard(() -> chaosSlashShader))
                    .setTextureState(new TextureStateShard(CHAOS_SLASH_TEXTURE, false, false))
                    .setTransparencyState(new TransparencyStateShard("no_transparency", () -> {
                    }, () -> {
                    }))
                    .setCullState(new CullStateShard(false))
                    .setLightmapState(new LightmapStateShard(true))
                    .setOverlayState(new OverlayStateShard(true))
                    .setWriteMaskState(new WriteMaskStateShard(true, true))
                    .setDepthTestState(new DepthTestStateShard("lequal_depth_test", GL_LEQUAL))
                    .createCompositeState(false)
    );
}
