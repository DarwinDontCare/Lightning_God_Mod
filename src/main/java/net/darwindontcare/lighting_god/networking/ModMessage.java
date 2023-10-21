package net.darwindontcare.lighting_god.networking;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.networking.packet.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessage {
    public static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(LightningGodMod.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(LightningC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(LightningC2SPacket::new)
                .encoder(LightningC2SPacket::toBytes)
                .consumerMainThread(LightningC2SPacket::handle)
                .add();

        net.messageBuilder(FireballC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(FireballC2SPacket::new)
                .encoder(FireballC2SPacket::toBytes)
                .consumerMainThread(FireballC2SPacket::handle)
                .add();

        net.messageBuilder(FreezeC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(FreezeC2SPacket::new)
                .encoder(FreezeC2SPacket::toBytes)
                .consumerMainThread(FreezeC2SPacket::handle)
                .add();

        net.messageBuilder(ElThorC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ElThorC2SPacket::new)
                .encoder(ElThorC2SPacket::toBytes)
                .consumerMainThread(ElThorC2SPacket::handle)
                .add();

        net.messageBuilder(IceSlideC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(IceSlideC2SPacket::new)
                .encoder(IceSlideC2SPacket::toBytes)
                .consumerMainThread(IceSlideC2SPacket::handle)
                .add();

        net.messageBuilder(StopIceSlideC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(StopIceSlideC2SPacket::new)
                .encoder(StopIceSlideC2SPacket::toBytes)
                .consumerMainThread(StopIceSlideC2SPacket::handle)
                .add();

        net.messageBuilder(AddForceToEntityS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(AddForceToEntityS2CPacket::new)
                .encoder(AddForceToEntityS2CPacket::toBytes)
                .consumerMainThread(AddForceToEntityS2CPacket::handle)
                .add();

        net.messageBuilder(FireBurstC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(FireBurstC2SPacket::new)
                .encoder(FireBurstC2SPacket::toBytes)
                .consumerMainThread(FireBurstC2SPacket::handle)
                .add();

        net.messageBuilder(EarthLaunchC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(EarthLaunchC2SPacket::new)
                .encoder(EarthLaunchC2SPacket::toBytes)
                .consumerMainThread(EarthLaunchC2SPacket::handle)
                .add();

        net.messageBuilder(SetClientCooldownS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SetClientCooldownS2CPacket::new)
                .encoder(SetClientCooldownS2CPacket::toBytes)
                .consumerMainThread(SetClientCooldownS2CPacket::handle)
                .add();

        net.messageBuilder(EarthWallC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(EarthWallC2SPacket::new)
                .encoder(EarthWallC2SPacket::toBytes)
                .consumerMainThread(EarthWallC2SPacket::handle)
                .add();

        net.messageBuilder(AddLaunchBlockS2CPakcet.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(AddLaunchBlockS2CPakcet::new)
                .encoder(AddLaunchBlockS2CPakcet::toBytes)
                .consumerMainThread(AddLaunchBlockS2CPakcet::handle)
                .add();

        net.messageBuilder(SummonEntityC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SummonEntityC2SPacket::new)
                .encoder(SummonEntityC2SPacket::toBytes)
                .consumerMainThread(SummonEntityC2SPacket::handle)
                .add();

        net.messageBuilder(AddForceToEntityC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(AddForceToEntityC2SPacket::new)
                .encoder(AddForceToEntityC2SPacket::toBytes)
                .consumerMainThread(AddForceToEntityC2SPacket::handle)
                .add();

        net.messageBuilder(StartFireFlightC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(StartFireFlightC2SPacket::new)
                .encoder(StartFireFlightC2SPacket::toBytes)
                .consumerMainThread(StartFireFlightC2SPacket::handle)
                .add();

        net.messageBuilder(StopFireFlightC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(StopFireFlightC2SPacket::new)
                .encoder(StopFireFlightC2SPacket::toBytes)
                .consumerMainThread(StopFireFlightC2SPacket::handle)
                .add();

        net.messageBuilder(FireScythDashS2CPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(FireScythDashS2CPacket::new)
                .encoder(FireScythDashS2CPacket::toBytes)
                .consumerMainThread(FireScythDashS2CPacket::handle)
                .add();

        net.messageBuilder(StartEarthStompC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(StartEarthStompC2SPacket::new)
                .encoder(StartEarthStompC2SPacket::toBytes)
                .consumerMainThread(StartEarthStompC2SPacket::handle)
                .add();

        net.messageBuilder(StopEarthStompC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(StopEarthStompC2SPacket::new)
                .encoder(StopEarthStompC2SPacket::toBytes)
                .consumerMainThread(StopEarthStompC2SPacket::handle)
                .add();

        net.messageBuilder(SetEntityMovementS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SetEntityMovementS2CPacket::new)
                .encoder(SetEntityMovementS2CPacket::toBytes)
                .consumerMainThread(SetEntityMovementS2CPacket::handle)
                .add();

        net.messageBuilder(ExplodeS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ExplodeS2CPacket::new)
                .encoder(ExplodeS2CPacket::toBytes)
                .consumerMainThread(ExplodeS2CPacket::handle)
                .add();

        net.messageBuilder(EarthMeteorC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(EarthMeteorC2SPacket::new)
                .encoder(EarthMeteorC2SPacket::toBytes)
                .consumerMainThread(EarthMeteorC2SPacket::handle)
                .add();

        net.messageBuilder(SetMeteorDataS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SetMeteorDataS2CPacket::new)
                .encoder(SetMeteorDataS2CPacket::toBytes)
                .consumerMainThread(SetMeteorDataS2CPacket::handle)
                .add();

        net.messageBuilder(SetMeteorDataC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SetMeteorDataC2SPacket::new)
                .encoder(SetMeteorDataC2SPacket::toBytes)
                .consumerMainThread(SetMeteorDataC2SPacket::handle)
                .add();

        net.messageBuilder(EarthTrapC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(EarthTrapC2SPacket::new)
                .encoder(EarthTrapC2SPacket::toBytes)
                .consumerMainThread(EarthTrapC2SPacket::handle)
                .add();

        net.messageBuilder(FirePullC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(FirePullC2SPacket::new)
                .encoder(FirePullC2SPacket::toBytes)
                .consumerMainThread(FirePullC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
