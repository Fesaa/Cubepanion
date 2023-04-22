package org.cubepanion.core.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Request.Method;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.I18nNamespaces;
import org.cubepanion.core.utils.Leaderboard;
import java.util.Objects;
import java.util.stream.IntStream;

public class LeaderboardAPICommands extends Command {

  private long lastUsed = 0;

  private final String mainKey = I18nNamespaces.globalNamespace + ".messages.leaderboardAPI.commands.";

  private final Component APIError = Component.translatable(this.mainKey + "APIError").color(Colours.Error);
  private final Component invalidResponse = Component.translatable(this.mainKey + "invalidResponse").color(Colours.Error);
  private final Component noResponse = Component.translatable(I18nNamespaces.globalNamespace + ".messages.leaderboardAPI.noResponse").color(Colours.Error);;
  private final Component helpMessage = Component.translatable(this.mainKey + "help.title", Colours.Title)
      .append(Component.translatable(this.mainKey + "help.info", Colours.Secondary).decorate(TextDecoration.ITALIC))
      .append(Component.text("\n/leaderboardAPI <userName>", Colours.Primary))
      .append(Component.translatable(this.mainKey + "help.player", Colours.Secondary))
      .append(Component.text("\n/leaderboardAPI ", Colours.Primary))
      .append(Component.text("<game>", Colours.Primary)
          .hoverEvent(HoverEvent.showText(
              Component.text(String.join("",
                      IntStream.range(0, Leaderboard.values().length)
                              .mapToObj(i -> {
                                Leaderboard lb = Leaderboard.values()[i];
                                if (!lb.equals(Leaderboard.NONE)) {
                                  return lb.getString() + (i != Leaderboard.values().length - 1 ? "\n" : "");
                                }
                                return null;
                              })
                              .filter(Objects::nonNull)
                              .toList()),
                  Colours.Hover)
          )))
      .append(Component.text(" [start]", Colours.Primary))
      .append(Component.translatable(this.mainKey + "help.leaderboard", Colours.Secondary));

