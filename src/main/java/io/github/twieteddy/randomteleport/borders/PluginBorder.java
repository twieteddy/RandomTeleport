package io.github.twieteddy.randomteleport.borders;

import com.wimbli.WorldBorder.BorderData;
import com.wimbli.WorldBorder.WorldBorder;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Location;
import org.bukkit.World;

public class PluginBorder extends Border {

  private final WorldBorder worldBorderPlugin;

  public PluginBorder(WorldBorder worldBorderPlugin) {
    this.worldBorderPlugin = worldBorderPlugin;
  }

  @Override
  public Location getRandomLocation(World world) {
    BorderData data = worldBorderPlugin.getWorldBorder(world.getName());
    if (data == null) {
      return null;
    }

    ThreadLocalRandom random = ThreadLocalRandom.current();
    Location center = new Location(world, data.getX(), 0, data.getZ());
    double x, z, d;

    do {
      x = random.nextDouble(-1 * data.getRadiusX(), data.getRadiusX());
      z = random.nextDouble(-1 * data.getRadiusZ(), data.getRadiusZ());
      d =
          Math.sqrt(
              Math.pow((2 * x) / (data.getRadiusX() * 2), 2)
                  + Math.pow((2 * z) / (data.getRadiusZ() * 2), 2));
    } while (data.getShape() != null && data.getShape() && d > 1);

    return world.getHighestBlockAt(center.add((int) x, 0, (int) z)).getLocation();
  }
}
