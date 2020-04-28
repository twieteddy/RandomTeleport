package io.github.twieteddy.randomteleport;

import com.wimbli.WorldBorder.BorderData;
import com.wimbli.WorldBorder.WorldBorder;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Border {

  private final WorldBorder worldBorderPlugin;
  private final ThreadLocalRandom random = ThreadLocalRandom.current();
  private final int maxVanillaRadius;

  public Border(int maxVanillaRadius) {
    this.maxVanillaRadius = maxVanillaRadius;
    this.worldBorderPlugin = (WorldBorder) Bukkit.getServer().getPluginManager()
        .getPlugin("WorldBorder");
  }

  public Location getRandomLocation(World world) {
    if (worldBorderPlugin != null) {
      BorderData borderData = worldBorderPlugin.getWorldBorder(world.getName());
      if (borderData != null) {
        return getRandomLocationFromPlugin(world, borderData);
      }
    }

    return getRandomLocationFromVanilla(world);
  }

  private Location getRandomLocationFromPlugin(World world, BorderData data) {
    double distanceX = data.getRadiusX() * Math.sqrt(random.nextDouble());
    double distanceZ = data.getRadiusZ() * Math.sqrt(random.nextDouble());
    double angle = random.nextDouble() * 2 * Math.PI;

    int x = (int) (data.getX() + distanceX * Math.cos(angle));
    int z = (int) (data.getZ() + distanceZ * Math.sin(angle));

    return world.getHighestBlockAt(x, z).getLocation();
  }

  private Location getRandomLocationFromVanilla(World world) {
    org.bukkit.WorldBorder vanillaBorder = world.getWorldBorder();
    int size = (int) vanillaBorder.getSize();

    // Limit border size to configured value
    if (size > maxVanillaRadius * 2) {
      size = maxVanillaRadius * 2;
    }

    int x = random.nextInt(size) - size / 2;
    int z = random.nextInt(size) - size / 2;
    Location randomLocation = vanillaBorder.getCenter().add(x, 0, z);

    return world.getHighestBlockAt(randomLocation).getLocation();
  }
}
