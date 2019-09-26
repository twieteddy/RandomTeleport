package io.github.twieteddy.randomteleport.commands;

import io.github.twieteddy.randomteleport.RandomTeleportPlugin;
import java.util.Random;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RtpCommand implements CommandExecutor {
  private final String playerFeedback;
  private final String safeSpotNotFound;

  public RtpCommand(RandomTeleportPlugin plugin) {
    playerFeedback = plugin.getMessage("player_feedback");
    safeSpotNotFound = plugin.getMessage("safe_spot_not_found");
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!(sender instanceof Player))
      return true;

    Player p = (Player) sender;
    Random random = new Random();
    WorldBorder border = p.getWorld().getWorldBorder();

    double size = (int) border.getSize();
    int x = random.nextInt((int) size / 2);
    int z = random.nextInt((int) size / 2);

    Location oldLocation = p.getLocation();
    Location newLocation = p.getWorld()
        .getHighestBlockAt(x, z)
        .getLocation()
        .add(border.getCenter());
    newLocation = p.getWorld().getHighestBlockAt(newLocation).getLocation();

    double distance = newLocation.distance(oldLocation);

    p.teleport(newLocation);
    p.spigot().sendMessage(
        ChatMessageType.CHAT,
        new ComponentBuilder(parsePlayerFeedback(
            oldLocation.getX(), oldLocation.getY(), oldLocation.getZ(),
            newLocation.getX(), newLocation.getY(), newLocation.getZ(),
            distance))
          .create());
    return true;
  }

  private String parsePlayerFeedback(
      double x_old, double y_old, double z_old,
      double x_new, double y_new, double z_new,
      double distance) {
    return ChatColor.translateAlternateColorCodes('&', playerFeedback)
        .replace("%_DISTANCE", String.valueOf((int) distance))
        .replace("%_OLD_X", String.valueOf((int) x_old))
        .replace("%_OLD_Y", String.valueOf((int) y_old))
        .replace("%_OLD_Z", String.valueOf((int) z_old))
        .replace("%_NEW_X", String.valueOf((int) x_new))
        .replace("%_NEW_Y", String.valueOf((int) y_new))
        .replace("%_NEW_Z", String.valueOf((int) z_new));
  }
}