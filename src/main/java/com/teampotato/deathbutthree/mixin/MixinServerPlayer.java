package com.teampotato.deathbutthree.mixin;

import com.teampotato.deathbutthree.api.ExtendedEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayer {
    @Inject(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z", ordinal = 0, shift = At.Shift.BEFORE))
    private void onDie(DamageSource damageSource, CallbackInfo ci) {
        if (damageSource == null) return;
        if (damageSource.getAttacker() instanceof LivingEntity sourceLiving) {
            int maxDeathAmount = ((ExtendedEntityType)sourceLiving.getType()).deathButThree$getMaxDeathTime();
            if (maxDeathAmount == -1) return;
            Set<String> tags = sourceLiving.getCommandTags();
            String uuid = sourceLiving.getUuidAsString();
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