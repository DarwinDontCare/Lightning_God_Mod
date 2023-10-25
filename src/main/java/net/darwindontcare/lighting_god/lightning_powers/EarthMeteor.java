package net.darwindontcare.lighting_god.lightning_powers;

import net.darwindontcare.lighting_god.entities.EntityInit;
import net.darwindontcare.lighting_god.entities.custom.MeteorProjectile;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.SetClientCooldownS2CPacket;
import net.darwindontcare.lighting_god.utils.RaycastUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.core.jmx.Server;

public class EarthMeteor {
    private static final double RANGE = 100;
    private static final int ManaCost = 100;
    public static void Strike(ServerPlayer player, int cooldown, float mana) {
        if (cooldown <= 0 && mana >= ManaCost) {
            HitResult resultBlock = player.pick(RANGE, 1.0f, false);
            RaycastUtil raycastUtil = new RaycastUtil();
            Entity entity = raycastUtil.getEntityInCrosshair(1.0f, RANGE);

            if (entity != null) {
                System.out.println(entity);
                summonMeteor(player, entity.position());
            } else if (resultBlock.getType().equals(HitResult.Type.BLOCK)) {
                summonMeteor(player, resultBlock.getLocation());
            } else {
                Vec3 strikePosition = Teleportation.GetNewPositionFromFacingDirection(player, RANGE);
                summonMeteor(player, strikePosition);
            }
            ModMessage.sendToPlayer(new SetClientCooldownS2CPacket("earth_meteor", ManaCost), player);
        }
    }

    private static void summonMeteor(ServerPlayer player, Vec3 position) {
        try {
            ServerLevel serverLevel = (ServerLevel)player.level();
            MeteorProjectile meteorProjectile = new MeteorProjectile(serverLevel, player, 0, 0, 0, 30, new Vec3(0, -2f, 0));
            meteorProjectile.setPower(30);
            try {
                meteorProjectile.setPos(position.add(new Vec3(0, 50, 0)));
                for (int i = 0; i < 30; i++) {
                    serverLevel.sendParticles(ParticleTypes.FLAME, meteorProjectile.getX(), meteorProjectile.getY(), meteorProjectile.getZ(), 1, player.getRandomY() * 0.01, player.getRandomY() * 0.01, 0, player.getRandomY() * 0.01);
                }
            } catch (Exception e) {
                meteorProjectile.setPos(position);
            }
            meteorProjectile.setOwner(player);

            serverLevel.addFreshEntity(meteorProjectile);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
