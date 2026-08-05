package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModSchools {

    public static final ResourceLocation GLUTTONY_RESOURCE = ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "gluttony");

    public static final DeferredRegister<SchoolType> SCHOOLS = DeferredRegister.create(SchoolRegistry.SCHOOL_REGISTRY_KEY, FARMERSSPELL.MODID);

    public static final TagKey<Item> GLUTTONY_FOCUS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "gluttony_focus"));

    public static final ResourceKey<DamageType> GLUTTONY_MAGIC = ResourceKey.create(
            net.minecraft.core.registries.Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "gluttony_magic")
    );

    @SuppressWarnings("removal")
    public static final RegistryObject<SchoolType> GLUTTONY = SCHOOLS.register("gluttony", () -> new SchoolType(
            GLUTTONY_RESOURCE,
            GLUTTONY_FOCUS,
            Component.translatable("school.farmers_spell.gluttony").withStyle(Style.EMPTY.withColor(0xFFD700)),
            LazyOptional.empty(),
            LazyOptional.empty(),
            LazyOptional.of(() -> SoundEvents.GENERIC_EAT),
            GLUTTONY_MAGIC
    ));

    public static void register(IEventBus eventBus) {
        SCHOOLS.register(eventBus);
    }
}
