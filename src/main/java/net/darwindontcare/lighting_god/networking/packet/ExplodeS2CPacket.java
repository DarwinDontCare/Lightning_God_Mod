package net.darwindontcare.lighting_god.networking.packet;

import net.darwindontcare.lighting_god.utils.SetEntityPos;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ExplodeS2CPacket {
    private Vec3 position;
    private float power;
    private Entity caster;

    public ExplodeS2CPacket(Vec3 position, Entity caster, float power) {
        this.position = position;
        this.caster = caster;
        this.power = power;
    }

    public ExplodeS2CPacket(FriendlyByteBuf buf) {
        double posX = buf.readDouble();
        double posY = buf.readDouble();
        double posZ = buf.readDouble();
        caster = Minecraft.getInstance().level.getEntity(buf.readInt());
        power = buf.readFloat();

        position = new Vec3(posX, posY, posZ);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(position.x);
        buf.writeDouble(position.y);
        buf.writeDouble(position.z);
        buf.writeInt(caster.getId());
        buf.writeFloat(power);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            Player player = context.getSender();
            player.level.explode(caster, position.x, position.y, position.z, power, Level.ExplosionInteraction.NONE);
        });

        return true;
    }
}
