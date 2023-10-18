package net.darwindontcare.lighting_god.networking.packet;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.lightning_powers.FireFlight;
import net.darwindontcare.lighting_god.lightning_powers.IceSlide;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StartFireFlightC2SPacket {
    private int cooldown;
    public StartFireFlightC2SPacket(int cooldown) {
        if (cooldown <= 0) LightningGodMod.setAlternativeGliding(true);
        this.cooldown = cooldown;
    }

    public StartFireFlightC2SPacket(FriendlyByteBuf buf) {
        cooldown = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            FireFlight.start_flight(player, cooldown);
        });

        return true;
    }
}
