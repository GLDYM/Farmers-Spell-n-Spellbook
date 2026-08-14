package com.chenjdy.farmers_spell.network;

import com.chenjdy.farmers_spell.init.ModSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BadAppleInstance extends AbstractTickableSoundInstance {
    
    private final BlockPos pos;
    private static final Map<UUID, BadAppleInstance> INSTANCES = new HashMap<>();
    
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
    
    public static void start(UUID entityUuid, BlockPos pos) {
        stop(entityUuid);
        BadAppleInstance instance = new BadAppleInstance(pos);
        INSTANCES.put(entityUuid, instance);
        Minecraft.getInstance().getSoundManager().play(instance);
    }

    public static void stop(UUID entityUuid) {
        BadAppleInstance instance = INSTANCES.remove(entityUuid);
        if (instance != null) {
            instance.stop();
        }
    }
    
        public void tick() {
        if (pos != null) {
            this.x = pos.getX() + 0.5;
            this.y = pos.getY() + 0.5;
            this.z = pos.getZ() + 0.5;
        }
    }
}
