package net.darwindontcare.lighting_god;

import com.mojang.logging.LogUtils;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.darwindontcare.lighting_god.blocks.ModBlocks;
import net.darwindontcare.lighting_god.blocks.entity.ModBlockEntities;
import net.darwindontcare.lighting_god.client.SetPlayerData;
import net.darwindontcare.lighting_god.client.render.LightningArrowRender;
import net.darwindontcare.lighting_god.entities.EntityInit;
import net.darwindontcare.lighting_god.entities.client.IceSpikesRenderer;
import net.darwindontcare.lighting_god.entities.client.MeteorProjectileRenderer;
import net.darwindontcare.lighting_god.event.TeleportEvent;
import net.darwindontcare.lighting_god.items.ModCreativeModeTab;
import net.darwindontcare.lighting_god.items.ModItems;
import net.darwindontcare.lighting_god.loot.ModLootModifiers;
import net.darwindontcare.lighting_god.networking.ModMessage;
import net.darwindontcare.lighting_god.utils.ModItemProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    private static int ICE_SPIKE_COOLDOWN = 0;
    private static final int MAX_ICE_SPIKE_COOLDOWN = 200;
    private static int MAX_ICE_SPIKE_COOLDOWN_PROCESSED = MAX_ICE_SPIKE_COOLDOWN;

    private static final int MAX_MANA = 100;
    private static float CURRENT_MANA = MAX_MANA;
    private static float MANA_REGEN = 0.5f;
    private static int MANA_BUFF = 0;
    private static boolean showPowerWheel = false;
    private static Player player;
    private static boolean alternativeGliding = false;
    private static boolean isIceSing = false;
    private static final ArrayList<Vec3> launchBlockPositions = new ArrayList<>();

    public static void setShowPowerWheel(boolean value) {
        showPowerWheel = value;
    }
    public static boolean getAlternativeGliding() {
        return alternativeGliding;
    }
    public static void setAlternativeGliding(boolean value) {
        alternativeGliding = value;
        if (!value) StopAnimation("fire_flyght");
    }
    public static boolean getIsIceSliding() {
        return isIceSing;
    }
    public static void setIsIceSliding(boolean value) {
        isIceSing = value;
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
    public static void setManaBuff(int value) {
        MANA_BUFF = value;
    }
    public static int getManaBuff() {return MANA_BUFF;}

    public static void setManaRegen(int value) {
        MANA_REGEN = value;
    }
    public static float getManaRegen() {return MANA_REGEN;}

    public static void addCurrentMana(float value) {
        CURRENT_MANA += value;
    }

    public static void removeCurrentMana(float value) {
        CURRENT_MANA -= value;
    }
    public static float getCurrentMana() {
        return CURRENT_MANA;
    }

    public static int getMaxMana() {return MAX_MANA;}

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
    public static int getIceSpikeCooldown() {
        return ICE_SPIKE_COOLDOWN;
    }
    public static void setIceSpikeCooldown(int value) {
        ICE_SPIKE_COOLDOWN = value;
    }
    public static int getMaxIceSpikeCooldown() {
        return MAX_ICE_SPIKE_COOLDOWN;
    }
    public static int getMaxProcessedIceSpikeCooldown() {
        return MAX_ICE_SPIKE_COOLDOWN_PROCESSED;
    }
    public static void setMaxProcessedIceSpikeCooldown(int value) {
        MAX_ICE_SPIKE_COOLDOWN_PROCESSED = value;
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
        ModCreativeModeTab.register(modEventBus);
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
            EntityRenderers.register(EntityInit.ICE_SPIKES.get(), IceSpikesRenderer::new);
            EntityRenderers.register(EntityInit.LIGHTNING_ARROW.get(), LightningArrowRender::new);

            String[] animations = {"earth_meteor_cast", "fireball_cast", "fire_flyght", "fire_flyght_turn"};

            for (String animation: animations) {
                PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new ResourceLocation(MOD_ID, animation), 42, (player) -> {
                    ModifierLayer<IAnimation> customAnimation = new ModifierLayer<>();

                    return customAnimation;
                });
            }
        }
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START && !Minecraft.getInstance().isPaused()) {
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
            } if (getIceSpikeCooldown() > 0) {
                setIceSpikeCooldown(getIceSpikeCooldown() - 1);
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

            if (CURRENT_MANA < MAX_MANA + MANA_BUFF && !isIceSing && !alternativeGliding) CURRENT_MANA += MANA_REGEN;
            if (CURRENT_MANA > MAX_MANA + MANA_BUFF) CURRENT_MANA = MAX_MANA + MANA_BUFF;
        }
    }

    public static void ReproduceAnimation(String animation) {
        try {
            ModifierLayer<IAnimation> customAnimation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(Minecraft.getInstance().player).get(new ResourceLocation(MOD_ID, animation));
            if (customAnimation.getAnimation() != null) {
                customAnimation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(20, Ease.LINEAR), null);
            }

            if (!animation.equals("fire_flyght") && !animation.equals("fire_flyght_turn")) {
                customAnimation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(10, Ease.LINEAR), new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation(MOD_ID, animation)))
                        .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL)
                        .setFirstPersonConfiguration(new FirstPersonConfiguration().setShowRightArm(true).setShowLeftItem(true)));
            } else {
                customAnimation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(0, Ease.LINEAR), new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation(MOD_ID, animation)))
                        .setFirstPersonMode(FirstPersonMode.VANILLA));
            }

            customAnimation.addModifier(new AbstractModifier() {
                public final float ninetyDegree = (float) Math.toRadians(90);
                public final float fortyFiveDegree = (float) Math.toRadians(45);
                public Vec3 prevPlayerPos = player.position();
                public float timeToPrint = 0;

                @Override
                public boolean canRemove() {
                    return super.canRemove();
                }

                @Override
                public void setHost(@Nullable ModifierLayer host) {
                    super.setHost(host);
                }

                @Override
                public void setAnim(@Nullable IAnimation newAnim) {
                    super.setAnim(newAnim);
                }

                @Override
                public @Nullable IAnimation getAnim() {
                    return super.getAnim();
                }

                @Override
                public boolean isActive() {
                    return super.isActive();
                }

                @Override
                public void tick() {
                    super.tick();
                }

                @Override
                public @NotNull Vec3f get3DTransform(@NotNull String modelName, @NotNull TransformType type, float tickDelta, @NotNull Vec3f value0) {
                    if (animation.equals("fire_flyght") || animation.equals("fire_flyght_turn")) {
                        if (modelName.equals("body") && type == TransformType.ROTATION) {
                            float desiredXRot = (float) -(Math.toRadians(player.getXRot()) + ninetyDegree);
                            float changeValue = getChangeValue(value0);
                            return super.get3DTransform(modelName, type, tickDelta, new Vec3f(desiredXRot, value0.getY(), changeValue));
                        }
                    }
                    return super.get3DTransform(modelName, type, tickDelta, value0);
                }

                private float getChangeValue(@NotNull Vec3f value0) {
                    double d1 = player.getX() - prevPlayerPos.x;
                    double d0 = player.getZ() - prevPlayerPos.z;
                    float f = (float)(d1 * d1 + d0 * d0);
                    float f1 = Minecraft.getInstance().getCameraEntity().getYRot();
                    float f2 = 0.0F;
                    float f3 = 0.0F;
                    if (f > 0.0025000002F) {
                        f3 = 1.0F;
                        f2 = (float)Math.sqrt((double)f) * 3.0F;
                        float f4 = (float) Mth.atan2(d0, d1) * (180F / (float)Math.PI) - 90.0F;
                        float f5 = Mth.abs(Mth.wrapDegrees(player.getYRot()) - f4);
                        f1 = f4;
//                        if (95.0F < f5 && f5 < 265.0F) {
//                            f1 = f4 - 180.0F;
//                        } else {
//                            f1 = f4;
//                        }
                    }

                    //double currentCameraYRot = Minecraft.getInstance().getCameraEntity().getYRot() - player.getYRot();
                    double deltaXRot = Math.tan(Math.toRadians(f1)) * 0.1;
                    float changeValue = 0;
                    if (value0.getZ() > deltaXRot && Math.abs(value0.getZ() - deltaXRot) > 0.005f) changeValue = 0.005f;
                    else if (value0.getZ() < deltaXRot && Math.abs(value0.getZ() - deltaXRot) > 0.005f) changeValue = -0.005f;

                    if (timeToPrint <= 0) {
                        System.out.println(deltaXRot);
                        prevPlayerPos = player.position();
                        timeToPrint = 100;
                    }
                    timeToPrint--;

                    return value0.getZ() + changeValue;
                }

                @Override
                public void setupAnim(float tickDelta) {
                    super.setupAnim(tickDelta);
                }

                @Override
                public @NotNull FirstPersonMode getFirstPersonMode(float tickDelta) {
                    return super.getFirstPersonMode(tickDelta);
                }

                @Override
                public @NotNull FirstPersonConfiguration getFirstPersonConfiguration(float tickDelta) {
                    return super.getFirstPersonConfiguration(tickDelta);
                }
            }, 1);

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void StopAnimation(String animation) {
        try {
            ModifierLayer<IAnimation> customAnimation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(Minecraft.getInstance().player).get(new ResourceLocation(MOD_ID, animation));
            if (customAnimation.getAnimation() != null) {
                customAnimation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(10, Ease.INOUTEXPO), null);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
