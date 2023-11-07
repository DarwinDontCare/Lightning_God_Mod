package net.darwindontcare.lighting_god.lightning_powers;

import net.darwindontcare.lighting_god.entities.CustomLightningBolt;
import net.darwindontcare.lighting_god.entities.EntityInit;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.SetClientCooldownS2CPacket;
import net.darwindontcare.lighting_god.utils.RaycastUtil;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.ArrayList;
import java.util.HashMap;

import static net.darwindontcare.lighting_god.utils.SummonEntity.clamp;

public class LightningBeam {
    public static final float ManaCost = 1f;
    private static final float damage = 2f;
    private static final float reach = 100f;
    private static final ArrayList<Boolean> beaming = new ArrayList<>();
    private static final ArrayList<Integer> playerList = new ArrayList<>();

    public static void Beam(ServerPlayer player, int cooldown, float mana) {
        if (cooldown <= 0 && mana > ManaCost) {
            if (!playerList.contains(player.getId())) {
                beaming.add(false);
                playerList.add(player.getId());
            }
            BeamTick(player, cooldown, mana);
        } else StopBeam(player, cooldown);
    }

    private static void BeamTick(ServerPlayer player, int cooldown, float mana) {
        boolean isBeaming = false;
        int playerIndex = 0;
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i) == player.getId()) {
                isBeaming = beaming.get(i);
                playerIndex = i;
                break;
            }
        }

        if (!isBeaming) {
            beaming.set(playerIndex, true);
            int finalPlayerIndex = playerIndex;
            new Thread(() ->{
                try {
                    while (beaming.get(finalPlayerIndex) && mana >= ManaCost && !player.isDeadOrDying()) {
                        HitResult resultBlock = player.pick(reach, 1.0f, false);
                        RaycastUtil raycastUtil = new RaycastUtil();
                        Entity entity = raycastUtil.getEntityInCrosshair(1.0f, reach);
                        if (entity != null) {
                            System.out.println(entity);
                            SummonLightning(player, entity.position());
                        } else if (resultBlock.getType().equals(HitResult.Type.BLOCK)) {
                            SummonLightning(player, resultBlock.getLocation());
                        } else {
                            Vec3 strikePosition = Teleportation.GetNewPositionFromFacingDirection(player, reach);
                            SummonLightning(player, strikePosition);
                        }
                        ModMessage.sendToPlayer(new SetClientCooldownS2CPacket("", ManaCost), player);
                        Thread.sleep(100);
                    }
                    StopBeam(player, cooldown);
                } catch (Exception e) {System.out.println(e.toString());}
            }).start();
        }
    }

    private static void SummonLightning(ServerPlayer player, Vec3 pos) {
        CustomLightningBolt lightningBolt = new CustomLightningBolt(EntityInit.CUSTOM_LIGHTNING.get(), player.level(), true, player);
        lightningBolt.setPos(pos);
        lightningBolt.setDamage(damage);
        RandomSource randomSource = RandomSource.create(lightningBolt.seed);
        double leftX = -Math.sin(Math.toRadians(player.getYRot() + 90));
        double leftZ = Math.cos(Math.toRadians(player.getYRot() + 90));
        Vec3 playerLeft = new Vec3(Math.round(leftX), 0, Math.round(leftZ));
        Vec3 up = new Vec3(0, player.getForward().y, 0);

        try {
            ArrayList<Vec3> currentPos = new ArrayList<>();
            ArrayList<Vec3> stopedBranches = new ArrayList<>();
            HashMap<Integer, Vec2> joints = new HashMap<>();

            joints.put(0, new Vec2(player.getXRot(), player.getYRot()));
            currentPos.add(player.getEyePosition().add(player.getForward()));
            ServerLevel serverLevel = (ServerLevel) player.level();

            for (int distance = 1; distance < reach; distance++) {
                for (int i = 0; i < currentPos.size(); i++) {
                    Vec3 currentPos1 = currentPos.get(i);
                    Vec2 currentJoint = joints.get(i);
                    if (!stopedBranches.contains(currentPos1) && currentJoint != null) {
                        for (float betweenSpace = 0; betweenSpace < 1; betweenSpace += 0.1f) {
                            double xDirection = -Math.sin(Math.toRadians(currentJoint.y));
                            double yDirection = -Math.sin(Math.toRadians(currentJoint.x));
                            double zDirection = Math.cos(Math.toRadians(currentJoint.y));
                            Vec3 direction = new Vec3(xDirection, yDirection, zDirection);

                            currentPos1 = currentPos1.add(direction.multiply(distance + betweenSpace, distance + betweenSpace, distance + betweenSpace));

                            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, currentPos1.x, currentPos1.y, currentPos1.z, 1, 0.01, 0.01, 0.01, 0.01);
                        }
                        boolean createBranch = randomSource.nextFloat() > 0.5 && currentPos.size() < 4;
                        boolean stopBranch = randomSource.nextFloat() > 0.5;
                        if (createBranch) {
                            float angleXValue = (float) clamp(randomSource.nextFloat() * 10, -60, 60);
                            float angleYValue = (float) clamp(randomSource.nextFloat() * 10, -60, 60);
                            System.out.println("x: "+angleXValue+", y: "+angleYValue);
                            joints.put(i, new Vec2(currentJoint.x + angleXValue, currentJoint.y + angleYValue));
                            currentPos.add(currentPos1);
                        }
                        if (stopBranch) stopedBranches.add(currentPos1);
                    }
                }
            }
        } catch (Exception e) {System.out.println(e.toString());}

        lightningBolt.shouldRender = true;
        player.level().addFreshEntity(lightningBolt);
    }

    public static void StopBeam(ServerPlayer player, int cooldown) {
        if (cooldown <= 0) {
            for (int i = 0; i < playerList.size(); i++) {
                if (playerList.get(i) == player.getId()) {
                    beaming.set(i, false);
                    break;
                }
            }
            ModMessage.sendToPlayer(new SetClientCooldownS2CPacket("lightning_beam", ManaCost), player);
        }
    }
}
