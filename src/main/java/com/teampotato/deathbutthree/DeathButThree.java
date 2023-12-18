package com.teampotato.deathbutthree;

import com.teampotato.deathbutthree.api.ExtendedEntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(DeathButThree.MOD_ID)
public class DeathButThree {
    public static final String MOD_ID = "deathbutthree";

    public DeathButThree() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.CONFIG);
        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLCommonSetupEvent event) -> event.enqueueWork(() -> {
            for (String entry : Config.ENTIRES.get()) {
                String registryName = entry.split(";")[0];
                ResourceLocation id = new ResourceLocation(registryName.split(":")[0], registryName.split(":")[1]);
                String maxDeathTime = entry.split(";")[1];
                EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(id);
                if (entityType == null) continue;
                ((ExtendedEntityType)entityType).deathButThree$setMaxDeathTime(Integer.parseInt(maxDeathTime));
            }
        }));
    }
}
