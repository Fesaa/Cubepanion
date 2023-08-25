package org.cubepanion.core.versionlinkers;

import static org.cubepanion.core.utils.Utils.handleResultError;

import art.ameliah.libs.weave.Result;
import art.ameliah.libs.weave.WeaveException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import art.ameliah.libs.weave.leaderboard.Leaderboard;
import art.ameliah.libs.weave.leaderboard.LeaderboardRow;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.ChatExecutor;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.reference.annotation.Referenceable;
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
  protected @Nullable Leaderboard currentLeaderboard;
  protected int currentPageNumber;
  protected int maxPageNumber;

  public abstract void onScreenOpen();

  protected void submitToApi() {
    if (this.currentLeaderboard == null) {
      return;
    }
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

    Result<Integer, WeaveException> result = Cubepanion.weave.getLeaderboardAPI()
        .submitLeaderboard(player.getUniqueId(), currentLeaderboard, cachedEntries);
    if (result.isOk()) {
      chat.displayClientMessage(
          Component.translatable("cubepanion.messages.leaderboardAPI.success",
                  Component.text(this.currentLeaderboard.displayName()))
              .color(Colours.Success));
    } else {
      handleResultError(getClass(), Cubepanion.get(), result.getError(),
          "Encountered an exception while getting getLeaderboardsForPlayer",
          I18nNamespaces.globalNamespace + ".messages.leaderboardAPI.commands.APIError_info",
          "cubepanion.messages.leaderboardAPI.error"
      );
    }
    this.currentLeaderboard = null;
    this.currentPageNumber = -1;
    this.maxPageNumber = -1;
    this.recordedPageNumbers.clear();
    this.cachedEntries.clear();
  }

  public void addLeaderboardEntry(LeaderboardRow entry) {
    this.cachedEntries.add(entry);
  }

  public @Nullable Leaderboard titelStringToLeaderboard(String s) {
    String name = s
        .substring(2)
        .replace("Leaderboard", "")
        .trim()
        .toLowerCase()
        .replace(" ", "_");

    Result<Leaderboard, WeaveException> res = Cubepanion.weave.getLeaderboardAPI().getLeaderboard(name);
    if (res.isErr()) {
      return null;
    }

    return res.getValue();
  }
}
