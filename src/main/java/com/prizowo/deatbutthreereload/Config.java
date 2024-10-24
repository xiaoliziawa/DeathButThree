package com.prizowo.deatbutthreereload;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class Config {
    public static final ModConfigSpec CONFIG;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> ENTIRES;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.push("DeathButThree");
        ENTIRES = builder.comment("Before ';': boss' registry name", "After ';': its max death times").defineList("ValidBossesAndMaxDeathTimes", ObjectArrayList.wrap(new String[]{"cataclysm:ignis;3"}), o -> o instanceof String);
        builder.pop();
        CONFIG = builder.build();
    }
}