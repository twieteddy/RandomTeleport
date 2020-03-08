package io.github.twieteddy.randomteleport;

import com.wimbli.WorldBorder.WorldBorder;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class RandomTeleport extends JavaPlugin {

  private static RandomTeleport plugin = null;
  private final Logger logger = Bukkit.getLogger();
  private Border border;

  @Override
  public void onEnable() {
    plugin = this;
    Config config = new Config(this);
    Messaging messaging = new Messaging(this);

    WorldBorder worldBorderPlugin =
        (WorldBorder) getServer().getPluginManager().getPlugin("WorldBorder");

    if (worldBorderPlugin != null) {
      border = new Border(config, worldBorderPlugin);
    }

    getCommand("rtp").setExecutor(new RtpCommand(config, messaging, border));
  }

  @Override
  public void onDisable() {
    plugin = null;
  }
}
