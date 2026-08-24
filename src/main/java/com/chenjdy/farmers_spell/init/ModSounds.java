package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, FARMERSSPELL.MODID);

    public static final RegistryObject<SoundEvent> BAD_APPLE = SOUND_EVENTS.register("bad_apple",
            () -> SoundEvent.createFixedRangeEvent(ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "bad_apple"), 100.0f));

    public static final RegistryObject<SoundEvent> BIAN = SOUND_EVENTS.register("baian",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "baian")));

    public static void register(net.minecraftforge.eventbus.api.IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
