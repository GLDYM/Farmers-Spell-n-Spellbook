package com.chenjdy.farmers_spell.client.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public abstract class ChaosSlashRenderType extends RenderType {
    private static final Function<ResourceLocation, RenderType> CHAOS_SLASH = Util.memoize(texture ->
            create("farmers_spell:chaos_slash", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 1536,
                    true, false,
                    RenderType.CompositeState.builder()
                            .setShaderState(new ShaderStateShard(GameRenderer::getRendertypeEyesShader))
                            .setTextureState(new TextureStateShard(texture, false, false))
                            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                            .setCullState(NO_CULL)
                            .setWriteMaskState(COLOR_WRITE)
                            .createCompositeState(false)));

    private ChaosSlashRenderType(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize,
                                 boolean affectsCrumbling, boolean sortOnUpload,
                                 Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    public static RenderType chaosSlash(ResourceLocation texture) {
        return CHAOS_SLASH.apply(texture);
    }
}
