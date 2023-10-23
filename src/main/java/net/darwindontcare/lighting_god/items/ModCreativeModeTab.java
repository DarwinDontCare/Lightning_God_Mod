package net.darwindontcare.lighting_god.items;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.CreativeModeTabRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = LightningGodMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeModeTab {
    //private static final ResourceLocation LOGO = new ResourceLocation(LightningGodMod.MOD_ID, "textures/icons/lightning_logo.png");
    //public static CreativeModeTab LIGHTNING_GOD_TAB;
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LightningGodMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> LIGHTNING_GOD_TAB = CREATIVE_MODE_TABS.register("lightning_god_tab", () ->
            CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.LIGHTNING_BOW.get()))
                    .title(Component.translatable("creativemodtab.lightningmod"))
                    .displayItems(((p_270258_, p_259752_) -> {
                        p_259752_.accept(ModItems.FIRE_BOOTS.get());
                        p_259752_.accept(ModItems.FIRE_LEGGINGS.get());
                        p_259752_.accept(ModItems.FIRE_CHESTPLATE.get());
                        p_259752_.accept(ModItems.FIRE_HELMET.get());

                        p_259752_.accept(ModItems.LIGHTNING_BOOTS.get());
                        p_259752_.accept(ModItems.LIGHTNING_LEGGINGS.get());
                        p_259752_.accept(ModItems.LIGHTNING_CHESTPLATE.get());
                        p_259752_.accept(ModItems.LIGHTNING_HELMET.get());

                        p_259752_.accept(ModItems.WATER_BOOTS.get());
                        p_259752_.accept(ModItems.WATER_LEGGINGS.get());
                        p_259752_.accept(ModItems.WATER_CHESTPLATE.get());
                        p_259752_.accept(ModItems.WATER_HELMET.get());

                        p_259752_.accept(ModItems.FIRE_SCYTH.get());
                        p_259752_.accept(ModItems.LIGHTNING_BOW.get());
                        p_259752_.accept(ModItems.LIGHTNING_ARROW.get());

                        p_259752_.accept(ModItems.FIRE_SCROLL.get());
                        p_259752_.accept(ModItems.LIGHTNING_SCROLL.get());
                        p_259752_.accept(ModItems.WATER_SCROLL.get());
                        p_259752_.accept(ModItems.EARTH_SCROLL.get());

                        p_259752_.accept(ModItems.FIRE_SCROLL_TIER_2.get());
                        p_259752_.accept(ModItems.LIGHTNING_SCROLL_TIER_2.get());
                        p_259752_.accept(ModItems.WATER_SCROLL_TIER_2.get());
                        p_259752_.accept(ModItems.EARTH_SCROLL_TIER_2.get());

                        p_259752_.accept(ModItems.WATER_SCROLL_TIER_3.get());
                        p_259752_.accept(ModItems.EARTH_SCROLL_TIER_3.get());
                        p_259752_.accept(ModItems.FIRE_SCROLL_TIER_3.get());
                        p_259752_.accept(ModItems.LIGHTNING_SCROLL_TIER_3.get());

                        p_259752_.accept(ModItems.WATER_SCROLL_TIER_4.get());
                        p_259752_.accept(ModItems.EARTH_SCROLL_TIER_4.get());
                        p_259752_.accept(ModItems.FIRE_SCROLL_TIER_4.get());
                        p_259752_.accept(ModItems.LIGHTNING_SCROLL_TIER_4.get());

                        p_259752_.accept(ModItems.LIGHTNING_ESSENCE.get());
                        p_259752_.accept(ModItems.FIRE_ESSENCE.get());
                        p_259752_.accept(ModItems.WATER_ESSENCE.get());
                        p_259752_.accept(ModItems.EARTH_ESSENCE.get());
                    }))
                    .build());

    public static void register(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
    }
}
