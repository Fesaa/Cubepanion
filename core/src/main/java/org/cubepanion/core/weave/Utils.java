package org.cubepanion.core.weave;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.util.concurrent.CompletableFuture;
import net.labymod.api.client.component.Component;
import net.labymod.api.notification.Notification;
import net.labymod.api.notification.Notification.Builder;
import net.labymod.api.notification.Notification.Type;
import net.labymod.api.util.io.web.request.Request;

class Utils {

  static void toast(String key) {
    Builder builder = Notification.builder();
    builder.title(Component.translatable("cubepanion.notifications.api.title"));
    builder.text(Component.translatable("cubepanion.notifications.api." + key));
    builder.duration(2000);
    builder.type(Type.SYSTEM);
    builder.buildAndPush();
  }

  static <T> CompletableFuture<T> makeRequest(String url, Class<T> clazz) {
    CompletableFuture<T> future = new CompletableFuture<>();

    Request.ofString()
        .url(url)
        .async()
        .execute(c -> {
          WeaveException e = WeaveException.fromResponse(c, 200, true);
          if (e != null) {
            throw new RuntimeException(e);
          }
          try {
            future.complete((new Gson()).fromJson(c.get(), clazz));
          } catch (JsonSyntaxException exp) {
            future.completeExceptionally(exp);
          }
        });

    return future;
  }
}
