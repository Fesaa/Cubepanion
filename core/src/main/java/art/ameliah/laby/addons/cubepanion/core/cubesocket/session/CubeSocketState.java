package art.ameliah.laby.addons.cubepanion.core.cubesocket.session;

public enum CubeSocketState {

  HELLO(-1),
  LOGIN(0),
  CONNECTED(1),
  OFFLINE(2);

  private final int id;

  CubeSocketState(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }


}
