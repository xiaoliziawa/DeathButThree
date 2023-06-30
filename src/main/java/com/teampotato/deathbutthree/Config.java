package com.teampotato.deathbutthree;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class Config {
    public static final ForgeConfigSpec configSpec;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> bosses;
    public static final ForgeConfigSpec.IntValue maxDeathTimes;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("DeathButThree");
        bosses = builder.defineList("ValidBosses", Lists.newArrayList("cataclysm:ignis"), o -> o instanceof String);
        maxDeathTimes = builder.defineInRange("maxDeathTimes", 3, 1, Integer.MAX_VALUE);
        builder.pop();
        configSpec = builder.build();
    }
}
