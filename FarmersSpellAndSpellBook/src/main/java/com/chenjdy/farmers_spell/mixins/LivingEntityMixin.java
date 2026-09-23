package com.chenjdy.farmers_spell.mixins;

import com.chenjdy.farmers_spell.init.ModEffects;
import com.chenjdy.farmers_spell.item.curios.RingManaBonusHelper;
import com.chenjdy.farmers_spell.mixins.accessor.LivingEntityAccessor;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @ModifyVariable(method = "travel", at = @At(value = "STORE"), ordinal = 0)
    private float modifyFriction(float friction) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.hasEffect(ModEffects.SEAL_OIL.get())) {
            return 0.98f;
        }
        return friction;
    }

    @Inject(method = "startUsingItem", at = @At("TAIL"))
    private void farmers_spell$halveFoodUseDuration(InteractionHand hand, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!(entity instanceof Player player)) return;
        ItemStack useItem = entity.getUseItem();
        if (useItem.isEmpty() || !useItem.getItem().isEdible()) return;
        if (!RingManaBonusHelper.isAnyRingEquipped(player)) return;
        int current = ((LivingEntityAccessor) entity).getUseItemRemaining();
        if (current > 1 && current == useItem.getUseDuration()) {
            ((LivingEntityAccessor) entity).setUseItemRemaining(Math.max(1, current / 2));
        }
    }
}