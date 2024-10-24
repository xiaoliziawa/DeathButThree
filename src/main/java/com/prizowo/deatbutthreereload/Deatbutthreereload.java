package com.prizowo.deatbutthreereload;

import com.prizowo.deatbutthreereload.api.ExtendedEntityType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Deatbutthreereload.MOD_ID)
public class Deatbutthreereload {
    public static final String MOD_ID = "deatbutthreereload";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public Deatbutthreereload(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.CONFIG);
        modContainer.getEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            for (String entry : Config.ENTIRES.get()) {
                String[] parts = entry.split(";");
                if (parts.length != 2) {
                    LOGGER.error("Invalid entry format: {}", entry);
                    continue;
                }
                String registryName = parts[0];
                String[] namespaceParts = registryName.split(":");
                if (namespaceParts.length != 2) {
                    LOGGER.error("Invalid registry name format: {}", registryName);
                    continue;
                }
                ResourceLocation id =ResourceLocation.fromNamespaceAndPath(namespaceParts[0], namespaceParts[1]);
                String maxDeathTime = parts[1];

                EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(id);
                if (entityType == null) {
                    LOGGER.warn("Entity type not found for id: {}", id);
                    continue;
                }

                try {
                    int maxDeathTimeInt = Integer.parseInt(maxDeathTime);
                    ((ExtendedEntityType) entityType).deathButThree$setMaxDeathTime(maxDeathTimeInt);
                    LOGGER.info("Set max death time for {} to {}", id, maxDeathTimeInt);
                } catch (NumberFormatException e) {
                    LOGGER.error("Invalid max death time value: {}", maxDeathTime);
                }
            }
        });
    }
}
