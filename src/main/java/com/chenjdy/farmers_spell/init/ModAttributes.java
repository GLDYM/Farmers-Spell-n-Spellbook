package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FarmersSpell;
import io.redspace.ironsspellbooks.api.attribute.MagicRangedAttribute;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.fml.common.EventBusSubscriber;
@EventBusSubscriber(modid = FarmersSpell.MODID)
public class ModAttributes {

    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, FarmersSpell.MODID);

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    public static final DeferredHolder<Attribute, Attribute> GLUTTONY_SPELL_POWER = ATTRIBUTES.register("gluttony_spell_power", 
            () -> (new MagicRangedAttribute("attribute.farmers_spell.gluttony_spell_power", 1.0D, -100, 100.0D).setSyncable(true)));

    public static final DeferredHolder<Attribute, Attribute> GLUTTONY_MAGIC_RESIST = ATTRIBUTES.register("gluttony_magic_resist", 
            () -> (new MagicRangedAttribute("attribute.farmers_spell.gluttony_magic_resist", 1.0D, -100, 100.0D).setSyncable(true)));

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent e) {
        e.getTypes().forEach(entity -> ATTRIBUTES.getEntries().forEach(attribute -> e.add(entity, attribute)));
    }
}
