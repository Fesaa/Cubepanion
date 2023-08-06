package org.cubepanion.v1_20_1;

import net.labymod.api.models.Implements;
import net.labymod.api.util.math.vector.FloatVector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.chunk.LevelChunk;
import org.cubepanion.core.versionlinkers.ChestFinderLink;
import org.jetbrains.annotations.NotNull;
import javax.inject.Singleton;
import java.util.Map;

@Singleton
@Implements(ChestFinderLink.class)
public class VersionedChestFinderLink extends ChestFinderLink {

  private final int range = 15;

  @Override
  protected @NotNull FloatVector3[] getChestLocations() {
    LocalPlayer player = Minecraft.getInstance().player;
    ClientPacketListener con = Minecraft.getInstance().getConnection();
    if (con == null || player == null) {
      return new FloatVector3[0];
    }
    ChunkPos pos = player.chunkPosition();
    ClientLevel level = Minecraft.getInstance().getConnection().getLevel();

    for (int x = -range; x <= range; x++) {
      for (int y = -range; y <= range; y++) {
        LevelChunk chunk = level.getChunk(x, y);
        for (Map.Entry<BlockPos, BlockEntity> entry : chunk.getBlockEntities().entrySet()) {
          if (entry.getValue().getType().equals(BlockEntityType.CHEST)) {
            
          }
        }
      }
    }

    return new FloatVector3[0];
  }
}
