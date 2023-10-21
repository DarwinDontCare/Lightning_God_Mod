package net.darwindontcare.lighting_god.networking.packet;

import net.darwindontcare.lighting_god.lightning_powers.FireBurst;
import net.darwindontcare.lighting_god.lightning_powers.FirePull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FirePullC2SPacket {
    private int cooldown;
    public FirePullC2SPacket(int cooldown) {
        this.cooldown = cooldown;
    }

    public FirePullC2SPacket(FriendlyByteBuf buf) {
        cooldown = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            FirePull.Pull(player, cooldown);
        });

        return true;
    }
}
