package com.chenjdy.farmers_spell.item.curios;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import vectorwing.farmersdelight.common.registry.ModEffects;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = FARMERSSPELL.MODID)
public class FoodgeistRing extends CurioBaseItem {
    public static final UUID MANA_BONUS_UUID = UUID.fromString("F4A5B6C7-D8E9-4F0A-1B2C-3D4E5F6A7B8C");

    public FoodgeistRing(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatable("item.farmers_spell.foodgeist_ring.tooltip"));
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;

        boolean hasRing = CuriosApi.getCuriosHelper().findFirstCurio(player,
            com.chenjdy.farmers_spell.init.ModItems.FOODGEIST_RING.get()).isPresent();

        if (!hasRing) {
            removeManaBonus(player);
            return;
        }

        MobEffectInstance nourishment = player.getEffect(ModEffects.NOURISHMENT.get());

        if (nourishment != null) {
            addManaBonus(player);
        } else {
            removeManaBonus(player);
        }
    }

    private static void addManaBonus(Player player) {
        player.getAttributes().getInstance(AttributeRegistry.MAX_MANA.get())
            .removeModifier(MANA_BONUS_UUID);
        player.getAttributes().getInstance(AttributeRegistry.MAX_MANA.get())
            .addPermanentModifier(new AttributeModifier(MANA_BONUS_UUID, "Spirit Ring Mana Bonus", 75.0, AttributeModifier.Operation.ADDITION));
    }

    private static void removeManaBonus(Player player) {
        player.getAttributes().getInstance(AttributeRegistry.MAX_MANA.get())
            .removeModifier(MANA_BONUS_UUID);
    }
}
