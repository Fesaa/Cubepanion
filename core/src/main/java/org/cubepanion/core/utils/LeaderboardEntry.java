package org.cubepanion.core.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public record LeaderboardEntry(Leaderboard leaderboard, String name, int position, int score) {

  public JsonElement getAsJsonElement() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("game", this.leaderboard.getString());
    jsonObject.addProperty("player", this.name);
    jsonObject.addProperty("position", this.position);
    jsonObject.addProperty("score", this.score);
    return jsonObject;
  }
}
