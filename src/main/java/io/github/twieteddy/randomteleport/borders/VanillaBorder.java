package io.github.twieteddy.randomteleport.borders;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;

public class VanillaBorder extends Border {

  @SuppressWarnings("unused")
  @Override
  public Location getRandomLocation(World world) {
    WorldBorder vanillaBorder = world.getWorldBorder();
    Random random = new Random();

    double size = vanillaBorder.getSize();
    int x = random.nextInt((int) size / 2);
    int z = random.nextInt((int) size / 2);

    Location newLocation = new Location(world, x, 0, z)
        .add(vanillaBorder.getCenter());

    return world.getHighestBlockAt(newLocation).getLocation();
  }
}
