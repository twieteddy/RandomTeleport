package io.github.twieteddy.randomteleport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

class Config {
  private static final String CONFIG_FILENAME = "config.yml";

  // Config keys
  private static final String KEY_SAFE_TELEPORT = "safe-teleport";
  private static final String KEY_MAX_TRIES = "max-tries";
  private static final String KEY_MAX_VANILLA_RADIUS = "max-vanilla-radius";
  private static final String KEY_COOLDOWN = "cooldown";
  private static final String KEY_UNSAFE_BLOCKS = "unsafe-blocks";

  // Settings with default values
  private final boolean isSafeTeleport = true;
  private final int maxTries = 10;
  private final int cooldownInSeconds = 10;
  private final int maxVanillaRadius = 4096;
  private final ArrayList<Material> unsafeBlocks = new ArrayList<>();

  Config(RandomTeleport plugin) {
    File file = new File(plugin.getDataFolder(), CONFIG_FILENAME);
    YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

    unsafeBlocks.add(Material.BEDROCK);
    unsafeBlocks.add(Material.FIRE);
    unsafeBlocks.add(Material.LAVA);
    unsafeBlocks.add(Material.MAGMA);
    unsafeBlocks.add(Material.WATER);
    unsafeBlocks.add(Material.STATIONARY_WATER);
    unsafeBlocks.add(Material.SIGN);
    unsafeBlocks.add(Material.SIGN_POST);
    unsafeBlocks.add(Material.WALL_SIGN);
    unsafeBlocks.add(Material.VINE);
    unsafeBlocks.add(Material.STRING);
    unsafeBlocks.add(Material.GOLD_PLATE);
    unsafeBlocks.add(Material.IRON_PLATE);
    unsafeBlocks.add(Material.STONE_PLATE);
    unsafeBlocks.add(Material.WOOD_PLATE);
    unsafeBlocks.add(Material.FENCE_GATE);
    unsafeBlocks.add(Material.PORTAL);
    unsafeBlocks.add(Material.ENDER_PORTAL);

    yaml.addDefault(KEY_SAFE_TELEPORT, isSafeTeleport);
    yaml.addDefault(KEY_MAX_VANILLA_RADIUS, maxVanillaRadius);
    yaml.addDefault(KEY_MAX_TRIES, maxTries);
    yaml.addDefault(KEY_COOLDOWN, cooldownInSeconds);
    yaml.addDefault(KEY_UNSAFE_BLOCKS, unsafeBlocks);
    yaml.options().copyDefaults(true);

    try {
      yaml.save(file);
    } catch (IOException e) {
      Bukkit.getLogger().severe("Couldn't save " + CONFIG_FILENAME);
    }
  }

  boolean isSafeTeleportEnabled() {
    return this.isSafeTeleport;
  }

  int getMaxTries() {
    return this.maxTries;
  }

  int getCooldown() {
    return this.cooldownInSeconds;
  }

  int getMaxVanillaRadius() {
    return this.maxVanillaRadius;
  }

  ArrayList<Material> getUnsafeBlocks() {
    return this.unsafeBlocks;
  }
}
