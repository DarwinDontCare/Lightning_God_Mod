package net.darwindontcare.lighting_god.networking.packet;

import net.darwindontcare.lighting_god.utils.SummonParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SummonParticleC2SPacket {
    private Vec3 position;
    private Vec3 movement;
    private float speed;
    private String usage;

    public SummonParticleC2SPacket(Vec3 position, Vec3 movement, float speed, String usage) {
        this.position = position;
        this.movement = movement;
        this.speed = speed;
        this.usage = usage;
    }

    public SummonParticleC2SPacket(FriendlyByteBuf buf) {
        double posX = buf.readDouble();
        double posY = buf.readDouble();
        double posZ = buf.readDouble();
        double movX = buf.readDouble();
        double movY = buf.readDouble();
        double movZ = buf.readDouble();
        speed = buf.readFloat();
        usage = buf.readUtf();


        position = new Vec3(posX, posY, posZ);
        movement = new Vec3(movX, movY, movZ);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(position.x);
        buf.writeDouble(position.y);
        buf.writeDouble(position.z);
        buf.writeDouble(movement.x);
        buf.writeDouble(movement.y);
        buf.writeDouble(movement.z);
        buf.writeFloat(speed);
        buf.writeUtf(usage);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            SummonParticle.summon(player, usage, position, speed, movement);
        });

        return true;
    }
}
