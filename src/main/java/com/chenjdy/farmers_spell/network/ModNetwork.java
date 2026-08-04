package com.chenjdy.farmers_spell.network;

import com.chenjdy.farmers_spell.FarmersSpell;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModNetwork {

    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(FarmersSpell.MODID).versioned("1");

        registrar.playBidirectional(
                BadApplePacket.TYPE,
                BadApplePacket.STREAM_CODEC,
                (payload, context) -> payload.handle(context)
        );
    }
}
