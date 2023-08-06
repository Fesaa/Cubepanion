package org.cubepanion.core.versionlinkers;

import java.util.List;
import net.labymod.api.reference.annotation.Referenceable;
import org.cubepanion.core.utils.ChestLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public abstract class ChestFinderLink {

  public abstract @NotNull List<ChestLocation> getChestLocations();

}