  public LeaderboardAPICommands(Cubepanion addon) {
    super("leaderboardAPI", "leaderboard", "lb");
    this.messagePrefix = addon.prefix();
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (arguments.length == 0) {
      this.displayMessage(this.helpMessage);
      return true;
    }

    long now = System.currentTimeMillis();
    if (now - this.lastUsed < 5000) {
      this.displayMessage(Component.translatable(this.mainKey + "coolDown", Component.text(5 - (now - this.lastUsed)/1000,
          NamedTextColor.DARK_RED)).color(Colours.Error));
      return true;
    }
    this.lastUsed = now;

    Leaderboard leaderboard = this.separateLeaderboardAndUserName(arguments);

    if (arguments.length == 1 && leaderboard.equals(Leaderboard.NONE)) { // User leaderboards
      String userName = arguments[0];
      if (!userName.matches("[a-zA-Z0-9_]{2,16}")) {
        this.displayMessage(Component.translatable(this.mainKey + "invalidUserName", Component.text(userName)).color(Colours.Error));
        return true;
      }
      Request.ofString()
          .url(Cubepanion.leaderboardAPI + "leaderboard_api/player/%s", userName)
          .method(Method.GET)
          .async()
          .execute(callBack -> {
            if (callBack.isPresent()) {
              if (callBack.getStatusCode() != 200) {
                this.displayMessage(this.APIError);
                return;
              }
              JsonArray leaderboards;
              try {
                leaderboards = JsonParser.parseString(callBack.get()).getAsJsonArray();
              } catch (JsonSyntaxException e) {
                this.displayMessage(this.invalidResponse);
                return;
              }

              if (leaderboards.size() == 0) {
                this.displayMessage(
                    Component.translatable(this.mainKey + "noLeaderboards",
                        Component.text(userName, Colours.Secondary).decorate(TextDecoration.BOLD))
                        .color(Colours.Primary));
                return;
              }

              Component toDisplay = Component.translatable(this.mainKey + "leaderboards.title",
                  Component.text(userName, Colours.Secondary).decorate(TextDecoration.BOLD),
                  Component.text(leaderboards.size(), Colours.Secondary))
                  .color(Colours.Primary);

              for (JsonElement element : leaderboards) {
                JsonObject info = element.getAsJsonObject();
                toDisplay = toDisplay.append(
                    Component.translatable(this.mainKey + "leaderboards.leaderboardInfo",
                        Component.text(info.get("game").getAsString()).color(Colours.Primary).decorate(TextDecoration.BOLD),
                        Component.text(info.get("position").getAsInt()).color(Colours.Secondary),
                            Component.text(info.get("score").getAsInt()).color(Colours.Secondary)
                    ).color(Colours.Success));
              }

              this.displayMessage(toDisplay);
            } else {
              this.displayMessage(this.noResponse);
            }
          });
      return true;
    }

    if (leaderboard.equals(Leaderboard.NONE)) {
      this.displayMessage(Component.translatable(this.mainKey + "invalidLeaderBoard",
          Component.text(String.join(" ", arguments)))
          .color(Colours.Error));
      return true;
    }

    String last = arguments[arguments.length - 1];
    int bound;
    try {
      bound = Integer.parseInt(last);
    } catch (NumberFormatException e) {
      bound = 1;
    }
    int bound_2 = Math.min(200, bound + 10);
    int finalBound = bound;
    Request.ofString()
        .url(Cubepanion.leaderboardAPI + "leaderboard_api/leaderboard/%s/bounded?lower=%d&upper=%d",
            leaderboard.getString().replace(" ", "%20"),
            bound,
            bound_2)
        .method(Method.GET)
        .async()
        .execute(callBack -> {
          if (callBack.isPresent()) {
            if (callBack.getStatusCode() != 200) {
              this.displayMessage(this.APIError);
              return;
            }
            JsonArray players;
            try {
              players = JsonParser.parseString(callBack.get()).getAsJsonArray();
            } catch (JsonSyntaxException e) {
              this.displayMessage(this.invalidResponse);
              return;
            }

            if (players.size() == 0) {
              this.displayMessage(
                  Component.translatable(this.mainKey + "noPlayers",
                      Component.text(leaderboard.getString(), Colours.Secondary).decorate(TextDecoration.BOLD),
                      Component.text(finalBound, Colours.Secondary),
                      Component.text(bound_2, Colours.Secondary))
                      .color(Colours.Primary));
              return;
            }

            Component toDisplay = Component.translatable(this.mainKey + "places.title",
                    Component.text(leaderboard.getString(), Colours.Secondary).decorate(TextDecoration.BOLD),
                    Component.text(finalBound, Colours.Secondary),
                    Component.text(bound_2, Colours.Secondary))
                .color(Colours.Primary);

            for (JsonElement element : players) {
              JsonObject info = element.getAsJsonObject();
              toDisplay = toDisplay.append(
                  Component.translatable(this.mainKey + "places.placeInfo",
                      Component.text(info.get("player").getAsString()).color(Colours.Primary).decorate(TextDecoration.BOLD),
                      Component.text(info.get("position").getAsInt()).color(Colours.Secondary),
                      Component.text(info.get("score").getAsInt()).color(Colours.Secondary)
                  ).color(Colours.Success));
            }
            this.displayMessage(toDisplay);
          } else {
            this.displayMessage(this.noResponse);
          }
        });
    return true;
  }

  private Leaderboard separateLeaderboardAndUserName(String[] arguments) {
    Leaderboard leaderboard = Leaderboard.NONE;
    String tryForLeaderboard = "";

    for (String s : arguments) {
      tryForLeaderboard = (tryForLeaderboard + " " + s).trim();
      leaderboard = Leaderboard.stringToLeaderboard(tryForLeaderboard);
      if (!leaderboard.equals(Leaderboard.NONE)) {
        return leaderboard;
      }

    }
    return leaderboard;
  }

}
