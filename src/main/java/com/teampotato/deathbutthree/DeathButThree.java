package com.teampotato.deathbutthree;

import com.teampotato.deathbutthree.api.ExtendedEntityType;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraftforge.fml.config.ModConfig;

public class DeathButThree implements ModInitializer {
	@Override
	public void onInitialize() {
		ForgeConfigRegistry.INSTANCE.register("deathbutthree", ModConfig.Type.COMMON, Config.CONFIG);
		for (String entry : Config.ENTIRES.get()) {
			String registryName = entry.split(";")[0];
			((ExtendedEntityType)Registries.ENTITY_TYPE.get(new Identifier(registryName.split(":")[0], registryName.split(":")[1])))
					.deathButThree$setMaxDeathTime(Integer.parseInt(entry.split(";")[1]));
		}
	}
}