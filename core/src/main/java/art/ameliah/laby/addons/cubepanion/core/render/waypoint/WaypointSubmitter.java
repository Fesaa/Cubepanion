package art.ameliah.laby.addons.cubepanion.core.render.waypoint;

import static java.lang.Math.clamp;
import static java.lang.Math.sqrt;
import static net.labymod.api.client.gfx.pipeline.renderer.text.FontFlags.DISPLAY_MODE_SEE_THROUGH;
import static net.labymod.api.client.gfx.pipeline.renderer.text.FontFlags.SHADOW;
import static net.labymod.api.laby3d.render.queue.submissions.IconSubmission.DisplayMode.SEE_THROUGH;

import net.labymod.api.Laby;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.render.state.world.CameraSnapshot;
import net.labymod.api.client.world.object.submit.WorldObjectSubmitter;
import net.labymod.api.laby3d.render.queue.SubmissionCollector;
import org.jetbrains.annotations.NotNull;

public class WaypointSubmitter extends WorldObjectSubmitter<WaypointObject, WaypointObjectSnapshot> {

  private static final float SCALE_BASE = .03f;
  private static final float SCALE_MAX = .25f;
  private static final float SCALE_MODIFIER = .0035f;

  private static final float ICON_SIZE = 16f;
  private static final float ICON_Y_MOD = -18f;

  @Override
  public @NotNull WaypointObjectSnapshot createSnapshot(
      @NotNull WaypointObject object,
      double x, double y, double z,
      int lightCoords,
      @NotNull CameraSnapshot camera
  ) {
    return new WaypointObjectSnapshot(
        x, y, z,
        lightCoords,
        object.icon,
        object.text,
        camera.getYaw(),
        camera.getPitch(),
        sqrt(x * x + y * y + z * z)
    );
  }

  @Override
  public void submit(
      @NotNull Stack stack,
      @NotNull SubmissionCollector collector,
      @NotNull WaypointObjectSnapshot snapshot
  ) {

    stack.push();

    stack.translate(snapshot.x(), snapshot.y(), snapshot.z());

    stack.rotate(snapshot.cameraYaw(), 0, -1, 0);
    stack.rotate(snapshot.cameraPitch(), 1, 0, 0);

    final float scale = (float) clamp(snapshot.distSq() * SCALE_MODIFIER, SCALE_BASE, SCALE_MAX);

    stack.scale(-scale, -scale, scale);

    if (snapshot.icon() != null) {
      final float iconX = -ICON_SIZE / 2f;
      final float iconY = (snapshot.text() != null) ? ICON_Y_MOD : 0;
      collector.submitIcon(
          stack,
          snapshot.icon(),
          SEE_THROUGH,
          iconX,
          iconY,
          ICON_SIZE,
          ICON_SIZE,
          0xFFFFFFFF
      );
    }

    if (snapshot.text() != null) {
      final float textX = -(Laby.references().componentRenderer().width(snapshot.text()) / 2);
      collector.submitComponent(
          stack,
          snapshot.text(),
          textX, 0f,
          0xFFFFFFFF,
          snapshot.lightCoords(),
          0x00000000,
          SHADOW | DISPLAY_MODE_SEE_THROUGH
      );
    }

    stack.pop();

  }

  public static void registerWaypointSubmitter() {
    Laby.references().worldObjectDispatcher()
        .registerSubmitter(WaypointObject.class, new WaypointSubmitter());
  }

}
