package com.chenjdy.farmers_spell.event;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.init.ModItems;
import io.redspace.ironsspellbooks.entity.mobs.keeper.KeeperEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.pyromancer.PyromancerEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FARMERSSPELL.MODID)
public class KeeperKillHandler {

    @SubscribeEvent
    public static void onPigDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }

        if (event.getEntity() instanceof Pig pig) {
            if (event.getSource().getEntity() instanceof KeeperEntity keeper) {
                if (keeper.getRandom().nextFloat() < 0.5f) {
                    pig.spawnAtLocation(new ItemStack(ModItems.CINDEROUS_HAM.get()));
                }
            }
        }
    }
}
