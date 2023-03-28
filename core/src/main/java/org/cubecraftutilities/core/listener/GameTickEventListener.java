package org.cubecraftutilities.core.listener;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.entity.LivingEntity.EquipmentSpot;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.managers.submanagers.DurabilityManager;
import org.cubecraftutilities.core.managers.submanagers.SpawnProtectionManager;
import org.cubecraftutilities.core.utils.Colours;

public class GameTickEventListener {

  private final CCU addon;
  private final SpawnProtectionManager spawnProtectionManager;
  private final DurabilityManager durabilityManager;
  private int counter = 0;

  public GameTickEventListener(CCU addon) {
    this.addon = addon;

    this.spawnProtectionManager = addon.getManager().getSpawnProtectionManager();
    this.durabilityManager = this.addon.getManager().getDurabilityManager();
  }

  @Subscribe
  public void onGameTickEvent(GameTickEvent gameTickEvent) {
    if (gameTickEvent.phase() == Phase.POST) {
      return;
    }

    int ticksInAMinute = 20 * 60;
    if (this.counter % ticksInAMinute == 0) {
      this.addon.configuration().getStatsTrackerSubConfig().checkForResets();
      this.addon.saveConfiguration();
    }
    if (this.counter % 20 == 0) { // Each second
      this.spawnProtectionManager.getClientPlayerSpawnProtection().update(true);
      this.spawnProtectionManager.updateSpawnProtectionComponentHashMap(true);

      ClientPlayer player = this.addon.labyAPI().minecraft().getClientPlayer();
      if (player != null) {
        this.processHelmetForWarningMessage(player.getEquipmentItemStack(EquipmentSpot.HEAD));
        this.processChestForWarningMessage(player.getEquipmentItemStack(EquipmentSpot.CHEST));
        this.processLegsForWarningMessage(player.getEquipmentItemStack(EquipmentSpot.LEGS));
        this.processFeetForWarningMessage(player.getEquipmentItemStack(EquipmentSpot.FEET));
      }
    } else if (this.counter % 2 == 0) { // Each two ticks & not on the second
      this.spawnProtectionManager.getClientPlayerSpawnProtection().update(false);
      this.spawnProtectionManager.updateSpawnProtectionComponentHashMap(false);
    }
    this.counter++;
  }

  private void processHelmetForWarningMessage(ItemStack itemStack) {
    if (itemStack.isAir()) {
      return;
    }

    boolean threshHoldPassed =
        itemStack.getMaximumDamage() - itemStack.getCurrentDamageValue() < this.addon.configuration().getAutomationConfig().getDurabilityWarning().get()
        && itemStack.getMaximumDamage() > 0;
    boolean hasAlreadyWarned = this.durabilityManager.isWarnedHelmet();

    if (threshHoldPassed && !hasAlreadyWarned) {
      this.displayWarning(EquipmentSpot.HEAD);
      this.durabilityManager.setWarnedHelmet(true);
    } else if (!threshHoldPassed) {
      this.durabilityManager.setWarnedHelmet(false);
    }
  }

  private void processChestForWarningMessage(ItemStack itemStack) {
    if (itemStack.isAir()) {
      return;
    }

    boolean threshHoldPassed =
        itemStack.getMaximumDamage() - itemStack.getCurrentDamageValue() < this.addon.configuration().getAutomationConfig().getDurabilityWarning().get()
        && itemStack.getMaximumDamage() > 0;
    boolean hasAlreadyWarned = this.durabilityManager.isWarnedChestplate();

    if (threshHoldPassed && !hasAlreadyWarned) {
      this.displayWarning(EquipmentSpot.CHEST);
      this.durabilityManager.setWarnedChestplate(true);
    } else if (!threshHoldPassed) {
      this.durabilityManager.setWarnedChestplate(false);
    }
  }

  private void processLegsForWarningMessage(ItemStack itemStack) {
    if (itemStack.isAir()) {
      return;
    }

    boolean threshHoldPassed =
        itemStack.getMaximumDamage() - itemStack.getCurrentDamageValue() < this.addon.configuration().getAutomationConfig().getDurabilityWarning().get()
        && itemStack.getMaximumDamage() > 0;
    boolean hasAlreadyWarned = this.durabilityManager.isWarnedLeggings();

    if (threshHoldPassed && !hasAlreadyWarned) {
      this.displayWarning(EquipmentSpot.LEGS);
      this.durabilityManager.setWarnedLeggings(true);
    } else if (!threshHoldPassed) {
      this.durabilityManager.setWarnedLeggings(false);
    }
  }

  private void processFeetForWarningMessage(ItemStack itemStack) {
    if (itemStack.isAir()) {
      return;
    }

    boolean threshHoldPassed =
        itemStack.getMaximumDamage() - itemStack.getCurrentDamageValue() < this.addon.configuration().getAutomationConfig().getDurabilityWarning().get()
        && itemStack.getMaximumDamage() > 0;
    boolean hasAlreadyWarned = this.durabilityManager.isWarnedBoots();

    if (threshHoldPassed && !hasAlreadyWarned) {
      this.displayWarning(EquipmentSpot.FEET);
      this.durabilityManager.setWarnedBoots(true);
    } else if (!threshHoldPassed) {
      this.durabilityManager.setWarnedBoots(false);
    }
  }

  private void displayWarning(EquipmentSpot spot) {
    Component warning = Component.text("Your ", Colours.Error).append(EquipmentSpotToComponent(spot)).append(Component.text(" about to break!"));

    this.addon.labyAPI().minecraft().chatExecutor().displayClientMessage(warning);
    this.addon.labyAPI().minecraft().chatExecutor().displayClientMessage(warning, true);
    this.addon.labyAPI().minecraft().sounds().playSound(ResourceLocation.create("minecraft", "entity.lightning_bolt.impact"), 100, 1);
  }

  private Component EquipmentSpotToComponent(EquipmentSpot spot) {
    switch (spot) {
      case HEAD:
        return Component.text("helmet", NamedTextColor.GOLD, TextDecoration.BOLD).append(Component.text(" is", Colours.Error).undecorate(TextDecoration.BOLD));
      case CHEST:
        return Component.text("chestplate", NamedTextColor.GOLD, TextDecoration.BOLD).append(Component.text(" is", Colours.Error).undecorate(TextDecoration.BOLD));
      case LEGS:
        return Component.text("leggings", NamedTextColor.GOLD, TextDecoration.BOLD).append(Component.text(" are", Colours.Error).undecorate(TextDecoration.BOLD));
      case FEET:
        return Component.text("boots", NamedTextColor.GOLD, TextDecoration.BOLD).append(Component.text(" are", Colours.Error).undecorate(TextDecoration.BOLD));
      default:
        return Component.text("??? is");
    }
  }

}
