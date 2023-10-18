package net.darwindontcare.lighting_god.networking.packet;

import net.darwindontcare.lighting_god.weapon_powers.FireScythDash;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FireScythDashS2CPacket {
    public FireScythDashS2CPacket() {

    }

    public FireScythDashS2CPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            FireScythDash.Dash(player);
        });

        return true;
    }
}
