package art.ameliah.laby.addons.cubepanion.core.commands.debug;

import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.SubCommand;
import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.managers.CubepanionManager;
import art.ameliah.laby.addons.cubepanion.core.managers.submanagers.FireballManager;
import art.ameliah.laby.addons.cubepanion.core.managers.submanagers.PartyManager;

public class State extends SubCommand {

  private final Cubepanion addon;
  
  protected State(Cubepanion addon) {
    super("state");
    
    this.addon = addon;
  }

  @Override
  public boolean execute(String s, String[] strings) {
    if (!addon.getManager().onCubeCraft()) {
      return false;
    }

    String addonState = String.format("RunTimeLoaded: ‰s",
        addon.wasLoadedInRuntime());

    CubepanionManager manager = addon.getManager();
    String state = String.format("Cubepanion State:\nCubeGame: %s\nLast CubeGame: %s\nMap Name: %s\nTeam Colour: %s\nRank: %s\nEliminated: %s\nIn Pre-Lobby: %s\nLost: %s\nGame Start Time: %d\n",
        addon.getManager().getDivision().getString(),
        manager.getLastDivision().getString(),
        manager.getMapName(),
        manager.getTeamColour(),
        manager.getRankString(),
        manager.isEliminated(),
        manager.isInPreLobby(),
        manager.hasLost(),
        manager.getGameStartTime());

    PartyManager partyManager = addon.getManager().getPartyManager();
    String partyState = String.format("Party State:\nIn Party: %s\nParty Owner: %s\nParty Chat: %s\nParty Members: %s\n",
        partyManager.isInParty(),
        partyManager.isPartyOwner(),
        partyManager.isPartyChat(),
        String.join(", ", partyManager.getPartyMembers()));

    FireballManager fireballManager = addon.getManager().getFireballManager();
    String fireballState = String.format("Fireball State:\nLast Use: %d\nCooldown: %d\nOn Cooldown: %s\n",
        fireballManager.getLastUse(),
        fireballManager.getCooldown(),
        fireballManager.onCooldown());


    
    displayMessage(addonState + "\n" + state + "\n" + partyState + "\n" + fireballState);
    return true;
  }
}
