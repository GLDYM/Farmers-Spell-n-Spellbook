package com.chenjdy.farmers_spell.mixins;

import com.chenjdy.farmers_spell.init.ModEffects;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSpell.class)
public class AbstractSpellCastTimeMixin {

    @Inject(method = "getEffectiveCastTime", at = @At("RETURN"), cancellable = true, remap = false)
    private void onGetEffectiveCastTime(int spellLevel, LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        if (entity != null && entity.hasEffect(ModEffects.SEAL_OIL)) {
            int original = cir.getReturnValue();
            cir.setReturnValue(Math.round(original * 1.25f));
        }
    }
}
