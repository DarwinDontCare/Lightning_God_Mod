package net.darwindontcare.lighting_god.networking.packet;

import net.darwindontcare.lighting_god.event.FireballEvent;
import net.darwindontcare.lighting_god.lightning_powers.Fireball;
import net.darwindontcare.lighting_god.utils.TeleportPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FireballC2SPacket {
    private int cooldown;
    public FireballC2SPacket(int cooldown) {
        this.cooldown = cooldown;
    }

    public FireballC2SPacket(FriendlyByteBuf buf) {
        cooldown = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            Fireball.ShootFireball(player, cooldown);
        });

        return true;
    }
}
