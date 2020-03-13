package io.github.twieteddy.randomteleport;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;

public class Config {

  // Config keys
  private static final String KEY_SAFE_TELEPORT = "safe-teleport";
  private static final String KEY_MAX_TRIES = "max-tries";
  private static final String KEY_MAX_VANILLA_RADIUS = "max-vanilla-radius";
  private static final String KEY_COOLDOWN = "cooldown";
  private static final String KEY_UNSAFE_BLOCKS = "unsafe-blocks";

  // Settings with default values
  private final boolean isSafeTeleport;
  private final int maxTries;
  private final int cooldownInSeconds;
  private final int maxVanillaRadius;
  private final ArrayList<Material> unsafeBlocks;

  public Config(RandomTeleport plugin) {
    // Save default values from config.yml resource
    plugin.getConfig().options().copyDefaults(true);
    plugin.saveConfig();

    this.isSafeTeleport = plugin.getConfig().getBoolean(Config.KEY_SAFE_TELEPORT);
    this.maxTries = plugin.getConfig().getInt(Config.KEY_MAX_TRIES);
    this.cooldownInSeconds = plugin.getConfig().getInt(Config.KEY_COOLDOWN);
    this.maxVanillaRadius = plugin.getConfig().getInt(Config.KEY_MAX_VANILLA_RADIUS);
    this.unsafeBlocks = new ArrayList<>();

    List<String> unsafeBlockNames = plugin.getConfig().getStringList(Config.KEY_UNSAFE_BLOCKS);
    for (String unsafeBlockName : unsafeBlockNames) {
      Material material = Material.getMaterial(unsafeBlockName);
      if (material != null) {
        unsafeBlocks.add(material);
      }
    }
  }

  public boolean isSafeTeleportEnabled() {
    return this.isSafeTeleport;
  }

  public int getMaxTries() {
    return this.maxTries;
  }

  public int getCooldown() {
    return this.cooldownInSeconds;
  }

  public int getMaxVanillaRadius() {
    return this.maxVanillaRadius;
  }

  public ArrayList<Material> getUnsafeBlocks() {
    return this.unsafeBlocks;
  }
}
