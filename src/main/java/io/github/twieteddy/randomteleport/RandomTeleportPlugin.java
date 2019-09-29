package io.github.twieteddy.randomteleport;

import com.wimbli.WorldBorder.WorldBorder;
import io.github.twieteddy.randomteleport.borders.Border;
import io.github.twieteddy.randomteleport.borders.PluginBorder;
import io.github.twieteddy.randomteleport.borders.VanillaBorder;
import io.github.twieteddy.randomteleport.commands.RtpCommand;
import java.io.File;
import java.util.HashMap;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class RandomTeleportPlugin extends JavaPlugin {
  private final HashMap<String, Object> options = new HashMap<>();
  private final HashMap<String, String> messages = new HashMap<>();
  private Border border;

  @Override
  public void onEnable() {
    loadConfigYml();
    loadMessagesYml();
    setupBorder();
    registerCommands();
  }

  private void loadConfigYml() {
    File configFile = new File(getDataFolder(), "config.yml");

    if (!configFile.exists())
      saveResource("config.yml", false);

    YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(configFile);
    options.put("safe_teleport", configYaml.get("safe_teleport"));
    options.put("border_mode", configYaml.get("border_mode"));
  }

  private void loadMessagesYml() {
    File messagesFile = new File(getDataFolder(), "messages.yml");

    if (!messagesFile.exists())
      saveResource("messages.yml", false);

    YamlConfiguration messagesYaml = YamlConfiguration.loadConfiguration(messagesFile);
    messages.put("player_feedback", messagesYaml.getString("player_feedback"));
    messages.put("safe_spot_not_found", messagesYaml.getString("safe_spot_not_found"));
  }

  private void registerCommands() {
    getCommand("rtp").setExecutor(new RtpCommand(this));
  }

  private void setupBorder() {
    if (getOption("border_mode").equals("plugin")) {
      WorldBorder worldBorderPlugin = (WorldBorder) getServer()
          .getPluginManager()
          .getPlugin("WorldBorder");
      border = new PluginBorder(worldBorderPlugin);
    } else {
      border = new VanillaBorder();
    }
  }

  @SuppressWarnings("WeakerAccess")
  public Object getOption(String name) {
    return options.getOrDefault(name, name);
  }

  public String getMessage(String name) {
    return messages.getOrDefault(name, name);
  }

  public Border getBorder() {
    return border;
  }
}
