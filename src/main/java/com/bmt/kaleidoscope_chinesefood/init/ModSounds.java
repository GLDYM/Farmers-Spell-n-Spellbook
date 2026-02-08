package com.bmt.kaleidoscope_chinesefood.init;

import com.bmt.kaleidoscope_chinesefood.KaleidoscopeChineseFood;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, KaleidoscopeChineseFood.MODID);

    public static final RegistryObject<SoundEvent> FREEZER_OPEN = SOUND_EVENTS.register("freezer_open",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(KaleidoscopeChineseFood.MODID, "freezer_open")));

    public static final RegistryObject<SoundEvent> FREEZER_CLOSE = SOUND_EVENTS.register("freezer_close",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(KaleidoscopeChineseFood.MODID, "freezer_close")));

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}