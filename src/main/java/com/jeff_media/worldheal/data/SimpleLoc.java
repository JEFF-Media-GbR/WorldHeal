package com.jeff_media.worldheal.data;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class SimpleLoc implements ConfigurationSerializable {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleLoc simpleLoc = (SimpleLoc) o;
        return x == simpleLoc.x && y == simpleLoc.y && z == simpleLoc.z && world.equals(simpleLoc.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, y, z);
    }

    @Getter private final UUID world;
    @Getter private final int x;
    @Getter private final int y;
    @Getter private final int z;

    public SimpleLoc(int x, int y, int z, UUID world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public double distanceSquared(SimpleLoc other) {
        return Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2) + Math.pow(z - other.z, 2);
    }

    @Override
    public String toString() {
        return "SimpleLoc{" + "world=" + world + ", x=" + x + ", y=" + y + ", z=" + z + '}';
    }

    public SimpleLoc(Block block) {
        this.x = block.getX();
        this.y = block.getY();
        this.z = block.getZ();
        this.world = block.getWorld().getUID();
    }

    public SimpleLoc(Location loc) {
        this.x = loc.getBlockX();
        this.y = loc.getBlockY();
        this.z = loc.getBlockZ();
        this.world = loc.getWorld().getUID();
    }

    public SimpleLoc(Map<String, Object> map) {
        this.world = UUID.fromString((String) map.get("world"));
        this.x = (int) map.get("x");
        this.y = (int) map.get("y");
        this.z = (int) map.get("z");
    }

    public @Nullable Block asBlock() {
        World world = Bukkit.getWorld(this.world);
        return world == null ? null : world.getBlockAt(x,y,z);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String,Object> map = new HashMap<>();
        map.put("world", world.toString());
        map.put("x", x);
        map.put("y", y);
        map.put("z", z);
        return map;
    }

    public static SimpleLoc deserialize(Map<String,Object> map) {
        return new SimpleLoc(map);
    }

    public Location asLocation() {
        World world = Bukkit.getWorld(this.world);
        return world == null ? null : new Location(world, x, y, z);
    }
}
