package com.chenjdy.farmers_spell.item;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.init.ModAttributes;
import io.redspace.ironsspellbooks.item.UpgradeOrbItem;
import io.redspace.ironsspellbooks.item.armor.UpgradeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

import java.util.Optional;

public class GluttonyUpgradeOrbItem extends UpgradeOrbItem {
    public static final UpgradeType TYPE = new UpgradeType() {
        @Override
        public net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> getAttribute() {
            return ModAttributes.GLUTTONY_SPELL_POWER;
        }

        @Override
        public AttributeModifier.Operation getOperation() {
            return AttributeModifier.Operation.ADD_MULTIPLIED_BASE;
        }

        @Override
        public float getAmountPerUpgrade() {
            return 0.05F;
        }

        @Override
        public ResourceLocation getId() {
            return ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "gluttony_power");
        }

        @Override
        public Optional<net.minecraft.core.Holder<Item>> getContainerItem() {
            return Optional.empty();
        }
    };

    static {
        UpgradeType.registerUpgrade(TYPE);
    }

    public GluttonyUpgradeOrbItem(Properties properties) {
        super(TYPE, properties);
    }
}
