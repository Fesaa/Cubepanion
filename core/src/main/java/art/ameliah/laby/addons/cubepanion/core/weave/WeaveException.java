package art.ameliah.laby.addons.cubepanion.core.weave;

import java.util.Map;
import net.labymod.api.util.io.web.request.Response;
import org.jetbrains.annotations.Nullable;


/**
 * Weave specific exception, any Exception emitted by Weave after construction will be of this type
 */
public class WeaveException extends Exception {

  /**
   * Artificial exception
   *
   * @param msg extra info
   */
  WeaveException(String msg) {
    super(msg);
  }

  /**
   * Artificial wrapper exception
   *
   * @param msg   extra info
   * @param cause original exception
   */
  WeaveException(String msg, Throwable cause) {
    super(msg, cause);
  }

  /**
   * Wrapper exception
   *
   * @param cause original exception
   */
  WeaveException(Throwable cause) {
    super(cause);
  }

  static @Nullable WeaveException fromResponse(Response<String> response, int wantedStatus,
      boolean needsBody) {
    if (response.hasException()) {
      return new WeaveException(response.exception());
    }

    if (response.isEmpty()) {
      return new WeaveException("Failed to get a response");
    }

    Map<String, String> headers = response.getHeaders();
    String rate = headers.get("retry-after");
    if (rate != null) {
      return new RateLimited(rate);
    }

    if (response.getStatusCode() != wantedStatus) {
      String msg = String.format("Got %d as status code, wanted %d.\n%s",
          response.getStatusCode(), wantedStatus,
          response.isPresent() ? response.get() : "Unknown.");
      return new WeaveException(msg);
    }

    if (needsBody && response.isEmpty()) {
      return new WeaveException("Wanted a response body, but did not get one");
    }

    return null;
  }

  /**
   * Special WeaveException for Rate Limits
   */
  public static class RateLimited extends WeaveException {

    /**
     * RateLimited constructor
     *
     * @param seconds time out (seconds as this is returned)
     */
    public RateLimited(String seconds) {
      super(String.format("You have been rate limited, try again in %s seconds.", seconds));
    }
  }

}
