package art.ameliah.laby.addons.cubepanion.core.commands.debug;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.CubeSocket;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.session.CubeSocketSession;
import art.ameliah.laby.addons.cubepanion.core.managers.CubepanionManager;
import art.ameliah.laby.addons.cubepanion.core.managers.submanagers.FireballManager;
import art.ameliah.laby.addons.cubepanion.core.managers.submanagers.PartyManager;
import art.ameliah.laby.addons.cubepanion.core.weave.ChestAPI;
import art.ameliah.laby.addons.cubepanion.core.weave.EggWarsMapAPI;
import art.ameliah.laby.addons.cubepanion.core.weave.LeaderboardAPI;
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
      //return false;
    }

    CubepanionManager manager = addon.getManager();
    String state = String.format(
        "Cubepanion State:\n\tCubeGame: %s\n\tLast CubeGame: %s\n\tMap Name: %s\n\tTeam Colour: %s\n\tRank: %s\n\tEliminated: %s\n\tIn Pre-Lobby: %s\n\tLost: %s\n\tGame Start Time: %d\n\t",
        addon.getManager().getDivision().getString(),
        manager.getLastDivision().getString(),
        manager.getMapName(),
        manager.getTeamColour(),
        manager.getRankString(),
        manager.isEliminated(),
        manager.isInPreLobby(),
        manager.hasLost(),
        manager.getGameStartTime());
    
    String weaveState = String.format(
        "Weave State:\n\tChest Seasons: %s\n\tChests loaded: %d\n\tEggWars Maps loaded: %d\n\tGames Loaded: %d\n\t",
        ChestAPI.getInstance().getSeason(),
        ChestAPI.getInstance().getChestLocations().size(),
        EggWarsMapAPI.getInstance().getConvertedEggWarsMaps().size(),
        LeaderboardAPI.getInstance().getAliases().size());

    PartyManager partyManager = addon.getManager().getPartyManager();
    String partyState = String.format(
        "Party State:\n\tIn Party: %s\n\tParty Owner: %s\n\tParty Chat: %s\n\tParty Members: %s\n\t",
        partyManager.isInParty(),
        partyManager.isPartyOwner(),
        partyManager.isPartyChat(),
        String.join(", ", partyManager.getPartyMembers()));

    FireballManager fireballManager = addon.getManager().getFireballManager();
    String fireballState = String.format(
        "Fireball State:\n\tLast Use: %d\n\tCooldown: %d\n\tOn Cooldown: %s\n\t",
        fireballManager.getLastUse(),
        fireballManager.getCooldown(),
        fireballManager.onCooldown());

    CubeSocket socket = addon.getSocket();
    CubeSocketSession session = socket.getSession();
    String socketState = String.format(
        "Socket State:\n\tConnected: %s\n\tCubeStocketState: %s\n\tKeep Alive Sent: %d\n\tKeep Alive Received: %d\n\t",
        socket.isConnected(),
        socket.getState(),
        session != null ? session.getKeepAlivesSent() : 0,
        session != null ? session.getKeepAlivesReceived() : 0);


    displayMessage(state + "\n" + weaveState + "\n" + partyState + "\n" + fireballState + "\n" + socketState);
    return true;
  }
}
