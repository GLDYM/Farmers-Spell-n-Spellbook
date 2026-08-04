package com.chenjdy.farmers_spell.network;

import com.chenjdy.farmers_spell.init.ModSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class BadAppleInstance extends AbstractTickableSoundInstance {
    
    private final BlockPos pos;
    private static BadAppleInstance instance = null;
    
    private BadAppleInstance(BlockPos pos) {
        super(ModSounds.BAD_APPLE.get(), SoundSource.NEUTRAL, RandomSource.create());
        this.pos = pos;
        this.x = pos.getX() + 0.5;
        this.y = pos.getY() + 0.5;
        this.z = pos.getZ() + 0.5;
        this.volume = 10.0f;
        this.pitch = 1.0f;
        this.looping = true;
        this.delay = 0;
        this.attenuation = Attenuation.LINEAR;
    }
    
    public static void play(BlockPos pos) {
        stopCurrent();
        instance = new BadAppleInstance(pos);
        net.minecraft.client.Minecraft.getInstance().getSoundManager().play(instance);
    }
    
    public static void stopCurrent() {
        if (instance != null) {
            instance.stop();
            instance = null;
        }
    }
    
    public static boolean isPlaying() {
        return instance != null && !instance.isStopped();
    }
    
        public void tick() {
        if (pos != null) {
            this.x = pos.getX() + 0.5;
            this.y = pos.getY() + 0.5;
            this.z = pos.getZ() + 0.5;
        }
    }
}