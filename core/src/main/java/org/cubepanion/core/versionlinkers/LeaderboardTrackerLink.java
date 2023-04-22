package org.cubepanion.core.versionlinkers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.ChatExecutor;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.reference.annotation.Referenceable;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Request.Method;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.I18nNamespaces;
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

  private final Component noResponse = Component.translatable(I18nNamespaces.globalNamespace + ".messages.leaderboardAPI.noResponse").color(Colours.Error);
  private final Component APISubmitError = Component.translatable("cubepanion.messages.leaderboardAPI.error").color(Colours.Error);

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
        .url(Cubepanion.leaderboardAPI + "leaderboard_api")
        .method(Method.POST)
        .json(main)
        .async()
        .execute(callBack -> {
          ChatExecutor chat = Laby.labyAPI().minecraft().chatExecutor();
          if (callBack.isPresent()) {
            int statusCode = callBack.getStatusCode();
            if (statusCode == 202) {
              chat.displayClientMessage(
                      Component.translatable(
                          "cubepanion.messages.leaderboardAPI.success",
                          Component.text(this.currentLeaderboard.getString()))
                          .color(Colours.Success));
            } else {
              if (Cubepanion.get().configuration().getLeaderboardAPIConfig().getErrorInfo().get()) {
                chat.displayClientMessage(
                    Component.translatable(
                        I18nNamespaces.globalNamespace + ".messages.leaderboardAPI.commands.APIError_info",
                            Component.text(callBack.get()))
                        .color(Colours.Error));
              } else {
                chat.displayClientMessage(this.APISubmitError);
              }
            }
          }  else {
            chat.displayClientMessage(this.noResponse);
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
