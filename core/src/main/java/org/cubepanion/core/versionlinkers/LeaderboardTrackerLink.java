package org.cubepanion.core.versionlinkers;

import net.labymod.api.reference.annotation.Referenceable;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.utils.Leaderboard;
import org.cubepanion.core.utils.LeaderboardEntry;
import org.jetbrains.annotations.Nullable;
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

  public abstract void onScreenOpen();


  protected boolean submitToApi() {
    Cubepanion.get().logger().info(this.cachedEntries.toString());
    Cubepanion.get().logger().info("Submitted " + this.cachedEntries.size() + " entries to the LeaderboardAPI.");
    this.cachedEntries.clear();

    /*
    Make API request
     */

    return true;
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
