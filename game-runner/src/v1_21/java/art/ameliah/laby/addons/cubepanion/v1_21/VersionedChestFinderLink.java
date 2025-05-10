package art.ameliah.laby.addons.cubepanion.v1_21;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.external.ChestLocation;
import art.ameliah.laby.addons.cubepanion.core.external.CubepanionAPI;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.ChestFinderLink;
import java.util.List;
import java.util.Map;
import javax.inject.Singleton;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;

@Singleton
@Implements(ChestFinderLink.class)
public class VersionedChestFinderLink extends ChestFinderLink {

  @Override
  public @NotNull List<ChestLocation> getChestLocations() {

    this.locations.clear();

    LocalPlayer player = Minecraft.getInstance().player;
    ClientPacketListener con = Minecraft.getInstance().getConnection();
    if (con == null || player == null) {
      return this.locations;
    }

    var validLocations = CubepanionAPI.I().getChestLocations();

    ChunkPos pos = player.chunkPosition();
    ClientLevel level = con.getLevel();
    int range = Cubepanion.get().configuration().getQolConfig().getRange().get();
    for (int x = -range; x <= range; x++) {
      for (int y = -range; y <= range; y++) {
        LevelChunk chunk = level.getChunk(pos.x + x, pos.z + y);
        for (Map.Entry<BlockPos, BlockEntity> entry : chunk.getBlockEntities().entrySet()) {
          if (entry.getValue().getType().equals(BlockEntityType.CHEST)) {
            ChestLocation loc = new ChestLocation(entry.getKey().getX(), entry.getKey().getY(), entry.getKey().getZ());
            if (validLocations.contains(loc)) {
              this.locations.add(loc);
            }
          }
        }
      }
    }
    return this.locations;
  }
}
