package net.darwindontcare.lighting_god.utils;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TeleportPlayer extends Event {

    private Vec3 position;
    private ServerPlayer player;

    public Vec3 getPos() {
        return position;
    }

    public ServerPlayer getPlayer() {
        return player;
    }
    public TeleportPlayer(Vec3 position, ServerPlayer player) {
        this.position = position;
        this.player = player;
    }
}
