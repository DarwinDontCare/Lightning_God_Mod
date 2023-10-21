package net.darwindontcare.lighting_god.lightning_powers;

import com.mojang.blaze3d.shaders.Effect;
import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.items.FreezingProjectileEntity;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.SetClientCooldownS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Freeze {
    private static final int FREEZE_RANGE = 6;
    private static final int FREEZE_DAMAGE = 10;
    private static final int FREEZE_TIME = 3;
    public void FreezingGust(ServerPlayer player, int cooldown) {
        Vec3 playerPos = player.position();
        if (cooldown <= 0) {
            Vec3 currentPos = playerPos.add(player.getForward());
            FreezingProjectileEntity freezingProjectile = new FreezingProjectileEntity(player.level, currentPos.x, currentPos.y, currentPos.z);
            freezingProjectile.setNoGravity(true);
            freezingProjectile.setInvisible(true);
            freezingProjectile.setOwner(player);
            freezingProjectile.setPos(freezingProjectile.position().x, freezingProjectile.position().y + player.getEyeHeight(), freezingProjectile.position().z);
            freezingProjectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.0f, 1.0f);
            player.level.addFreshEntity(freezingProjectile);
            for (int idx = 0; idx < 15; idx++) {
                player.level.addParticle(ParticleTypes.END_ROD, currentPos.x, (currentPos.y) + player.getRandom().nextDouble() * 2.0D, currentPos.z, player.getRandom().nextGaussian(), player.getRandom().nextGaussian(), player.getRandom().nextGaussian());
            }
            player.level.playSound(null, player.position().x, player.position().y + player.getEyeHeight(), player.position().z, SoundEvents.SNOW_HIT, SoundSource.NEUTRAL, 0.5F, 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
            ModMessage.sendToPlayer(new SetClientCooldownS2CPacket("freeze"), player);
        }
    }
}
