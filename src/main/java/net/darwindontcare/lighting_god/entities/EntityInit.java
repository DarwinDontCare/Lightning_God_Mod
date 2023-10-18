package net.darwindontcare.lighting_god.entities;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityInit {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, LightningGodMod.MOD_ID);

    public static final RegistryObject<EntityType<LightningArrowEntity>> LIGHTNING_ARROW = ENTITY_TYPES.register("lightning_arrow",
            () -> EntityType.Builder.of((EntityType.EntityFactory<LightningArrowEntity>) LightningArrowEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build("lightning_arrow"));
}
