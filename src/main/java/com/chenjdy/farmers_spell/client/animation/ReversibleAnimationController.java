package com.chenjdy.farmers_spell.client.animation;

import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;

/**
 * GeckoLib animation controller that can play an animation backwards at a
 * configurable length. Used for the Foodgeist stand-up transition.
 */
public class ReversibleAnimationController<T extends GeoAnimatable> extends AnimationController<T> {
    private boolean reversed;
    private double reverseLengthTicks;
    private double reverseStartTick = Double.NaN;

    public ReversibleAnimationController(T animatable, String name, int transitionTickTime,
                                         AnimationStateHandler<T> animationHandler) {
        super(animatable, name, transitionTickTime, animationHandler);
    }

    public void setReversed(double lengthSeconds) {
        if (!this.reversed) {
            this.reverseStartTick = Double.NaN;
        }
        this.reversed = true;
        this.reverseLengthTicks = lengthSeconds * 20.0D;
    }

    public void setForward() {
        this.reversed = false;
    }

    @Override
    protected double adjustTick(double tick) {
        double forwardElapsed = super.adjustTick(tick);
        if (!this.reversed) {
            this.reverseStartTick = Double.NaN;
            return forwardElapsed;
        }
        if (Double.isNaN(this.reverseStartTick)) {
            this.reverseStartTick = tick;
            return 0.0D;
        }
        if (tick <= this.reverseStartTick) {
            return 0.0D;
        }
        return Math.max(this.reverseLengthTicks - (tick - this.reverseStartTick), 0.0D);
    }
}
