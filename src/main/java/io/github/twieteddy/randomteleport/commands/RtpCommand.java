package io.github.twieteddy.randomteleport.commands;

import io.github.twieteddy.randomteleport.Border;
import io.github.twieteddy.randomteleport.configs.GeneralConfig;
import io.github.twieteddy.randomteleport.configs.MessageConfig;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class RtpCommand implements CommandExecutor {
  private final GeneralConfig generalConfig;
  private final MessageConfig messaging;
  private final Border border;
  private final HashMap<UUID, Long> cooldowns;

  public RtpCommand(GeneralConfig generalConfig, MessageConfig messaging, Border border) {
    this.generalConfig = generalConfig;
    this.messaging = messaging;
    this.border = border;
    this.cooldowns = new HashMap<>();
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    if (!(sender instanceof Player)) {
      return true;
    }

    if (!sender.hasPermission("randomteleport.command.rtp")) {
      return true;
    }

    Player player = (Player) sender;

    // Calculate cooldown
    if (!player.hasPermission("randomteleport.cooldown.bypass")) {
      Long lastCommandUsage = cooldowns.getOrDefault(player.getUniqueId(), 0L);
      long nextUsage = lastCommandUsage + generalConfig.getCooldown() * 1000;
      if (nextUsage > System.currentTimeMillis()) {
        long diff = nextUsage - System.currentTimeMillis();
        messaging.sendCooldownMessage(player, generalConfig.getCooldown(), diff);
        return true;
      }
    }

    // Update current cooldown
    cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    Location oldLocation = player.getLocation();

    // Search for safe spot and teleport
    for (int i = 0; i < generalConfig.getMaxTries(); i++) {
      Location newLocation = border.getRandomLocation(player.getWorld());
      if (generalConfig.isSafeTeleportEnabled()) {
        Material topBlock = newLocation.subtract(0, 1, 0).getBlock().getType();
        if (generalConfig.getUnsafeBlocks().contains(topBlock)) {
          continue;
        }
      }
      player.teleport(newLocation.add(0.5D, 1D, 0.5D));
      messaging.sendTeleportSuccessMessage(player, (int) newLocation.distance(oldLocation));
      return true;
    }

    return true;
  }
}
