package org.cubepanion.core.versionlinkers;

import art.ameliah.libs.weave.LeaderboardAPI.Leaderboard;
import art.ameliah.libs.weave.LeaderboardAPI.LeaderboardRow;
import art.ameliah.libs.weave.WeaveException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.ChatExecutor;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.reference.annotation.Referenceable;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Request.Method;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.I18nNamespaces;
import org.cubepanion.core.utils.LOGGER;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public abstract class LeaderboardTrackerLink {

  protected final Set<LeaderboardRow> cachedEntries = new HashSet<>(200);
  protected final Set<Integer> recordedPageNumbers = new HashSet<>();
  private final HashMap<Leaderboard, Long> lastSubmit = new HashMap<>();
  private final Component APISubmitError = Component.translatable(
      "cubepanion.messages.leaderboardAPI.error").color(Colours.Error);
  protected Leaderboard currentLeaderboard;
  protected int currentPageNumber;
  protected int maxPageNumber;

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
    if (now - lastSubmit < 1000 * 60 * 5) {
      Laby.labyAPI().minecraft().chatExecutor().displayClientMessage(
          Component.translatable("cubepanion.messages.leaderboardAPI.coolDown",
                  Component.text(5 - (now - lastSubmit) / 60000, NamedTextColor.DARK_RED))
              .color(Colours.Error),
          true
      );
      return;
    }
    this.lastSubmit.put(this.currentLeaderboard, now);

    ChatExecutor chat = Laby.labyAPI().minecraft().chatExecutor();
    try {
      Cubepanion.weave.getLeaderboardAPI().submitLeaderboard(player.getUniqueId(), currentLeaderboard, cachedEntries);
      chat.displayClientMessage(
          Component.translatable(
                  "cubepanion.messages.leaderboardAPI.success",
                  Component.text(this.currentLeaderboard.getString()))
              .color(Colours.Success));
    } catch (WeaveException e) {
      if (Cubepanion.get().configuration().getDebug().get()) {
        LOGGER.info(getClass(), e,
            "Encountered an exception while getting getLeaderboardsForPlayer");
      }
      if (Cubepanion.get().configuration().getLeaderboardAPIConfig().getErrorInfo().get()) {
        chat.displayClientMessage(
            Component.translatable(
                    I18nNamespaces.globalNamespace
                        + ".messages.leaderboardAPI.commands.APIError_info",
                    Component.text(e.getMessage()))
                .color(Colours.Error));
      } else {
        chat.displayClientMessage(this.APISubmitError);
      }
    }
    this.currentLeaderboard = Leaderboard.NONE;
    this.currentPageNumber = -1;
    this.maxPageNumber = -1;
    this.recordedPageNumbers.clear();
    this.cachedEntries.clear();
  }

  public void addLeaderboardEntry(LeaderboardRow entry) {
    this.cachedEntries.add(entry);
  }

  public Leaderboard titelStringToLeaderboard(String s) {
    String name = s
        .substring(2)
        .replace("Leaderboard", "")
        .trim();
    try {
      return Leaderboard.stringToLeaderboard(name);
    } catch (WeaveException e) {
      return Leaderboard.NONE;
    }
  }
}
