package io.github.twieteddy.randomteleport.borders;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;

public class Border {

  public Location getRandomLocation(World world) {
    WorldBorder vanillaBorder = world.getWorldBorder();
    Random random = new Random();

    int size = (int) vanillaBorder.getSize();
    int x = random.nextInt(size) - size / 2;
    int z = random.nextInt(size) - size / 2;

    return world
        .getHighestBlockAt(vanillaBorder.getCenter().add(x, 0, z))
        .getLocation();
  }
}
