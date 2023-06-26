package com.teampotato.deathbutthree;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;

@Mod(DeathButThree.ID)
@Mod.EventBusSubscriber(modid = DeathButThree.ID)
public class DeathButThree {
    public static final String ID = "deathbutthree";
    public DeathButThree() {
        ModLoadingContext.get().registerConfig(Type.COMMON, Config.configSpec);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (!event.isCanceled() && event.getEntity() instanceof LivingEntity entity) {
            entity.setHealth(entity.getMaxHealth());
        }
    }
}
