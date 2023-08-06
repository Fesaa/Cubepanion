package org.cubepanion.core.managers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Request.Method;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.utils.ChestLocation;
import org.cubepanion.core.utils.LOGGER;
import org.jetbrains.annotations.Nullable;

public class CubepanionAPIManager {

  public final static List<ChestLocation> chestLocations = new ArrayList<>();
  private static String baseURL;

  public static void init() {
    CubepanionAPIManager.baseURL = Cubepanion.leaderboardAPI;
  }

  public static void updateChestLocations() {
    updateChestLocations(null);
  }

  public static void updateChestLocations(@Nullable String season) {
    LOGGER.info(CubepanionAPIManager.class, "Handling updateChestLocations request");
    String url = baseURL + "chest_api/" + (season == null ? "current" : "season/" + season);
    LOGGER.info(CubepanionAPIManager.class, url);
    Request.ofString()
        .url(url)
        .method(Method.GET)
        .async()
        .execute(callBack -> {
          if (callBack.isPresent()) {
            if (callBack.getStatusCode() != 200) {
              LOGGER.info(CubepanionAPIManager.class,
                  "Status code wasn't 200, was: " + callBack.getStatusCode());
              return;
            }
            JsonArray json;
            try {
              json = JsonParser.parseString(callBack.get()).getAsJsonArray();
            } catch (JsonSyntaxException e) {
              LOGGER.info(CubepanionAPIManager.class, "Could not parse json");
              return;
            }

            chestLocations.clear();
            for (JsonElement el : json) {
              JsonObject chestLocation = el.getAsJsonObject();
              LOGGER.info(CubepanionAPIManager.class, "Loc from request" + chestLocation);
              chestLocations.add(new ChestLocation(
                  chestLocation.get("x").getAsInt(),
                  chestLocation.get("y").getAsInt(),
                  chestLocation.get("z").getAsInt()));
            }
            LOGGER.info(CubepanionAPIManager.class, "Added chest locations");
          } else {
            LOGGER.info(CubepanionAPIManager.class, "Callback was not present");
          }
        });
  }

}
