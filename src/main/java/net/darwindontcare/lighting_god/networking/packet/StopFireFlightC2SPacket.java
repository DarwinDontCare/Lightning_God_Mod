package net.darwindontcare.lighting_god.networking.packet;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.lightning_powers.FireFlight;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StopFireFlightC2SPacket {
    private int cooldown;
    public StopFireFlightC2SPacket(int cooldown) {
        if (cooldown <= 0) LightningGodMod.setAlternativeGliding(false);
        this.cooldown = cooldown;
    }

    public StopFireFlightC2SPacket(FriendlyByteBuf buf) {
        cooldown = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            FireFlight.stop_flight(player, cooldown);
        });

        return true;
    }
}
