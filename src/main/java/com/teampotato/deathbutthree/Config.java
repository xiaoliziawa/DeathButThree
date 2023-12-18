package com.teampotato.deathbutthree;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class Config {
    public static final ForgeConfigSpec CONFIG;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ENTIRES;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("DeathButThree");
        ENTIRES = builder.comment("Before ';': boss' registry name", "After ';': its max death times").defineList("ValidBossesAndMaxDeathTimes", ObjectArrayList.wrap(new String[]{"cataclysm:ignis;3"}), o -> o instanceof String);
        builder.pop();
        CONFIG = builder.build();
    }
}