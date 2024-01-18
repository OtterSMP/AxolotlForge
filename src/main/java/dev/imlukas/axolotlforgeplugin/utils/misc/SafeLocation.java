package dev.imlukas.axolotlforgeplugin.utils.misc;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

@Getter
public class SafeLocation {

    private final UUID worldId;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public SafeLocation(Location location) {
        this.worldId = location.getWorld().getUID();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public Location asBukkitLocation() {
        return new Location(Bukkit.getWorld(worldId), x, y, z, yaw, pitch);
    }

    public World getWorld() {
        return Bukkit.getWorld(worldId);
    }
}
