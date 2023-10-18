package net.darwindontcare.lighting_god.items;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LightningGodMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeModeTab {
    //private static final ResourceLocation LOGO = new ResourceLocation(LightningGodMod.MOD_ID, "textures/icons/lightning_logo.png");
    public static CreativeModeTab LIGHTNING_GOD_TAB;

    @SubscribeEvent
    public static void registerCreativeModTab(CreativeModeTabEvent.Register event) {
        LIGHTNING_GOD_TAB = event.registerCreativeModeTab(new ResourceLocation(LightningGodMod.MOD_ID, "item/lightning_bow.png"),
                builder -> builder.icon(() -> new ItemStack(ModItems.LIGHTNING_BOW.get())).title(Component.translatable("creativemodtab.lightningmod")));
    }
}
