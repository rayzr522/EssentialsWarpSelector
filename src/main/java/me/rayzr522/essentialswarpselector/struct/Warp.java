package me.rayzr522.essentialswarpselector.struct;

import org.bukkit.Location;

public class Warp {
    private final String name;
    private final Location location;

    public Warp(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }
}
