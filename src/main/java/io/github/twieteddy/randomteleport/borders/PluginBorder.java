package io.github.twieteddy.randomteleport.borders;

import com.wimbli.WorldBorder.BorderData;
import com.wimbli.WorldBorder.WorldBorder;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.World;


public class PluginBorder extends Border {

  private final WorldBorder worldBorderPlugin;

  public PluginBorder(WorldBorder worldBorderPlugin) {
    this.worldBorderPlugin = worldBorderPlugin;
  }

  @SuppressWarnings("unused")
  @Override
  public Location getRandomLocation(World world) {
    Random random = new Random();
    BorderData data = worldBorderPlugin.getWorldBorder(world.getName());

    Location center = new Location(world, data.getX(), 0, data.getZ());

    int x = random.nextInt(data.getRadiusX() * 2) - data.getRadiusX();
    int z = random.nextInt(data.getRadiusZ() * 2) - data.getRadiusZ();

    return world.getHighestBlockAt(center.add(x, 0, z))
        .getLocation()
        .add(0, 0.5D, 0);
  }
}
