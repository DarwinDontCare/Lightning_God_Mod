package net.darwindontcare.lighting_god.networking.packet;

import net.darwindontcare.lighting_god.utils.AddForceToEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AddForceToEntityS2CPacket {
    private Vec3 direction;
    private Entity entity;
    private boolean horizontalMovementOnly;
    public AddForceToEntityS2CPacket(Vec3 direction, Entity entity, boolean horizontalMovementOnly) {
        this.direction = direction;
        this.entity = entity;
        this.horizontalMovementOnly = horizontalMovementOnly;
    }

    public AddForceToEntityS2CPacket(FriendlyByteBuf buf) {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        entity = Minecraft.getInstance().level.getEntity(buf.readInt());
        boolean horizontalMovementOnly = buf.readBoolean();

        direction = new Vec3(x, y, z);
        this.horizontalMovementOnly = horizontalMovementOnly;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(direction.x);
        buf.writeDouble(direction.y);
        buf.writeDouble(direction.z);
        buf.writeInt(entity.getId());
        buf.writeBoolean(horizontalMovementOnly);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            AddForceToEntity.AddForce(entity, direction, horizontalMovementOnly);
        });

        return true;
    }
}
