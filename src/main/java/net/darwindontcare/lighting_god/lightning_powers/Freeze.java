package net.darwindontcare.lighting_god.lightning_powers;

import net.darwindontcare.lighting_god.items.FreezingProjectileEntity;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.SetClientCooldownS2CPacket;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import net.minecraft.world.phys.Vec3;


public class Freeze {
    private static final int FREEZE_RANGE = 6;
    private static final int FREEZE_DAMAGE = 10;
    private static final int FREEZE_TIME = 3;
    private static final float ManaCost = 30f;
    public void FreezingGust(ServerPlayer player, int cooldown, float mana) {
        Vec3 playerPos = player.position();
        if (cooldown <= 0 && mana >= ManaCost) {
            Vec3 currentPos = playerPos.add(player.getForward());
            FreezingProjectileEntity freezingProjectile = new FreezingProjectileEntity(player.level(), currentPos.x, currentPos.y, currentPos.z);
            freezingProjectile.setNoGravity(true);
            freezingProjectile.setInvisible(true);
            freezingProjectile.setOwner(player);
            freezingProjectile.setPos(freezingProjectile.position().x, freezingProjectile.position().y + player.getEyeHeight(), freezingProjectile.position().z);
            freezingProjectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.0f, 1.0f);
            player.level().addFreshEntity(freezingProjectile);
            for (int idx = 0; idx < 15; idx++) {
                player.level().addParticle(ParticleTypes.END_ROD, currentPos.x, (currentPos.y) + player.getRandom().nextDouble() * 2.0D, currentPos.z, player.getRandom().nextGaussian(), player.getRandom().nextGaussian(), player.getRandom().nextGaussian());
            }
            player.level().playSound(null, player.position().x, player.position().y + player.getEyeHeight(), player.position().z, SoundEvents.SNOW_HIT, SoundSource.NEUTRAL, 0.5F, 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
            ModMessage.sendToPlayer(new SetClientCooldownS2CPacket("freeze", ManaCost), player);
        }
    }
}
