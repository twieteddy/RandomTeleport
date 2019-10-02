package io.github.twieteddy.randomteleport;

import com.wimbli.WorldBorder.WorldBorder;
import io.github.twieteddy.randomteleport.borders.Border;
import io.github.twieteddy.randomteleport.borders.PluginBorder;
import io.github.twieteddy.randomteleport.commands.RtpCommand;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class RandomTeleport extends JavaPlugin {

  private final Logger logger = Bukkit.getLogger();
  private final HashMap<String, Object> properties = new HashMap<>();
  private final HashMap<String, String> messages = new HashMap<>();
  private Border border;

  @Override
  public void onEnable() {
    loadConfigYml();
    loadMessagesYml();
    setupBorder();
    registerCommands();

    log("Border: " + border.getClass().getSimpleName());
  }

  private void loadConfigYml() {
    File configFile = new File(getDataFolder(), "config.yml");
    if (!configFile.exists()) {
      saveResource("config.yml", false);
    }

    YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(configFile);
    properties.put("safe_teleport", configYaml.get("safe_teleport"));
    properties.put("border_mode", configYaml.get("border_mode"));
    properties.put("max_tries", configYaml.get("max_tries"));

    log("UNSAFE BLOCKS:");
    ArrayList<Material> unsafeBlocks = new ArrayList<>();
    for (String materialName : configYaml.getStringList("unsafe_blocks")) {
      Material material = Material.matchMaterial(materialName);
      if (material != null) {
        unsafeBlocks.add(material);
        log(" - " + materialName);
      } else {
        log(" - " + materialName + " not found");
      }
    }
    properties.put("unsafe_blocks", unsafeBlocks);
  }

  private void loadMessagesYml() {
    File messagesFile = new File(getDataFolder(), "messages.yml");
    if (!messagesFile.exists()) {
      saveResource("messages.yml", false);
    }

    YamlConfiguration messagesYaml = YamlConfiguration.loadConfiguration(messagesFile);
    messages.put(
        "player_feedback", translateColor(messagesYaml.getString("player_feedback")));
    messages.put(
        "safe_spot_not_found", translateColor(messagesYaml.getString("safe_spot_not_found")));
    messages.put(
        "border_not_configured", translateColor(messagesYaml.getString("border_not_configured")));
  }

  private void registerCommands() {
    getCommand("rtp").setExecutor(new RtpCommand(this));
  }

  private void setupBorder() {
    WorldBorder worldBorderPlugin =
        (WorldBorder) getServer().getPluginManager().getPlugin("WorldBorder");

    if (getProperty("border_mode").equals("plugin") && worldBorderPlugin != null) {
      border = new PluginBorder(worldBorderPlugin);
    } else {
      border = new Border();
    }
  }

  private String translateColor(String message) {
    return ChatColor.translateAlternateColorCodes('&', message);
  }

  public Object getProperty(String name) {
    return properties.getOrDefault(name, name);
  }

  public String getMessage(String name) {
    return messages.getOrDefault(name, name);
  }

  public Border getBorder() {
    return border;
  }

  private void log(String message) {
    logger.info(String.format("[%s] %s", getClass().getSimpleName(), message));
  }
}
