package art.ameliah.laby.addons.cubepanion.core.utils.gamemaps;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.external.GameMap;
import art.ameliah.laby.addons.cubepanion.core.utils.Colours;
import art.ameliah.laby.addons.cubepanion.core.utils.I18nNamespaces;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import net.labymod.api.client.component.Component;
import net.labymod.api.util.GsonUtil;
import net.labymod.api.util.logging.Logging;

public abstract class AbstractGameMap {

  private static final Logging log = Logging.create(Cubepanion.class.getSimpleName());

  private static final TypeToken<List<String>> flatListToken =  new TypeToken<>() {};
  private static final TypeToken<List<List<String>>> nestedToken =  new TypeToken<>() {};

  protected final Component teamFillerSpaces = spaceMaker(6);
  protected final Component sideSpaces = spaceMaker(2);
  protected final Component betweenSpaces = spaceMaker(4);
  protected final String mapName;
  protected final int teamSize;
  protected final int buildLimit;
  protected String currentTeamColour = "";

  public AbstractGameMap(String mapName, int teamSize, int buildLimit) {
    this.mapName = mapName;
    this.teamSize = teamSize;
    this.buildLimit = buildLimit;
  }

  public static AbstractGameMap constructFromAPI(GameMap map) {
    return switch (map.mapLayout()) {
      case DOUBLE_CROSS ->
          new DoubleCrossGameMap(map.mapName(), map.teamSize(), map.buildLimit(),
              extractFromJson(map.colours(), nestedToken));
      case TRIANGLE -> new TriangleGameMap(map.mapName(), map.teamSize(), map.buildLimit(),
          extractFromJson(map.colours(), nestedToken));
      case SQUARE -> new SquareGameMap(map.mapName(), map.teamSize(), map.buildLimit(),
          extractFromJson(map.colours(), nestedToken));
      case CROSS -> new CrossGameMap(map.mapName(), map.teamSize(), map.buildLimit(),
          extractFromJson(map.colours(), flatListToken));
    };
  }

  private static <T> T extractFromJson(String in, TypeToken<T> token) {
    return GsonUtil.DEFAULT_GSON.fromJson(in, token);
  }


  protected static Component spaceMaker(int n) {
    return Component.text(" ".repeat(Math.max(0, n)));
  }

  public int getBuildLimit() {
    return buildLimit;
  }

  public abstract Component getMapLayoutComponent();

  public Component getBuildLimitMessage() {
    return Component.translatable(
            I18nNamespaces.managerNameSpace + "GameMapInfoManager.buildLimit", Colours.Primary)
        .append(Component.text(this.buildLimit, Colours.Secondary));
  }

  public String getName() {
    return this.mapName;
  }

  public abstract void setCurrentTeamColour(String teamColour);

  public Component getTeamFiller(String colour) {
    return Component.text("||||||", Colours.colourToNamedTextColor(colour));
  }
}
