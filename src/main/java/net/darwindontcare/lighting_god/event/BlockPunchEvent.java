package net.darwindontcare.lighting_god.event;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.SummonEntityC2SPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = LightningGodMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BlockPunchEvent {
    private static final float VELOCITY = 1f;
    private static boolean holdingBlock = false;

    public static void GrabBlock(BlockPos position, Player player) {
        try {
            System.out.println("is holding block: "+holdingBlock);
            if (!holdingBlock && LightningGodMod.getCurrentPower().equals("earth")) {
                ArrayList<Vec3> positions = LightningGodMod.GetLaunchBlockArray();
                for (int i = 0; i < positions.size(); i++) {
                    Vec3 pos = positions.get(i);
                    if (position.getCenter().equals(pos)) {
                        holdingBlock = true;
                        Vec3 BlockPosition = new Vec3(position.getX(), position.getY(), position.getZ());
                        ModMessage.sendToServer(new SummonEntityC2SPacket(player, new Vec3(0, 0, 0), BlockPosition, true, holdingBlock));
                        LightningGodMod.RemoveLaunchBlockToArray(pos);
                    }
                }
            }
        } catch (Exception e) {System.out.println(e.toString());}
    }

    public static void LaunchBlock(BlockPos position, Player player) {
        if (LightningGodMod.getCurrentPower().equals("earth")) {
            ArrayList<Vec3> positions = LightningGodMod.GetLaunchBlockArray();
            for (int i = 0; i < positions.size(); i++) {
                Vec3 pos = positions.get(i);
                if (position.getCenter().equals(pos)) {
                    resetHoldingBlock(player);
                    Vec3 BlockPosition = new Vec3(position.getX(), position.getY(), position.getZ());
                    ModMessage.sendToServer(new SummonEntityC2SPacket(player, new Vec3(0, 0, 0), BlockPosition, true, holdingBlock));
                    LightningGodMod.RemoveLaunchBlockToArray(pos);
                }
            }
        }
    }

    public static void resetHoldingBlock(Player player) {
        holdingBlock = false;
        ModMessage.sendToServer(new SummonEntityC2SPacket(player, new Vec3(0, 0, 0), new Vec3(0, 0, 0), true, false));
    }

    public static boolean isHoldingBlock() {
        return holdingBlock;
    }
}
