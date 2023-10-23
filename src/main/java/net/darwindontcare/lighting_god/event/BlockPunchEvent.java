package net.darwindontcare.lighting_god.event;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.networking.packet.SummonEntityC2SPacket;
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
    public static void onPlayerRightClick(PlayerInteractEvent.LeftClickEmpty event) {
        if (BlockPunchEvent.isHoldingBlock()) {
            BlockPunchEvent.resetHoldingBlock();
        }
    }

    @SubscribeEvent
    public static void onBlockRightClick(PlayerInteractEvent.RightClickBlock event) {
        // Este evento é acionado quando o jogador clica com o botão direito em um bloco.
        // Coloque seu código aqui para lidar com o clique direito em um bloco.
        Player player = event.getEntity();
        try {
            if (!holdingBlock && LightningGodMod.getCurrentPower().equals("earth")) {
                ArrayList<Vec3> positions = LightningGodMod.GetLaunchBlockArray();
                for (int i = 0; i < positions.size(); i++) {
                    Vec3 pos = positions.get(i);
                    if (event.getPos().getCenter().equals(pos)) {
                        Vec3 position = new Vec3(event.getPos().getX(), event.getPos().getY(), event.getPos().getZ());
                        ModMessage.sendToServer(new SummonEntityC2SPacket(player, new Vec3(0, 0, 0), position, true, true));
                        LightningGodMod.RemoveLaunchBlockToArray(pos);
                        holdingBlock = true;
                    }
                }
            }
        } catch (Exception e) {System.out.println(e.toString());}
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
        try {
            if (!holdingBlock && LightningGodMod.getCurrentPower().equals("earth")) {
                ArrayList<Vec3> positions = LightningGodMod.GetLaunchBlockArray();
                for (int i = 0; i < positions.size(); i++) {
                    Vec3 pos = positions.get(i);
                    if (event.getPos().getCenter().equals(pos)) {
                        Vec3 position = new Vec3(event.getPos().getX(), event.getPos().getY(), event.getPos().getZ());
                        ModMessage.sendToServer(new SummonEntityC2SPacket(player, new Vec3(0, 0, 0), position, true, false));
                        LightningGodMod.RemoveLaunchBlockToArray(pos);
                    }
                }
            } else if (holdingBlock) {
                BlockPunchEvent.resetHoldingBlock();
            }
        } catch (Exception e) {System.out.println(e.toString());}
    }
}
