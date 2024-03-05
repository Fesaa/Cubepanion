package art.ameliah.laby.addons.cubepanion.core.commands.debug;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.CubeSocket;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.session.CubeSocketSession;
import art.ameliah.laby.addons.cubepanion.core.managers.CubepanionManager;
import art.ameliah.laby.addons.cubepanion.core.managers.submanagers.FireballManager;
import art.ameliah.laby.addons.cubepanion.core.managers.submanagers.PartyManager;
import net.labymod.api.client.chat.command.SubCommand;

public class State extends SubCommand {

  private final Cubepanion addon;

  protected State(Cubepanion addon) {
    super("state", "s");

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
    String state = String.format(
        "Cubepanion State:\nCubeGame: %s\nLast CubeGame: %s\nMap Name: %s\nTeam Colour: %s\nRank: %s\nEliminated: %s\nIn Pre-Lobby: %s\nLost: %s\nGame Start Time: %d\n",
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
    String partyState = String.format(
        "Party State:\nIn Party: %s\nParty Owner: %s\nParty Chat: %s\nParty Members: %s\n",
        partyManager.isInParty(),
        partyManager.isPartyOwner(),
        partyManager.isPartyChat(),
        String.join(", ", partyManager.getPartyMembers()));

    FireballManager fireballManager = addon.getManager().getFireballManager();
    String fireballState = String.format(
        "Fireball State:\nLast Use: %d\nCooldown: %d\nOn Cooldown: %s\n",
        fireballManager.getLastUse(),
        fireballManager.getCooldown(),
        fireballManager.onCooldown());

    CubeSocket socket = addon.getSocket();
    CubeSocketSession session = socket.getSession();
    String socketState = String.format(
        "Socket State:\nConnected: %s\nCubeStocketState: %s\nKeep Alive Sent: %d\nKeep Alive Received: %d\n",
        socket.isConnected(),
        socket.getState(),
        session != null ? session.getKeepAlivesSent() : 0,
        session != null ? session.getKeepAlivesReceived() : 0);


    displayMessage(addonState + "\n" + state + "\n" + partyState + "\n" + fireballState + "\n" + socketState);
    return true;
  }
}
