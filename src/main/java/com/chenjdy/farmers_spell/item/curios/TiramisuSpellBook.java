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
import net.minecraft.world.item.Item;


public class TiramisuSpellBook extends SimpleAttributeSpellBook {
    private static final ImmutableMultimap<Attribute, AttributeModifier> MODIFIERS;

    static {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(AttributeRegistry.SPELL_POWER.get(), new AttributeModifier(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.chenjdy.farmers_spell.FARMERSSPELL.MODID, "tiramisu_spell_power"), 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        builder.put(AttributeRegistry.MAX_MANA.get(), new AttributeModifier(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.chenjdy.farmers_spell.FARMERSSPELL.MODID, "tiramisu_max_mana"), 150.0, AttributeModifier.Operation.ADD_VALUE));
        builder.put(AttributeRegistry.COOLDOWN_REDUCTION.get(), new AttributeModifier(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.chenjdy.farmers_spell.FARMERSSPELL.MODID, "tiramisu_cooldown"), 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        MODIFIERS = builder.build();
    }

    public TiramisuSpellBook() {
        super(12, SpellRarity.LEGENDARY, MODIFIERS);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext level, List<Component> lines, TooltipFlag flag) {
        super.appendHoverText(itemStack, level, lines, flag);
    }
}
