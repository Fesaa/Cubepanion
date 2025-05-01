package art.ameliah.laby.addons.cubepanion.core.versionlinkers;

import static art.ameliah.laby.addons.cubepanion.core.utils.Utils.handleAPIError;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.utils.Colours;
import art.ameliah.laby.addons.cubepanion.core.utils.I18nNamespaces;
import art.ameliah.laby.addons.cubepanion.core.weave.APIGame;
import art.ameliah.laby.addons.cubepanion.core.weave.GamesAPI;
import art.ameliah.laby.addons.cubepanion.core.weave.LeaderboardAPI;
import art.ameliah.laby.addons.cubepanion.core.weave.LeaderboardAPI.LeaderboardRow;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.ChatExecutor;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public abstract class LeaderboardTrackerLink {

  protected final Set<LeaderboardRow> cachedEntries = new HashSet<>(200);
  protected final Set<Integer> recordedPageNumbers = new HashSet<>();
  private final HashMap<APIGame, Long> lastSubmit = new HashMap<>();
  protected @Nullable APIGame currentLeaderboard;
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

    APIGame submittedFor = currentLeaderboard;
    LeaderboardAPI.getInstance()
        .submitLeaderboard(player.getUniqueId(), currentLeaderboard, cachedEntries)
        .whenComplete((integer, throwable) -> {
          if (throwable != null) {
            handleAPIError(getClass(), Cubepanion.get(), throwable,
                "Encountered an exception while getting getLeaderboardsForPlayer",
                I18nNamespaces.globalNamespace + ".messages.leaderboardAPI.commands.APIError_info",
                "cubepanion.messages.leaderboardAPI.error"
            );
          } else if (integer != null && integer == 202) {
            chat.displayClientMessage(
                Component.translatable("cubepanion.messages.leaderboardAPI.success",
                        Component.text(submittedFor.displayName()))
                    .color(Colours.Success));
          }
        });
    this.currentLeaderboard = null;
    this.currentPageNumber = -1;
    this.maxPageNumber = -1;
    this.recordedPageNumbers.clear();
    this.cachedEntries.clear();
  }

  public void addLeaderboardEntry(LeaderboardRow entry) {
    this.cachedEntries.add(entry);
  }

  public @Nullable APIGame titelStringToLeaderboard(String s) {
    String name = s
        .substring(2)
        .replace("Leaderboard", "")
        .trim()
        .toLowerCase();

    var game = GamesAPI.I().getGame(name);
    if (game != null) {
      return game;
    }

    name = name.replace(" ", "_");
    return GamesAPI.I().getGame(name);
  }
}
