package net.darwindontcare.lighting_god.lightning_powers;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.utils.FreezeHandler;
import net.darwindontcare.lighting_god.utils.RaycastUtil;
import net.darwindontcare.lighting_god.utils.TeleportPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

public class Teleportation {
    private static final float ManaCost = 20f;
    public static void TeleportationPower(ServerPlayer player, int cooldown, float mana) {
        if (cooldown <= 0 && mana >= ManaCost) {
            double distance = 100;
            HitResult result = player.pick(distance, 1.0f, false);
            RaycastUtil raycastUtil = new RaycastUtil();
            Entity entity = raycastUtil.getEntityInCrosshair(1.0f, distance);
            if (entity != null) {
                TeleportPlayer teleportPlayer = new TeleportPlayer(entity.position(), player, ManaCost);
                MinecraftForge.EVENT_BUS.post(teleportPlayer);
            } else if (result.getType() == HitResult.Type.BLOCK) {
                TeleportPlayer teleportPlayer = new TeleportPlayer(result.getLocation(), player, ManaCost);
                MinecraftForge.EVENT_BUS.post(teleportPlayer);
            } else {
                TeleportPlayer teleportPlayer = new TeleportPlayer(GetNewPositionFromFacingDirection(player, distance), player, ManaCost);
                MinecraftForge.EVENT_BUS.post(teleportPlayer);
            }
            FreezeHandler.removeFrozenEntity((LivingEntity) player);
        }
    }

    public static Vec3 GetNewPositionFromFacingDirection(Player player,double distance) {
        Vec3 forwardDirectionDistance = new Vec3(player.getForward().x * distance, (player.getForward().y * distance) + player.getEyeHeight(), player.getForward().z * distance);
        return player.getPosition(1F).add(forwardDirectionDistance);
    }
}
