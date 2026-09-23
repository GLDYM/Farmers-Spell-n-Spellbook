package com.chenjdy.farmers_spell.event;

import com.chenjdy.farmers_spell.init.ModItems;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class WanderingTradesHandler {

    @SubscribeEvent
    public static void addWanderingTrades(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> trades = List.of(
                (trader, random) -> new MerchantOffer(
                        new ItemStack(ItemRegistry.ARCANE_ESSENCE.get(), 6),
                        new ItemStack(ModItems.INK_BEER.get()),
                        4,
                        2,
                        0.05f
                )
        );
        event.getGenericTrades().addAll(trades);
    }
}