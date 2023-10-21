package net.darwindontcare.lighting_god;

import com.mojang.logging.LogUtils;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import dev.kosmx.playerAnim.core.data.AnimationFormat;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.data.gson.AnimationJson;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.darwindontcare.lighting_god.blocks.ModBlocks;
import net.darwindontcare.lighting_god.blocks.entity.ModBlockEntities;
import net.darwindontcare.lighting_god.client.SetPlayerData;
import net.darwindontcare.lighting_god.client.render.LightningArrowRender;
import net.darwindontcare.lighting_god.entities.EntityInit;
import net.darwindontcare.lighting_god.entities.client.MeteorProjectileRenderer;
import net.darwindontcare.lighting_god.event.TeleportEvent;
import net.darwindontcare.lighting_god.items.ModCreativeModeTab;
import net.darwindontcare.lighting_god.items.ModItems;
import net.darwindontcare.lighting_god.loot.ModLootModifiers;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.utils.ModItemProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

import java.util.ArrayList;
import java.util.Arrays;

@Mod(LightningGodMod.MOD_ID)
public class LightningGodMod
{
    public static final String MOD_ID = "lightning_god";
    private static final Logger LOGGER = LogUtils.getLogger();

    private static int TELEPORTATION_COOLDOWN = 0;
    private static final int MAX_TELEPORTATION_COOLDOWN = 100;
    private static int MAX_TELEPORTATION_COOLDOWN_PROCESSED = MAX_TELEPORTATION_COOLDOWN;
    private static int FIREBALL_COOLDOWN = 0;
    private static final int MAX_FIREBALL_COOLDOWN = 100;
    private static int MAX_FIREBALL_COOLDOWN_PROCESSED = MAX_FIREBALL_COOLDOWN;
    private static int FREEZE_COOLDOWN = 0;
    private static final int MAX_FREEZE_COOLDOWN = 100;
    private static int MAX_FREEZE_COOLDOWN_PROCESSED = MAX_FREEZE_COOLDOWN;
    private static int EL_THOR_COOLDOWN = 0;
    private static final int MAX_EL_THOR_COOLDOWN = 150;
    private static int MAX_EL_THOR_COOLDOWN_PROCESSED = MAX_EL_THOR_COOLDOWN;
    private static int ICE_SLIDE_COOLDOWN = 0;
    private static int MAX_ICE_SLIDE_COOLDOWN = 120;
    private static int MAX_ICE_SLIDE_COOLDOWN_PROCESSED = MAX_ICE_SLIDE_COOLDOWN;
    private static int FIRE_BURST_COOLDOWN = 0;
    private static int MAX_FIRE_BURST_COOLDOWN = 800;
    private static int MAX_FIRE_BURST_COOLDOWN_PROCESSED = MAX_FIRE_BURST_COOLDOWN;
    private static int EARTH_LAUNCH_COOLDOWN = 0;
    private static final int MAX_EARTH_LAUNCH_COOLDOWN = 120;
    private static int MAX_EARTH_LAUNCH_COOLDOWN_PROCESSED = MAX_EARTH_LAUNCH_COOLDOWN;
    private static int EARTH_WALL_COOLDOWN = 0;
    private static final int MAX_EARTH_WALL_COOLDOWN = 170;
    private static int MAX_EARTH_WALL_COOLDOWN_PROCESSED = MAX_EARTH_LAUNCH_COOLDOWN;
    private static int FIRE_FLIGHT_COOLDOWN = 0;
    private static final int MAX_FIRE_FLIGHT_COOLDOWN = 200;
    private static int MAX_FIRE_FLIGHT_COOLDOWN_PROCESSED = MAX_FIRE_FLIGHT_COOLDOWN;
    private static int EARTH_METEOR_COOLDOWN = 0;
    private static final int MAX_EARTH_METEOR_COOLDOWN = 800;
    private static int MAX_EARTH_METEOR_COOLDOWN_PROCESSED = MAX_EARTH_METEOR_COOLDOWN;
    private static int EARTH_TRAP_COOLDOWN = 0;
    private static final int MAX_EARTH_TRAP_COOLDOWN = 400;
    private static int MAX_EARTH_TRAP_COOLDOWN_PROCESSED = MAX_EARTH_TRAP_COOLDOWN;
    private static int FIRE_PULL_COOLDOWN = 0;
    private static final int MAX_FIRE_PULL_COOLDOWN = 200;
    private static int MAX_FIRE_PULL_COOLDOWN_PROCESSED = MAX_FIRE_PULL_COOLDOWN;
    private static boolean showPowerWheel = false;
    private static Player player;
    private static boolean alternativeGliding = false;
    private static final ArrayList<Vec3> launchBlockPositions = new ArrayList<>();

