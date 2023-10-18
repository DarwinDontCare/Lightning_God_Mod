package net.darwindontcare.lighting_god.networking.packet;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetClientCooldownS2CPacket {
    private String powerName;
    public SetClientCooldownS2CPacket(String powerName) {
        this.powerName = powerName;
    }

    public SetClientCooldownS2CPacket(FriendlyByteBuf buf) {
        powerName = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(powerName);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            if (powerName.equals("freeze")) {
                LightningGodMod.setFreezeCooldown(LightningGodMod.getMaxProcessedFreezeCooldown());
            } else if (powerName.equals("ice_slide")) {
                LightningGodMod.setIceSlideCooldown(LightningGodMod.getMaxProcessedIceSlideCooldown());
            } else if (powerName.equals("fireball")) {
                LightningGodMod.setFireballCooldown(LightningGodMod.getMaxProcessedFireballCooldown());
            } else if (powerName.equals("fire_burst")) {
                LightningGodMod.setFireBurstCooldown(LightningGodMod.getMaxProcessedFireBurstCooldown());
            }else if (powerName.equals("fire_flight")) {
                LightningGodMod.setFireFlightCooldown(LightningGodMod.getMaxProcessedFireFlightCooldown());
            } else if (powerName.equals("teleport")) {
                LightningGodMod.setTeleportCooldown(LightningGodMod.getMaxProcessedTeleportCooldown());
            } else if (powerName.equals("el_thor")) {
                LightningGodMod.setElThorCooldown(LightningGodMod.getMaxProcessedElThorCooldown());
            } else if (powerName.equals("earth_launch")) {
                LightningGodMod.setEarthLaunchCooldown(LightningGodMod.getMaxProcessedEarthLaunchCooldown());
            } else if (powerName.equals("earth_wall")) {
                LightningGodMod.setEarthWallCooldown(LightningGodMod.getMaxProcessedEarthWallCooldown());
            }
        });

        return true;
    }
}
