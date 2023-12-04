package net.darwindontcare.lighting_god.enchantments;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnchantmentsInit {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, LightningGodMod.MOD_ID);

    public static final RegistryObject<Enchantment> SKY_JUMP = ENCHANTMENTS.register("sky_jump", SkyJumpEnchantment::new);

    public static void register(IEventBus bus) {
        ENCHANTMENTS.register(bus);
    }
}
