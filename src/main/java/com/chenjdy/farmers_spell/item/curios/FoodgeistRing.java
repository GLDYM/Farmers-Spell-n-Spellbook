package com.chenjdy.farmers_spell.item.curios;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import vectorwing.farmersdelight.common.registry.ModEffects;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = FARMERSSPELL.MODID)
public class FoodgeistRing extends CurioBaseItem {
    public static final UUID MANA_BONUS_UUID = UUID.fromString("F4A5B6C7-D8E9-4F0A-1B2C-3D4E5F6A7B8C");

    public FoodgeistRing(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatable("item.farmers_spell.foodgeist_ring.tooltip"));
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        boolean hasRing = CuriosApi.getCuriosInventory(player).flatMap(handler -> handler.findFirstCurio(com.chenjdy.farmers_spell.init.ModItems.FOODGEIST_RING.get())).isPresent();

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
        player.getAttributes().getInstance(AttributeRegistry.MAX_MANA.get())
            .removeModifier(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "spirit_ring_mana_bonus"));
        player.getAttributes().getInstance(AttributeRegistry.MAX_MANA.get())
            .addPermanentModifier(new AttributeModifier(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "spirit_ring_mana_bonus"), 75.0, AttributeModifier.Operation.ADD_VALUE));
    }

    private static void removeManaBonus(Player player) {
        player.getAttributes().getInstance(AttributeRegistry.MAX_MANA.get())
            .removeModifier(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "spirit_ring_mana_bonus"));
    }
}
