package net.darwindontcare.lighting_god.items;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.client.materials.ArmorItemMaterial;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LightningGodMod.MOD_ID);

    public static final RegistryObject<Item> LIGHTNING_BOW = ITEMS.register("lightning_bow",
            () -> new LightningBow(new Item.Properties().durability(1000).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> FIRE_SCYTH = ITEMS.register("fire_scyth",
            () -> new FireScyth(new FireTiers(1000, 4, 4, 2, 300, Ingredient.of(MinecartItem.byId(266))), 4, 0.08f, new Item.Properties().durability(1000).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> LIGHTNING_ARROW = ITEMS.register("lightning_arrow",
            () -> new LightningArrowItem(new Item.Properties()));
    public static final RegistryObject<Item> LIGHTNING_SCROLL = ITEMS.register("lightning_scroll",
            () -> new PowerScroll(new Item.Properties().rarity(Rarity.RARE), 1, "lightning"));
    public static final RegistryObject<Item> FIRE_SCROLL = ITEMS.register("fire_scroll",
            () -> new PowerScroll(new Item.Properties().rarity(Rarity.RARE), 1, "fire"));
    public static final RegistryObject<Item> WATER_SCROLL = ITEMS.register("water_scroll",
            () ->new PowerScroll(new Item.Properties().rarity(Rarity.RARE), 1, "water"));
    public static final RegistryObject<Item> EARTH_SCROLL = ITEMS.register("earth_scroll",
            () -> new PowerScroll(new Item.Properties().rarity(Rarity.RARE), 1, "earth"));

    public static final RegistryObject<Item> LIGHTNING_SCROLL_TIER_2 = ITEMS.register("lightning_scroll_tier_2",
            () -> new PowerScroll(new Item.Properties().rarity(Rarity.RARE), 2, "lightning"));
    public static final RegistryObject<Item> FIRE_SCROLL_TIER_2 = ITEMS.register("fire_scroll_tier_2",
            () -> new PowerScroll(new Item.Properties().rarity(Rarity.RARE), 2, "fire"));
    public static final RegistryObject<Item> WATER_SCROLL_TIER_2 = ITEMS.register("water_scroll_tier_2",
            () ->new PowerScroll(new Item.Properties().rarity(Rarity.RARE), 2, "water"));
    public static final RegistryObject<Item> EARTH_SCROLL_TIER_2 = ITEMS.register("earth_scroll_tier_2",
            () -> new PowerScroll(new Item.Properties().rarity(Rarity.RARE), 2, "earth"));

    public static final RegistryObject<Item> LIGHTNING_SCROLL_TIER_3 = ITEMS.register("lightning_scroll_tier_3",
            () -> new PowerScroll(new Item.Properties().rarity(Rarity.RARE), 3, "lightning"));
    public static final RegistryObject<Item> FIRE_SCROLL_TIER_3 = ITEMS.register("fire_scroll_tier_3",
            () -> new PowerScroll(new Item.Properties().rarity(Rarity.RARE), 3, "fire"));
    public static final RegistryObject<Item> WATER_SCROLL_TIER_3 = ITEMS.register("water_scroll_tier_3",
            () ->new PowerScroll(new Item.Properties().rarity(Rarity.RARE), 3, "water"));
    public static final RegistryObject<Item> EARTH_SCROLL_TIER_3 = ITEMS.register("earth_scroll_tier_3",
            () -> new PowerScroll(new Item.Properties().rarity(Rarity.RARE), 3, "earth"));

    public static final RegistryObject<Item> LIGHTNING_SCROLL_TIER_4 = ITEMS.register("lightning_scroll_tier_4",
            () -> new PowerScroll(new Item.Properties().rarity(Rarity.RARE), 4, "lightning"));
    public static final RegistryObject<Item> FIRE_SCROLL_TIER_4 = ITEMS.register("fire_scroll_tier_4",
            () -> new PowerScroll(new Item.Properties().rarity(Rarity.RARE), 4, "fire"));
    public static final RegistryObject<Item> WATER_SCROLL_TIER_4 = ITEMS.register("water_scroll_tier_4",
            () ->new PowerScroll(new Item.Properties().rarity(Rarity.RARE), 4, "water"));
    public static final RegistryObject<Item> EARTH_SCROLL_TIER_4 = ITEMS.register("earth_scroll_tier_4",
            () -> new PowerScroll(new Item.Properties().rarity(Rarity.RARE), 4, "earth"));


    public static final RegistryObject<Item> LIGHTNING_BOOTS = ITEMS.register("lightning_boots",
            () -> new LightningArmor(ArmorItemMaterial.LIGHTNING, ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> LIGHTNING_CHESTPLATE = ITEMS.register("lightning_chestplate",
            () -> new LightningArmor(ArmorItemMaterial.LIGHTNING, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> LIGHTNING_LEGGINGS = ITEMS.register("lightning_leggings",
            () -> new LightningArmor(ArmorItemMaterial.LIGHTNING, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> LIGHTNING_HELMET = ITEMS.register("lightning_helmet",
            () -> new LightningArmor(ArmorItemMaterial.LIGHTNING, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<Item> FIRE_BOOTS = ITEMS.register("fire_boots",
            () -> new FireArmor(ArmorItemMaterial.FIRE, ArmorItem.Type.BOOTS, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> FIRE_CHESTPLATE = ITEMS.register("fire_chestplate",
            () -> new FireArmor(ArmorItemMaterial.FIRE, ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> FIRE_LEGGINGS = ITEMS.register("fire_leggings",
            () -> new FireArmor(ArmorItemMaterial.FIRE, ArmorItem.Type.LEGGINGS, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> FIRE_HELMET = ITEMS.register("fire_helmet",
            () -> new FireArmor(ArmorItemMaterial.FIRE, ArmorItem.Type.HELMET, new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> WATER_BOOTS = ITEMS.register("water_boots",
            () -> new WaterArmorItem(ArmorItemMaterial.WATER, ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> WATER_CHESTPLATE = ITEMS.register("water_chestplate",
            () -> new WaterArmorItem(ArmorItemMaterial.WATER, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> WATER_LEGGINGS = ITEMS.register("water_leggings",
            () -> new WaterArmorItem(ArmorItemMaterial.WATER, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> WATER_HELMET = ITEMS.register("water_helmet",
            () -> new WaterArmorItem(ArmorItemMaterial.WATER, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<Item> LIGHTNING_ESSENCE = ITEMS.register("lightning_essence",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> FIRE_ESSENCE = ITEMS.register("fire_essence",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> WATER_ESSENCE = ITEMS.register("water_essence",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> EARTH_ESSENCE = ITEMS.register("earth_essence",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
