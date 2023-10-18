package net.darwindontcare.lighting_god.blocks;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.entities.CustomFallingBlockEntity;
import net.darwindontcare.lighting_god.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, LightningGodMod.MOD_ID);

//    public static final RegistryObject<Block> EBONY_PLANKS = registerBlock("ebony_planks",
//            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)
//                    .strength(5f)) {
//                @Override
//                public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
//                    return true;
//                }
//
//                @Override
//                public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
//                    return 5;
//                }
//
//                @Override
//                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
//                    return 20;
//                }
//            });



    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
