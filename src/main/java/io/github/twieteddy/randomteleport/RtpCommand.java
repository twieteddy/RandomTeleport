package io.github.twieteddy.randomteleport;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RtpCommand implements CommandExecutor {

  private final Config config;
  private final Border border;
  private final HashMap<UUID, Long> cooldowns;

  public RtpCommand(Config config) {
    this.config = config;
    this.border = config.getBorder();
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
    Location oldLocation = player.getLocation();

    // Calculate cooldown
    if (!player.hasPermission("randomteleport.cooldown.bypass")) {
      Long lastCommandUsage = cooldowns.getOrDefault(player.getUniqueId(), 0L);
      long nextUsage = lastCommandUsage + config.getCooldown() * 1000;
      if (nextUsage > System.currentTimeMillis()) {
        long diff = nextUsage - System.currentTimeMillis();
        player.sendMessage(config.getCooldownMessage(config.getCooldown(), diff));
        return true;
      } else {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
      }
    }

    // Search for safe spot and teleport
    for (int i = 0; i < config.getMaxTries(); i++) {
      Location newLocation = border.getRandomLocation(player.getWorld());
      if (config.isSafeTeleportEnabled()) {
        Material blockType = newLocation.getBlock().getType();
        if (config.getUnsafeBlocks().contains(blockType)) {
          continue;
        }
      }
      player.teleport(newLocation.add(0.5d, 1d, 0.5d));
      player.sendMessage(config.getTeleportSuccessMessage((int) newLocation.distance(oldLocation)));
      return true;
    }

    player.sendMessage(config.getSafespotNotFoundMessage());
    return false;
  }
}
