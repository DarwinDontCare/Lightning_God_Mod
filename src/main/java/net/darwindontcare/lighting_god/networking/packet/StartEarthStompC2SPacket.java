package net.darwindontcare.lighting_god.networking.packet;

import net.darwindontcare.lighting_god.lightning_powers.EarthStomp;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StartEarthStompC2SPacket {
    private int level;
    private int cooldown;
    public StartEarthStompC2SPacket(int level, int cooldown) {
        this.level = level;
        //this.cooldown = cooldown;
    }

    public StartEarthStompC2SPacket(FriendlyByteBuf buf) {
        level = buf.readInt();
        //cooldown = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(level);
        //buf.writeInt(cooldown);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            EarthStomp.StartStomp(player, level);
        });

        return true;
    }
}
