package net.darwindontcare.lighting_god.networking.packet;

import net.darwindontcare.lighting_god.lightning_powers.Fireball;
import net.darwindontcare.lighting_god.lightning_powers.Freeze;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FreezeC2SPacket {
    private int cooldown;
    public FreezeC2SPacket(int cooldown) {
        this.cooldown = cooldown;
    }

    public FreezeC2SPacket(FriendlyByteBuf buf) {
        cooldown = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            Freeze freeze = new Freeze();
            freeze.FreezingGust(player, cooldown);
        });

        return true;
    }
}
