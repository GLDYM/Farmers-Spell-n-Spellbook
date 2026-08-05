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

public class LasagnowledgeSpellBook extends SimpleAttributeSpellBook {
    private static final ImmutableMultimap<Attribute, AttributeModifier> MODIFIERS;

    static {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(AttributeRegistry.SPELL_POWER.get(), new AttributeModifier(
                UUID.fromString("c4d5e6f7-a8b9-40c1-d2e3-f4a5b6c7d8e9"), "Lasagna Spell Power", 0.10, AttributeModifier.Operation.MULTIPLY_BASE));
        builder.put(AttributeRegistry.MAX_MANA.get(), new AttributeModifier(
                UUID.fromString("c4d5e6f7-a8b9-40c1-d2e3-f4a5b6c7d8ea"), "Lasagna Max Mana", 100.0, AttributeModifier.Operation.ADDITION));
        MODIFIERS = builder.build();
    }

    public LasagnowledgeSpellBook() {
        super(10, SpellRarity.EPIC, MODIFIERS);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> lines, TooltipFlag flag) {
        super.appendHoverText(itemStack, level, lines, flag);
    }
}
