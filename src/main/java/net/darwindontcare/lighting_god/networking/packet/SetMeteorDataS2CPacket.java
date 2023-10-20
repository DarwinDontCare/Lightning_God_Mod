package net.darwindontcare.lighting_god.networking.packet;

import net.darwindontcare.lighting_god.entities.custom.MeteorProjectile;
import net.darwindontcare.lighting_god.utils.AddForceToEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetMeteorDataS2CPacket {
    private Vec3 direction;
    private float maxSpeed;
    private MeteorProjectile entity;
    public SetMeteorDataS2CPacket(Vec3 direction, float maxSpeed, MeteorProjectile entity) {
        this.direction = direction;
        this.maxSpeed = maxSpeed;
        this.entity = entity;
    }

    public SetMeteorDataS2CPacket(FriendlyByteBuf buf) {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        maxSpeed = buf.readFloat();
        entity = (MeteorProjectile) Minecraft.getInstance().level.getEntity(buf.readInt());

        direction = new Vec3(x, y, z);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(direction.x);
        buf.writeDouble(direction.y);
        buf.writeDouble(direction.z);
        buf.writeFloat(maxSpeed);
        buf.writeInt(entity.getId());
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            entity.setClientData(direction, maxSpeed, entity.getId());
        });

        return true;
    }
}
