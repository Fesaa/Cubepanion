package art.ameliah.laby.addons.cubepanion.core.versionlinkers;


import static art.ameliah.laby.addons.cubepanion.core.utils.Utils.chestLocationsComponent;

import java.util.ArrayList;
import java.util.List;
import net.labymod.api.client.component.Component;
import net.labymod.api.reference.annotation.Referenceable;
import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.utils.Colours;
import art.ameliah.laby.addons.cubepanion.core.weave.ChestAPI.ChestLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public abstract class ChestFinderLink {

  private static final Component NOT_FOUND =
      Component.translatable("cubepanion.messages.chests_finder.not_found", Colours.Error);

  protected final List<ChestLocation> locations = new ArrayList<>();

  public abstract @NotNull List<ChestLocation> getChestLocations();

  public void displayLocations() {
    List<ChestLocation> chestLocations = getChestLocations();
    if (!chestLocations.isEmpty()) {
      for (ChestLocation loc : chestLocations) {
        Cubepanion.get().displayMessage(chestLocationsComponent(loc));
      }
    } else {
      Cubepanion.get().displayMessage(NOT_FOUND);
    }
  }

}
