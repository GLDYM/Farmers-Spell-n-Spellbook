package com.chenjdy.farmers_spell.mixins;

import com.chenjdy.farmers_spell.init.ModEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @ModifyVariable(method = "travel", at = @At(value = "STORE"), ordinal = 0)
    private float modifyFriction(float friction) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.hasEffect(ModEffects.SEAL_OIL)) {
            return 0.98f;
        }
        return friction;
    }
}