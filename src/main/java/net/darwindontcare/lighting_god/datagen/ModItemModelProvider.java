package net.darwindontcare.lighting_god.datagen;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.items.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, LightningGodMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.FIRE_BOOTS);
        simpleItem(ModItems.EARTH_SCROLL);
        simpleItem(ModItems.FIRE_CHESTPLATE);
        simpleItem(ModItems.LIGHTNING_ARROW);
        simpleItem(ModItems.LIGHTNING_BOW);
        simpleItem(ModItems.FIRE_HELMET);
        simpleItem(ModItems.FIRE_LEGGINGS);
        simpleItem(ModItems.FIRE_SCROLL);
        handheldItem(ModItems.FIRE_SCYTH);
        simpleItem(ModItems.LIGHTNING_BOOTS);
        simpleItem(ModItems.LIGHTNING_CHESTPLATE);
        simpleItem(ModItems.LIGHTNING_HELMET);
        simpleItem(ModItems.LIGHTNING_LEGGINGS);
        simpleItem(ModItems.LIGHTNING_SCROLL);

        handheldItem(ModItems.WATER_SCROLL);
        handheldItem(ModItems.WATER_SCROLL_TIER_2);
        handheldItem(ModItems.EARTH_SCROLL_TIER_2);
        handheldItem(ModItems.FIRE_SCROLL_TIER_2);
        handheldItem(ModItems.LIGHTNING_SCROLL_TIER_2);
        handheldItem(ModItems.WATER_SCROLL_TIER_3);
        handheldItem(ModItems.EARTH_SCROLL_TIER_3);
        handheldItem(ModItems.FIRE_SCROLL_TIER_3);
        handheldItem(ModItems.LIGHTNING_SCROLL_TIER_3);
        handheldItem(ModItems.WATER_SCROLL_TIER_4);
        handheldItem(ModItems.EARTH_SCROLL_TIER_4);
        handheldItem(ModItems.FIRE_SCROLL_TIER_4);
        handheldItem(ModItems.LIGHTNING_SCROLL_TIER_4);
        simpleItem(ModItems.LIGHTNING_ESSENCE);
        simpleItem(ModItems.FIRE_ESSENCE);
        simpleItem(ModItems.WATER_ESSENCE);
        simpleItem(ModItems.EARTH_ESSENCE);

        //withExistingParent(ModItems.TIGER_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
    }

    private ItemModelBuilder saplingItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(LightningGodMod.MOD_ID,"block/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(LightningGodMod.MOD_ID,"item/" + item.getId().getPath()));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(LightningGodMod.MOD_ID,"item/" + item.getId().getPath()));
    }
}