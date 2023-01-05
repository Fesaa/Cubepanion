package org.ccu.core.listener;

import com.google.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.entity.player.GameMode;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import org.ccu.core.CCU;
import org.ccu.core.config.imp.GameStatsTracker;
import org.ccu.core.config.internal.CCUinternalConfig;
import org.ccu.core.config.subconfig.EndGameSubConfig;
import org.ccu.core.config.subconfig.StatsTrackerSubConfig;
import org.ccu.core.utils.EggWarsMapInfo;

public class ChatReceiveEventListener {

  private final CCU addon;
  private final Pattern playerElimination = Pattern.compile("([a-zA-Z0-9_]{2,16}) has been eliminated from the game.");
  private final Pattern mightBeKillMessage = Pattern.compile(this.userNameRegex + ".{1,100}" + this.userNameRegex + ".{1,100}" + this.assistRegex);
  private final String userNameRegex = "([a-zA-Z0-9_]{2,16})";
  private final String assistRegex = "(\\s{0,5}\\(\\+\\d{1,2} assists?\\))?";
  private final HashMap<Pattern, Integer> customKillMessages = new HashMap<>();
  private final HashMap<Pattern, Integer> defaultKillMessages = new HashMap<>();

  @Inject
  public ChatReceiveEventListener(CCU addon) {
    this.addon = addon;

    // A B C D E F G H I J K L M N O P Q R S T U V W X Y Z

    //Default kill messages
    this.defaultKillMessages.put(Pattern.compile(this.userNameRegex + " died in the void while escaping " + this.userNameRegex + "\\." + this.assistRegex), 2);
    this.defaultKillMessages.put(Pattern.compile(this.userNameRegex + " was slain by " + this.userNameRegex + "\\." + this.assistRegex), 2);
    this.defaultKillMessages.put(Pattern.compile(this.userNameRegex + " thought they could survive in the void while escaping " + this.userNameRegex + "\\." + this.assistRegex), 2);
    this.defaultKillMessages.put(Pattern.compile(this.userNameRegex + " kicked " + this.userNameRegex + " into the void\\." + this.assistRegex), 1);
    this.defaultKillMessages.put(Pattern.compile(this.userNameRegex + " couldn't fly while escaping " + this.userNameRegex + "\\." + this.assistRegex), 2);
    this.defaultKillMessages.put(Pattern.compile(this.userNameRegex + " tried to escape " + this.userNameRegex + " by jumping into the void\\." + this.assistRegex), 2);


    // Custom Kill messages
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " \"accidentally\" tripped " + this.userNameRegex + " into the void\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " \"accidentally\" made " + this.userNameRegex + " fall into the void\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex+  " and " + this.userNameRegex + " settled their differences and became friends... Just kidding, " + this.userNameRegex + " killed " + this.userNameRegex + "\\." + this.assistRegex), 3);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " assisted " + this.userNameRegex + " with their void-traume\\." + this.assistRegex), 1);

    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " betrayed " + this.userNameRegex + "\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " bought " + this.userNameRegex + " a gift\\.{3} Oh wait, it's just death\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " brawled " + this.userNameRegex + " to death\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " burnt to a crisp while fighting " + this.userNameRegex + "\\." + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " burned to death while fighting " + this.userNameRegex + "\\." + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " blew " + this.userNameRegex + " into the void\\." + this.assistRegex), 1);

    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " destroyed " + this.userNameRegex + " into oblivion\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " diagnosed " + this.userNameRegex + " with death\\." + this.assistRegex), 1);

    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " flabbergasted " + this.userNameRegex + " with death\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " fell to their death while escpaing " + this.userNameRegex + "\\." + this.assistRegex), 2);

    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " gave " + this.userNameRegex + " the L!" + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " got a face lift by " + this.userNameRegex + "\\." + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " got sent into the void by " + this.userNameRegex + "\\." + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " got snapped away by " + this.userNameRegex + "\\." + this.assistRegex), 2);

    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " had to register their hands as deadly weapons after what they did to " + this.userNameRegex + "\\.{3}" + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " has been sent to the after life by " + this.userNameRegex + "\\." + this.assistRegex), 2);

    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " kicked " + this.userNameRegex + " into the abyss\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " killed " + this.userNameRegex + "\\. LOL!" + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " killed " + this.userNameRegex + "\\. RIP!" + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " killed " + this.userNameRegex + "\\. Now now\\.{3} don't be salty!" + this.assistRegex), 1);

    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " made " + this.userNameRegex + " jump into the void\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " made " + this.userNameRegex + " disappear\\." + this.assistRegex), 1);

    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " OwO'd " + this.userNameRegex + " to death!" + this.assistRegex), 1);

    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " pushed " + this.userNameRegex + " into the void\\." + this.assistRegex), 1);

    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " scared " + this.userNameRegex + " into the void\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " screamed \"This is Sparta!\" before kicking " + this.userNameRegex + " into the void\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " sent " + this.userNameRegex + " into a deep freeze\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " sent " + this.userNameRegex + " into a deep sleep!" + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " sent " + this.userNameRegex + " into the upside down\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " sent " + this.userNameRegex + " to the underworld\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " showcased their weapon to " + this.userNameRegex + "\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " showed " + this.userNameRegex + " their karate skills!" + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " slapped " + this.userNameRegex + " to death!" + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " shoved " + this.userNameRegex + " into the void\\." + this.assistRegex), 1);

    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " tried being a ninja against " + this.userNameRegex + " but it didn't turn out so well\\.{3}" + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " tripped " + this.userNameRegex + " into the void\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " tucked " + this.userNameRegex + " in for a dirt nap\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " turned " + this.userNameRegex + " into a spooky ghost!" + this.assistRegex), 1);

    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " used " + this.userNameRegex + " as a punching bag\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " UwU'd " + this.userNameRegex + " to death!" + this.assistRegex), 1);

    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " went too hard on " + this.userNameRegex + "\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " wanted to show " + this.userNameRegex + " something cool\\.{3} DEATH!" + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " wiped the floor with " + this.userNameRegex + "\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " was annihilated by " + this.userNameRegex + "\\." + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " was bombarded by " + this.userNameRegex + "\\." + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " was finished by " + this.userNameRegex + "\\. Fatality!" + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " was forced to kill " + this.userNameRegex + "\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " was murdered by " + this.userNameRegex + "!" + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " was no match for " + this.userNameRegex + "\\." + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " was poked to death by " + this.userNameRegex + "\\." + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " was pulled into the void by " + this.userNameRegex + "'s pet space kraken\\." + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " was pummelled by " + this.userNameRegex + "'s Kung Fu moves\\." + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " was sniped by " + this.userNameRegex + "\\." + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " was slapped into the void by " + this.userNameRegex + "\\." + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " was smacked into next week by " + this.userNameRegex + "\\." + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " was sucked into a black hole while " + this.userNameRegex +  " watched\\." + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " was ticked to death by " + this.userNameRegex + "\\." + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " was too scared to fight " + this.userNameRegex + "\\." + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " was tossed into the void by " + this.userNameRegex + "\\." + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " went medieval on " + this.userNameRegex + "\\." + this.assistRegex), 1);

    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " yeeted " + this.userNameRegex + " into the void\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " yeeted their weapon into " + this.userNameRegex + "\\." + this.assistRegex), 1);

    //this.customKillMessages.put(Pattern.compile(this.userNameRegex + "" + this.userNameRegex + "" + this.assistRegex));
  }

  @Subscribe
  public void onChatReceiveEvent(ChatReceiveEvent chatReceiveEvent) {
    String msg = chatReceiveEvent.chatMessage().getPlainText();
    ClientPlayer clientPlayer = this.addon.labyAPI().minecraft().clientPlayer();
    String userName = clientPlayer.getName();

    // Win Streak Counter
    StatsTrackerSubConfig statsTrackerSubConfig = this.addon.configuration().getStatsTrackerSubConfig();
    if (statsTrackerSubConfig.isEnabled()) {
      if (msg.equals("Congratulations, you win!")) {
        GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers().get(CCUinternalConfig.name);
        if (gameStatsTracker != null) {
          gameStatsTracker.registerWin();
        } else if (GameStatsTracker.shouldMakeGameStatsTracker(CCUinternalConfig.name)) {
          gameStatsTracker = new GameStatsTracker(CCUinternalConfig.name);
          gameStatsTracker.registerWin();
          statsTrackerSubConfig.getGameStatsTrackers().put(CCUinternalConfig.name, gameStatsTracker);
        }
        CCUinternalConfig.won = true;
      }
    }

    // Auto GG
    EndGameSubConfig config = this.addon.configuration().getEndGameSubConfig();
    if (config.isEnabled().get() && !CCUinternalConfig.hasSaidGG) {
      String eliminationMessage = this.addon.labyAPI().minecraft().clientPlayer().getName() + " has been eliminated from the game.";
      if (msg.equals("Congratulations, you win!") || (msg.equals(eliminationMessage) && config.getOnElimination().get())) {
        this.addon.labyAPI().minecraft().chatExecutor().chat(config.getGameEndMessage().get().msg, false);
        if (!config.getCustomMessage().isDefaultValue()) {
          this.addon.labyAPI().minecraft().chatExecutor().chat(config.getCustomMessage().get(), false);
        }
        CCUinternalConfig.hasSaidGG = true;
        return;
      }
    }

    // Friend Message Sound
    if (this.addon.configuration().friendMessageSound().get()) {
      if (msg.matches("\\[Friend\\] ([a-zA-Z0-9_]{2,16}) -> Me: .*")) {
        this.addon.labyAPI().minecraft().sounds().playSound(
            ResourceLocation.create("minecraft", "entity.experience_orb.pickup"), 1000, 1);
        return;
      }
    }

    // Start of game
    if (msg.equals("Let the games begin!")) {
      CCUinternalConfig.inPreLobby = false;
      Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors()).schedule(() -> {
        if (this.addon.configuration().getEggWarsMapInfoSubConfig().isEnabled().get()) {
          CCUinternalConfig.updateTeamColour(this.addon);
          EggWarsMapInfo.eggWarsMapInfo(this.addon);
        }
        if (this.addon.configuration().getAutoVoteSubConfig().isEnabled()) {
          //AutoVote.vote(this.addon);
          this.addon.logger().info("Tried to auto vote");
        }
        this.addon.rpcManager.startOfGame();
        this.addon.rpcManager.updateRPC();
      },1000, TimeUnit.MILLISECONDS);
      return;
    }

    // RPC
    Matcher matcher = playerElimination.matcher(msg);
    if (matcher.matches()) {
      this.addon.rpcManager.registerDeath(matcher.group(1));
      return;
    }

    // Party Tracker
    if (msg.matches("You have joined [a-zA-Z0-9_]{2,16}'s party!")) {
      CCUinternalConfig.partyStatus = true;
      return;
    }
    if (msg.matches("You have left your party!")
        || msg.matches("You were kicked from your party!")
        || msg.matches("The party has been disbanded!")) {
      CCUinternalConfig.partyStatus = false;
      return;
    }

    if (msg.matches("[a-zA-Z0-9_]{2,16} joined the party!")) {
      CCUinternalConfig.partyStatus = true;
    }


    // Kills & Death Tracker
    if (msg.contains(userName)) {
      if (this.mightBeKillMessage.matcher(msg).matches()) {

        // Check default first as these are more likely
        for (Map.Entry<Pattern, Integer> set : this.defaultKillMessages.entrySet()) {
          if (this.processKillMessage(set.getKey(), set.getValue(), msg, userName)) {
            return;
          }
        }

        // Check custom kill messages
        for (Map.Entry<Pattern, Integer> set : this.customKillMessages.entrySet()) {
          if (this.processKillMessage(set.getKey(), set.getValue(), msg, userName)) {
            return;
          }
        }
      }

      // "Natural" causes
      if (msg.equals(userName + " tried to survive in the void.")
       || msg.equals(userName + " died in the void.")) {

        this.displayKillMessage(userName, "void");
        this.registerCustomDeath("void");
      } else if (msg.equals(userName + " blew up.")) {

        this.displayKillMessage(userName, "tnt");
        this.registerCustomDeath("tnt");
      }

    }

  }

  private boolean processKillMessage(Pattern killMessagePattern, int killerGroup, String msg, String userName) {
    Matcher killMessageMatcher = killMessagePattern.matcher(msg);
    if (killMessageMatcher.matches()) {
      String killer = killMessageMatcher.group(killerGroup);
      String other;

      String userOne = killMessageMatcher.group(1);
      String userTwo = killMessageMatcher.group(2);

      if (userOne.equals(userName)) {
        other = userTwo;
      } else {
        other = userOne;
      }

      if (killer.equals(userName)) {
        this.registerCustomKill(other);
        this.displayKillMessage(other, killer);
      } else {
        this.registerCustomDeath(other);
        this.displayKillMessage(userName, killer);
      }
      return true;
    }
    return false;
  }

  private void registerCustomDeath(String reason) {
    GameStatsTracker gameStatsTracker = this.addon.configuration().getStatsTrackerSubConfig().getGameStatsTrackers().get(CCUinternalConfig.name);
    if (gameStatsTracker != null) {
      gameStatsTracker.registerDeath(reason);
    } else if (GameStatsTracker.shouldMakeGameStatsTracker(CCUinternalConfig.name)){
      gameStatsTracker = new GameStatsTracker(CCUinternalConfig.name);
      gameStatsTracker.registerDeath(reason);
      this.addon.configuration().getStatsTrackerSubConfig().getGameStatsTrackers().put(CCUinternalConfig.name, gameStatsTracker);
    }
  }

  private void registerCustomKill(String reason) {
    GameStatsTracker gameStatsTracker = this.addon.configuration().getStatsTrackerSubConfig().getGameStatsTrackers().get(CCUinternalConfig.name);
    if (gameStatsTracker != null) {
      gameStatsTracker.registerKill(reason);
    } else  if (GameStatsTracker.shouldMakeGameStatsTracker(CCUinternalConfig.name)) {
      gameStatsTracker = new GameStatsTracker(CCUinternalConfig.name);
      gameStatsTracker.registerKill(reason);
      this.addon.configuration().getStatsTrackerSubConfig().getGameStatsTrackers().put(CCUinternalConfig.name, gameStatsTracker);
    }
  }

  private void displayKillMessage(String murdered, String murderer) {
    this.addon.labyAPI().minecraft().chatExecutor().displayClientMessage(Component.empty()
        .append(Component.text(murdered, NamedTextColor.RED))
        .append(Component.text(" killed by "))
        .append(Component.text(murderer, NamedTextColor.GREEN)));
  }

  private boolean isSpectator(String userName) {
    for (Player player : this.addon.labyAPI().minecraft().clientWorld().getPlayers()) {
      if (player.getName().equals(userName)) {
        return player.networkPlayerInfo().gameMode().equals(GameMode.SPECTATOR);
      }
    }
    return true;
  }
}
