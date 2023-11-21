package com.teampotato.deathbutthree;

import com.teampotato.deathbutthree.api.ExtendedEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(DeathButThree.ID)
public class DeathButThree {
    public static final String ID = "deathbutthree";

    public DeathButThree() {
        ModLoadingContext.get().registerConfig(Type.COMMON, Config.CONFIG);
        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLCommonSetupEvent event) -> event.enqueueWork(() -> {
            for (EntityType<?> entityType : ForgeRegistries.ENTITIES) {
                ResourceLocation id = entityType.getRegistryName();
                if (id == null) continue;
                ((ExtendedEntityType)entityType).deathButThree$setIsBoss(Config.BOSSES.get().contains(id.toString()));
            }
        }));
    }
}
