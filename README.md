# RandomTeleport
Adds the ability to teleport yourself to a random location inside the world border. Supports 
Brettflan's [WorldBorder](https://dev.bukkit.org/projects/worldborder) plugin, both rectangular and elliptic mode.

## Requirements
* Craftbukkit 1.12.2+
* Java 1.8+
* WorldBorder 1.8.7 *(optional)*

### Permissions
| Permission                 | Description   | Default |
| -------------------------- | ------------- | :-----: |
| randomteleport.command.rtp | Grants access to random teleports  | op |

### Settings
* **safe_teleport:** *<true|false>* If turned on, deny teleports on blocks specified in the unsafe blocks list
* **max_tries:** *10* Max tries to find a safe location to teleport to
* **border_mode:** *<vanilla|plugin>* Whether to use the WorldBorder plugin or the vanilla border
* **unsafe_blocks:** *[..]* List of unsafe blocks to land on. Use [Spigot's notation for materials](https://helpch.at/docs/1.12.2/index.html?org/bukkit/Material.html) to specify blocks

### Licence
This project is licensed under the MIT License - see the LICENSE.md file for details.
