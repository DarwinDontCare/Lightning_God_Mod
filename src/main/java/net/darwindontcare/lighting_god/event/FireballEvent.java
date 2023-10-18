package net.darwindontcare.lighting_god.event;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.lightning_powers.Fireball;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.Event;

public class FireballEvent extends Event {
    private ServerPlayer player;

    public ServerPlayer getPlayer() {
        return player;
    }
    public FireballEvent(ServerPlayer player) {
        this.player = player;
    }
}
