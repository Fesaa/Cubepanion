package art.ameliah.laby.addons.cubepanion.core.cubesocket;

import art.ameliah.laby.addons.cubepanion.core.cubesocket.events.CubeSocketConnectEvent;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.events.CubeSocketDisconnectEvent;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.events.CubeSocketReloadRequest;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;
import net.labymod.api.notification.Notification;
import net.labymod.api.notification.Notification.Type;
import net.labymod.api.notification.NotificationController;

public class CubeSocketNotifications {

  private final CubeSocket socket;
  private final NotificationController notificationController;
  private final ResourceLocation resourceLocation = ResourceLocation.create("cubepanion",
      "sprites_64.png");

  public CubeSocketNotifications(CubeSocket socket, NotificationController notificationController) {
    this.socket = socket;
    this.notificationController = notificationController;
  }

  @Subscribe
  public void onReload(CubeSocketReloadRequest e) {
    notificationController.push(
        Notification.builder()
            .icon(Icon.sprite(resourceLocation, 0, 0, 64, 64, 256, 256))
            .title(Component.translatable("cubepanion.notifications.cubesocket.reload.title"))
            .text(Component.translatable("cubepanion.notifications.cubesocket.reload.text"))
            .type(Type.SYSTEM)
            .build()
    );
  }

  @Subscribe
  public void onCubeSocketConnect(CubeSocketConnectEvent e) {
    notificationController.push(
        Notification
            .builder()
            .icon(Icon.sprite(resourceLocation, 0, 0, 64, 64, 256, 256))
            .title(Component.translatable("cubepanion.notifications.cubesocket.connect.title"))
            .text(Component.translatable("cubepanion.notifications.cubesocket.connect.text"))
            .type(Type.SYSTEM)
            .build()
    );
  }

  @Subscribe
  public void onCubeSocketDisconnect(CubeSocketDisconnectEvent e) {
    notificationController.push(
        Notification
            .builder()
            .icon(Icon.sprite(resourceLocation, 1 << 6, 0, 64, 64, 256, 256))
            .title(Component.translatable("cubepanion.notifications.cubesocket.disconnect.title"))
            .text(Component.translatable(e.getReason()))
            .type(Type.SYSTEM)
            .build()
    );
  }
}
