package org.cubepanion.core.versionlinkers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.reference.annotation.Referenceable;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Request.Method;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.Leaderboard;
import org.cubepanion.core.utils.LeaderboardEntry;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Nullable
@Referenceable
public abstract class LeaderboardTrackerLink {

  protected Set<LeaderboardEntry> cachedEntries = new HashSet<>(200);
  protected Leaderboard currentLeaderboard;
  protected Set<Integer> recordedPageNumbers = new HashSet<>();
  protected int currentPageNumber;
  protected int maxPageNumber;

  private final HashMap<Leaderboard, Long> lastSubmit = new HashMap<>();

  public abstract void onScreenOpen();


  protected void submitToApi() {
    ClientPlayer player = Laby.labyAPI().minecraft().getClientPlayer();
    if (player == null) {
      return;
    }

    Long lastSubmit = this.lastSubmit.get(this.currentLeaderboard);
    if (lastSubmit == null) {
      lastSubmit = 0L;
    }

    long now = System.currentTimeMillis();
    if (now - lastSubmit < 1000*60*5) {
      return;
    }
    this.lastSubmit.put(this.currentLeaderboard, now);

    JsonObject main = new JsonObject();
    main.addProperty("uuid", player.getUniqueId().toString());
    main.addProperty("unix_time_stamp", System.currentTimeMillis());
    main.addProperty("game", this.currentLeaderboard.getString());

    JsonArray entries = new JsonArray(200);
    for (LeaderboardEntry entry : this.cachedEntries) {
      entries.add(entry.getAsJsonElement());
    }
    main.add("entries", entries);

    Request.ofString()
        .url("http://127.0.0.1:8080/leaderboard_api")
        .method(Method.POST)
        .json(main)
        .async()
        .execute(callBack -> {
          if (callBack.isPresent()) {
            int statusCode = callBack.getStatusCode();
            if (statusCode == 202) {
              Laby.labyAPI().minecraft().chatExecutor()
                  .displayClientMessage(
                      Component.translatable(
                          "cubepanion.messages.leaderboardAPI.success",
                          Component.text(this.currentLeaderboard.getString()))
                          .color(Colours.Success));
            } else {
              Laby.labyAPI().minecraft().chatExecutor()
                  .displayClientMessage(
                      Component.translatable(
                              "cubepanion.messages.leaderboardAPI.error",
                              Component.text(this.currentLeaderboard.getString()))
                          .color(Colours.Error));
            }
          }
          this.currentLeaderboard = Leaderboard.NONE;
          this.currentPageNumber = -1;
          this.maxPageNumber = -1;
          this.recordedPageNumbers.clear();
          this.cachedEntries.clear();
        });
  }

  public void addLeaderboardEntry(LeaderboardEntry entry) {
    this.cachedEntries.add(entry);
  }

  public Leaderboard titelStringToLeaderboard(String s) {
    String name = s
        .substring(2)
        .replace("Leaderboard", "")
        .trim();
    return Leaderboard.stringToLeaderboard(name);
  }



}
