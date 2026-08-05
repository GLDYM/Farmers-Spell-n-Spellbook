package com.chenjdy.farmers_spell.event;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.chenjdy.farmers_spell.init.ModItems;
import io.redspace.ironsspellbooks.entity.mobs.keeper.KeeperEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = FarmersSpell.MODID)
public class KeeperKillHandler {

    @SubscribeEvent
    public static void onPigDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }

        if (event.getEntity() instanceof Pig pig && event.getSource().getEntity() instanceof KeeperEntity keeper) {
            if (keeper.getRandom().nextFloat() < 0.5f) {
                pig.spawnAtLocation(new ItemStack(ModItems.CINDEROUS_HAM.get()));
            }
        }
    }
}
