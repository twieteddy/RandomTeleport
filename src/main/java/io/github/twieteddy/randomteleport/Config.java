package io.github.twieteddy.randomteleport;

import com.wimbli.WorldBorder.WorldBorder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

  // Config keys
  private static class Key {

    public static final String RTP_ONJOIN_WORLDS = "rtp-onjoin-worlds";
    private static final String SAFE_TELEPORT = "safe-teleport";
    private static final String MAX_TRIES = "max-tries";
    private static final String MAX_VANILLA_RADIUS = "max-vanilla-radius";
    private static final String COOLDOWN = "cooldown";
    private static final String UNSAFE_BLOCKS = "unsafe-blocks";
    private static final String MSG_TELEPORT_SUCCESS = "messages.teleport-success";
    private static final String MSG_SAFESPOT_NOT_FOUND = "messages.safespot-not-found";
    private static final String MSG_COOLDOWN = "messages.cooldown";
    private static final String MSG_ONJOIN_RTP = "messages.onjoin-rtp";
  }

  private static class Variable {
    private static final String DISTANCE = "$distanceTeleported";
    private static final String COOLDOWN = "$cooldown";
    private static final String TIME_LEFT = "$timeLeft";
  }

  private final RandomTeleportPlugin plugin;
  private final Border border;

  // Settings with default values
  private final boolean isSafeTeleport;
  private final int maxTries;
  private final int cooldownInSeconds;
  private final int maxVanillaRadius;
  private final ArrayList<Material> unsafeBlocks;
  private final ArrayList<String> rtpOnjoinWorlds;
  private final HashMap<String, String> messages;

  public Config(RandomTeleportPlugin plugin) {
    this.plugin = plugin;

    // Save default values from config.yml resource
    FileConfiguration config = plugin.getConfig();
    config.options().copyDefaults(true);
    plugin.saveConfig();

    this.isSafeTeleport = config.getBoolean(Config.Key.SAFE_TELEPORT);
    this.maxTries = config.getInt(Config.Key.MAX_TRIES);
    this.cooldownInSeconds = config.getInt(Config.Key.COOLDOWN);
    this.maxVanillaRadius = config.getInt(Config.Key.MAX_VANILLA_RADIUS);

    this.messages = new HashMap<>();
    this.messages.put(Key.MSG_TELEPORT_SUCCESS, config.getString(Key.MSG_TELEPORT_SUCCESS));
    this.messages.put(Key.MSG_SAFESPOT_NOT_FOUND, config.getString(Key.MSG_SAFESPOT_NOT_FOUND));
    this.messages.put(Key.MSG_COOLDOWN, config.getString(Key.MSG_COOLDOWN));
    this.messages.put(Key.MSG_ONJOIN_RTP, config.getString(Key.MSG_ONJOIN_RTP));

    // Apply alternative color codes
    this.messages.replaceAll((k, v) -> ChatColor.translateAlternateColorCodes('&', v));

    this.unsafeBlocks = new ArrayList<>();
    List<String> unsafeBlockNames = plugin.getConfig().getStringList(Config.Key.UNSAFE_BLOCKS);
    for (String unsafeBlockName : unsafeBlockNames) {
      Material material = Material.getMaterial(unsafeBlockName);
      if (material == null) {
        Bukkit.getLogger().info(() -> "Material not found: " + unsafeBlockName);
        continue;
      }
      unsafeBlocks.add(material);
    }

    this.rtpOnjoinWorlds = new ArrayList<>();
    rtpOnjoinWorlds.addAll(plugin.getConfig().getStringList(Key.RTP_ONJOIN_WORLDS));
    this.border = new Border(maxVanillaRadius);
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

  public List<Material> getUnsafeBlocks() {
    return this.unsafeBlocks;
  }

  public String getCooldownMessage(int cooldown, long timeLeft) {
    return this.messages
        .get(Key.MSG_COOLDOWN)
        .replace(Variable.COOLDOWN, String.valueOf(cooldown))
        .replace(Variable.TIME_LEFT, String.format("%.3f", (float) timeLeft / 1000));
  }

  public String getSafespotNotFoundMessage() {
    return this.messages.get(Key.MSG_SAFESPOT_NOT_FOUND);
  }

  public String getOnjoinRtpMessage() {
    return this.messages.get(Key.MSG_ONJOIN_RTP);
  }

  public String getTeleportSuccessMessage(int distanceTeleported) {
    return this.messages
        .get(Key.MSG_TELEPORT_SUCCESS)
        .replace(Variable.DISTANCE, String.valueOf(distanceTeleported));
  }

  public List<String> getRtpOnjoinWorlds() {
    return this.rtpOnjoinWorlds;
  }

  public RandomTeleportPlugin RandomTeleportPlugin() {
    return this.plugin;
  }

  public Border getBorder() {
    return this.border;
  }
}
