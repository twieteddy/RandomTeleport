package io.github.twieteddy.randomteleport;

import com.wimbli.WorldBorder.BorderData;
import com.wimbli.WorldBorder.WorldBorder;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Location;
import org.bukkit.World;

class Border {

  private final Config config;
  private final WorldBorder worldBorderPlugin;

  Border(Config config, WorldBorder plugin) {
    this.config = config;
    this.worldBorderPlugin = plugin;
  }

  Location getRandomLocation(World world) {
    if (worldBorderPlugin != null) {
      BorderData borderData = worldBorderPlugin.getWorldBorder(world.getName());
      if (borderData != null) {
        return getRandomLocationFromPlugin(world, borderData);
      }
    }
    return getRandomLocationFromVanilla(world);
  }

  private Location getRandomLocationFromPlugin(World world, BorderData data) {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    Location borderCenter = new Location(world, data.getX(), 0,  data.getZ());

    double distanceX = data.getRadiusX() * Math.sqrt(random.nextDouble());
    double distanceZ = data.getRadiusZ() * Math.sqrt(random.nextDouble());
    double angle = random.nextDouble() * 2 * Math.PI;

    int x = (int) (data.getX() + distanceX * Math.cos(angle));
    int z = (int) (data.getZ() + distanceZ * Math.sin(angle));

    return world.getHighestBlockAt(x, z).getLocation();
  }

  private Location getRandomLocationFromVanilla(World world) {
    org.bukkit.WorldBorder vanillaBorder = world.getWorldBorder();
    Random random = new Random();

    int size = (int) vanillaBorder.getSize();

    // Limit border size to configured value
    if (size > config.getMaxVanillaRadius() * 2) {
      size = config.getMaxVanillaRadius() * 2;
    }

    int x = random.nextInt(size) - size / 2;
    int z = random.nextInt(size) - size / 2;

    return world.getHighestBlockAt(vanillaBorder.getCenter().add(x, 0, z)).getLocation();
  }
}
