package com.chenjdy.farmers_spell.item.curios;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.init.ModItems;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import top.theillusivec4.curios.api.CuriosApi;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.List;
import java.util.UUID;

@EventBusSubscriber(modid = FarmersSpell.MODID)
public class AffinityRingGlutton extends CurioBaseItem {
    public static final UUID MANA_BONUS_UUID = UUID.fromString("A5B6C7D8-E9F0-4A5B-8C9D-0E1F2A3B4C5D");
    private static final ResourceLocation BONUS_ID = ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "glutton_ring_mana_bonus");

    public AffinityRingGlutton(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatable("item.farmers_spell.affinity_ring_glutton.tooltip"));
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        boolean hasRing = CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.isEquipped(ModItems.AFFINITY_RING_GLUTTON.get()))
                .orElse(false);

        if (!hasRing) {
            removeManaBonus(player);
            return;
        }

        MobEffectInstance nourishment = player.getEffect(ModEffects.NOURISHMENT);
        if (nourishment != null) {
            addManaBonus(player);
        } else {
            removeManaBonus(player);
        }
    }

    private static void addManaBonus(Player player) {
        var attribute = player.getAttributes().getInstance(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.MAX_MANA.get()));
        if (attribute == null) {
            return;
        }
        attribute.removeModifier(BONUS_ID);
        attribute.addPermanentModifier(new AttributeModifier(BONUS_ID, 150.0, AttributeModifier.Operation.ADD_VALUE));
    }

    private static void removeManaBonus(Player player) {
        var attribute = player.getAttributes().getInstance(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributeRegistry.MAX_MANA.get()));
        if (attribute != null) {
            attribute.removeModifier(BONUS_ID);
        }
    }
}
