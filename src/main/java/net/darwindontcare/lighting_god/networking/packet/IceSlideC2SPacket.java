package net.darwindontcare.lighting_god.networking.packet;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.lightning_powers.Freeze;
import net.darwindontcare.lighting_god.lightning_powers.IceSlide;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class IceSlideC2SPacket {
    private int cooldown;
    private float mana;
    public IceSlideC2SPacket(int cooldown, float mana) {
        this.cooldown = cooldown;
        this.mana = mana;
    }

    public IceSlideC2SPacket(FriendlyByteBuf buf) {
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
            IceSlide.StartSlide(player, cooldown, mana);
        });

        return true;
    }
}
