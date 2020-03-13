package io.github.twieteddy.randomteleport;

import com.wimbli.WorldBorder.WorldBorder;
import io.github.twieteddy.randomteleport.commands.RtpCommand;
import io.github.twieteddy.randomteleport.configs.GeneralConfig;
import io.github.twieteddy.randomteleport.configs.MessageConfig;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class RandomTeleportPlugin extends JavaPlugin {

  private static final Logger logger = Bukkit.getLogger();
  private Border border;

  @Override
  public void onEnable() {
    GeneralConfig generalConfig = new GeneralConfig(this);
    MessageConfig messaging = new MessageConfig(this);

    WorldBorder worldBorderPlugin =
        (WorldBorder) getServer().getPluginManager().getPlugin("WorldBorder");

    if (worldBorderPlugin != null) {
      border = new Border(generalConfig, worldBorderPlugin);
    }

    getCommand("rtp").setExecutor(new RtpCommand(generalConfig, messaging, border));
  }
}
