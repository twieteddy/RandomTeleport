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
    BorderData data = worldBorderPlugin.getWorldBorder(world.getName());
    if (data == null) {
      return null;
    }

    Random random = new Random();
    Location center = new Location(world, data.getX(), 0, data.getZ());
    double x, z;

    // round border if getShape() is null or false, else it's rectangular
    if (data.getShape() != null && data.getShape()) {
      double rnd = random.nextDouble();
      double angle = rnd * 2 * Math.PI;
      x = data.getRadiusX() * Math.sqrt(rnd) * Math.cos(angle);
      z = data.getRadiusX() * Math.sqrt(rnd) * Math.sin(angle);
    } else {
      x = random.nextInt(data.getRadiusX() * 2) - data.getRadiusX();
      z = random.nextInt(data.getRadiusZ() * 2) - data.getRadiusZ();
    }

    return world.getHighestBlockAt(center.add((int) x, 0, (int) z)).getLocation();
  }
}
