package net.darwindontcare.lighting_god.lightning_powers;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.entities.CustomLightningBolt;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.SetClientCooldownS2CPacket;
import net.darwindontcare.lighting_god.utils.RaycastUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ElThor {
    private static final double RANGE = 100;
    private static final int ManaCost = 50;
    public static void Attack(ServerPlayer player, int cooldown, float mana) {
        try {
            if (cooldown <= 0 && mana >= ManaCost) {
                HitResult resultBlock = player.pick(RANGE, 1.0f, false);
                RaycastUtil raycastUtil = new RaycastUtil();
                Entity entity = raycastUtil.getEntityInCrosshair(1.0f, RANGE);
                if (entity != null) {
                    System.out.println(entity);
                    summonLightning(player, entity.position());
                } else if (resultBlock.getType().equals(HitResult.Type.BLOCK)) {
                    summonLightning(player, resultBlock.getLocation());
                } else {
                    Vec3 strikePosition = Teleportation.GetNewPositionFromFacingDirection(player, RANGE);
                    summonLightning(player, strikePosition);
                }
                player.setPose(Pose.DIGGING);
                ModMessage.sendToPlayer(new SetClientCooldownS2CPacket("el_thor", ManaCost), player);
            }
        } catch (Exception e) {System.out.println(e.toString());}
    }

    private static void summonLightning(ServerPlayer player, Vec3 position) {
        new Thread(() -> {
            try {
                CustomLightningBolt lightningBolt = new CustomLightningBolt(EntityType.LIGHTNING_BOLT, player.level(), true, player);
                lightningBolt.setPos(position);
                player.level().addFreshEntity(lightningBolt);

                Thread.sleep(200);

                CustomLightningBolt lightningBolt2 = new CustomLightningBolt(EntityType.LIGHTNING_BOLT, player.level(), true, player);
                lightningBolt2.setPos(new Vec3(position.x, position.y, position.z + 1));
                player.level().addFreshEntity(lightningBolt2);

                Thread.sleep(200);

                CustomLightningBolt lightningBolt3 = new CustomLightningBolt(EntityType.LIGHTNING_BOLT, player.level(), true, player);
                lightningBolt3.setPos(new Vec3(position.x + 1, position.y, position.z + 1));
                player.level().addFreshEntity(lightningBolt3);

                Thread.sleep(200);

                CustomLightningBolt lightningBolt4 = new CustomLightningBolt(EntityType.LIGHTNING_BOLT, player.level(), true, player);
                lightningBolt4.setPos(new Vec3(position.x + 1, position.y, position.z));
                player.level().addFreshEntity(lightningBolt4);
            } catch (Exception exception) {
                System.out.println(exception.toString());
            }
        }).start();
    }
}
