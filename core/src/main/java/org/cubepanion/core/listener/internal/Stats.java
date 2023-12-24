package org.cubepanion.core.listener.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.events.GameEndEvent;
import org.cubepanion.core.events.PlayerDeathEvent;
import org.cubepanion.core.events.PlayerEliminationEvent;
import org.cubepanion.core.managers.CubepanionManager;

public class Stats {

  private final Cubepanion addon;
  private final CubepanionManager manager;
  private final String userNameRegex = "(?:.{0,5} |)([a-zA-Z0-9_]{2,16})(?: .{0,5}|)";
  private final String assistRegex = "(\\s{0,5}\\(\\+\\d{1,2} assists?\\))?";
  private final Pattern mightBeKillMessage = Pattern.compile(
      this.userNameRegex + ".{1,100}" + this.userNameRegex + ".{1,100}" + this.assistRegex);
  private final Pattern playerElimination = Pattern.compile(
      "([a-zA-Z0-9_]{2,16}) has been eliminated from the game\\.");

  private final HashMap<Pattern, Integer> customKillMessages = new HashMap<>(69);
  private final HashMap<Pattern, Integer> defaultKillMessages = new HashMap<>(8);

  public Stats(Cubepanion addon) {
    this.addon = addon;
    this.manager = addon.getManager();

    // A B C D E F G H I J K L M N O P Q R S T U V W X Y Z

    //Default kill messages
    this.defaultKillMessages.put(Pattern.compile(
        this.userNameRegex + " died in the void while escaping " + this.userNameRegex + "\\."
            + this.assistRegex), 2);
    this.defaultKillMessages.put(Pattern.compile(
        this.userNameRegex + " was slain by " + this.userNameRegex + "\\." + this.assistRegex), 2);
    this.defaultKillMessages.put(Pattern.compile(
            this.userNameRegex + " was blown up by " + this.userNameRegex + "\\." + this.assistRegex),
        2);
    this.defaultKillMessages.put(Pattern.compile(
        this.userNameRegex + " thought they could survive in the void while escaping "
            + this.userNameRegex + "\\." + this.assistRegex), 2);
    this.defaultKillMessages.put(Pattern.compile(
        this.userNameRegex + " kicked " + this.userNameRegex + " into the void\\."
            + this.assistRegex), 1);
    this.defaultKillMessages.put(Pattern.compile(
        this.userNameRegex + " couldn't fly while escaping " + this.userNameRegex + "\\."
            + this.assistRegex), 2);
    this.defaultKillMessages.put(Pattern.compile(
        this.userNameRegex + " tried to escape " + this.userNameRegex
            + " by jumping into the void\\." + this.assistRegex), 2);
    this.defaultKillMessages.put(Pattern.compile(
        this.userNameRegex + " was turned into a snowman by " + this.userNameRegex + "\\."), 2);

    // Custom Kill messages
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " \"accidentally\" tripped " + this.userNameRegex + " into the void\\."
            + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " \"accidentally\" made " + this.userNameRegex
            + " fall into the void\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " and " + this.userNameRegex
        + " settled their differences and became friends... Just kidding, " + this.userNameRegex
        + " killed " + this.userNameRegex + "\\." + this.assistRegex), 3);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " assisted " + this.userNameRegex + " with their void-traume\\."
            + this.assistRegex), 1);

    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " betrayed " + this.userNameRegex + "\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(this.userNameRegex + " bought " + this.userNameRegex
        + " a gift\\.{3} Oh wait, it's just death\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
            this.userNameRegex + " brawled " + this.userNameRegex + " to death\\." + this.assistRegex),
        1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " burnt to a crisp while fighting " + this.userNameRegex + "\\."
            + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " burned to death while fighting " + this.userNameRegex + "\\."
            + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " blew " + this.userNameRegex + " into the void\\."
            + this.assistRegex), 1);

    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " destroyed " + this.userNameRegex + " into oblivion\\."
            + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " diagnosed " + this.userNameRegex + " with death\\."
            + this.assistRegex), 1);

    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " flabbergasted " + this.userNameRegex + " with death\\."
            + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " fell to their death while escpaing " + this.userNameRegex + "\\."
            + this.assistRegex), 2);

    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " gave " + this.userNameRegex + " the L!" + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " got a face lift by " + this.userNameRegex + "\\."
            + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " got sent into the void by " + this.userNameRegex + "\\."
            + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " got snapped away by " + this.userNameRegex + "\\."
            + this.assistRegex), 2);

    this.customKillMessages.put(Pattern.compile(this.userNameRegex
        + " had to register their hands as deadly weapons after what they did to "
        + this.userNameRegex + "\\.{3}" + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " has been sent to the after life by " + this.userNameRegex + "\\."
            + this.assistRegex), 2);

    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " kicked " + this.userNameRegex + " into the abyss\\."
            + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " killed " + this.userNameRegex + "\\. LOL!" + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " killed " + this.userNameRegex + "\\. RIP!" + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " killed " + this.userNameRegex + "\\. Now now\\.{3} don't be salty!"
            + this.assistRegex), 1);

    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " made " + this.userNameRegex + " jump into the void\\."
            + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
            this.userNameRegex + " made " + this.userNameRegex + " disappear\\." + this.assistRegex),
        1);

    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " OwO'd " + this.userNameRegex + " to death!" + this.assistRegex), 1);

    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " pushed " + this.userNameRegex + " into the void\\."
            + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " picked up " + this.userNameRegex + " and threw them into the void\\."
            + this.assistRegex), 1);

    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " scared " + this.userNameRegex + " into the void\\."
            + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " screamed \"This is Sparta!\" before kicking " + this.userNameRegex
            + " into the void\\." + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " sent " + this.userNameRegex + " into a deep freeze\\."
            + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " sent " + this.userNameRegex + " into a deep sleep!"
            + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " sent " + this.userNameRegex + " into the Upside down\\."
            + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " sent " + this.userNameRegex + " to the underworld\\."
            + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " showcased their weapon to " + this.userNameRegex + "\\."
            + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " showed " + this.userNameRegex + " their karate skills!"
            + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
            this.userNameRegex + " slapped " + this.userNameRegex + " to death!" + this.assistRegex),
        1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " shoved " + this.userNameRegex + " into the void\\."
            + this.assistRegex), 1);

    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " tried being a ninja against " + this.userNameRegex
            + " but it didn't turn out so well\\.{3}" + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " tripped " + this.userNameRegex + " into the void\\."
            + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " tucked " + this.userNameRegex + " in for a dirt nap\\."
            + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " turned " + this.userNameRegex + " into a spooky ghost!"
            + this.assistRegex), 1);

    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " used " + this.userNameRegex + " as a punching bag\\."
            + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " UwU'd " + this.userNameRegex + " to death!" + this.assistRegex), 1);

    this.customKillMessages.put(Pattern.compile(
            this.userNameRegex + " went too hard on " + this.userNameRegex + "\\." + this.assistRegex),
        1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " wanted to show " + this.userNameRegex
            + " something cool\\.{3} DEATH!" + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " wiped the floor with " + this.userNameRegex + "\\."
            + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " was annihilated by " + this.userNameRegex + "\\."
            + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(
            this.userNameRegex + " was bombarded by " + this.userNameRegex + "\\." + this.assistRegex),
        2);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " was finished by " + this.userNameRegex + "\\. Fatality!"
            + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " was forced to kill " + this.userNameRegex + "\\."
            + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " was murdered by " + this.userNameRegex + "!" + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(
            this.userNameRegex + " was no match for " + this.userNameRegex + "\\." + this.assistRegex),
        2);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " was poked to death by " + this.userNameRegex + "\\."
            + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " was pulled into the void by " + this.userNameRegex
            + "'s pet space kraken\\." + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " was pummelled by " + this.userNameRegex + "'s Kung Fu moves\\."
            + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " was RKO'd out of nowhere by " + this.userNameRegex + "\\."
            + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " was sniped by " + this.userNameRegex + "\\." + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " was slapped into the void by " + this.userNameRegex + "\\."
            + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " was smacked into next week by " + this.userNameRegex + "\\."
            + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " was sucked into a black hole while " + this.userNameRegex
            + " watched\\." + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " was ticked to death by " + this.userNameRegex + "\\."
            + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " was too scared to fight " + this.userNameRegex + "\\."
            + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " was tossed into the void by " + this.userNameRegex + "\\."
            + this.assistRegex), 2);
    this.customKillMessages.put(Pattern.compile(
            this.userNameRegex + " went medieval on " + this.userNameRegex + "\\." + this.assistRegex),
        1);

    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " yeeted " + this.userNameRegex + " into the void\\."
            + this.assistRegex), 1);
    this.customKillMessages.put(Pattern.compile(
        this.userNameRegex + " yeeted their weapon into " + this.userNameRegex + "\\."
            + this.assistRegex), 1);
  }

  @Subscribe
  public void onPlayerElimination(PlayerEliminationEvent e) {
    if (!e.isClientPlayer()) {
      return;
    }
    manager.setEliminated(true);
  }

  @Subscribe
  public void onChatReceiveEvent(ChatReceiveEvent e) {
    if (!this.addon.getManager().onCubeCraft()) {
      return;
    }
    String msg = e.chatMessage().getPlainText();
    String userName = SessionTracker.get().username();

    if (msg.equals("Congratulations, you win!")) {
      GameEndEvent event = new GameEndEvent(
          manager.getDivision(),
          true,
          false,
          manager.getGameStartTime());
      Laby.fireEvent(event);
      manager.setWon(true);
      return;
    }

    Matcher eliminationMatcher = this.playerElimination.matcher(msg);
    if (eliminationMatcher.matches()) {
      String eliminatedPlayer = eliminationMatcher.group(1);
      boolean isClient = userName.equals(eliminatedPlayer);
      PlayerEliminationEvent event = new PlayerEliminationEvent(isClient, eliminatedPlayer);
      Laby.fireEvent(event);
      return;
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
          || msg.equals(userName + " died in the void.")
          || msg.equals(userName + " thought they could survive in the void.")) {
        Laby.fireEvent(new PlayerDeathEvent(true, "void", userName));
      } else if (msg.equals(userName + " blew up.")) {
        Laby.fireEvent(new PlayerDeathEvent(true, "tnt", userName));
      }

    }

  }

  private boolean processKillMessage(Pattern killMessagePattern, int killerGroup, String msg,
      String userName) {
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
        Laby.fireEvent(new PlayerDeathEvent(false, userName, other));
      } else {
        Laby.fireEvent(new PlayerDeathEvent(true, killer, userName));
      }
      return true;
    }
    return false;
  }
}
