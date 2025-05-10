package art.ameliah.laby.addons.cubepanion.core.versionlinkers;

import art.ameliah.laby.addons.cubepanion.core.external.Game;
import art.ameliah.laby.addons.cubepanion.core.external.LeaderboardRow;
import art.ameliah.laby.addons.cubepanion.core.utils.Colours;
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
  private final HashMap<Game, Long> lastSubmit = new HashMap<>();
  protected @Nullable Game currentLeaderboard;
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


    this.currentLeaderboard = null;
    this.currentPageNumber = -1;
    this.maxPageNumber = -1;
    this.recordedPageNumbers.clear();
    this.cachedEntries.clear();
  }

  public void addLeaderboardEntry(LeaderboardRow entry) {
    this.cachedEntries.add(entry);
  }


}
