package com.chenjdy.farmers_spell.item.curios;

import com.google.common.collect.ImmutableMultimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.item.spell_books.SimpleAttributeSpellBook;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class TiramisuSpellBook extends SimpleAttributeSpellBook {
    private static final ImmutableMultimap<Attribute, AttributeModifier> MODIFIERS;

    static {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(AttributeRegistry.SPELL_POWER.get(), new AttributeModifier(
                UUID.fromString("b5a6c7d8-e9f0-41a2-b3c4-d5e6f7a8b9c0"), "Tiramisu Spell Power", 0.10, AttributeModifier.Operation.MULTIPLY_BASE));
        builder.put(AttributeRegistry.MAX_MANA.get(), new AttributeModifier(
                UUID.fromString("b5a6c7d8-e9f0-41a2-b3c4-d5e6f7a8b9c1"), "Tiramisu Max Mana", 150.0, AttributeModifier.Operation.ADDITION));
        builder.put(AttributeRegistry.COOLDOWN_REDUCTION.get(), new AttributeModifier(
                UUID.fromString("b5a6c7d8-e9f0-41a2-b3c4-d5e6f7a8b9c2"), "Tiramisu Cooldown", 0.10, AttributeModifier.Operation.MULTIPLY_BASE));
        MODIFIERS = builder.build();
    }

    public TiramisuSpellBook() {
        super(12, SpellRarity.LEGENDARY, MODIFIERS);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> lines, TooltipFlag flag) {
        super.appendHoverText(itemStack, level, lines, flag);
    }
}
