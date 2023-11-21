package com.teampotato.deathbutthree.mixin;

import com.mojang.authlib.GameProfile;
import com.teampotato.deathbutthree.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends Player {
    @Shadow @Final public MinecraftServer server;

    public MixinServerPlayer(Level pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile, @Nullable ProfilePublicKey pProfilePublicKey) {
        super(pLevel, pPos, pYRot, pGameProfile, pProfilePublicKey);
    }

    @Inject(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z", ordinal = 0, shift = At.Shift.BEFORE))
    private void onDie(DamageSource damageSource, CallbackInfo ci) {
        if (damageSource == null) return;
        if (damageSource.getEntity() instanceof LivingEntity sourceLiving) {
            ResourceLocation sourceName = ForgeRegistries.ENTITY_TYPES.getKey(sourceLiving.getType());
            if (sourceName != null && Config.bosses.get().contains(sourceName.toString())) {
                Set<String> tags = sourceLiving.getTags();
                String uuid = sourceLiving.getStringUUID();
                int maxDeathAmount = Config.maxDeathTimes.get();
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
}
