package io.github.twieteddy.randomteleport.commands;

import io.github.twieteddy.randomteleport.RandomTeleport;
import io.github.twieteddy.randomteleport.borders.Border;
import io.github.twieteddy.randomteleport.constants.Messages;
import io.github.twieteddy.randomteleport.constants.Permissions;
import io.github.twieteddy.randomteleport.constants.Properties;
import io.github.twieteddy.randomteleport.constants.Variables;
import java.util.ArrayList;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
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
    super();
    playerFeedback = plugin.getMessage(Messages.PLAYER_FEEDBACK);
    safeSpotNotFound = plugin.getMessage(Messages.SAFE_SPOT_NOT_FOUND);
    borderNotConfigured = plugin.getMessage(Messages.BORDER_NOT_CONFIGURED);
    maxTries = (Integer) plugin.getProperty(Properties.MAX_TRIES);
    safeTeleport = (Boolean) plugin.getProperty(Properties.SAFE_TELEPORT);
    unsafeBlocks = (ArrayList<Material>) plugin.getProperty(Properties.UNSAFE_BLOCKS);
    border = plugin.getBorder();
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!(sender instanceof Player)) {
      return true;
    }

    if (!sender.hasPermission(Permissions.RTP)) {
      return true;
    }

    Player p = (Player) sender;
    Location oldLocation = p.getLocation();
    Location newLocation = border.getRandomLocation(p.getWorld());

    if (newLocation == null) {
      p.spigot().sendMessage(
          ChatMessageType.CHAT,
          new ComponentBuilder(borderNotConfigured).create()
      );
      return true;
    }

    for (int i = 0; i < maxTries && safeTeleport; i++) {
      Material topBlock = newLocation.subtract(0, 1, 0).getBlock().getType();
      if (!unsafeBlocks.contains(topBlock)) {
        break;
      }
      if (i == maxTries - 1) {
        p.spigot().sendMessage(
            ChatMessageType.CHAT,
            new ComponentBuilder(safeSpotNotFound).create());
        return true;
      }
      newLocation = border.getRandomLocation(p.getWorld());
    }

    p.teleport(newLocation.add(0.5D, 1D, 0.5D));
    p.spigot().sendMessage(
        ChatMessageType.CHAT,
        new ComponentBuilder(
            parsePlayerFeedback(
                oldLocation.getX(), oldLocation.getY(), oldLocation.getZ(),
                newLocation.getX(), newLocation.getY(), newLocation.getZ(),
                newLocation.distance(oldLocation)))
            .create());
    return true;
  }

  private String parsePlayerFeedback(
      double xOld, double yOld, double zOld,
      double xNew, double yNew, double zNew,
      double distance) {
    return playerFeedback
        .replace(Variables.DISTANCE, String.valueOf((int) distance))
        .replace(Variables.OLD_X, String.valueOf((int) xOld))
        .replace(Variables.OLD_Y, String.valueOf((int) yOld))
        .replace(Variables.OLD_Z, String.valueOf((int) zOld))
        .replace(Variables.NEW_X, String.valueOf((int) xNew))
        .replace(Variables.NEW_Y, String.valueOf((int) yNew))
        .replace(Variables.NEW_Z, String.valueOf((int) zNew));
  }
}
