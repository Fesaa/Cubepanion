package art.ameliah.laby.addons.cubepanion.core.external;

import com.google.gson.annotations.SerializedName;

public record GameMap(int gameId, String uniqueName, String mapName, int teamSize, MapLayout mapLayout, String colours, int buildLimit) {


  public enum MapLayout {
    @SerializedName("0")
    DOUBLE_CROSS,
    @SerializedName("1")
    TRIANGLE,
    @SerializedName("2")
    SQUARE,
    @SerializedName("3")
    CROSS
  }

}
