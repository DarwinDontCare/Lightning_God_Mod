package net.darwindontcare.lighting_god.utils;

import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.AddForceToEntityS2CPacket;
import net.darwindontcare.lighting_god.networking.packet.SetEntityMovementS2CPacket;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.commands.SummonCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.List;

public class SummonEntity {
    private static final int FORCE_MULTIPLIER = 2;
    public static void Summon(ServerPlayer player, Entity entity, Vec3 position, Vec3 motion, boolean isEarthAttack, boolean holdEntity) {
        ServerLevel serverLevel = (ServerLevel) player.level;
        if (isEarthAttack) {
            LaunchBlock(player, (ServerLevel) player.level, new BlockPos((int)position.x, (int)position.y, (int)position.z), holdEntity);
        } else {
            entity.setPos(position);
            entity.setDeltaMovement(motion);
            serverLevel.addFreshEntity(entity);
        }
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private static void HoldBlock(FallingBlockEntity fallingBlockEntity, ServerPlayer owner) {
        new Thread(() -> {
            try {

                while (!owner.swinging) {
                    Vec3 ownerEyePos = new Vec3(owner.position().x, owner.position().y + owner.getEyeHeight() - 0.1, owner.position().z);

                    double destinationX = clamp((ownerEyePos.x + (owner.getForward().x * 1.5)) - fallingBlockEntity.position().x, -0.5, 0.5);
                    double destinationY = clamp((ownerEyePos.y + (owner.getForward().y * 1.5)) - fallingBlockEntity.position().y, -0.5, 0.5);
                    double destinationZ = clamp((ownerEyePos.z + (owner.getForward().z * 1.5)) - fallingBlockEntity.position().z, -0.5, 0.5);

                    Vec3 destination = new Vec3(destinationX, destinationY, destinationZ);

                    fallingBlockEntity.lerpMotion(destinationX, destinationY, destinationZ);
                    fallingBlockEntity.setYRot(-owner.getYRot());
                    ModMessage.sendToPlayer(new SetEntityMovementS2CPacket(fallingBlockEntity, destination, -owner.getYRot(), fallingBlockEntity.getXRot()), owner);
                    fallingBlockEntity.noPhysics = true;

                    Thread.sleep(50);
                }
                double forwardX = owner.getForward().x * FORCE_MULTIPLIER;
                double forwardY = owner.getForward().y * FORCE_MULTIPLIER;
                double forwardZ = owner.getForward().z * FORCE_MULTIPLIER;

                Vec3 direction = new Vec3(forwardX, forwardY, forwardZ);
                FallingBlockTick(fallingBlockEntity, owner, direction, fallingBlockEntity.position());
            } catch (Exception e) {System.out.println(e.toString());}
        }).start();
    }

    private static void LaunchBlock(ServerPlayer player, ServerLevel level, BlockPos pos, boolean holdEntity) {
        try {
            double forwardX = player.getForward().x * FORCE_MULTIPLIER;
            double forwardY = player.getForward().y * FORCE_MULTIPLIER;
            double forwardZ = player.getForward().z * FORCE_MULTIPLIER;

            Vec3 playerForward = new Vec3(forwardX, forwardY, forwardZ);

            level.removeBlock(pos, true);
            FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(level, pos, Blocks.STONE.defaultBlockState());
            fallingBlockEntity.setNoGravity(true);
            fallingBlockEntity.disableDrop();
            if (!holdEntity) FallingBlockTick(fallingBlockEntity, player, playerForward, pos.getCenter());
            else HoldBlock(fallingBlockEntity, player);
        } catch (Exception e) {System.out.println(e.toString());}
    }

    private static void FallingBlockTick(FallingBlockEntity fallingBlockEntity, Entity owner, Vec3 direction, Vec3 origin) {
        new Thread(() -> {
            try {
                Thread.sleep(100);
                fallingBlockEntity.noPhysics = false;
                Vec3 originForward = origin.add(new Vec3(owner.getForward().x, 0, owner.getForward().z));
                Vec3 processedDirection = direction;
                if (!owner.level.getBlockState(new BlockPos((int) originForward.x, (int) originForward.y, (int) originForward.z)).getMaterial().isSolid()) {
                    fallingBlockEntity.setPos(originForward);
                    processedDirection = direction.subtract(new Vec3(owner.getForward().x, 0, owner.getForward().z));
                }
                fallingBlockEntity.setNoGravity(false);
                fallingBlockEntity.addDeltaMovement(processedDirection);
                Vec3 lastDirection = fallingBlockEntity.getDeltaMovement();
                while (true) {
                    Level level = fallingBlockEntity.level;
                    Vec3 currentPos = fallingBlockEntity.position();
                    boolean foundBlock = false;
                    if (fallingBlockEntity.isOnGround() && fallingBlockEntity.position().distanceTo(origin) > 1.5 || fallingBlockEntity.getDeltaMovement().distanceTo(lastDirection) > 0.3 && fallingBlockEntity.position().distanceTo(origin) > 1.5) {
                        Vec3i pos = new Vec3i((int) currentPos.x, (int) currentPos.y, (int) currentPos.z);
                        BlockPos blockPos = new BlockPos(pos);
                        level.explode(owner, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 2, Level.ExplosionInteraction.NONE);
                        fallingBlockEntity.kill();
                        break;
                    }
                    List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(
                            LivingEntity.class,
                            new AABB(currentPos.x, currentPos.y - 3, currentPos.z - 3, currentPos.x, currentPos.y + 3, currentPos.z + 3)
                    );
                    for (Entity entity : nearbyEntities) {
                        if (entity != owner && !(entity instanceof ItemEntity)) {
                            level.explode(owner, currentPos.x, currentPos.y, currentPos.z, 2, Level.ExplosionInteraction.NONE);
                            foundBlock = true;
                            fallingBlockEntity.kill();
                            break;
                        }
                    }
                    if (foundBlock) break;
                    lastDirection = fallingBlockEntity.getDeltaMovement();
                    Thread.sleep(50);
                }
            } catch (Exception e) {System.out.println(e.toString());}
        }).start();
    }
}