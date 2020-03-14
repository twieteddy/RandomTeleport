package io.github.twieteddy.randomteleport;

import com.wimbli.WorldBorder.WorldBorder;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class RandomTeleportPlugin extends JavaPlugin {

  private Border border;

  @Override
  public void onEnable() {
    Config config = new Config(this);

    WorldBorder worldBorderPlugin =
        (WorldBorder) getServer().getPluginManager().getPlugin("WorldBorder");

    if (worldBorderPlugin != null) {
      border = new Border(config, worldBorderPlugin);
    }

    getCommand("rtp").setExecutor(new RtpCommand(config, border));
  }
}
