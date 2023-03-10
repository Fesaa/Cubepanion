package org.cubecraftutilities.core.config.submanagers;

import net.labymod.api.Laby;
import org.cubecraftutilities.core.utils.imp.OnlineFriendLocation;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FriendTrackerManager {

  private final Set<String> tracking;
  private final HashMap<String, OnlineFriendLocation> friendLocations;

  private boolean isUpdating = false;
  private boolean running = false;
  private int updateInterVal = 60;

  public FriendTrackerManager() {
    this.tracking = new HashSet<>();
    this.friendLocations = new HashMap<>();
  }

  public HashMap<String, OnlineFriendLocation> getFriendLocations() {
    return this.friendLocations;
  }

  public Set<String> getTracking() {
    return this.tracking;
  }

  public boolean isTracking(String username) {
    return this.tracking.contains(username);
  }

  public void addTracking(String username) {
    this.tracking.add(username);
  }

  public void unTrack(String username) {
    this.tracking.remove(username);
    this.friendLocations.remove(username);
  }

  public @Nullable OnlineFriendLocation getFriendLocation(String username) {
    return this.friendLocations.getOrDefault(username, null);
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

  public void beginLoop() {
    this.running = true;
    this.loop();
  }

  public void setRunning(boolean running) {
    this.running = running;
  }

  public void setUpdateInterVal(int i) {
    this.updateInterVal = i;
  }

  private void loop() {
    this.isUpdating = true;
    Laby.labyAPI().minecraft().chatExecutor().chat("/fl", false);
    if (this.running) {
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
