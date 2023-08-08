package org.cubepanion.v1_19_4.mixins;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.prediction.BlockStatePredictionHandler;
import net.minecraft.world.level.storage.LevelData;
import org.cubepanion.v1_19_4.VersionedVotingLink;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {


  @Shadow
  abstract BlockStatePredictionHandler getBlockStatePredictionHandler();

  @Inject(at = @At("TAIL"), method = "getLevelData()Lnet/minecraft/world/level/storage/LevelData;")
  public void getLevelInject(CallbackInfoReturnable<LevelData> cir) {
    VersionedVotingLink.handler = getBlockStatePredictionHandler();
  }

}
