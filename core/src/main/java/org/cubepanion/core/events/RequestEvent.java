package org.cubepanion.core.events;

import net.labymod.api.event.Event;

/**
 * Internal event used for requested things in other classes
 */
public class RequestEvent implements Event {

  private final RequestType type;

  public RequestEvent(RequestType type) {
    this.type = type;
  }

  public RequestType getType() {
    return type;
  }


  public enum RequestType {
    FULL_FRIEND_LIST,
    RANK_TAG,
    UPDATE_RPC,
  }
}
