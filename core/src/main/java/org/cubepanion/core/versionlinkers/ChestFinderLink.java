package org.cubepanion.core.versionlinkers;

import art.ameliah.libs.weave.ChestAPI.ChestLocation;
import java.util.List;
import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public abstract class ChestFinderLink {

  public abstract @NotNull List<ChestLocation> getChestLocations();

}
