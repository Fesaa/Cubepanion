package art.ameliah.laby.addons.cubepanion.core.render.waypoint;

import static net.labymod.api.laby3d.renderer.snapshot.Extras.empty;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.world.object.snapshot.AbstractWorldObjectSnapshot;
import org.jetbrains.annotations.Nullable;

public class WaypointObjectSnapshot extends AbstractWorldObjectSnapshot {

  private final Icon icon;
  private final Component text;

  private final float cameraYaw;
  private final float cameraPitch;
  private final double distSq;

  public WaypointObjectSnapshot(
      double x, double y, double z,
      int lightCoords,
      @Nullable Icon icon,
      @Nullable Component text,
      float cameraYaw,
      float cameraPitch,
      double distSq
  ) {
    super(x, y, z, lightCoords, empty());
    this.icon = icon;
    this.text = text;
    this.cameraYaw = cameraYaw;
    this.cameraPitch = cameraPitch;
    this.distSq = distSq;
  }

  public @Nullable Icon icon() {
    return this.icon;
  }

  public @Nullable Component text() {
    return this.text;
  }

  public float cameraYaw() {
    return this.cameraYaw;
  }

  public float cameraPitch() {
    return this.cameraPitch;
  }

  public double distSq() {
    return this.distSq;
  }

}
