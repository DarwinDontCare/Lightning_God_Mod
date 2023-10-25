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

    @SubscribeEvent
    public static void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        if (BlockPunchEvent.isHoldingBlock()) {
            BlockPunchEvent.resetHoldingBlock();
        }
    }

    @SubscribeEvent
    public static void onBlockRightClick(PlayerInteractEvent.RightClickBlock event) {
        // Este evento é acionado quando o jogador clica com o botão direito em um bloco.
        // Coloque seu código aqui para lidar com o clique direito em um bloco.
        Player player = event.getEntity();
        GrabBlock(event.getPos(), player);
    }

    public static void GrabBlock(BlockPos position, Player player) {
        try {
            if (!holdingBlock && LightningGodMod.getCurrentPower().equals("earth")) {
                ArrayList<Vec3> positions = LightningGodMod.GetLaunchBlockArray();
                for (int i = 0; i < positions.size(); i++) {
                    Vec3 pos = positions.get(i);
                    if (position.getCenter().equals(pos)) {
                        Vec3 BlockPosition = new Vec3(position.getX(), position.getY(), position.getZ());
                        ModMessage.sendToServer(new SummonEntityC2SPacket(player, new Vec3(0, 0, 0), BlockPosition, true, true));
                        LightningGodMod.RemoveLaunchBlockToArray(pos);
                        holdingBlock = true;
                    }
                }
            }
        } catch (Exception e) {System.out.println(e.toString());}
    }

    public static void LaunchBlock(BlockPos position, Player player) {
        if (!holdingBlock && LightningGodMod.getCurrentPower().equals("earth")) {
            ArrayList<Vec3> positions = LightningGodMod.GetLaunchBlockArray();
            for (int i = 0; i < positions.size(); i++) {
                Vec3 pos = positions.get(i);
                if (position.getCenter().equals(pos)) {
                    Vec3 BlockPosition = new Vec3(position.getX(), position.getY(), position.getZ());
                    ModMessage.sendToServer(new SummonEntityC2SPacket(player, new Vec3(0, 0, 0), BlockPosition, true, false));
                    LightningGodMod.RemoveLaunchBlockToArray(pos);
                }
            }
        } else if (holdingBlock) {
            BlockPunchEvent.resetHoldingBlock();
        }
    }

    public static void resetHoldingBlock() {
        holdingBlock = false;
    }

    public static boolean isHoldingBlock() {
        return holdingBlock;
    }

    @SubscribeEvent
    public static void onBlockLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        LaunchBlock(event.getPos(), player);
    }
}
