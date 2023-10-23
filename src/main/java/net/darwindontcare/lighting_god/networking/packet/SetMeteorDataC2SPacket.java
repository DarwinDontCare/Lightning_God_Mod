package net.darwindontcare.lighting_god.networking.packet;

import net.darwindontcare.lighting_god.entities.custom.MeteorProjectile;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class SetMeteorDataC2SPacket {
    private boolean canMove;
    private int entity;
    public SetMeteorDataC2SPacket(int entity, boolean canMove) {
        this.canMove = canMove;
        this.entity = entity;
    }

    public SetMeteorDataC2SPacket(FriendlyByteBuf buf) {
        entity = buf.readInt();
        canMove = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(entity);
        buf.writeBoolean(canMove);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            try {
                MeteorProjectile projectile = (MeteorProjectile) player.level().getEntity(entity);
                if (projectile == null) {
                    List<MeteorProjectile> projectiles = player.level().getEntitiesOfClass(
                            MeteorProjectile.class,
                            new AABB(player.getX() - 1000, player.getY() - 1000, player.getZ() - 1000, player.getX() + 1000, player.getY() + 1000, player.getZ() + 1000)
                    );

                    for (MeteorProjectile meteorProjectile: projectiles) {
                        if (!meteorProjectile.canStartMoving()) {
                            projectile = meteorProjectile;
                        }
                    }
                }
                projectile.setServerData(canMove);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        });

        return true;
    }
}
