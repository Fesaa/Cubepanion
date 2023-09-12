package org.cubepanion.v1_20_1.mixins;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.prediction.BlockStatePredictionHandler;
import org.cubepanion.v1_20_1.client.VersionedBSPHAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientLevel.class)
public class ClientLevelMixin implements VersionedBSPHAccessor {

  @Final
  @Shadow
  private BlockStatePredictionHandler blockStatePredictionHandler;


  @Override
  public BlockStatePredictionHandler cubepanion$get() {
    return blockStatePredictionHandler;
  }
}
