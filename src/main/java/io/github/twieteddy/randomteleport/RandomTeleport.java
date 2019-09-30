package io.github.twieteddy.randomteleport;

import com.wimbli.WorldBorder.WorldBorder;
import io.github.twieteddy.randomteleport.borders.Border;
import io.github.twieteddy.randomteleport.borders.PluginBorder;
import io.github.twieteddy.randomteleport.borders.VanillaBorder;
import io.github.twieteddy.randomteleport.commands.RtpCommand;
import io.github.twieteddy.randomteleport.dataclasses.Commands;
import io.github.twieteddy.randomteleport.dataclasses.Configs;
import io.github.twieteddy.randomteleport.dataclasses.Filenames;
import io.github.twieteddy.randomteleport.dataclasses.Messages;
import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class RandomTeleport extends JavaPlugin {

  private final Logger logger = Bukkit.getLogger();
  private final HashMap<String, Object> options = new HashMap<>();
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
    File configFile = new File(getDataFolder(), Filenames.CONFIG);

    if (!configFile.exists()) {
      saveResource(Filenames.CONFIG, false);
    }

    YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(configFile);
    options.put(
        Configs.SAFE_TELEPORT.toString(),
        configYaml.get(Configs.SAFE_TELEPORT.toString()));
    options.put(
        Configs.BORDER_MODE,
        configYaml.get(Configs.BORDER_MODE));
  }

  private void loadMessagesYml() {
    File messagesFile = new File(getDataFolder(), Filenames.MESSAGES);

    if (!messagesFile.exists()) {
      saveResource(Filenames.MESSAGES, false);
    }

    YamlConfiguration messagesYaml = YamlConfiguration.loadConfiguration(messagesFile);
    messages.put(
        Messages.PLAYER_FEEDBACK,
        messagesYaml.getString(Messages.PLAYER_FEEDBACK));
    messages.put(
        Messages.SAFE_SPOT_NOT_FOUND,
        messagesYaml.getString(Messages.PLAYER_FEEDBACK));
  }

  private void registerCommands() {
    getCommand(Commands.RTP).setExecutor(new RtpCommand(this));
  }

  private void setupBorder() {
    WorldBorder worldBorderPlugin = (WorldBorder) getServer()
        .getPluginManager()
        .getPlugin("WorldBorder");

    if (getOption(Configs.BORDER_MODE).equals("plugin") && worldBorderPlugin != null) {
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

  public void log(String message) {
    logger.info(String.format("[%s] %s",
        getClass().getSimpleName(),
        message));
  }
}
