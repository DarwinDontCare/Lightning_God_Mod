package net.darwindontcare.lighting_god.lightning_powers;

import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.AddLaunchBlockS2CPakcet;
import net.darwindontcare.lighting_god.networking.packet.SetClientCooldownS2CPacket;
import net.darwindontcare.lighting_god.utils.AddForceToEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class EarthWall {
    private static int RANGE = 5;
    public static void useWall(ServerPlayer player, int cooldown) {
        if (cooldown <= 0) {
            new Thread(() -> {
                HitResult hitResult = player.pick(RANGE, 1f, false);

                double forwardX = -Math.sin(Math.toRadians(player.getYRot())) * 2;
                double forwardZ = Math.cos(Math.toRadians(player.getYRot())) * 2;
                double leftX = -Math.sin(Math.toRadians(player.getYRot() + 90));
                double leftZ = Math.cos(Math.toRadians(player.getYRot() + 90));
                Vec3 playerLeft = new Vec3(Math.round(leftX), 0, Math.round(leftZ));
                Vec3 playerForward = new Vec3(Math.round(forwardX), 0, Math.round(forwardZ));
                Vec3 playerPos = new Vec3(Math.round(player.position().x), Math.round(player.position().y), Math.round(player.position().z));

                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    playerForward = new Vec3(0, 0, 0);
                    playerPos = new Vec3(hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z);
                }

                List<LivingEntity> hitEntities = player.level.getEntitiesOfClass(
                        LivingEntity.class,
                        new AABB(playerPos.x + (playerLeft.x * -1) + playerForward.x + 1, playerPos.y, playerPos.z + (playerLeft.z * -1) + playerForward.z + 1, playerPos.x + playerLeft.x + playerForward.x - 1, playerPos.y + 2, playerPos.z + playerLeft.z + playerForward.z - 1)
                );

                for (LivingEntity entity : hitEntities) {
                    if (!entity.equals(player)) {
                        AddForceToEntity.AddForce(entity, new Vec3(0, 2, 0), false);
                    }
                }

                ServerLevel serverLevel = (ServerLevel) player.level;
                ArrayList<Vec3> wallBlocksPos = new ArrayList<>();
                for (int height = 0; height < 3; height++) {
                    try {
                        Vec3 pos1 = new Vec3(playerLeft.x, playerLeft.y, playerLeft.z);
                        Vec3 pos2 = new Vec3(0, playerLeft.y, 0);
                        Vec3 pos3 = new Vec3(playerLeft.x * -1, playerLeft.y, playerLeft.z * -1);
                        Vec3[] postitions = {pos1, pos2, pos3};

                        for (Vec3 psotion : postitions) {
                            Vec3 currentPos = new Vec3((playerPos.x + playerForward.x) + psotion.x, playerPos.y + height, playerPos.z + playerForward.z + psotion.z);
                            BlockPos blockPos = new BlockPos((int) currentPos.x,(int) currentPos.y,(int) currentPos.z);
                            BlockPos groundBlockPos = new BlockPos((int) currentPos.x,(int) currentPos.y - 1,(int) currentPos.z);
                            BlockState gorundBlock = serverLevel.getBlockState(groundBlockPos);

                            if (!serverLevel.getBlockState(blockPos).getMaterial().isSolid() && gorundBlock.getMaterial().isSolid()) {
                                //System.out.println("placing "+block.getName()+" block on "+currentPos);
                                serverLevel.setBlock(blockPos, Blocks.STONE.defaultBlockState(), 3);
                                serverLevel.gameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                                for (int t = 0; t < 25; t++) serverLevel.addParticle(ParticleTypes.HAPPY_VILLAGER, currentPos.x, currentPos.y + player.getRandom().nextDouble() * 2.0D, currentPos.z, player.getRandom().nextGaussian(), player.getRandom().nextGaussian(), player.getRandom().nextGaussian());
                                serverLevel.playSound(null, currentPos.x, currentPos.y, currentPos.z, SoundEvents.STONE_PLACE, SoundSource.NEUTRAL, 0.5F, 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
                                wallBlocksPos.add(blockPos.getCenter());
                            }
                        }
                        Thread.sleep(200);
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
                String positions = "";
                for (int i = 0; i < wallBlocksPos.size(); i++) {
                    Vec3 pos = wallBlocksPos.get(i);
                    if (i < wallBlocksPos.size() - 1) positions += pos.x+","+pos.y+","+pos.z+"!";
                    else positions += pos.x+","+pos.y+","+pos.z;
                }
                ModMessage.sendToPlayer(new AddLaunchBlockS2CPakcet(positions), player);
            }).start();

            ModMessage.sendToPlayer(new SetClientCooldownS2CPacket("earth_wall"), player);
        }
    }
}
