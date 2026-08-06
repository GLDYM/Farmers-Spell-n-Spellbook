package com.chenjdy.farmers_spell.network;

import com.chenjdy.farmers_spell.FARMERSSPELL;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    
    public static final String PROTOCOL_VERSION = "1";
    
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(FARMERSSPELL.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    
    private static int id = 0;
    
    public static void register() {
        CHANNEL.registerMessage(id++, BadApplePacket.class,
                BadApplePacket::encode,
                BadApplePacket::new,
                BadApplePacket::handle);
    }
}