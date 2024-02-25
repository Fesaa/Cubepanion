package art.ameliah.laby.addons.cubepanion.core.listener;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.ArmourBreakWarningSubConfig;
import art.ameliah.laby.addons.cubepanion.core.managers.submanagers.DurabilityManager;
import art.ameliah.laby.addons.cubepanion.core.utils.Colours;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.LivingEntity.EquipmentSpot;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.entity.player.Inventory;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import net.labymod.api.notification.Notification;

public class GameTickEventListener {

  private final Cubepanion addon;
  private final DurabilityManager durabilityManager;
  private final ArmourBreakWarningSubConfig armourBreakWarningSubConfig;
  private int counter = 0;

  public GameTickEventListener(Cubepanion addon) {
    this.addon = addon;

    this.durabilityManager = this.addon.getManager().getDurabilityManager();
    this.armourBreakWarningSubConfig = this.addon.configuration().getAutomationConfig()
        .getArmourBreakWarningSubConfig();
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
    if (this.counter % 2 == 0) { // Each two ticks
      this.durabilityUpdater(
          this.addon.configuration().getAutomationConfig().getArmourBreakWarningSubConfig()
              .getEnabled().get());
    }
    this.counter++;
  }

  private void durabilityUpdater(boolean warning) {
    int threshHold = this.addon.configuration().getAutomationConfig()
        .getArmourBreakWarningSubConfig().getDurabilityWarning().get();

    ClientPlayer player = this.addon.labyAPI().minecraft().getClientPlayer();
    if (player == null) {
      return;
    }
    Inventory inventory = player.inventory();
    int helmet = 0;
    int chest = 0;
    int leggings = 0;
    int boots = 0;

    ItemStack helmetItemStack = player.getEquipmentItemStack(EquipmentSpot.HEAD);
    ItemStack chestPlateItemStack = player.getEquipmentItemStack(EquipmentSpot.CHEST);
    ItemStack leggingsItemStack = player.getEquipmentItemStack(EquipmentSpot.LEGS);
    ItemStack bootsItemStack = player.getEquipmentItemStack(EquipmentSpot.FEET);
    ItemStack offHand = player.getOffHandItemStack();

    if (helmetItemStack.getAsItem().getIdentifier().getPath().contains("helmet")) {
      int durabilityLeft =
          helmetItemStack.getMaximumDamage() - helmetItemStack.getCurrentDamageValue();
      helmet += durabilityLeft;

      if (warning) {
        this.processForWarningMessage(EquipmentSpot.HEAD, durabilityLeft < threshHold);
      }
    }
    if (chestPlateItemStack.getAsItem().getIdentifier().getPath().contains("chest")) {
      int durabilityLeft =
          chestPlateItemStack.getMaximumDamage() - chestPlateItemStack.getCurrentDamageValue();
      chest += durabilityLeft;

      if (warning) {
        this.processForWarningMessage(EquipmentSpot.CHEST, durabilityLeft < threshHold);
      }
    }
    if (leggingsItemStack.getAsItem().getIdentifier().getPath().contains("leggings")) {
      int durabilityLeft =
          leggingsItemStack.getMaximumDamage() - leggingsItemStack.getCurrentDamageValue();
      leggings += durabilityLeft;

      if (warning) {
        this.processForWarningMessage(EquipmentSpot.LEGS, durabilityLeft < threshHold);
      }
    }
    if (bootsItemStack.getAsItem().getIdentifier().getPath().contains("boots")) {
      int durabilityLeft =
          bootsItemStack.getMaximumDamage() - bootsItemStack.getCurrentDamageValue();
      boots += durabilityLeft;

      if (warning) {
        this.processForWarningMessage(EquipmentSpot.FEET, durabilityLeft < threshHold);
      }
    }

    if (offHand.getAsItem().getIdentifier().getPath().contains("helmet")) {
      helmet += offHand.getMaximumDamage() - offHand.getCurrentDamageValue();
    } else if (offHand.getAsItem().getIdentifier().getPath().contains("chest")) {
      chest += offHand.getMaximumDamage() - offHand.getCurrentDamageValue();
    } else if (offHand.getAsItem().getIdentifier().getPath().contains("leggings")) {
      leggings += offHand.getMaximumDamage() - offHand.getCurrentDamageValue();
    } else if (offHand.getAsItem().getIdentifier().getPath().contains("boots")) {
      boots += offHand.getMaximumDamage() - offHand.getCurrentDamageValue();
    }

    for (int i = 0; i < 46; i++) {
      ItemStack iStack = inventory.itemStackAt(i);
      if (iStack.isAir()) {
        continue;
      }
      if (iStack.getAsItem().getIdentifier().getPath().contains("helmet")) {
        helmet += iStack.getMaximumDamage() - iStack.getCurrentDamageValue();
      } else if (iStack.getAsItem().getIdentifier().getPath().contains("chestplate")) {
        chest += iStack.getMaximumDamage() - iStack.getCurrentDamageValue();
      } else if (iStack.getAsItem().getIdentifier().getPath().contains("leggings")) {
        leggings += iStack.getMaximumDamage() - iStack.getCurrentDamageValue();
      } else if (iStack.getAsItem().getIdentifier().getPath().contains("boots")) {
        boots += iStack.getMaximumDamage() - iStack.getCurrentDamageValue();
      }
    }
    this.durabilityManager.resetCache();
    this.durabilityManager.updateInfo(EquipmentSpot.HEAD, helmet);
    this.durabilityManager.updateInfo(EquipmentSpot.CHEST, chest);
    this.durabilityManager.updateInfo(EquipmentSpot.LEGS, leggings);
    this.durabilityManager.updateInfo(EquipmentSpot.FEET, boots);
  }

  private void processForWarningMessage(EquipmentSpot spot, boolean threshHoldPassed) {
    ClientPlayer player = this.addon.labyAPI().minecraft().getClientPlayer();
    if (player == null) {
      return;
    }
    if (threshHoldPassed && !this.durabilityManager.getWarned(spot)) {
      this.displayWarning(spot);
      this.durabilityManager.updateInfo(spot, true);
    } else if (!threshHoldPassed) {
      this.durabilityManager.updateInfo(spot, false);
    }
  }

  private void displayWarning(EquipmentSpot spot) {
    Component warning = this.durabilityManager.getWarningComponent(spot);
    Minecraft minecraft = this.addon.labyAPI().minecraft();

    if (this.armourBreakWarningSubConfig.getNotification().get()) {
      this.addon.labyAPI().notificationController().push(Notification.builder()
          .title(Component.text("Warning!", Colours.Error))
          .text(warning)
          .duration(3000)
          .build());
    }

    if (this.armourBreakWarningSubConfig.getChat().get()) {
      minecraft.chatExecutor().displayClientMessage(warning);
    }

    if (this.armourBreakWarningSubConfig.getActionbar().get()) {
      minecraft.chatExecutor().displayClientMessage(warning, true);
    }
    minecraft.sounds()
        .playSound(this.armourBreakWarningSubConfig.getMinecraftSoundResourceLocation(spot), 100,
            1);
  }
}
