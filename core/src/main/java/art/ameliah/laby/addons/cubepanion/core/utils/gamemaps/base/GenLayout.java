package art.ameliah.laby.addons.cubepanion.core.utils.gamemaps.base;

import art.ameliah.laby.addons.cubepanion.core.utils.Colours;
import art.ameliah.laby.addons.cubepanion.core.utils.I18nNamespaces;
import java.util.List;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;

public class GenLayout {

  private final List<MapGenerator> generators;

  private final Component layoutComponent;

  public GenLayout(List<MapGenerator> generators) {
    this.generators = generators;
    this.layoutComponent = this.createLayoutComponent();
  }

  public Component getLayoutComponent() {
    return this.layoutComponent;
  }

  private Component createLayoutComponent() {
    Component base = Component.empty();
    Component semi = Component.empty();
    Component middle = Component.empty();

    String mainKey = I18nNamespaces.managerNameSpace + "EggWarsMapInfoManager.genLayout.";
    for (MapGenerator gen : this.generators) {
      switch (gen.loc()) {
        case BASE -> base = base.append(
                Component.translatable(mainKey + "count", Component.text(gen.count()),
                    Component.text(gen.level())).color(Colours.Secondary))
            .append(
                Component.translatable(mainKey + gen.type().getString(), gen.type().getColour()))
            .append(Component.translatable(mainKey + "end", Colours.Secondary));
        case MIDDLE -> middle = middle.append(
                Component.translatable(mainKey + "count", Component.text(gen.count()),
                    Component.text(gen.level())).color(Colours.Secondary))
            .append(
                Component.translatable(mainKey + gen.type().getString(), gen.type().getColour()))
            .append(Component.translatable(mainKey + "end", Colours.Secondary));
        case SEMI_MIDDLE -> semi = semi.append(
                Component.translatable(mainKey + "count", Component.text(gen.count()),
                    Component.text(gen.level())).color(Colours.Secondary))
            .append(
                Component.translatable(mainKey + gen.type().getString(), gen.type().getColour()))
            .append(Component.translatable(mainKey + "end", Colours.Secondary));
      }
    }

    Component out = Component.empty();
    if (!middle.equals(Component.empty())) {
      out = out.append(Component.translatable(mainKey + "middle", Colours.Primary))
          .append(middle);
    }
    if (!semi.equals(Component.empty())) {
      out = out.append(Component.translatable(mainKey + "semi", Colours.Primary))
          .append(semi);
    }
    if (!base.equals(Component.empty())) {
      out = out.append(Component.translatable(mainKey + "base", Colours.Primary))
          .append(base);
    }
    if (!out.equals(Component.empty())) {
      out = Component.translatable(mainKey + "title", Colours.Title).append(out);
    }

    return out;
  }


  public enum Location {
    MIDDLE,
    SEMI_MIDDLE,
    BASE
  }

  public enum Generator {
    DIAMOND(NamedTextColor.AQUA, "diamond"),
    GOLD(NamedTextColor.GOLD, "gold"),
    IRON(NamedTextColor.GRAY, "iron");

    private final TextColor colour;
    private final String string;

    Generator(TextColor colour, String string) {
      this.colour = colour;
      this.string = string;
    }

    public TextColor getColour() {
      return colour;
    }

    public String getString() {
      return string;
    }
  }

  public record MapGenerator(Generator type, Location loc, int level, int count) {

  }

}
