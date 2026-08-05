package com.chenjdy.farmers_spell.mixins;

import com.chenjdy.farmers_spell.init.ModEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "setSecondsOnFire", at = @At("HEAD"), cancellable = true)
    private void onSetSecondsOnFire(int pSeconds, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(ModEffects.GOLDEN_ARMOR.get())) {
            ci.cancel();
        }
    }

    @Inject(method = "setRemainingFireTicks", at = @At("HEAD"), cancellable = true)
    private void onSetRemainingFireTicks(int pTicks, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(ModEffects.GOLDEN_ARMOR.get())) {
            if (pTicks > 0) {
                ci.cancel();
            }
        }
    }
}