package art.ameliah.laby.addons.cubepanion.core.versionlinkers;

import static art.ameliah.laby.addons.cubepanion.core.utils.Utils.chestLocationsComponent;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.external.ChestLocation;
import art.ameliah.laby.addons.cubepanion.core.render.waypoint.WaypointObject;
import art.ameliah.laby.addons.cubepanion.core.utils.Colours;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.reference.annotation.Referenceable;
import net.labymod.api.util.math.vector.DoubleVector3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public abstract class ChestFinderLink {

  private static final Component CHEST_DISPLAY_COMPONENT =
      Component.translatable("cubepanion.hologram.chest_finder", Colours.Title);

  private static final ResourceLocation CHEST_DISPLAY_ICON_RESOURCE =
      ResourceLocation.create("minecraft", "textures/item/compass_00.png");

  private static final Icon CHEST_DISPLAY_ICON =
      Icon.texture(CHEST_DISPLAY_ICON_RESOURCE);

  private static final Component NOT_FOUND =
      Component.translatable("cubepanion.messages.chests_finder.not_found", Colours.Error);

  protected final List<ChestLocation> locations = new ArrayList<>();

  public abstract @NotNull List<ChestLocation> getChestLocations();

  public void displayLocations() {
    List<ChestLocation> chestLocations = getChestLocations();
    if (!chestLocations.isEmpty()) {
      for (ChestLocation loc : chestLocations) {
        Cubepanion.get().displayMessage(chestLocationsComponent(loc));
        Laby.references().worldObjectRegistry()
            .register(loc.toString(), new WaypointObject(
                new DoubleVector3(loc.x() + .5, loc.y() + 1.5, loc.z() + .5),
                CHEST_DISPLAY_ICON, CHEST_DISPLAY_COMPONENT
            ));
      }
    } else {
      Cubepanion.get().displayMessage(NOT_FOUND);
    }
  }

  public void clearLocations() {
    for (ChestLocation location : this.locations) {
      Laby.references().worldObjectRegistry()
          .unregister(location.toString());
    }
    this.locations.clear();
  }

  public @NotNull List<ChestLocation> getLocations() {
    return this.locations;
  }

}
