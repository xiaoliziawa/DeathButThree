package com.teampotato.deathbutthree;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;

@Mod(DeathButThree.ID)
public class DeathButThree {
    public static final String ID = "deathbutthree";
    public DeathButThree() {
        ModLoadingContext.get().registerConfig(Type.COMMON, Config.configSpec);
    }
}
