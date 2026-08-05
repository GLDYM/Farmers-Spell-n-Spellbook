package com.chenjdy.farmers_spell.init;

import com.google.gson.JsonObject;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.Optional;

public class ModTriggers {

    // 注册触发器到Forge事件总线（空实现，实际注册在CommonSetup中）
    public static void register(IEventBus eventBus) {
        // 实际注册在FMLCommonSetupEvent中通过enqueueWork完成
    }

    // 在FMLCommonSetupEvent中调用此方法来注册所有触发器
    public static void registerTriggers() {
        CriteriaTriggers.register(BLAZE_SCROLL_TRIGGER);
        CriteriaTriggers.register(BUTTER_HIT_TRIGGER);
        CriteriaTriggers.register(PHANTOM_LOOT_TRIGGER);
    }

    // 放置炽焰卷轴触发器
    public static final BlazeScrollTrigger BLAZE_SCROLL_TRIGGER = new BlazeScrollTrigger();

    // 投掷黄油击中实体触发器
    public static final ButterHitTrigger BUTTER_HIT_TRIGGER = new ButterHitTrigger();

    // 神出鬼没窃取战利品触发器
    public static final PhantomLootTrigger PHANTOM_LOOT_TRIGGER = new PhantomLootTrigger();

    // ==================== BlazeScrollTrigger ====================

    public static class BlazeScrollTrigger extends SimpleCriterionTrigger<BlazeScrollTrigger.Instance> {
        private static final ResourceLocation ID = new ResourceLocation("farmers_spell", "blaze_scroll");

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        public void trigger(ServerPlayer player) {
            this.trigger(player, instance -> true);
        }

        @Override
        public BlazeScrollTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext context) {
            return new BlazeScrollTrigger.Instance(player);
        }

        public static BlazeScrollTrigger.Instance instance() {
            return new BlazeScrollTrigger.Instance(ContextAwarePredicate.ANY);
        }

        public static class Instance extends AbstractCriterionTriggerInstance {
            public Instance(ContextAwarePredicate player) {
                super(ID, player);
            }

            public boolean matches() {
                return true;
            }
        }
    }

    // ==================== ButterHitTrigger ====================

    public static class ButterHitTrigger extends SimpleCriterionTrigger<ButterHitTrigger.Instance> {
        private static final ResourceLocation ID = new ResourceLocation("farmers_spell", "butter_hit");

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        public void trigger(ServerPlayer player) {
            this.trigger(player, instance -> true);
        }

        @Override
        public ButterHitTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext context) {
            return new ButterHitTrigger.Instance(player);
        }

        public static ButterHitTrigger.Instance instance() {
            return new ButterHitTrigger.Instance(ContextAwarePredicate.ANY);
        }

        public static class Instance extends AbstractCriterionTriggerInstance {
            public Instance(ContextAwarePredicate player) {
                super(ID, player);
            }

            public boolean matches() {
                return true;
            }
        }
    }

    // ==================== PhantomLootTrigger ====================

    public static class PhantomLootTrigger extends SimpleCriterionTrigger<PhantomLootTrigger.Instance> {
        private static final ResourceLocation ID = new ResourceLocation("farmers_spell", "phantom_loot");

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        public void trigger(ServerPlayer player) {
            this.trigger(player, instance -> true);
        }

        @Override
        public PhantomLootTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext context) {
            return new PhantomLootTrigger.Instance(player);
        }

        public static PhantomLootTrigger.Instance instance() {
            return new PhantomLootTrigger.Instance(ContextAwarePredicate.ANY);
        }

        public static class Instance extends AbstractCriterionTriggerInstance {
            public Instance(ContextAwarePredicate player) {
                super(ID, player);
            }

            public boolean matches() {
                return true;
            }
        }
    }
}
