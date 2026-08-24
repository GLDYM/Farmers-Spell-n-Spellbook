package com.chenjdy.farmers_spell.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

public class GoldenSparkleParticle extends TextureSheetParticle {

    private final float peakScale;

    public GoldenSparkleParticle(ClientLevel level, double x, double y, double z,
                                  double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, 0.0, 0.0, 0.0);
        this.xd = 0.0;
        this.yd = 0.0;
        this.zd = 0.0;
        this.gravity = 0.0F;
        this.friction = 1.0F;
        this.hasPhysics = false;
        this.lifetime = 8 + this.random.nextInt(9);
        this.peakScale = 0.08F + this.random.nextFloat() * 0.14F;
        this.quadSize = 0.0F;
        this.setSprite(sprites.get(this.random));
    }

    @Override
public void tick() {
    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;
    if (this.age++ >= this.lifetime) {
        this.remove();
        return;
    }
    float progress = (float) this.age / (float) this.lifetime;

    float scale = (float) Math.sin(Math.PI * progress);

    this.quadSize = this.peakScale * scale;
}

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
}
