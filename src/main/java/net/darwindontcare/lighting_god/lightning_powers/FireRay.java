package net.darwindontcare.lighting_god.lightning_powers;

import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.AddForceToEntityS2CPacket;
import net.darwindontcare.lighting_god.networking.packet.SetClientCooldownS2CPacket;
import net.darwindontcare.lighting_god.utils.AddForceToEntity;
import net.darwindontcare.lighting_god.utils.RaycastUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class FireRay {
    private static final int RANGE = 15;
    private static final float FORCE = 2f;
    private static final float DAMAGE = 15;
    private static final int FIRE_TIME = 10;
    private static final float ManaCost = 30f;


    public static void Ray(ServerPlayer player, int cooldown, float mana) {
        if (cooldown <= 0 && mana >= ManaCost) {
            new Thread(() -> {
                ServerLevel serverLevel = (ServerLevel) player.level();
                ArrayList<LivingEntity> affectedEntities = new ArrayList<>();
                serverLevel.playSound(player, player.position().x, player.position().y + player.getEyeHeight(), player.position().z, SoundEvents.WARDEN_SONIC_BOOM, SoundSource.NEUTRAL, 0.5F, 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));;

                for (int distance = 1; distance < RANGE; distance++) {
                    Vec3 currentPos = player.getEyePosition().add(player.getForward().multiply(distance, distance, distance));
                    for (int i = 0; i < 10; i++) serverLevel.sendParticles(ParticleTypes.FLAME, currentPos.x + player.getRandomY() * 0.001, currentPos.y + player.getRandomY() * 0.001, currentPos.z + player.getRandomY() * 0.001, 1, player.getRandomY() * 0.001, player.getRandomY() * 0.001, player.getRandomY() * 0.001, player.getRandomY() * 0.001);
                    List<LivingEntity> hitEntities = serverLevel.getEntitiesOfClass(
                            LivingEntity.class,
                            new AABB(currentPos.x + 1, currentPos.y + 1, currentPos.z + 1, currentPos.x - 1, currentPos.y -1, currentPos.z - 1)
                    );

                    for (LivingEntity entity : hitEntities) {
                        if (!entity.equals(player) && !((Entity) entity instanceof ItemEntity) && !affectedEntities.contains(entity)) {
                            entity.setSecondsOnFire(FIRE_TIME);
                            Vec3 direction = player.getForward().multiply(new Vec3(FORCE, 1, FORCE));
                            AddForceToEntity.AddForce(entity, direction, false);
                            entity.hurt(player.damageSources().playerAttack(player), DAMAGE);
                            affectedEntities.add(entity);
                        }
                    }
                }
            }).start();
            ModMessage.sendToPlayer(new SetClientCooldownS2CPacket("fire_pull", ManaCost), player);
        }
    }
}
