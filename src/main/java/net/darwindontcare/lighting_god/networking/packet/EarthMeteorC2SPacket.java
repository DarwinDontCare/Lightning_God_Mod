package net.darwindontcare.lighting_god.networking.packet;

import net.darwindontcare.lighting_god.lightning_powers.EarthLaunch;
import net.darwindontcare.lighting_god.lightning_powers.EarthMeteor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class EarthMeteorC2SPacket {
    private int cooldown;
    public EarthMeteorC2SPacket(int cooldown) {
        this.cooldown = cooldown;
    }

    public EarthMeteorC2SPacket(FriendlyByteBuf buf) {
        cooldown = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            EarthMeteor.Strike(player, cooldown);
        });

        return true;
    }
}
