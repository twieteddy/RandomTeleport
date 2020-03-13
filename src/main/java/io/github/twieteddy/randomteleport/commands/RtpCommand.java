package io.github.twieteddy.randomteleport.commands;

import io.github.twieteddy.randomteleport.Border;
import io.github.twieteddy.randomteleport.Config;
import io.github.twieteddy.randomteleport.Messaging;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RtpCommand implements CommandExecutor {
  private final Config config;
  private final Messaging messaging;
  private final Border border;
  private final HashMap<UUID, Long> cooldowns;

  public RtpCommand(Config config, Messaging messaging, Border border) {
    this.config = config;
    this.messaging = messaging;
    this.border = border;
    this.cooldowns = new HashMap<>();
  }

  @Override
  public boolean onCommand(
      CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
    // Only player are allowed to use teleports
    if (!(sender instanceof Player)) {
      return true;
    }

    // Check permission
    if (!sender.hasPermission("randomteleport.command.rtp")) {
      return true;
    }

    Player p = (Player) sender;

    // Calculate cooldown
    Long lastCommandUsage = cooldowns.getOrDefault(p.getUniqueId(), Long.valueOf("0"));
    if (!p.hasPermission("randomteleport.cooldown.bypass")) {
      long nextUsage = lastCommandUsage + config.getCooldown() * 1000;
      if (nextUsage > System.currentTimeMillis()) {
        long diff = nextUsage - System.currentTimeMillis();
        messaging.sendCooldownMessage(p, config.getCooldown(), diff);
        return true;
      }
    }

    cooldowns.put(p.getUniqueId(), System.currentTimeMillis());
    Location oldLocation = p.getLocation();
    int tries = 0;

    while (true) {
      // Get new location and it's material
      Location newLocation = border.getRandomLocation(p.getWorld());
      Material topBlock = newLocation.subtract(0, 1, 0).getBlock().getType();

      // Break after a few tries
      if (tries++ >= config.getMaxTries()) {
        messaging.sendSafespotNotFoundMessage(p);
        break;
      }

      // Teleport if the highest block isn't blacklisted and safeTeleport is enabled
      if (config.isSafeTeleportEnabled() && !config.getUnsafeBlocks().contains(topBlock)) {
        p.teleport(newLocation.add(0.5D, 1D, 0.5D));
        messaging.sendTeleportSuccessMessage(p, (int) newLocation.distance(oldLocation));
        break;
      }
    }

    return true;
  }
}
