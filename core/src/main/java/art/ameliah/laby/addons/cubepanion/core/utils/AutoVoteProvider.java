package art.ameliah.laby.addons.cubepanion.core.utils;

import art.ameliah.laby.addons.cubepanion.core.config.subconfig.AutoVoteSubConfig;
import art.ameliah.laby.addons.cubepanion.core.listener.games.AutoVote.VotePair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class AutoVoteProvider {

  private static Map<CubeGame, AutoVoteProvider> providers;
  private final int hotbarSlot;
  private final List<Supplier<VotePair>> votePairSuppliers = new ArrayList<>();

  private AutoVoteProvider(int hotbarSlot, List<Supplier<VotePair>> votePairSuppliers) {
    this.votePairSuppliers.addAll(votePairSuppliers);
    this.hotbarSlot = hotbarSlot;
  }

  // TODO: Throw this into the API
  public static void init(AutoVoteSubConfig config) {
    providers = new HashMap<>();
    providers.put(CubeGame.TEAM_EGGWARS, AutoVoteProvider.of(0,
        () -> VotePair.of(11, config.getEggWarsPerk().get().slot, "Perk Voting"),
        () -> VotePair.of(13, config.getEggWarsItems().get().slot, "Items Voting"),
        () -> VotePair.of(15, config.getEggWarsHealth().get().slot, "Health Voting")
    ));
    providers.put(CubeGame.SOLO_SKYWARS, AutoVoteProvider.of(0,
        () -> VotePair.of(11, config.getSkyWarsChests().get().slot, "Chest Items"),
        () -> VotePair.of(13, config.getSkyWarsProjectiles().get().slot, "Projectile Options"),
        () -> VotePair.of(15, config.getSkyWarsTime().get().slot, "Time")
    ));
    providers.put(CubeGame.SOLO_LUCKYISLANDS, AutoVoteProvider.of(0,
        () -> VotePair.of(12, config.getLuckyIslandsBlocks().get().slot, "Game Option Voting"),
        () -> VotePair.of(14, config.getLuckyIslandsTime().get().slot, "Time Voting")
    ));
    providers.put(CubeGame.PILLARS_OF_FORTUNE, AutoVoteProvider.of(0,
        () -> VotePair.of(12, config.getPofGameMode().get().slot, "Game Mode"),
        () -> VotePair.of(14, config.getPofMapMode().get().slot, "Map Mode")
    ));
    providers.put(CubeGame.BEDWARS, AutoVoteProvider.of(0,
        () -> VotePair.of(-1, config.getBedWarsModifier().get().slot, "Modifiers")
    ));
    providers.put(CubeGame.TEAM_LUCKY_ISLANDS, AutoVoteProvider.of(0,
        () -> VotePair.of(12, config.getLuckyIslandsBlocks().get().slot, "Game Option Voting"),
        () -> VotePair.of(14, config.getLuckyIslandsTime().get().slot, "Time Voting")
    ));
    providers.put(CubeGame.ENDER, AutoVoteProvider.of(0,
        () -> VotePair.of(-1, config.getEnder().get().slot, "")
    ));
    /*providers.put(CubeGame.DISASTERS, AutoVoteProvider.of(0,
        () -> VotePair.of(-1, config.getDisasters().get().slot, "")
    ));*/
  }

  public static AutoVoteProvider getProvider(CubeGame game) {
    return providers.get(game);
  }

  @SafeVarargs
  public static AutoVoteProvider of(int hotbarSlot, Supplier<VotePair>... supplier) {
    return new AutoVoteProvider(hotbarSlot, List.of(supplier));
  }

  public static AutoVoteProvider of(int hotbarSlot, List<Supplier<VotePair>> suppliers) {
    return new AutoVoteProvider(hotbarSlot, suppliers);
  }

  public int getHotbarSlot() {
    return hotbarSlot;
  }

  public List<Supplier<VotePair>> getVotePairSuppliers() {
    return votePairSuppliers;
  }

}
