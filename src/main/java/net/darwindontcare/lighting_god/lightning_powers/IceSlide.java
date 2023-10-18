package net.darwindontcare.lighting_god.lightning_powers;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.AddForceToEntityS2CPacket;
import net.darwindontcare.lighting_god.networking.packet.SetClientCooldownS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.core.jmx.Server;

public class IceSlide {
    private static final double MAX_SLIDE_SPEED = 1;
    private static double CURRENT_SPEED = 0;
    private static boolean isSliding = false;
    public static void StartSlide(ServerPlayer player, int cooldown) {
        if (cooldown <= 0) Slide(player);
    }

    public static void FinishSlide(ServerPlayer player, int cooldown) {
        if (cooldown <= 0) {
            ModMessage.sendToPlayer(new SetClientCooldownS2CPacket("ice_slide"), player);
            isSliding = false;
            CURRENT_SPEED = 0;
        }
    }

    private static void Slide(ServerPlayer player) {
        if (!isSliding) {
            CURRENT_SPEED = player.getSpeed();
            isSliding = true;
            ServerLevel serverLevel = (ServerLevel) player.level;
            new Thread(() ->{
                int particleCooldown = 0;
                while (isSliding) {
                    try {
                        if (CURRENT_SPEED < MAX_SLIDE_SPEED) {
                            CURRENT_SPEED += 0.001;
                        }
                        //System.out.println("player movement: " + player.getDeltaMovement());
                        if (player.level.getBlockState(new BlockPos((int) (player.position().x + player.getForward().x), (int) player.position().y, (int) (player.position().z + player.getForward().z))).getMaterial().isSolid()) {
                            if (!player.level.getBlockState(new BlockPos((int) (player.position().x + player.getForward().x), (int) player.position().y + 1, (int) (player.position().z + player.getForward().z))).getMaterial().isSolid() && !player.level.getBlockState(new BlockPos((int) (player.position().x + player.getForward().x), (int) player.position().y + 2, (int) (player.position().z + player.getForward().z))).getMaterial().isSolid()) {
                                System.out.println("jumped block");
                                player.teleportTo(player.position().x, player.position().y + 1, player.position().z);
                            }
                        }
                        for (int x = -1; x < 1; x++) {
                            for (int z = -1; z < 1; z++) {
                                BlockPos currentPos = new BlockPos((int) player.position().x + x, (int) player.position().y - 1, (int) player.position().z + z);
                                if (player.level.getBlockState(currentPos).getBlock().equals(Blocks.WATER)) {
                                    serverLevel.setBlock(currentPos, Blocks.FROSTED_ICE.defaultBlockState(), 3);
                                    serverLevel.gameEvent(null, GameEvent.BLOCK_PLACE, currentPos);
                                } else if (player.level.getBlockState(currentPos).getBlock().equals(Blocks.LAVA)) {
                                    serverLevel.setBlock(currentPos, Blocks.OBSIDIAN.defaultBlockState(), 3);
                                    serverLevel.gameEvent(null, GameEvent.BLOCK_PLACE, currentPos);
                                }
                            }
                        }

                        if (particleCooldown <= 0) {
                            serverLevel.sendParticles(ParticleTypes.END_ROD, player.getX() + player.getRandomY() * 0.0005, player.getY() + 0.5 + player.getRandomY() * 0.0005, player.getZ() + player.getRandomY() * 0.0005, 1, player.getRandomY() * 0.0005, player.getRandomY() * 0.0005, player.getRandomY() * 0.0005, player.getRandomY() * 0.0005);
                            player.level.playSound(null, player.position().x, player.position().y, player.position().z, SoundEvents.SNOW_BREAK, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
                            particleCooldown = 100;
                        }

                        double motionX = -Math.sin(Math.toRadians(player.getYRot())) * CURRENT_SPEED;
                        double motionZ = Math.cos(Math.toRadians(player.getYRot())) * CURRENT_SPEED;

                        if (player.isOnGround())
                            ModMessage.sendToPlayer(new AddForceToEntityS2CPacket(new Vec3(motionX, 0, motionZ), player, true), player);
                        else
                            ModMessage.sendToPlayer(new AddForceToEntityS2CPacket(new Vec3(motionX, 0, motionZ), player, true), player);
                        particleCooldown--;
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
            }).start();
        }
    }
}
