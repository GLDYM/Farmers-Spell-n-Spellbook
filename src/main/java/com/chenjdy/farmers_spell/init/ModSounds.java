package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, FARMERSSPELL.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> BAD_APPLE = SOUND_EVENTS.register("bad_apple",
            () -> SoundEvent.createFixedRangeEvent(ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "bad_apple"), 100.0f));

    public static void register(net.neoforged.bus.api.IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
