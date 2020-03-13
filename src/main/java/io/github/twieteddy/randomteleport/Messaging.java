package io.github.twieteddy.randomteleport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Messaging {
  private final HashMap<String, String> messages;
  private static final String CONFIG_FILENAME = "messages.yml";

  // Message keys
  private static final String KEY_TELEPORT_SUCCESS = "player-feedback.teleport-success";
  private static final String KEY_SAFESPOT_NOT_FOUND = "player-feedback.safespot-not-found";
  private static final String KEY_COOLDOWN = "player-feedback.cooldown";

  // Text variables
  private static final String VAR_DISTANCE = "$distanceTeleported";
  private static final String VAR_COOLDOWN = "$cooldown";
  private static final String VAR_TIME_LEFT = "$timeLeft";

  // Default messages
  private static final String MSG_TELEPORT_SUCCESS = "&aDu wurdest $distanceTeleported "
      + "Blöcke weit weg teleportiert";
  private static final String MSG_SAFESPOT_NOT_FOUND = "&cEin sicherer Ort konnte leider "
      + "nicht gefunden werden :(";
  private static final String MSG_COOLDOWN = "&cDu darfst dich nur alle $cooldown Sekunden "
      + "teleportieren. Versuche es in $timeLeft Sekunden noch einmal";

  public Messaging(RandomTeleport plugin) {
    File file = new File(plugin.getDataFolder(), CONFIG_FILENAME);
    YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
    messages = new HashMap<>();

    yaml.addDefault(KEY_TELEPORT_SUCCESS, MSG_TELEPORT_SUCCESS);
    yaml.addDefault(KEY_SAFESPOT_NOT_FOUND, MSG_SAFESPOT_NOT_FOUND);
    yaml.addDefault(KEY_COOLDOWN, MSG_COOLDOWN);
    yaml.options().copyDefaults(true);

    try {
      yaml.save(file);
    } catch(IOException e) {
      Bukkit.getLogger().severe("Couldn't save "+ CONFIG_FILENAME);
    }

    for (String key : yaml.getKeys(true)) {
      String message = yaml.getString(key);
      messages.put(key, translate(message));
    }
  }

  private String translate(String text)  {
    return ChatColor.translateAlternateColorCodes('&', text);
  }

  public void sendTeleportSuccessMessage(Player player, int distanceTeleported) {
    String message = getMessage(KEY_TELEPORT_SUCCESS)
      .replace(VAR_DISTANCE, String.valueOf(distanceTeleported));
    player.sendMessage(message);
  }

  public void sendSafespotNotFoundMessage(Player player) {
    String message = getMessage(KEY_SAFESPOT_NOT_FOUND);
    player.sendMessage(message);
  }

  public void sendCooldownMessage(Player player, int cooldown, long timeLeft) {
    String message = getMessage(KEY_COOLDOWN)
        .replace(VAR_COOLDOWN, String.valueOf(cooldown))
        .replace(VAR_TIME_LEFT, String.format("%.3f", (float) timeLeft / 1000));
    player.sendMessage(message);
  }

  private String getMessage(String path) {
    return messages.getOrDefault(path, path);
  }
}
