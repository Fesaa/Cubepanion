package org.cubepanion.core.managers.submanagers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import net.labymod.api.Laby;
import net.labymod.api.util.concurrent.task.Task;
import org.cubepanion.core.utils.OnlineFriendLocation;

public class FriendTrackerManager {

  private final Set<String> tracking  = new HashSet<>();
  private final HashMap<String, OnlineFriendLocation> friendLocations  = new HashMap<>();
  private final List<Integer> runningLoops = new ArrayList<>();

  private boolean isUpdating = false;
  private int currentLoop;
  private int updateInterVal = 30;

  private Task friendTrackerLoopTask = Task.builder(() -> {
    if (this.runningLoops.contains(this.currentLoop)) {
      this.isUpdating = true;
      Laby.labyAPI().minecraft().chatExecutor().chat("/fl", false);
      this.friendTrackerLoopTask.execute();
    }
  }).delay(this.updateInterVal, TimeUnit.SECONDS).build();

  public FriendTrackerManager() {
  }

  private void updateFriendTrackerLoopTask() {
    this.friendTrackerLoopTask = Task.builder(() -> {
      if (this.runningLoops.contains(this.currentLoop)) {
        this.isUpdating = true;
        Laby.labyAPI().minecraft().chatExecutor().chat("/fl", false);
        this.friendTrackerLoopTask.execute();
      }
    }).delay(this.updateInterVal, TimeUnit.SECONDS).build();
  }

  public HashMap<String, OnlineFriendLocation> getFriendLocations() {
    return friendLocations;
  }

  public Collection<OnlineFriendLocation> getOnlineFriendLocations() {
    return this.friendLocations.values();
  }

  public int getUpdateInterVal() {
    return updateInterVal;
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
    this.endCurrentLoop();
    this.runningLoops.clear();
  }

  public void updateFriendLocation(OnlineFriendLocation friendLocation) {
    if (!this.isTracking(friendLocation.username())) {
      return;
    }

    OnlineFriendLocation currentFriendLocation = this.friendLocations.get(friendLocation.username());
    this.friendLocations.put(friendLocation.username(), friendLocation);
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
    this.friendTrackerLoopTask.execute();
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
    this.updateFriendTrackerLoopTask();
  }

  public void forceUpdate() {
    this.isUpdating = true;
    Laby.labyAPI().minecraft().chatExecutor().chat("/fl", false);
  }
}
