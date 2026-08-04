package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FarmersSpell;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSchools {

    public static final ResourceLocation GLUTTONY_RESOURCE = ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "gluttony");

    public static final DeferredRegister<SchoolType> SCHOOLS = DeferredRegister.create(SchoolRegistry.SCHOOL_REGISTRY_KEY, FarmersSpell.MODID);

    public static final TagKey<Item> GLUTTONY_FOCUS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "gluttony_focus"));

    public static final ResourceKey<DamageType> GLUTTONY_MAGIC = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "gluttony_magic")
    );

    public static final DeferredHolder<SchoolType, SchoolType> GLUTTONY = SCHOOLS.register("gluttony", () -> new SchoolType(
            GLUTTONY_RESOURCE,
            GLUTTONY_FOCUS,
            Component.translatable("school.farmers_spell.gluttony").withStyle(Style.EMPTY.withColor(0xFFD700)),
            ModAttributes.GLUTTONY_SPELL_POWER,
            ModAttributes.GLUTTONY_MAGIC_RESIST,
            BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.GENERIC_EAT),
            GLUTTONY_MAGIC,
            true,
            true
    ));

    public static void register(IEventBus eventBus) {
        SCHOOLS.register(eventBus);
    }
}
