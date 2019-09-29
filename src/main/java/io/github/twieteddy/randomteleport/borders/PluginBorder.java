package io.github.twieteddy.randomteleport.borders;

import com.wimbli.WorldBorder.WorldBorder;
import org.bukkit.Location;
import org.bukkit.World;


public class PluginBorder extends Border {
  @SuppressWarnings({"FieldCanBeLocal", "unused"})
  private final WorldBorder worldBorderPlugin;

  public PluginBorder(WorldBorder worldBorderPlugin) {
    this.worldBorderPlugin = worldBorderPlugin;
  }

  @SuppressWarnings("unused")
  @Override
  public Location getRandomLocation(World world) {
    return new Location(world, 0, 128, 0);
  }
}
