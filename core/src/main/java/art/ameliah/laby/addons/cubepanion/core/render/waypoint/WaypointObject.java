package art.ameliah.laby.addons.cubepanion.core.render.waypoint;

import static net.labymod.api.client.world.object.CullVolume.point;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.world.object.AbstractWorldObject;
import net.labymod.api.client.world.object.CullVolume;
import net.labymod.api.util.math.vector.DoubleVector3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WaypointObject extends AbstractWorldObject {

  Icon icon;      // nullable
  Component text; // nullable

  public WaypointObject(
      @NotNull DoubleVector3 position,
      @Nullable Icon icon,
      @Nullable Component text
  ) {
    super(position);
    this.icon = icon;
    this.text = text;
  }

  @Override
  public @NotNull CullVolume cullVolume() {
    return point(this.position());
  }

}
