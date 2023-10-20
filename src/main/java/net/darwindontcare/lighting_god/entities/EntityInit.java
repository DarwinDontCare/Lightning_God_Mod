package net.darwindontcare.lighting_god.entities;

import net.darwindontcare.lighting_god.LightningGodMod;
import net.darwindontcare.lighting_god.entities.custom.MeteorProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityInit {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, LightningGodMod.MOD_ID);

    public static final RegistryObject<EntityType<LightningArrowEntity>> LIGHTNING_ARROW = ENTITY_TYPES.register("lightning_arrow",
            () -> EntityType.Builder.of((EntityType.EntityFactory<LightningArrowEntity>) LightningArrowEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build(new ResourceLocation(LightningGodMod.MOD_ID,"lightning_arrow").toString()));

    public static final RegistryObject<EntityType<MeteorProjectile>> EARTH_METEOR = ENTITY_TYPES.register("earth_meteor",
            () -> EntityType.Builder.of((EntityType.EntityFactory<MeteorProjectile>) MeteorProjectile::new, MobCategory.MISC).sized(4F, 4F).build(new ResourceLocation(LightningGodMod.MOD_ID, "earth_meteor").toString()));

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
}
