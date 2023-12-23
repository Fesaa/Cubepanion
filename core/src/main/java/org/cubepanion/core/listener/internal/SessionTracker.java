package org.cubepanion.core.listener.internal;

import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.session.SessionUpdateEvent;
import org.cubepanion.core.utils.LOGGER;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public class SessionTracker {

  private static SessionTracker instance;

  public static SessionTracker get() {
    if (instance == null) {
      throw new IllegalStateException("SessionTracker has not been initialized yet!");
    }
    return instance;
  }

  public SessionTracker() {
    instance = this;


  }

  private UUID uuid;
  private String username;

  public @NotNull UUID uuid() {
    if (uuid == null) {
      query();
    }
    return uuid;
  }

  public @NotNull String username() {
    if (username == null) {
      query();
    }
    return username;
  }

  @Subscribe
  public void onSessionUpdate(SessionUpdateEvent e) {
    uuid = e.newSession().getUniqueId();
    username = e.newSession().getUsername();
  }

  private void query() {
    ClientPlayer p = Laby.labyAPI().minecraft().getClientPlayer();
    if (p == null) {
      LOGGER.info(getClass(),"FUCK");
      return;
    }

    username = p.getName();
    uuid = p.getUniqueId();
  }

}
