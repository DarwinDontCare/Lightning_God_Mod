package net.darwindontcare.lighting_god.networking.packet;

import net.darwindontcare.lighting_god.utils.AddForceToEntity;
import net.darwindontcare.lighting_god.utils.SummonEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SummonEntityC2SPacket {
    private Vec3 motion;
    private Vec3 position;
    private Entity entity;
    private boolean isEarthAttack;
    private boolean holdEntity;
    public SummonEntityC2SPacket(Entity entity, Vec3 motion, Vec3 position, boolean isEarthAttack, boolean holdEntity) {
        this.entity = entity;
        this.motion = motion;
        this.position = position;
        this.isEarthAttack = isEarthAttack;
        this.holdEntity = holdEntity;
    }

    public SummonEntityC2SPacket(FriendlyByteBuf buf) {
        double motionX = buf.readDouble();
        double motionY = buf.readDouble();
        double motionZ = buf.readDouble();
        double posX = buf.readDouble();
        double posY = buf.readDouble();
        double posZ = buf.readDouble();
        entity = Minecraft.getInstance().level.getEntity(buf.readInt());
        this.isEarthAttack = buf.readBoolean();
        this.holdEntity = buf.readBoolean();

        motion = new Vec3(motionX, motionY, motionZ);
        position = new Vec3(posX, posY, posZ);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(motion.x);
        buf.writeDouble(motion.y);
        buf.writeDouble(motion.z);
        buf.writeDouble(position.x);
        buf.writeDouble(position.y);
        buf.writeDouble(position.z);
        buf.writeInt(entity.getId());
        buf.writeBoolean(isEarthAttack);
        buf.writeBoolean(holdEntity);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            SummonEntity.Summon(player, entity, position, motion, isEarthAttack, holdEntity);
        });

        return true;
    }
}
