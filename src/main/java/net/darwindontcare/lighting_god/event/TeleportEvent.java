package net.darwindontcare.lighting_god.event;

import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.SetClientCooldownS2CPacket;
import net.darwindontcare.lighting_god.utils.TeleportPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TeleportEvent {

    Vec3 position;
    ServerPlayer player;

    @SubscribeEvent
    public void TeleportPlayer(TeleportPlayer event) {

        try {
            player = event.getPlayer();
            position = event.getPos();
            position = new Vec3(Math.round(position.x), Math.round(position.y), Math.round(position.z));
            double distance = player.position().distanceTo(position);
            ServerLevel serverLevel = (ServerLevel) player.level();

            for (int i = (int) distance; i > 0; i--) {
                BlockState block_head = player.level().getBlockState(new BlockPos((int) ((player.position().x + player.getForward().x) * i), (int) (((player.position().y + player.getForward().y) * i) + 2), (int) ((player.position().z + player.getForward().z) * i)));
                BlockState block_feet = player.level().getBlockState(new BlockPos((int) ((player.position().x + player.getForward().x) * i), (int) (((player.position().y + player.getForward().y) * i) + 1), (int) ((player.position().z + player.getForward().z) * i)));
                BlockState block_ground = player.level().getBlockState(new BlockPos((int) ((player.position().x + player.getForward().x) * i), (int) ((player.position().y + player.getForward().y) * i), (int) ((player.position().z + player.getForward().z) * i)));

                if (!block_head.isSolid() && !block_feet.isSolid()) {
                    System.out.println(player.position());
                    player.level().playSound(player, player.position().x, player.position().y, player.position().z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 0.5F, 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
                    for(int idx = 0; idx < 20; idx++) {
                        serverLevel.sendParticles(ParticleTypes.PORTAL, player.getX() + player.getRandomY() * 0.005, player.getY() + player.getRandomY() * 0.005, player.getZ() + player.getRandomY() * 0.005, 1, player.getRandomY() * 0.005, player.getRandomY() * 0.005, player.getRandomY() * 0.005, player.getRandomY() * 0.005);
                        serverLevel.sendParticles(ParticleTypes.PORTAL, player.getX() + player.getRandomY() * 0.005, player.getY() + player.getEyeHeight() + player.getRandomY() * 0.005, player.getZ() + player.getRandomY() * 0.005, 1, player.getRandomY() * 0.005, player.getRandomY() * 0.005, player.getRandomY() * 0.005, player.getRandomY() * 0.005);
                    }
                    MobEffectInstance invisibility = new MobEffectInstance(MobEffects.INVISIBILITY, 50, 1, false, false);
                    player.addEffect(invisibility);
                    player.teleportTo(position.x, position.y + 1, position.z);
                    player.resetFallDistance();
                    ModMessage.sendToPlayer(new SetClientCooldownS2CPacket("teleport", event.getManaCost()), player);

                    break;
                } else if (!block_feet.isSolid() && !block_ground.isSolid()) {
                    System.out.println(player.position());
                    for(int idx = 0; idx < 15; idx++){player.level().addParticle(ParticleTypes.PORTAL, player.getX(), player.getY() + player.getRandom().nextDouble() * 2.0D, player.getZ(), event.getPlayer().getRandom().nextGaussian(), event.getPlayer().getRandom().nextGaussian(), event.getPlayer().getRandom().nextGaussian());}
                    player.level().playSound(player, player.position().x, player.position().y, player.position().z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 0.5F, 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
                    MobEffectInstance invisibility = new MobEffectInstance(MobEffects.INVISIBILITY, 50, 1, false, false);
                    player.addEffect(invisibility);
                    player.teleportTo(position.x, position.y, position.z);
                    player.resetFallDistance();
                    ModMessage.sendToPlayer(new SetClientCooldownS2CPacket("teleport", event.getManaCost()), player);

                    break;
                }

            }
        } catch (Exception exception) {
            System.out.println(exception.toString());
        }
    }
}
