package net.darwindontcare.lighting_god.networking.packet;

import net.darwindontcare.lighting_god.lightning_powers.FireRay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FirePullC2SPacket {
    private int cooldown;
    private float mana;
    public FirePullC2SPacket(int cooldown, float mana) {
        this.cooldown = cooldown;
        this.mana = mana;
    }

    public FirePullC2SPacket(FriendlyByteBuf buf) {
        cooldown = buf.readInt();
        mana = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
        buf.writeFloat(mana);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            FireRay.Ray(player, cooldown, mana);
        });

        return true;
    }
}
