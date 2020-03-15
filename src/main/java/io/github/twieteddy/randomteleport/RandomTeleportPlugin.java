package io.github.twieteddy.randomteleport;

import com.wimbli.WorldBorder.WorldBorder;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class RandomTeleportPlugin extends JavaPlugin {

  @Override
  public void onEnable() {
    Config config = new Config(this);

    WorldBorder worldBorderPlugin =
        (WorldBorder) getServer().getPluginManager().getPlugin("WorldBorder");

    getCommand("rtp").setExecutor(new RtpCommand(config, new Border(config, worldBorderPlugin)));
  }
}
