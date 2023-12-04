package net.darwindontcare.lighting_god.lightning_powers;

import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.AddForceToEntityS2CPacket;
import net.darwindontcare.lighting_god.networking.packet.SetClientCooldownS2CPacket;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public class EarthLaunch {
    public static final int ManaCost = 20;

    public static void Launch(ServerPlayer player, int cooldown, float mana, float POWER) {
        System.out.println("final force: "+POWER);
        if (cooldown <= 0 && mana >= ManaCost) {
            double motionX = player.getForward().x * POWER;
            double motionY = player.getForward().y * POWER;
            double motionZ = player.getForward().z * POWER;

            ServerLevel serverLevel = (ServerLevel) player.level();
            Vec3 currentPos = player.position();

            for(int i = 0; i < 35; i++){serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, player.getFeetBlockState()), currentPos.x + player.getRandomY() * 0.005, currentPos.y + player.getRandomY() * 0.005, currentPos.z + player.getRandomY() * 0.005, 1, player.getRandomY() * 0.005, player.getRandomY() * 0.005, player.getRandomY() * 0.005, player.getRandomY() * 0.005);}
            ModMessage.sendToPlayer(new AddForceToEntityS2CPacket(new Vec3(motionX, motionY, motionZ), player, false), player);
            player.level().explode(player, player.getX(), player.getY(0.0625D), player.getZ(), 3.0F, Level.ExplosionInteraction.NONE);
            ModMessage.sendToPlayer(new SetClientCooldownS2CPacket("earth_launch", ManaCost), player);
        }
    }
}
