package net.darwindontcare.lighting_god.networking.packet;

import net.darwindontcare.lighting_god.entities.custom.IceSpikes;
import net.darwindontcare.lighting_god.lightning_powers.Fireball;
import net.darwindontcare.lighting_god.utils.AddForceToEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class IceSpikeDamageC2SPacket {
    private Vec3 position;
    private Entity iceSpike;
    public IceSpikeDamageC2SPacket(Vec3 position, Entity iceSpike) {
        this.position = position;
        this.iceSpike = iceSpike;
    }

    public IceSpikeDamageC2SPacket(FriendlyByteBuf buf) {
        try {
            double posX = buf.readDouble();
            double posY = buf.readDouble();
            double posZ = buf.readDouble();
            position = new Vec3(posX, posY, posZ);
            iceSpike = Minecraft.getInstance().level.getEntity(buf.readInt());
        } catch (Exception e) {System.out.println(e.toString());}
    }

    public void toBytes(FriendlyByteBuf buf) {
        try {
            buf.writeDouble(position.x);
            buf.writeDouble(position.y);
            buf.writeDouble(position.z);
            buf.writeInt(iceSpike.getId());
        } catch (Exception e) {System.out.println(e.toString());}
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (iceSpike != null) {
                if (iceSpike instanceof IceSpikes) {
                    List<LivingEntity> nearbyEntities = player.level().getEntitiesOfClass(
                            LivingEntity.class,
                            new AABB(position.x, position.y - 3, position.z - 3, position.x, position.y + 3, position.z + 3)
                    );

                    for (LivingEntity entity : nearbyEntities) {
                        if (entity != ((IceSpikes) iceSpike).getOwner() && !(((Entity) entity) instanceof ItemEntity)) {
                            AddForceToEntity.AddForce(entity, new Vec3(0, 2, 0), false);
                            entity.hurt(((IceSpikes) iceSpike).damageSources().playerAttack((Player) ((IceSpikes) iceSpike).getOwner()), 15);
                        }
                    }
                }
            }
        });

        return true;
    }
}
