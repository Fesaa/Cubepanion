package org.cubepanion.core.versionlinkers;


import net.labymod.api.reference.annotation.Referenceable;
import org.cubepanion.core.weave.ChestAPI.ChestLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

@Nullable
@Referenceable
public abstract class ChestFinderLink {

  public abstract @NotNull List<ChestLocation> getChestLocations();

}
