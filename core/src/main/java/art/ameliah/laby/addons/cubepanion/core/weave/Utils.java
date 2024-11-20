package art.ameliah.laby.addons.cubepanion.core.weave;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;
import net.labymod.api.util.io.web.request.Request;

class Utils {

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

  static <T> CompletableFuture<T> makeRequest(String url, Type type) {
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
            future.complete((new Gson()).fromJson(c.get(), type));
          } catch (JsonSyntaxException exp) {
            future.completeExceptionally(exp);
          }
        });

    return future;
  }
}