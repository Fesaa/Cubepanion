package org.cubecraftutilities.core.managers.submanagers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import net.labymod.api.Laby;
import org.cubecraftutilities.core.utils.eggwarsmaps.OnlineFriendLocation;

public class FriendTrackerManager {

  private final Set<String> tracking;
  private final HashMap<String, OnlineFriendLocation> friendLocations;
  private final List<Integer> runningLoops;

  private boolean isUpdating = false;
  private int currentLoop;
  private int updateInterVal = 30;

  public FriendTrackerManager() {
    this.tracking = new HashSet<>();
    this.friendLocations = new HashMap<>();
    this.runningLoops = new ArrayList<>();
  }

  public Set<String> getTracking() {
    return this.tracking;
  }

  public boolean isTracking(String username) {
    return this.tracking.contains(username);
  }

  public void addTracking(String username) {
    this.tracking.add(username);

    if (this.runningLoops.size() == 0) {
      this.beginLoop((int) Instant.now().getEpochSecond());
    }

  }

  public void unTrack(String username) {
    this.tracking.remove(username);
    this.friendLocations.remove(username);

    if (this.tracking.size() == 0) {
      this.endCurrentLoop();
    }
  }

  public void resetTrackers() {
    this.tracking.clear();
    this.friendLocations.clear();
  }

  public void updateFriendLocation(OnlineFriendLocation friendLocation) {
    if (!this.isTracking(friendLocation.getUsername())) {
      return;
    }

    OnlineFriendLocation currentFriendLocation = this.friendLocations.get(friendLocation.getUsername());
    this.friendLocations.put(friendLocation.getUsername(), friendLocation);
    if (currentFriendLocation == null) {
      return;
    }
    currentFriendLocation.broadcastMove(friendLocation);
  }

  public boolean isUpdating() {
    return isUpdating;
  }

  public void setUpdating(boolean updating) {
    isUpdating = updating;
  }

  public void beginLoop(int loopID) {
    this.endCurrentLoop();

    this.currentLoop = loopID;
    this.runningLoops.add(this.currentLoop);
    Executors.newScheduledThreadPool(
            Runtime.getRuntime().availableProcessors())
        .schedule(this::loop,5, TimeUnit.SECONDS);
  }

  public void endCurrentLoop() {
    for (int i = 0; i < this.runningLoops.size(); i ++) {
      if (this.runningLoops.get(i) == this.currentLoop) {
        this.runningLoops.remove(i);
        return;
      }
    }
  }


  public void setUpdateInterVal(int i) {
    this.updateInterVal = i;
  }

  private void loop() {
    if (this.runningLoops.contains(this.currentLoop)) {
      this.isUpdating = true;
      Laby.labyAPI().minecraft().chatExecutor().chat("/fl", false);
      Executors.newScheduledThreadPool(
          Runtime.getRuntime().availableProcessors())
          .schedule(this::loop,this.updateInterVal, TimeUnit.SECONDS);
    }
  }

  public void forceUpdate() {
    this.isUpdating = true;
    Laby.labyAPI().minecraft().chatExecutor().chat("/fl", false);
  }
}
