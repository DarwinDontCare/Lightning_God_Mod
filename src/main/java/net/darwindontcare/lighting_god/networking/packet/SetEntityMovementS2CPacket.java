package net.darwindontcare.lighting_god.networking.packet;

import net.darwindontcare.lighting_god.utils.SetEntityPos;
import net.darwindontcare.lighting_god.utils.SummonEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetEntityMovementS2CPacket {
    private Vec3 position;
    private float rotationY;
    private float rotationX;
    private Entity entity;
    public SetEntityMovementS2CPacket(Entity entity, Vec3 position, float rotationY, float rotationX) {
        this.entity = entity;
        this.position = position;
        this.rotationY = rotationY;
        this.rotationX = rotationX;
    }

    public SetEntityMovementS2CPacket(FriendlyByteBuf buf) {
        double posX = buf.readDouble();
        double posY = buf.readDouble();
        double posZ = buf.readDouble();
        rotationY = buf.readFloat();
        rotationX = buf.readFloat();
        entity = Minecraft.getInstance().level.getEntity(buf.readInt());

        position = new Vec3(posX, posY, posZ);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(position.x);
        buf.writeDouble(position.y);
        buf.writeDouble(position.z);
        buf.writeFloat(rotationY);
        buf.writeFloat(rotationX);
        buf.writeInt(entity.getId());
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            SetEntityPos.SetPos(entity, position, rotationY, rotationX);
        });

        return true;
    }
}
