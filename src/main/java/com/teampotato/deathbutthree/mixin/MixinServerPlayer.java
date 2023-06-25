package com.teampotato.deathbutthree.mixin;

import com.mojang.authlib.GameProfile;
import com.teampotato.deathbutthree.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends Player {
    public MixinServerPlayer(Level pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile, @Nullable ProfilePublicKey pProfilePublicKey) {
        super(pLevel, pPos, pYRot, pGameProfile, pProfilePublicKey);
    }

    @Inject(method = "die", at = @At("HEAD"))
    private void onDie(DamageSource damageSource, CallbackInfo ci) {
        if (damageSource == null) return;
        Entity sourceEntity = damageSource.getEntity();
        if (sourceEntity instanceof LivingEntity sourceLiving) {
            ResourceLocation sourceName = ForgeRegistries.ENTITY_TYPES.getKey(sourceLiving.getType());
            if (sourceName != null && Config.bosses.get().contains(sourceName.toString())) {
                Set<String> tags = sourceLiving.getTags();
                if (tags.contains("death_but_two")) {
                    tags.remove("death_but_two");
                    sourceLiving.setHealth(sourceLiving.getMaxHealth());
                } else if (tags.contains("death_but_one")) {
                    tags.remove("death_but_one");
                    tags.add("death_but_two");
                } else {
                    tags.add("death_but_one");
                }
            }
        }
    }
}
