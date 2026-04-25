package art.ameliah.laby.addons.cubepanion.core.listener.misc;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.config.QOLConfig;
import art.ameliah.laby.addons.cubepanion.core.utils.Colours;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.ChestFinderLink;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.render.font.text.TextDrawMode;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.event.client.render.world.RenderWorldEvent;
import net.labymod.api.laby3d.pipeline.RenderStates;
import net.labymod.api.util.concurrent.task.Task;
import org.jetbrains.annotations.NotNull;

public class ChestFinder {

  private static final Component CHEST_HOLOGRAM =
      Component.translatable("cubepanion.hologram.chest_finder", Colours.Title);

  private static final ResourceLocation CHEST_ITEM_TEXTURE =
      ResourceLocation.create("minecraft", "textures/item/compass_00.png");

  private static final Icon CHEST_ITEM_ICON =
      Icon.texture(CHEST_ITEM_TEXTURE);

  private final Task task;
  private final QOLConfig config;

  public ChestFinder(Cubepanion addon, @NotNull ChestFinderLink link) {
    config = addon.configuration().getQolConfig();

    task = Task.builder(() -> {
      if (addon.getManager().getDivision().equals(CubeGame.LOBBY)) {
        link.displayLocations();
      }
    }).delay(2000, TimeUnit.MILLISECONDS).build();
  }

  @Subscribe
  public void onChatMessage(ChatReceiveEvent e) {

    if (!config.getChestLocation().get()) {
      return;
    }

    final var message = e.chatMessage().getPlainText();
    final String foundChestPart = "found the Hidden Chest!";
    final String chestMessage = "A chest has been hidden somewhere in the Lobby with some goodies inside!";

    if (message.equalsIgnoreCase(chestMessage)) {
      task.execute();
    } else if (message.contains(foundChestPart) && !message.contains(":")) {
      Cubepanion.get().getChestFinderLink()
          .getLocations()
          .clear();
    }

  }

  @Subscribe
  public void onRenderWorld(RenderWorldEvent e) {

    if (e.phase() != Phase.POST) {
      return;
    }

    var link = Cubepanion.get().getChestFinderLink();
    var chests = List.copyOf(link.getLocations());

    if (chests.isEmpty()) {
      return;
    }

    for (var chest : chests) {

      var stack = e.stack();
      var camera = e.camera();

      var dx = chest.x() - camera.position().getX() + .5f;
      var dy = chest.y() - camera.position().getY() + 1.5f;
      var dz = chest.z() - camera.position().getZ() + .5f;

      var distSq = Math.sqrt(dx * dx + dy * dy + dz * dz);

      stack.push();
      stack.translate(dx, dy, dz);

      stack.rotate(camera.getYaw(), 0, -1, 0);
      stack.rotate(camera.getPitch(), 1, 0, 0);

      final var maxScale = .25f;
      final var baseScale = .03f;
      final var scale =
          (float) Math.max(baseScale, Math.min(maxScale, distSq * .002));

      stack.scale(-scale, -scale, scale);

      final var iconSize = 16f;
      final var iconX = -iconSize / 2f;
      final var iconY = -18f;

      CHEST_ITEM_ICON.render(
          RenderStates.SEE_THROUGH_NAMETAG_ICON,
          stack,
          iconX,
          iconY,
          iconSize,
          iconSize
      );

      Laby.references().componentRenderer().builder()
          .text(CHEST_HOLOGRAM)
          .centered(true)
          .shadow(true)
          .drawMode(TextDrawMode.SEE_THROUGH)
          .render(stack);

      stack.pop();

    }

  }

}
