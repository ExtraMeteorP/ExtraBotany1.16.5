package com.meteor.extrabotany.common.network;

import com.meteor.extrabotany.common.items.ItemFirstFractal;
import com.meteor.extrabotany.common.items.ModItems;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class FractalLeftClickPack {

    public FractalLeftClickPack(PacketBuffer buffer) {

    }

    public FractalLeftClickPack() {

    }

    public void toBytes(PacketBuffer buf) {

    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
                ServerPlayerEntity player = ctx.get().getSender();
                ctx.get().enqueueWork(() -> ((ItemFirstFractal) ModItems.firstfractal).trySpawnPhantomSword(player, null));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