    public static void setShowPowerWheel(boolean value) {
        showPowerWheel = value;
    }
    public static boolean getAlternativeGliding() {
        return alternativeGliding;
    }
    public static void setAlternativeGliding(boolean value) {
        alternativeGliding = value;
    }

    public static void AddLaunchBlockToArray(ArrayList<Vec3> positions) {
        System.out.println("received positions: "+positions);
        launchBlockPositions.addAll(positions);
    }
    public static void RemoveLaunchBlockToArray(Vec3 position) {
        launchBlockPositions.remove(position);
    }

    public static ArrayList<Vec3> GetLaunchBlockArray() {
        return launchBlockPositions;
    }

    public static String getCurrentPower() {
        if (player != null) {
            CompoundTag playerData = player.getPersistentData();
            return playerData.getString("current_power");
        }
        return null;
    }
    public static void setCurrentPower(String value) {
        if (player != null) {
            SetPlayerData.setString(player, "current_power", value);
        }
    }
    
    public static void setPlayer(Player player1) {
        player = player1;
    }
    
    public static Player getPlayer() {return player;}

    public static boolean getShowPowerWheel() {
        return showPowerWheel;
    }
    public static int getTeleportCooldown() {
        return TELEPORTATION_COOLDOWN;
    }
    public static void setTeleportCooldown(int value) {
        TELEPORTATION_COOLDOWN = value;
    }
    public static int getMaxTeleportCooldown() {
        return MAX_TELEPORTATION_COOLDOWN;
    }
    public static int getMaxProcessedTeleportCooldown() {
        return MAX_TELEPORTATION_COOLDOWN_PROCESSED;
    }
    public static void setMaxProcessedTeleportCooldown(int value) {
        MAX_TELEPORTATION_COOLDOWN_PROCESSED = value;
    }
    public static int getFireballCooldown() {
        return FIREBALL_COOLDOWN;
    }
    public static void setFireballCooldown(int value) {
        FIREBALL_COOLDOWN = value;
    }
    public static int getMaxFireballCooldown() {
        return MAX_FIREBALL_COOLDOWN;
    }
    public static int getMaxProcessedFireballCooldown() {
        return MAX_FIREBALL_COOLDOWN_PROCESSED;
    }
    public static void setMaxProcessedFireballCooldown(int value) {
        MAX_FIREBALL_COOLDOWN_PROCESSED = value;
    }
    public static int getFreezeCooldown() {
        return FREEZE_COOLDOWN;
    }
    public static void setFreezeCooldown(int value) {
        FREEZE_COOLDOWN = value;
    }
    public static int getMaxFreezeCooldown() {
        return MAX_FREEZE_COOLDOWN;
    }
    public static int getMaxProcessedFreezeCooldown() {
        return MAX_FREEZE_COOLDOWN_PROCESSED;
    }
    public static void setMaxProcessedFreezeCooldown(int value) {
        MAX_FREEZE_COOLDOWN_PROCESSED = value;
    }
    public static int getElThorCooldown() {
        return EL_THOR_COOLDOWN;
    }
    public static void setElThorCooldown(int value) {
        EL_THOR_COOLDOWN = value;
    }
    public static int getMaxElThorCooldown() {
        return MAX_EL_THOR_COOLDOWN;
    }
    public static int getMaxProcessedElThorCooldown() {
        return MAX_EL_THOR_COOLDOWN_PROCESSED;
    }
    public static void setMaxProcessedElThorCooldown(int value) {
        MAX_EL_THOR_COOLDOWN_PROCESSED = value;
    }
    public static int getIceSlideCooldown() {
        return ICE_SLIDE_COOLDOWN;
    }
    public static void setIceSlideCooldown(int value) {
        ICE_SLIDE_COOLDOWN = value;
    }
    public static int getMaxIceSlideCooldown() {
        return MAX_ICE_SLIDE_COOLDOWN;
    }
    public static int getMaxProcessedIceSlideCooldown() {
        return MAX_ICE_SLIDE_COOLDOWN_PROCESSED;
    }
    public static void setMaxProcessedIceSlideCooldown(int value) {
        MAX_ICE_SLIDE_COOLDOWN_PROCESSED = value;
    }
    public static int getFireBurstCooldown() {
        return FIRE_BURST_COOLDOWN;
    }
    public static void setFireBurstCooldown(int value) {
        FIRE_BURST_COOLDOWN = value;
    }
    public static int getMaxFireBurstCooldown() {
        return MAX_FIRE_BURST_COOLDOWN;
    }
    public static int getMaxProcessedFireBurstCooldown() {
        return MAX_FIRE_BURST_COOLDOWN_PROCESSED;
    }
    public static void setMaxProcessedFireBurstCooldown(int value) {
        MAX_FIRE_BURST_COOLDOWN_PROCESSED = value;
    }
    public static int getEarthLaunchCooldown() {
        return EARTH_LAUNCH_COOLDOWN;
    }
    public static void setEarthLaunchCooldown(int value) {
        EARTH_LAUNCH_COOLDOWN = value;
    }
    public static int getMaxEarthLaunchCooldown() {
        return MAX_EARTH_LAUNCH_COOLDOWN;
    }
    public static int getMaxProcessedEarthLaunchCooldown() {
        return MAX_EARTH_LAUNCH_COOLDOWN_PROCESSED;
    }
    public static void setMaxProcessedEarthLaunchCooldown(int value) {
        MAX_EARTH_LAUNCH_COOLDOWN_PROCESSED = value;
    }
    public static int getEarthWallCooldown() {
        return EARTH_WALL_COOLDOWN;
    }
    public static void setEarthWallCooldown(int value) {
        EARTH_WALL_COOLDOWN = value;
    }
    public static int getMaxEarthWallCooldown() {
        return MAX_EARTH_WALL_COOLDOWN;
    }
    public static int getMaxProcessedEarthWallCooldown() {
        return MAX_EARTH_WALL_COOLDOWN_PROCESSED;
    }
    public static void setMaxProcessedEarthWallCooldown(int value) {
        MAX_EARTH_WALL_COOLDOWN_PROCESSED = value;
    }
    public static int getFireFlightCooldown() {
        return FIRE_FLIGHT_COOLDOWN;
    }
    public static void setFireFlightCooldown(int value) {
        FIRE_FLIGHT_COOLDOWN = value;
    }
    public static int getMaxFireFlightCooldown() {
        return MAX_FIRE_FLIGHT_COOLDOWN;
    }
    public static int getMaxProcessedFireFlightCooldown() {
        return MAX_FIRE_FLIGHT_COOLDOWN_PROCESSED;
    }
    public static void setMaxProcessedFireFlightCooldown(int value) {
        MAX_FIRE_FLIGHT_COOLDOWN_PROCESSED = value;
    }
    public static int getEarthMeteorCooldown() {
        return EARTH_METEOR_COOLDOWN;
    }
    public static void setEarthMeteorCooldown(int value) {
        EARTH_METEOR_COOLDOWN = value;
    }
    public static int getMaxEarthMeteorCooldown() {
        return MAX_EARTH_METEOR_COOLDOWN;
    }
    public static int getMaxProcessedEarthMeteorCooldown() {
        return MAX_EARTH_METEOR_COOLDOWN_PROCESSED;
    }
    public static void setMaxProcessedEarthMeteorCooldown(int value) {
        MAX_EARTH_METEOR_COOLDOWN_PROCESSED = value;
    }
    public static int getEarthTrapCooldown() {
        return EARTH_TRAP_COOLDOWN;
    }
    public static void setEarthTrapCooldown(int value) {
        EARTH_TRAP_COOLDOWN = value;
    }
    public static int getMaxEarthTrapCooldown() {
        return MAX_EARTH_TRAP_COOLDOWN;
    }
    public static int getMaxProcessedEarthTrapCooldown() {
        return MAX_EARTH_TRAP_COOLDOWN_PROCESSED;
    }
    public static void setMaxProcessedEarthTrapCooldown(int value) {
        MAX_EARTH_TRAP_COOLDOWN_PROCESSED = value;
    }
    public static int getFirePullCooldown() {
        return FIRE_PULL_COOLDOWN;
    }
    public static void setFirePullCooldown(int value) {
        FIRE_PULL_COOLDOWN = value;
    }
    public static int getMaxFirePullCooldown() {
        return MAX_FIRE_PULL_COOLDOWN;
    }
    public static int getMaxProcessedFirePullCooldown() {
        return MAX_FIRE_PULL_COOLDOWN_PROCESSED;
    }
    public static void setMaxProcessedFirePullCooldown(int value) {
        MAX_FIRE_PULL_COOLDOWN_PROCESSED = value;
    }

