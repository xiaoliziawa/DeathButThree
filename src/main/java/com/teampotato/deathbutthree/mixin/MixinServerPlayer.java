package com.teampotato.deathbutthree.mixin;

import com.teampotato.deathbutthree.api.ExtendedEntityType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer {
    @Inject(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z", ordinal = 0, shift = At.Shift.BEFORE))
    private void onDie(DamageSource damageSource, CallbackInfo ci) {
        if (damageSource == null) return;
        if (damageSource.getEntity() instanceof LivingEntity) {
            LivingEntity sourceLiving = (LivingEntity) damageSource.getEntity();
            int maxDeathAmount = ((ExtendedEntityType)sourceLiving.getType()).deathButThree$getMaxDeathTime();
            if (maxDeathAmount == -1) return;
            Set<String> tags = sourceLiving.getTags();
            String uuid = sourceLiving.getStringUUID();
            if (tags.contains("death_but_" + (maxDeathAmount - 1) + uuid) || maxDeathAmount == 1) {
                if (maxDeathAmount != 1) tags.remove("death_but_" + (maxDeathAmount - 1) + uuid);
                sourceLiving.heal(sourceLiving.getMaxHealth());
            } else {
                boolean have_died = false;
                for (int deathAmount = maxDeathAmount - 2; deathAmount > 0; deathAmount--) {
                    if (tags.contains("death_but_" + deathAmount + uuid)) {
                        tags.remove("death_but_" + deathAmount + uuid);
                        tags.add("death_but_" + (deathAmount + 1) + uuid);
                        have_died = true;
                        break;
                    }
                }
                if (!have_died) {
                    tags.add("death_but_" + 1 + uuid);
                }
            }
        }
    }
}