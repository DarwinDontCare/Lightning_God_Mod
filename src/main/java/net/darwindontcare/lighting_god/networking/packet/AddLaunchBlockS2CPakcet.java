package net.darwindontcare.lighting_god.networking.packet;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;

public class AddLaunchBlockS2CPakcet {
    private String positions;
    public AddLaunchBlockS2CPakcet(String positions) {
        this.positions = positions;
    }

    public AddLaunchBlockS2CPakcet(FriendlyByteBuf buf) {
        positions = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(positions);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {
            ArrayList<Vec3> positions = new ArrayList<>();
            String[] posStrings = this.positions.split("!");
            for (String pos: posStrings) {
                double x = Double.parseDouble(pos.split(",")[0]);
                double y = Double.parseDouble(pos.split(",")[1]);
                double z = Double.parseDouble(pos.split(",")[2]);
                positions.add(new Vec3(x, y, z));
            }
            LightningGodMod.AddLaunchBlockToArray(positions);
        });

        return true;
    }
}
