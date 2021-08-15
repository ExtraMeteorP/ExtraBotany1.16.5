package com.meteor.extrabotany.common.network.flamescion;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class FlamescionLeftClickPack {

    public FlamescionLeftClickPack(PacketBuffer buffer) {

    }

    public FlamescionLeftClickPack() {

    }

    public void toBytes(PacketBuffer buf) {

    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
                ServerPlayerEntity player = ctx.get().getSender();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
