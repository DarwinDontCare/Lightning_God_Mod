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
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.FrostWalkerEnchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FrostedIceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.apache.logging.log4j.core.jmx.Server;

public class IceSlide {
    private static final double MAX_SLIDE_SPEED = 0.7;
    private static double CURRENT_SPEED = 0;
    private static boolean isSliding = false;
    private static final float ManaCost = 0.1f;
    public static void StartSlide(ServerPlayer player, int cooldown, float mana) {
        if (cooldown <= 0) Slide(player, mana, cooldown);
    }

    public static void FinishSlide(ServerPlayer player, int cooldown) {
        if (cooldown <= 0) {
            ModMessage.sendToPlayer(new SetClientCooldownS2CPacket("ice_slide", 0), player);
            isSliding = false;
            CURRENT_SPEED = 0;
        }
    }

    private static void Slide(ServerPlayer player, float mana, int cooldown) {
        if (!isSliding && mana >= ManaCost) {
            CURRENT_SPEED = player.getSpeed();
            isSliding = true;
            ServerLevel serverLevel = (ServerLevel) player.level();
            new Thread(() -> {
                while (isSliding && cooldown <= 0) {
                    try {
                        ModMessage.sendToPlayer(new SetClientCooldownS2CPacket("", ManaCost), player);
                        if (CURRENT_SPEED < MAX_SLIDE_SPEED) {
                            CURRENT_SPEED += 0.01;
                        }
                        Vec3 playerForward = player.position().add(new Vec3(player.getForward().x, 0, player.getForward().z));
                        BlockPos nextBlockPos = new BlockPos((int)playerForward.x, (int)playerForward.y, (int)playerForward.z);
                        BlockPos nextBlockPosUp = new BlockPos((int)playerForward.x, (int)playerForward.y + 1, (int)playerForward.z);
                        if (!player.isInWater()) {
                            if (serverLevel.getBlockState(nextBlockPos).isSolid() && !serverLevel.getBlockState(nextBlockPosUp).isSolid()) {
                                player.teleportTo(player.position().x, player.position().y + 1, player.position().z);
                            }
                            if (!player.isCrouching()) generateIce(player, serverLevel, new BlockPos((int) player.position().x, (int) player.position().y, (int) player.position().z), 2);

                            for (int i = 0; i < 10; i++) serverLevel.sendParticles(ParticleTypes.END_ROD, player.getX() + player.getRandomY() * 0.0005, player.getY() + 0.1 + player.getRandomY() * 0.0005, player.getZ() + player.getRandomY() * 0.0005, 1, player.getRandomY() * 0.0005, player.getRandomY() * 0.0005, player.getRandomY() * 0.0005, player.getRandomY() * 0.0005);
                            player.level().playSound(player, player.position().x, player.position().y, player.position().z, SoundEvents.SNOW_BREAK, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));

                            double motionX = -Math.sin(Math.toRadians(player.getYRot())) * CURRENT_SPEED;
                            double motionZ = Math.cos(Math.toRadians(player.getYRot())) * CURRENT_SPEED;

                            player.walkDist = 1;
                            player.walkDistO = 1;

                            ModMessage.sendToPlayer(new AddForceToEntityS2CPacket(new Vec3(motionX, 0, motionZ), player, true), player);
                        } else {
                            for (int i = 0; i < 10; i++)serverLevel.sendParticles(ParticleTypes.BUBBLE, player.getX() + player.getRandomY() * 0.0005, player.getY() + 0.6 + player.getRandomY() * 0.0005, player.getZ() + player.getRandomY() * 0.0005, 1, player.getRandomY() * 0.0005, player.getRandomY() * 0.0005, player.getRandomY() * 0.0005, player.getRandomY() * 0.0005);
                            player.level().playSound(player, player.position().x, player.position().y, player.position().z, SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundSource.NEUTRAL, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));

                            double motionX = player.getForward().x * CURRENT_SPEED;
                            double motionY = player.getForward().y * CURRENT_SPEED;
                            double motionZ = player.getForward().z * CURRENT_SPEED;

                            player.setSwimming(true);
                            player.setSprinting(true);

                            ModMessage.sendToPlayer(new AddForceToEntityS2CPacket(new Vec3(motionX, motionY, motionZ), player, false), player);
                        }
                        Thread.sleep(50);
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
                FinishSlide(player, cooldown);
            }).start();
        }
    }

    public static void generateIce(LivingEntity livingEntity, Level level, BlockPos blockPos, int modifier) {
        BlockState uppedBlock = level.getBlockState(blockPos);
        if (uppedBlock.isAir()) {
            BlockState blockstate = Blocks.FROSTED_ICE.defaultBlockState();
            int i = Math.min(16, 2 + modifier);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for(BlockPos blockpos : BlockPos.betweenClosed(blockPos.offset(-i, -1, -i), blockPos.offset(i, -1, i))) {
                if (blockpos.closerToCenterThan(livingEntity.position(), (double)i)) {
                    blockpos$mutableblockpos.set(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
                    BlockState blockstate1 = level.getBlockState(blockpos$mutableblockpos);
                    if (blockstate1.isAir()) {
                        BlockState blockstate2 = level.getBlockState(blockpos);
                        if (blockstate2 == FrostedIceBlock.meltsInto() && blockstate.canSurvive(level, blockpos) && level.isUnobstructed(blockstate, blockpos, CollisionContext.empty()) && !net.minecraftforge.event.ForgeEventFactory.onBlockPlace(livingEntity, net.minecraftforge.common.util.BlockSnapshot.create(level.dimension(), level, blockpos), net.minecraft.core.Direction.UP)) {
                            level.setBlockAndUpdate(blockpos, blockstate);
                            level.scheduleTick(blockpos, Blocks.FROSTED_ICE, Mth.nextInt(level.getRandom(), 60, 120));
                        }
                    }
                }
            }

        }
    }
}
