package com.chenjdy.farmers_spell.event;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import com.chenjdy.farmers_spell.item.weapons.GospelButterKnife;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FARMERSSPELL.MODID)
public class WeaponEventHandler {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        if (target.getMobType() != MobType.UNDEAD) {
            return;
        }

        if (event.getSource().getDirectEntity() != event.getSource().getEntity()) {
            return;
        }

        if (event.getSource().getEntity() instanceof LivingEntity attacker
                && attacker.getMainHandItem().getItem() instanceof GospelButterKnife) {
            event.setAmount(event.getAmount() + 2.5f);
        }
    }
}
