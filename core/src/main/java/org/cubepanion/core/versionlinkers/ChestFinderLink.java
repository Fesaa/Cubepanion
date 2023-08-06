package org.cubepanion.core.versionlinkers;

import net.labymod.api.reference.annotation.Referenceable;
import net.labymod.api.util.math.vector.FloatVector3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public abstract class ChestFinderLink {

  protected abstract @NotNull FloatVector3[] getChestLocations();

}
