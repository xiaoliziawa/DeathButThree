package com.teampotato.deathbutthree.mixin;

import com.mojang.authlib.GameProfile;
import com.teampotato.deathbutthree.Config;
import com.teampotato.deathbutthree.api.ExtendedEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayer extends PlayerEntity {

    public MixinServerPlayer(World pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile) {
        super(pLevel, pPos, pYRot, pGameProfile);
    }

    @Inject(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$RuleKey;)Z", ordinal = 0, shift = At.Shift.BEFORE))
    private void onDie(DamageSource damageSource, CallbackInfo ci) {
        if (damageSource == null) return;
        if (damageSource.getEntity() instanceof LivingEntity) {
            LivingEntity sourceLiving = (LivingEntity)damageSource.getEntity();
            if (!((ExtendedEntityType)sourceLiving.getType()).deathButThree$getIsBoss()) return;
            Set<String> tags = sourceLiving.getTags();
            String uuid = sourceLiving.getStringUUID();
            int maxDeathAmount = Config.MAX_DEATH_TIMES.get();
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
