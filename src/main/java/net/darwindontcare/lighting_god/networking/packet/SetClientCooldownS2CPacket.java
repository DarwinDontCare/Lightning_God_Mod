package net.darwindontcare.lighting_god.networking.packet;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetClientCooldownS2CPacket {
    private String powerName;
    private float cost;
    public SetClientCooldownS2CPacket(String powerName, float cost) {
        this.powerName = powerName;
        this.cost = cost;
    }

    public SetClientCooldownS2CPacket(FriendlyByteBuf buf) {
        powerName = buf.readUtf();
        cost = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(powerName);
        buf.writeFloat(cost);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            if (powerName.equals("freeze")) {
                LightningGodMod.setFreezeCooldown(LightningGodMod.getMaxProcessedFreezeCooldown());
            } else if (powerName.equals("ice_slide")) {
                LightningGodMod.setIsIceSliding(false);
                LightningGodMod.setIceSlideCooldown(LightningGodMod.getMaxProcessedIceSlideCooldown());
            } else if (powerName.equals("ice_spike")) {
                LightningGodMod.setIceSpikeCooldown(LightningGodMod.getMaxProcessedIceSpikeCooldown());
            } else if (powerName.equals("fireball")) {
                LightningGodMod.setFireballCooldown(LightningGodMod.getMaxProcessedFireballCooldown());
            } else if (powerName.equals("fire_burst")) {
                LightningGodMod.setFireBurstCooldown(LightningGodMod.getMaxProcessedFireBurstCooldown());
            } else if (powerName.equals("fire_flight")) {
                LightningGodMod.setAlternativeGliding(false);
                LightningGodMod.setFireFlightCooldown(LightningGodMod.getMaxProcessedFireFlightCooldown());
            } else if (powerName.equals("fire_pull")) {
                LightningGodMod.setFirePullCooldown(LightningGodMod.getMaxProcessedFirePullCooldown());
            } else if (powerName.equals("teleport")) {
                LightningGodMod.setTeleportCooldown(LightningGodMod.getMaxProcessedTeleportCooldown());
            } else if (powerName.equals("el_thor")) {
                LightningGodMod.setElThorCooldown(LightningGodMod.getMaxProcessedElThorCooldown());
            }  else if (powerName.equals("lightning_beam")) {
                LightningGodMod.setLightningBeamCooldown(LightningGodMod.getMaxProcessedLightningBeamCooldown());
                LightningGodMod.setCanRegenMana(true);
            } else if (powerName.equals("earth_launch")) {
                LightningGodMod.setEarthLaunchCooldown(LightningGodMod.getMaxProcessedEarthLaunchCooldown());
            } else if (powerName.equals("earth_wall")) {
                LightningGodMod.setEarthWallCooldown(LightningGodMod.getMaxProcessedEarthWallCooldown());
            } else if (powerName.equals("earth_meteor")) {
                LightningGodMod.setEarthMeteorCooldown(LightningGodMod.getMaxProcessedEarthMeteorCooldown());
            } else if (powerName.equals("earth_trap")) {
                LightningGodMod.setEarthTrapCooldown(LightningGodMod.getMaxProcessedEarthTrapCooldown());
            }

            LightningGodMod.removeCurrentMana(cost);
        });

        return true;
    }
}
