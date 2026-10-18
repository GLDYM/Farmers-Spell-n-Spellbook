package com.chenjdy.farmers_spell.init;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import io.redspace.ironsspellbooks.fluids.NoopFluid;
import io.redspace.ironsspellbooks.fluids.SimpleTintedClientFluidType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class ModFluids {

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, FARMERSSPELL.MODID);
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(Registries.FLUID, FARMERSSPELL.MODID);

    private static final ResourceLocation WATER_STILL_RL =
            ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_still");

    public static final RegistryObject<FluidType> ORIGINAL_NECTAR_TYPE = registerType("original_nectar", 0xFFB347);
    public static final RegistryObject<FluidType> AMETHYST_TEQUILA_TYPE = registerType("amethyst_tequila", 0x8E7CC3);
    public static final RegistryObject<FluidType> CATACOMBS_WINE_TYPE = registerType("catacombs_wine", 0x8B0000);
    public static final RegistryObject<FluidType> ICE_VENOM_WINE_TYPE = registerType("ice_venom_wine", 0x00BFFF);

    public static final RegistryObject<Fluid> ORIGINAL_NECTAR = registerVirtual("original_nectar", ORIGINAL_NECTAR_TYPE);
    public static final RegistryObject<Fluid> AMETHYST_TEQUILA = registerVirtual("amethyst_tequila", AMETHYST_TEQUILA_TYPE);
    public static final RegistryObject<Fluid> CATACOMBS_WINE = registerVirtual("catacombs_wine", CATACOMBS_WINE_TYPE);
    public static final RegistryObject<Fluid> ICE_VENOM_WINE = registerVirtual("ice_venom_wine", ICE_VENOM_WINE_TYPE);

    private static RegistryObject<FluidType> registerType(String name, int tintColor) {
        return FLUID_TYPES.register(name, () -> new FluidType(FluidType.Properties.create()) {
            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(new SimpleTintedClientFluidType(WATER_STILL_RL, tintColor));
            }
        });
    }

    private static RegistryObject<Fluid> registerVirtual(String name, RegistryObject<FluidType> type) {
        RegistryObject<Fluid> fluid = RegistryObject.create(
                ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, name), ForgeRegistries.FLUIDS);
        ForgeFlowingFluid.Properties properties = new ForgeFlowingFluid.Properties(type, fluid, fluid)
                .bucket(() -> Items.BUCKET);
        FLUIDS.register(name, () -> new NoopFluid(properties));
        return fluid;
    }

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
        FLUIDS.register(eventBus);
    }
}
