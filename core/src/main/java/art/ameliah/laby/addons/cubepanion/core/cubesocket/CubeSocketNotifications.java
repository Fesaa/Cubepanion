package art.ameliah.laby.addons.cubepanion.core.cubesocket;

import art.ameliah.laby.addons.cubepanion.core.cubesocket.events.CubeSocketConnectEvent;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.events.CubeSocketDisconnectEvent;
import net.labymod.api.client.component.Component;
import net.labymod.api.event.Subscribe;
import net.labymod.api.notification.Notification;
import net.labymod.api.notification.Notification.Type;
import net.labymod.api.notification.NotificationController;

public class CubeSocketNotifications {

  private final CubeSocket socket;
  private final NotificationController notificationController;

  public CubeSocketNotifications(CubeSocket socket, NotificationController notificationController) {
    this.socket = socket;
    this.notificationController = notificationController;
  }

  @Subscribe
  public void onCubeSocketConnect(CubeSocketConnectEvent e) {
    notificationController.push(
        Notification
            .builder()
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
            .title(Component.translatable("cubepanion.notifications.cubesocket.disconnect.title"))
            .text(Component.translatable("cubepanion.notifications.cubesocket.disconnect.text"))
            .type(Type.SYSTEM)
            .build()
    );
  }
}
