package com.chenjdy.farmers_spell.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BadApplePacket {
    
    private final BlockPos pos;
    private final boolean play;
    
    public BadApplePacket(BlockPos pos, boolean play) {
        this.pos = pos;
        this.play = play;
    }
    
    public BadApplePacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.play = buf.readBoolean();
    }
    
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeBoolean(play);
    }
    
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (play) {
                BadAppleInstance.play(pos);
            } else {
                BadAppleInstance.stopCurrent();
            }
        });
        context.get().setPacketHandled(true);
    }
    
    public static void sendToAll(Level level, BlockPos pos, boolean play) {
        if (level instanceof ServerLevel serverLevel) {
            BadApplePacket packet = new BadApplePacket(pos, play);
            for (ServerPlayer player : serverLevel.players()) {
                NetworkHandler.CHANNEL.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            }
        }
    }
}