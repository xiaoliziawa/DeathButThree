package com.teampotato.deathbutthree.mixin;

import com.teampotato.deathbutthree.api.ExtendedEntityType;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityType.class)
public class EntityTypeMixin implements ExtendedEntityType {
    @Unique private int deathButThree$maxDeathTime = -1;

    @Override
    public int deathButThree$getMaxDeathTime() {
        return this.deathButThree$maxDeathTime;
    }

    @Override
    public void deathButThree$setMaxDeathTime(int maxDeathTime) {
        this.deathButThree$maxDeathTime = maxDeathTime;
    }
}
