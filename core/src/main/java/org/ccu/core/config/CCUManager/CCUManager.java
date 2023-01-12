package org.ccu.core.config.CCUManager;

import java.util.HashMap;
import java.util.UUID;
import org.ccu.core.CCU;
import org.ccu.core.gui.imp.SpawnProtectionComponent;

public class CCUManager {

  private final CCU addon;

  private HashMap<UUID, SpawnProtectionComponent> uuidSpawnProtectionComponentHashMap;

  public CCUManager(CCU addon) {
    this.addon = addon;
    this.uuidSpawnProtectionComponentHashMap = new HashMap<>();
  }

  public void registerNewDivision() {
    this.uuidSpawnProtectionComponentHashMap = new HashMap<>();
  }

  public void registerDeath(UUID uuid) {
    SpawnProtectionComponent spawnProtectionComponent = new SpawnProtectionComponent(this.addon);
    spawnProtectionComponent.enable();
    this.uuidSpawnProtectionComponentHashMap.put(uuid, spawnProtectionComponent);
  }

  public SpawnProtectionComponent getSpawnProtectionComponent(UUID uuid) {
    return this.uuidSpawnProtectionComponentHashMap.get(uuid);
  }

  public HashMap<UUID, SpawnProtectionComponent> getUuidSpawnProtectionComponentHashMap() {
    return uuidSpawnProtectionComponentHashMap;
  }

  public void updateSpawnProtectionComponentHashMap(boolean endOfSecond) {
    for (SpawnProtectionComponent spawnProtectionComponentGen : this.uuidSpawnProtectionComponentHashMap.values()) {
      if (spawnProtectionComponentGen != null) {
        spawnProtectionComponentGen.update(endOfSecond);
      }
    }
  }
}
