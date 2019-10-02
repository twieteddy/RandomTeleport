package io.github.twieteddy.randomteleport.commands;

import io.github.twieteddy.randomteleport.RandomTeleport;
import io.github.twieteddy.randomteleport.borders.Border;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RtpCommand implements CommandExecutor {

  private final String playerFeedback;
  private final String safeSpotNotFound;
  private final String borderNotConfigured;
  private final ArrayList<Material> unsafeBlocks;
  private final int maxTries;
  private final Boolean safeTeleport;
  private final Border border;

  public RtpCommand(RandomTeleport plugin) {
    playerFeedback = plugin.getMessage("player_feedback");
    safeSpotNotFound = plugin.getMessage("safe_spot_not_found");
    borderNotConfigured = plugin.getMessage("border_not_configured");
    maxTries = (Integer) plugin.getProperty("max_tries");
    safeTeleport = (Boolean) plugin.getProperty("safe_teleport");
    //noinspection unchecked
    unsafeBlocks = (ArrayList<Material>) plugin.getProperty("unsafe_blocks");
    border = plugin.getBorder();
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!(sender instanceof Player)) {
      return true;
    }

    if (!sender.hasPermission("randomteleport.command.rtp")) {
      return true;
    }

    Player p = (Player) sender;
    Location oldLocation = p.getLocation();
    Location newLocation = border.getRandomLocation(p.getWorld());

    if (newLocation == null) {
      p.sendMessage(borderNotConfigured);
      return true;
    }

    for (int i = 0; i < maxTries && safeTeleport; i++) {
      Material topBlock = newLocation.subtract(0, 1, 0).getBlock().getType();
      if (!unsafeBlocks.contains(topBlock)) {
        break;
      }
      if (i == maxTries - 1) {
        p.sendMessage(safeSpotNotFound);
        return true;
      }
      newLocation = border.getRandomLocation(p.getWorld());
    }

    p.teleport(newLocation.add(0.5D, 1D, 0.5D));
    p.sendMessage(
        playerFeedback
            .replace("%_DISTANCE", String.valueOf((int) newLocation.distance(oldLocation)))
            .replace("%_OLD_X", String.valueOf((int) oldLocation.getX()))
            .replace("%_OLD_Y", String.valueOf((int) oldLocation.getY()))
            .replace("%_OLD_Z", String.valueOf((int) oldLocation.getZ()))
            .replace("%_NEW_X", String.valueOf((int) newLocation.getX()))
            .replace("%_NEW_Y", String.valueOf((int) newLocation.getY()))
            .replace("%_NEW_Z", String.valueOf((int) newLocation.getZ())));
    return true;
  }
}
