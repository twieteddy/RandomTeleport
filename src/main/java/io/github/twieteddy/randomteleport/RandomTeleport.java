package io.github.twieteddy.randomteleport;

import com.wimbli.WorldBorder.WorldBorder;
import io.github.twieteddy.randomteleport.borders.Border;
import io.github.twieteddy.randomteleport.borders.PluginBorder;
import io.github.twieteddy.randomteleport.borders.VanillaBorder;
import io.github.twieteddy.randomteleport.commands.RtpCommand;
import io.github.twieteddy.randomteleport.constants.Commands;
import io.github.twieteddy.randomteleport.constants.Filenames;
import io.github.twieteddy.randomteleport.constants.Messages;
import io.github.twieteddy.randomteleport.constants.Properties;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    File configFile = new File(getDataFolder(), Filenames.CONFIG_YAML);
    if (!configFile.exists()) {
      saveResource(Filenames.CONFIG_YAML, false);
    }

    YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(configFile);
    properties.put(Properties.SAFE_TELEPORT, configYaml.get(Properties.SAFE_TELEPORT));
    properties.put(Properties.BORDER_MODE, configYaml.get(Properties.BORDER_MODE));
    properties.put(Properties.MAX_TRIES, configYaml.get(Properties.MAX_TRIES));

    log("UNSAFE BLOCKS:");
    List<Material> unsafeBlocks = new ArrayList<>();
    for (String materialName : configYaml.getStringList(Properties.UNSAFE_BLOCKS)) {
      Material material = Material.matchMaterial(materialName);
      if (material != null) {
        unsafeBlocks.add(material);
        log(" - " + materialName);
      } else {
        log(" - " + materialName + " not found");
      }
    }

    properties.put(Properties.UNSAFE_BLOCKS, unsafeBlocks);
  }

  private void loadMessagesYml() {
    File messagesFile = new File(getDataFolder(), Filenames.MESSAGES_YAML);
    if (!messagesFile.exists()) {
      saveResource(Filenames.MESSAGES_YAML, false);
    }

    YamlConfiguration messagesYaml = YamlConfiguration.loadConfiguration(messagesFile);
    messages.put(
        Messages.PLAYER_FEEDBACK,
        translateColor(messagesYaml.getString(Messages.PLAYER_FEEDBACK)));
    messages.put(
        Messages.SAFE_SPOT_NOT_FOUND,
        translateColor(messagesYaml.getString(Messages.SAFE_SPOT_NOT_FOUND)));
    messages.put(
        Messages.BORDER_NOT_CONFIGURED,
        translateColor(messagesYaml.getString(Messages.BORDER_NOT_CONFIGURED)));
  }

  private void registerCommands() {
    getCommand(Commands.RTP).setExecutor(new RtpCommand(this));
  }

  private void setupBorder() {
    WorldBorder worldBorderPlugin = (WorldBorder) getServer()
        .getPluginManager()
        .getPlugin("WorldBorder");

    if (getProperty(Properties.BORDER_MODE).equals("plugin") && worldBorderPlugin != null) {
      border = new PluginBorder(worldBorderPlugin);
    } else {
      border = new VanillaBorder();
    }
  }

  private String translateColor(String text) {
    return ChatColor.translateAlternateColorCodes('&', text);
  }

  @SuppressWarnings("WeakerAccess")
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
    logger.info(String.format("[%s] %s",
        getClass().getSimpleName(),
        message));
  }
}