    public static int getPowerTier(String power) {
        if (player != null) {
            CompoundTag compoundTag = player.getPersistentData();
            if (compoundTag.getInt(power+"_tier") < 1) return 1;
            return compoundTag.getInt(power+"_tier");
        }
        return 0;
    }

    public static ArrayList<String> getPowers () {
        if (player != null) {
            CompoundTag playerData = player.getPersistentData();
            return new ArrayList<String> (Arrays.asList(playerData.getString("currentPowers").split(",")));
        }
        return null;
    }


    public LightningGodMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModLootModifiers.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        GeckoLib.initialize();

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new TeleportEvent());

        EntityInit.ENTITY_TYPES.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        ModMessage.register();
    }


    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            ModItemProperties.addCustomItemProperties();
            EntityRenderers.register(EntityInit.EARTH_METEOR.get(), MeteorProjectileRenderer::new);
            EntityRenderers.register(EntityInit.LIGHTNING_ARROW.get(), LightningArrowRender::new);
        }
    }

    private void addCreative(CreativeModeTabEvent.BuildContents event) {
        if (event.getTab() == ModCreativeModeTab.LIGHTNING_GOD_TAB) {
            event.accept(ModItems.FIRE_BOOTS);
            event.accept(ModItems.FIRE_LEGGINGS);
            event.accept(ModItems.FIRE_CHESTPLATE);
            event.accept(ModItems.FIRE_HELMET);

            event.accept(ModItems.LIGHTNING_BOOTS);
            event.accept(ModItems.LIGHTNING_LEGGINGS);
            event.accept(ModItems.LIGHTNING_CHESTPLATE);
            event.accept(ModItems.LIGHTNING_HELMET);

            event.accept(ModItems.WATER_BOOTS);
            event.accept(ModItems.WATER_LEGGINGS);
            event.accept(ModItems.WATER_CHESTPLATE);
            event.accept(ModItems.WATER_HELMET);

            event.accept(ModItems.FIRE_SCYTH);
            event.accept(ModItems.LIGHTNING_BOW);
            event.accept(ModItems.LIGHTNING_ARROW);

            event.accept(ModItems.FIRE_SCROLL);
            event.accept(ModItems.LIGHTNING_SCROLL);
            event.accept(ModItems.WATER_SCROLL);
            event.accept(ModItems.EARTH_SCROLL);

            event.accept(ModItems.FIRE_SCROLL_TIER_2);
            event.accept(ModItems.LIGHTNING_SCROLL_TIER_2);
            event.accept(ModItems.WATER_SCROLL_TIER_2);
            event.accept(ModItems.EARTH_SCROLL_TIER_2);

            event.accept(ModItems.WATER_SCROLL_TIER_3);
            event.accept(ModItems.EARTH_SCROLL_TIER_3);
            event.accept(ModItems.FIRE_SCROLL_TIER_3);
            event.accept(ModItems.LIGHTNING_SCROLL_TIER_3);
            
            event.accept(ModItems.WATER_SCROLL_TIER_4);
            event.accept(ModItems.EARTH_SCROLL_TIER_4);
            event.accept(ModItems.FIRE_SCROLL_TIER_4);
            event.accept(ModItems.LIGHTNING_SCROLL_TIER_4);

            event.accept(ModItems.LIGHTNING_ESSENCE);
            event.accept(ModItems.FIRE_ESSENCE);
            event.accept(ModItems.WATER_ESSENCE);
            event.accept(ModItems.EARTH_ESSENCE);
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (getTeleportCooldown() > 0) {
                setTeleportCooldown(getTeleportCooldown() - 1);
            } if (getFireballCooldown() > 0) {
                setFireballCooldown(getFireballCooldown() - 1);
            } if (getFreezeCooldown() > 0) {
                setFreezeCooldown(getFreezeCooldown() - 1);
            } if (getElThorCooldown() > 0) {
                setElThorCooldown(getElThorCooldown() - 1);
            } if (getIceSlideCooldown() > 0) {
                setIceSlideCooldown(getIceSlideCooldown() - 1);
            } if (getFireBurstCooldown() > 0) {
                setFireBurstCooldown(getFireBurstCooldown() - 1);
            } if (getEarthLaunchCooldown() > 0) {
                setEarthLaunchCooldown(getEarthLaunchCooldown() - 1);
            } if (getEarthWallCooldown() > 0) {
                setEarthWallCooldown(getEarthWallCooldown() - 1);
            } if (getEarthMeteorCooldown() > 0) {
                setEarthMeteorCooldown(getEarthMeteorCooldown() - 1);
            } if (getEarthTrapCooldown() > 0) {
                setEarthTrapCooldown(getEarthTrapCooldown() - 1);
            } if (getFireFlightCooldown() > 0) {
                setFireFlightCooldown(getFireFlightCooldown() - 1);
            } if (getFirePullCooldown() > 0) {
                setFirePullCooldown(getFirePullCooldown() - 1);
            }
        }
    }
}
