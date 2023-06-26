package com.teampotato.deathbutthree.mixin;

import com.mojang.authlib.GameProfile;
import com.teampotato.deathbutthree.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends Player {
    @Shadow public abstract @NotNull ServerLevel getLevel();

    public MixinServerPlayer(Level pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile, @Nullable ProfilePublicKey pProfilePublicKey) {
        super(pLevel, pPos, pYRot, pGameProfile, pProfilePublicKey);
    }

    @Unique
    private static final RemovalReason DISCARDED = RemovalReason.DISCARDED;

    @Inject(method = "die", at = @At("HEAD"))
    private void onDie(DamageSource damageSource, CallbackInfo ci) {
        if (damageSource == null) return;
        Entity sourceEntity = damageSource.getEntity();
        if (sourceEntity instanceof LivingEntity sourceLiving) {
            ResourceLocation sourceName = ForgeRegistries.ENTITY_TYPES.getKey(sourceLiving.getType());
            if (sourceName != null && Config.bosses.get().contains(sourceName.toString())) {
                Set<String> tags = sourceLiving.getTags();
                String uuid = sourceLiving.getStringUUID();
                if (tags.contains("death_but_two" + uuid)) {
                    tags.remove("death_but_two" + uuid);
                    MinecraftServer server = this.getLevel().getServer();
                    server.getCommands().performPrefixedCommand(server.createCommandSourceStack().withSuppressedOutput(),
                            "execute in " + sourceLiving.level.dimension().location() + " run summon " + sourceName + " " +
                                    sourceLiving.getX() + " " + sourceLiving.getY() + " " + sourceLiving.getZ());
                    sourceLiving.remove(DISCARDED);
                } else if (tags.contains("death_but_one" + uuid)) {
                    tags.remove("death_but_one" + uuid);
                    tags.add("death_but_two" + uuid);
                } else {
                    tags.add("death_but_one" + uuid);
                }
            }
        }
    }
}
