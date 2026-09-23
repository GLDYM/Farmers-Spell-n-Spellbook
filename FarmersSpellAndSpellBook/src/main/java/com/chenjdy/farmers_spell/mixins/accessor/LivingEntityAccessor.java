package com.chenjdy.farmers_spell.mixins.accessor;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {

    @Accessor("useItemRemaining")
    int getUseItemRemaining();

    @Accessor("useItemRemaining")
    void setUseItemRemaining(int value);
}
