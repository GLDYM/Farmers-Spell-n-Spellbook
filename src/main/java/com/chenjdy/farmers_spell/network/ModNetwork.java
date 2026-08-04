package com.chenjdy.farmers_spell.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModNetwork {

    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(com.chenjdy.farmers_spell.FARMERSSPELL.MODID).versioned("1");

        registrar.playBidirectional(
                BadApplePacket.TYPE,
                BadApplePacket.STREAM_CODEC,
                (payload, context) -> BadApplePacket.handle(payload, context)
        );
    }
}
