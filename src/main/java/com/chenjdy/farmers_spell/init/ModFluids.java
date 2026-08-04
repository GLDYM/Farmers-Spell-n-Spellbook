package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FarmersSpell;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import java.util.function.Consumer;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModFluids {

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, FarmersSpell.MODID);

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, FarmersSpell.MODID);

    private static final ResourceLocation WATER_STILL_RL = ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_still");

    private static final ResourceLocation WATER_FLOWING_RL = ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_flow");

    public static final DeferredHolder<FluidType, FluidType> ORIGINAL_NECTAR_TYPE = FLUID_TYPES.register("original_nectar", () -> new FluidType(FluidType.Properties.create()) {

        @Override
        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new ColoredClientExtensions(WATER_STILL_RL, WATER_FLOWING_RL, 0xFFFFB347));
        }
    });

    public static final DeferredHolder<FluidType, FluidType> AMETHYST_TEQUILA_TYPE = FLUID_TYPES.register("amethyst_tequila", () -> new FluidType(FluidType.Properties.create()) {

        @Override
        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new ColoredClientExtensions(WATER_STILL_RL, WATER_FLOWING_RL, 0xFF8E7CC3));
        }
    });

    public static final DeferredHolder<FluidType, FluidType> CATACOMBS_WINE_TYPE = FLUID_TYPES.register("catacombs_wine", () -> new FluidType(FluidType.Properties.create()) {

        @Override
        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new ColoredClientExtensions(WATER_STILL_RL, WATER_FLOWING_RL, 0xFF8B0000));
        }
    });

    public static final DeferredHolder<FluidType, FluidType> ICE_VENOM_WINE_TYPE = FLUID_TYPES.register("ice_venom_wine", () -> new FluidType(FluidType.Properties.create()) {

        @Override
        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new ColoredClientExtensions(WATER_STILL_RL, WATER_FLOWING_RL, 0xFF00BFFF));
        }
    });

    public static final DeferredHolder<Fluid, Fluid> ORIGINAL_NECTAR = FLUIDS.register("original_nectar", () -> new BaseFlowingFluid.Source(ModFluids.ORIGINAL_NECTAR_PROPS));

    public static final DeferredHolder<Fluid, Fluid> ORIGINAL_NECTAR_FLOWING = FLUIDS.register("original_nectar_flowing", () -> new BaseFlowingFluid.Flowing(ModFluids.ORIGINAL_NECTAR_PROPS));

    public static final BaseFlowingFluid.Properties ORIGINAL_NECTAR_PROPS = new BaseFlowingFluid.Properties(ORIGINAL_NECTAR_TYPE, ORIGINAL_NECTAR, ORIGINAL_NECTAR_FLOWING).slopeFindDistance(3).levelDecreasePerBlock(2);

    public static final DeferredHolder<Fluid, Fluid> AMETHYST_TEQUILA = FLUIDS.register("amethyst_tequila", () -> new BaseFlowingFluid.Source(ModFluids.AMETHYST_TEQUILA_PROPS));

    public static final DeferredHolder<Fluid, Fluid> AMETHYST_TEQUILA_FLOWING = FLUIDS.register("amethyst_tequila_flowing", () -> new BaseFlowingFluid.Flowing(ModFluids.AMETHYST_TEQUILA_PROPS));

    public static final BaseFlowingFluid.Properties AMETHYST_TEQUILA_PROPS = new BaseFlowingFluid.Properties(AMETHYST_TEQUILA_TYPE, AMETHYST_TEQUILA, AMETHYST_TEQUILA_FLOWING).slopeFindDistance(3).levelDecreasePerBlock(2);

    public static final DeferredHolder<Fluid, Fluid> CATACOMBS_WINE = FLUIDS.register("catacombs_wine", () -> new BaseFlowingFluid.Source(ModFluids.CATACOMBS_WINE_PROPS));

    public static final DeferredHolder<Fluid, Fluid> CATACOMBS_WINE_FLOWING = FLUIDS.register("catacombs_wine_flowing", () -> new BaseFlowingFluid.Flowing(ModFluids.CATACOMBS_WINE_PROPS));

    public static final BaseFlowingFluid.Properties CATACOMBS_WINE_PROPS = new BaseFlowingFluid.Properties(CATACOMBS_WINE_TYPE, CATACOMBS_WINE, CATACOMBS_WINE_FLOWING).slopeFindDistance(3).levelDecreasePerBlock(2);

    public static final DeferredHolder<Fluid, Fluid> ICE_VENOM_WINE = FLUIDS.register("ice_venom_wine", () -> new BaseFlowingFluid.Source(ModFluids.ICE_VENOM_WINE_PROPS));

    public static final DeferredHolder<Fluid, Fluid> ICE_VENOM_WINE_FLOWING = FLUIDS.register("ice_venom_wine_flowing", () -> new BaseFlowingFluid.Flowing(ModFluids.ICE_VENOM_WINE_PROPS));

    public static final BaseFlowingFluid.Properties ICE_VENOM_WINE_PROPS = new BaseFlowingFluid.Properties(ICE_VENOM_WINE_TYPE, ICE_VENOM_WINE, ICE_VENOM_WINE_FLOWING).slopeFindDistance(3).levelDecreasePerBlock(2);

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
        FLUIDS.register(eventBus);
    }

    private static class ColoredClientExtensions implements IClientFluidTypeExtensions {

        private final ResourceLocation stillTexture;

        private final ResourceLocation flowingTexture;

        private final int tintColor;

        ColoredClientExtensions(ResourceLocation stillTexture, ResourceLocation flowingTexture, int tintColor) {
            this.stillTexture = stillTexture;
            this.flowingTexture = flowingTexture;
            this.tintColor = tintColor;
        }

        @Override
        public ResourceLocation getStillTexture() {
            return stillTexture;
        }

        @Override
        public ResourceLocation getFlowingTexture() {
            return flowingTexture;
        }

        @Override
        public int getTintColor() {
            return tintColor;
        }
    }
}
