package com.teampotato.deathbutthree.mixin;

import com.teampotato.deathbutthree.api.ExtendedEntityType;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityType.class)
public class EntityTypeMixin implements ExtendedEntityType {
    @Unique private boolean deathButThree$isBoss;

    @Override
    public boolean deathButThree$getIsBoss() {
        return this.deathButThree$isBoss;
    }

    @Override
    public void deathButThree$setIsBoss(boolean isBoss) {
        this.deathButThree$isBoss = isBoss;
    }
}
