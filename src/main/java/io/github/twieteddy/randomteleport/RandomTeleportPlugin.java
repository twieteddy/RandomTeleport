package io.github.twieteddy.randomteleport;

import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class RandomTeleportPlugin extends JavaPlugin {

  @Override
  public void onEnable() {
    Config config = new Config(this);
    getCommand("rtp").setExecutor(new RtpCommand(config));
  }
}
