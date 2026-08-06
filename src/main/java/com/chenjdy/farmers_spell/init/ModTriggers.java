package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FarmersSpell;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

public class ModTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS =
            DeferredRegister.create(Registries.TRIGGER_TYPE, FarmersSpell.MODID);

    public static final DeferredHolder<CriterionTrigger<?>, BlazeScrollTrigger> BLAZE_SCROLL_TRIGGER =
            TRIGGERS.register("blaze_scroll", BlazeScrollTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, ButterHitTrigger> BUTTER_HIT_TRIGGER =
            TRIGGERS.register("butter_hit", ButterHitTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, PhantomLootTrigger> PHANTOM_LOOT_TRIGGER =
            TRIGGERS.register("phantom_loot", PhantomLootTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, FoodgeistSatisfiedTrigger> FOODGEIST_SATISFIED_TRIGGER =
            TRIGGERS.register("foodgeist_satisfied", FoodgeistSatisfiedTrigger::new);

    public static void register(IEventBus eventBus) {
        TRIGGERS.register(eventBus);
    }

    public static class BlazeScrollTrigger extends SimpleCriterionTrigger<BlazeScrollTrigger.TriggerInstance> {
        @Override
        public Codec<TriggerInstance> codec() {
            return TriggerInstance.CODEC;
        }

        public void trigger(ServerPlayer player) {
            this.trigger(player, instance -> true);
        }

        public record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {
            public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance ->
                    instance.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player))
                            .apply(instance, TriggerInstance::new));
        }
    }

    public static class ButterHitTrigger extends SimpleCriterionTrigger<ButterHitTrigger.TriggerInstance> {
        @Override
        public Codec<TriggerInstance> codec() {
            return TriggerInstance.CODEC;
        }

        public void trigger(ServerPlayer player) {
            this.trigger(player, instance -> true);
        }

        public record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {
            public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance ->
                    instance.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player))
                            .apply(instance, TriggerInstance::new));
        }
    }

    public static class PhantomLootTrigger extends SimpleCriterionTrigger<PhantomLootTrigger.TriggerInstance> {
        @Override
        public Codec<TriggerInstance> codec() {
            return TriggerInstance.CODEC;
        }

        public void trigger(ServerPlayer player) {
            this.trigger(player, instance -> true);
        }

        public record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {
            public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance ->
                    instance.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player))
                            .apply(instance, TriggerInstance::new));
        }
    }

    public static class FoodgeistSatisfiedTrigger extends SimpleCriterionTrigger<FoodgeistSatisfiedTrigger.TriggerInstance> {
        @Override
        public Codec<TriggerInstance> codec() {
            return TriggerInstance.CODEC;
        }

        public void trigger(ServerPlayer player) {
            this.trigger(player, instance -> true);
        }

        public record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {
            public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance ->
                    instance.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player))
                            .apply(instance, TriggerInstance::new));
        }
    }
}
