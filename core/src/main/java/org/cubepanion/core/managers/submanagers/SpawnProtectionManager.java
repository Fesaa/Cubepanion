package org.cubepanion.core.managers.submanagers;

import java.util.HashMap;
import java.util.UUID;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.gui.imp.ClientPlayerSpawnProtection;
import org.cubepanion.core.gui.imp.SpawnProtectionComponent;

public class SpawnProtectionManager {

  private final Cubepanion addon;

  private final HashMap<UUID, SpawnProtectionComponent> uuidSpawnProtectionComponentHashMap;
  private final ClientPlayerSpawnProtection clientPlayerSpawnProtection;

  public SpawnProtectionManager(Cubepanion addon) {
    this.addon = addon;

    this.clientPlayerSpawnProtection = new ClientPlayerSpawnProtection(addon);
    this.uuidSpawnProtectionComponentHashMap = new HashMap<>();
  }

  public void registerDeath(UUID uuid) {
    SpawnProtectionComponent spawnProtectionComponent = new SpawnProtectionComponent(this.addon);
    spawnProtectionComponent.enable(false);
    this.uuidSpawnProtectionComponentHashMap.put(uuid, spawnProtectionComponent);
  }

  public SpawnProtectionComponent getSpawnProtectionComponent(UUID uuid) {
    return this.uuidSpawnProtectionComponentHashMap.get(uuid);
  }

  public void removeSpawnProtectionComponent(UUID uuid) {
    this.uuidSpawnProtectionComponentHashMap.remove(uuid);
  }

  public void resetHasMap() {
    this.uuidSpawnProtectionComponentHashMap.clear();
  }

  public ClientPlayerSpawnProtection getClientPlayerSpawnProtection() {
    return clientPlayerSpawnProtection;
  }
}
