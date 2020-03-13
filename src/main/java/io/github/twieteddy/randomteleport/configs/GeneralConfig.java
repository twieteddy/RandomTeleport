package io.github.twieteddy.randomteleport.configs;

import io.github.twieteddy.randomteleport.RandomTeleportPlugin;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class GeneralConfig {

  // Config keys
  private static class Keys {
    private static final String SAFE_TELEPORT = "safe-teleport";
    private static final String MAX_TRIES = "max-tries";
    private static final String MAX_VANILLA_RADIUS = "max-vanilla-radius";
    private static final String COOLDOWN = "cooldown";
    private static final String UNSAFE_BLOCKS = "unsafe-blocks";
  }

  // Settings with default values
  private final boolean isSafeTeleport;
  private final int maxTries;
  private final int cooldownInSeconds;
  private final int maxVanillaRadius;
  private final ArrayList<Material> unsafeBlocks;

  public GeneralConfig(RandomTeleportPlugin plugin) {
    // Save default values from config.yml resource
    plugin.getConfig().options().copyDefaults(true);
    plugin.saveConfig();

    this.isSafeTeleport = plugin.getConfig().getBoolean(GeneralConfig.Keys.SAFE_TELEPORT);
    this.maxTries = plugin.getConfig().getInt(GeneralConfig.Keys.MAX_TRIES);
    this.cooldownInSeconds = plugin.getConfig().getInt(GeneralConfig.Keys.COOLDOWN);
    this.maxVanillaRadius = plugin.getConfig().getInt(GeneralConfig.Keys.MAX_VANILLA_RADIUS);
    this.unsafeBlocks = new ArrayList<>();

    List<String> unsafeBlockNames = plugin.getConfig().getStringList(GeneralConfig.Keys.UNSAFE_BLOCKS);
    for (String unsafeBlockName : unsafeBlockNames) {
      Material material = Material.getMaterial(unsafeBlockName);
      if (material == null) {
        Bukkit.getLogger().info("Material not found: " + unsafeBlockName);
        continue;
      }
      unsafeBlocks.add(material);
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
