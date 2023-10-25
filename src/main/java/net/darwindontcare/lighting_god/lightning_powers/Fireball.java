package net.darwindontcare.lighting_god.lightning_powers;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.entities.CustomFireball;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.SetClientCooldownS2CPacket;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class Fireball {
    private static final int ManaCost = 30;
    public static void ShootFireball(ServerPlayer player, int cooldown, float mana) {
        try {
            ServerLevel serverLevel = (ServerLevel) player.level();
            player.swing(InteractionHand.MAIN_HAND);
            if (cooldown <= 0 && mana >= ManaCost) {
                CustomFireball largeFireball = new CustomFireball(player.level(), player, player.getForward().x, player.getForward().y, player.getForward().z, 2);
                largeFireball.setPos(largeFireball.position().x, largeFireball.position().y + player.getEyeHeight(), largeFireball.position().z);
                largeFireball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 4.0f, 4.0f);
                player.level().addFreshEntity(largeFireball);
                for(int i = 0; i < 35; i++){serverLevel.sendParticles(ParticleTypes.FLAME, player.getX() + player.getRandomY() * 0.005, player.getY() + player.getEyeHeight() + player.getRandomY() * 0.005, player.getZ() + player.getRandomY() * 0.005, 1, player.getRandomY() * 0.005, player.getRandomY() * 0.005, player.getRandomY() * 0.005, player.getRandomY() * 0.005);}
                player.level().playSound(null, player.position().x, player.position().y + player.getEyeHeight(), player.position().z, SoundEvents.GHAST_SHOOT, SoundSource.NEUTRAL, 0.5F, 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
                ModMessage.sendToPlayer(new SetClientCooldownS2CPacket("fireball", ManaCost), player);
            }
        } catch (Exception exception) {
            System.out.println(exception.toString());
        }
    }
}
