# RandomTeleport
Adds the ability to teleport yourself to a random location inside the world border. Supports 
Brettflan's [WorldBorder](https://www.spigotmc.org/resources/worldborder.60905/) plugin, both rectangular and elliptic mode.

## Requirements
* Spigot 1.13.2+
* Java 1.8+
* WorldBorder 1.9.10 *(optional)*

### Permissions
| Permission                 | Description   | Default |
| -------------------------- | ------------- | :-----: |
| randomteleport.command.rtp | Grants access to random teleports  | op |

### Settings
* **safe_teleport:** *<true|false>* If turned on, deny teleports on blocks specified in the unsafe blocks list
* **max_tries:** *10* Max tries to find a safe location to teleport to
* **border_mode:** *<vanilla|plugin>* Whether to use the WorldBorder plugin or the vanilla border
* **unsafe_blocks:** *[..]* List of unsafe blocks to land on. Use [Paper's notation for materials](https://papermc.io/javadocs/paper/1.13/index.html?org/bukkit/Bukkit.html) to specify blocks

### Licence
This project is licensed under the MIT License - see the LICENSE.md file for details.
