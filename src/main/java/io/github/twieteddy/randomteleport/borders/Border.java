package io.github.twieteddy.randomteleport.borders;

import org.bukkit.Location;
import org.bukkit.World;

@SuppressWarnings("unused")
public abstract class Border {
  public abstract Location getRandomLocation(World world);
}
