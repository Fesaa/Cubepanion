package art.ameliah.laby.addons.cubepanion.core.external.autovote;

public record AutoVoteCategoryOption(int slot, String name) {

  public static AutoVoteCategoryOption DontVoteOption = new AutoVoteCategoryOption(-1, "Don't vote");

}
