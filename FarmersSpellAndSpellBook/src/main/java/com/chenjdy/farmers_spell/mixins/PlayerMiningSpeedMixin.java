package com.chenjdy.farmers_spell.mixins;

import com.chenjdy.farmers_spell.item.curios.RingManaBonusHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Player.class)
public abstract class PlayerMiningSpeedMixin {

    @Inject(method = "getDigSpeed", at = @At("RETURN"), cancellable = true, remap = false)
    private void farmers_spell$applyRingMiningSpeed(BlockState state, @Nullable BlockPos pos, CallbackInfoReturnable<Float> cir) {
        Player player = (Player) (Object) this;
        if (RingManaBonusHelper.isAnyRingEquipped(player) && RingManaBonusHelper.hasNourishmentOrComfort(player)) {
            cir.setReturnValue(cir.getReturnValue() * 1.40F);
        }
    }
}
