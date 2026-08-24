package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, FARMERSSPELL.MODID);

    public static final RegistryObject<SimpleParticleType> GOLDEN_SPARKLE =
            PARTICLE_TYPES.register("golden_sparkle", () -> new SimpleParticleType(false));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
