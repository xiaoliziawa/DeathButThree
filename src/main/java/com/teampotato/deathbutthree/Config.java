package com.teampotato.deathbutthree;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class Config {
    public static final ForgeConfigSpec CONFIG;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> BOSSES;
    public static final ForgeConfigSpec.IntValue MAX_DEATH_TIMES;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("DeathButThree");
        BOSSES = builder.defineList("ValidBosses", ObjectArrayList.wrap(new String[]{"cataclysm:ignis"}), o -> o instanceof String);
        MAX_DEATH_TIMES = builder.defineInRange("maxDeathTimes", 3, 1, Integer.MAX_VALUE);
        builder.pop();
        CONFIG = builder.build();
    }
}
