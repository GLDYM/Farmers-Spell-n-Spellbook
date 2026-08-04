package com.chenjdy.farmers_spell.network;

import com.chenjdy.farmers_spell.FarmersSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;


public class BadApplePacket implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<BadApplePacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(FarmersSpell.MODID, "bad_apple"));

    public static final StreamCodec<FriendlyByteBuf, BadApplePacket> STREAM_CODEC =
            StreamCodec.of((buf, packet) -> packet.encode(buf), BadApplePacket::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    
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
    
    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (play) {
                BadAppleInstance.play(pos);
            } else {
                BadAppleInstance.stopCurrent();
            }
        });
    }
    
    public static void sendToAll(Level level, BlockPos pos, boolean play) {
        if (level instanceof ServerLevel serverLevel) {
            BadApplePacket packet = new BadApplePacket(pos, play);
            for (ServerPlayer player : serverLevel.players()) {
                PacketDistributor.sendToPlayer(player, packet);
            }
        }
    }
}
