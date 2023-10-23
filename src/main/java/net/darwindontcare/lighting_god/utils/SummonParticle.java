package net.darwindontcare.lighting_god.utils;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class SummonParticle {
    public static void summon(ServerPlayer player, String usage, Vec3 position, float speed, Vec3 movement) {
        ServerLevel serverLevel = (ServerLevel) player.level();
        if (Objects.equals(usage, "ice_spike")) {
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.GLASS.defaultBlockState()), position.x, position.y, position.z, 1, speed, movement.x, movement.y, movement.z);
        }
    }
}
