package net.darwindontcare.lighting_god.networking.packet;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.lightning_powers.IceSlide;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StopIceSlideC2SPacket {
    private int cooldown;
    public StopIceSlideC2SPacket(int cooldown) {
        if (cooldown <= 0) LightningGodMod.setIsIceSliding(false);
        this.cooldown = cooldown;
    }

    public StopIceSlideC2SPacket(FriendlyByteBuf buf) {
        cooldown = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            IceSlide.FinishSlide(player, cooldown);
        });

        return true;
    }
}
