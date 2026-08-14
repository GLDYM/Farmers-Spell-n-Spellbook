package com.chenjdy.farmers_spell.network;

import com.chenjdy.farmers_spell.init.ModSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BadAppleInstance extends AbstractTickableSoundInstance {
    
    private final BlockPos pos;
    private final UUID entityUUID;
    private static final Map<UUID, BadAppleInstance> instances = new HashMap<>();
    
    private BadAppleInstance(UUID entityUUID, BlockPos pos) {
        super(ModSounds.BAD_APPLE.get(), SoundSource.NEUTRAL, RandomSource.create());
        this.entityUUID = entityUUID;
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
    
    public static void play(UUID entityUUID, BlockPos pos) {
        stop(entityUUID);
        BadAppleInstance instance = new BadAppleInstance(entityUUID, pos);
        instances.put(entityUUID, instance);
        net.minecraft.client.Minecraft.getInstance().getSoundManager().play(instance);
    }
    
    public static void stop(UUID entityUUID) {
        BadAppleInstance instance = instances.remove(entityUUID);
        if (instance != null) {
            instance.stop();
        }
    }
    
    public static void stopAll() {
        for (BadAppleInstance instance : instances.values()) {
            instance.stop();
        }
        instances.clear();
    }
    
    public static boolean isPlaying(UUID entityUUID) {
        BadAppleInstance instance = instances.get(entityUUID);
        return instance != null && !instance.isStopped();
    }
    
    @Override
    public void tick() {
        if (pos != null) {
            this.x = pos.getX() + 0.5;
            this.y = pos.getY() + 0.5;
            this.z = pos.getZ() + 0.5;
        }
    }
}