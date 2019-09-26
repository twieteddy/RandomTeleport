package io.github.twieteddy.randomteleport;

import io.github.twieteddy.randomteleport.commands.RtpCommand;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class RandomTeleportPlugin extends JavaPlugin {
  private HashMap<String, Object> options = new HashMap<String, Object>();
  private HashMap<String, String> messages = new HashMap<String, String>();

  @Override
  public void onEnable() {
    loadConfig();
    registerCommands();
  }

  private void loadConfig() {
    File configFile = new File(getDataFolder(), "config.yml");
    File messagesFile = new File(getDataFolder(), "messages.yml");

    if (!configFile.exists())
      saveResource("config.yml", false);

    if (!messagesFile.exists())
      saveResource("messages.yml", false);

    YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(configFile);
    options.put("safe_teleport", configYaml.get("safe_teleport"));
    options.put("border_mode", configYaml.get("border_mode"));

    YamlConfiguration messagesYaml = YamlConfiguration.loadConfiguration(messagesFile);
    messages.put("player_feedback", messagesYaml.getString("player_feedback"));
    messages.put("safe_spot_not_found", messagesYaml.getString("safe_spot_not_found"));
  }

  private void registerCommands() {
    getCommand("rtp").setExecutor(new RtpCommand(this));
  }

  public Object getOption(String name) {
    return options.getOrDefault(name, name);
  }

  public String getMessage(String name) {
    return messages.getOrDefault(name, name);
  }
}
