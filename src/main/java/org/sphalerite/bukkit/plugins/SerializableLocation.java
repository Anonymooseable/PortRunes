package org.sphalerite.bukkit.plugins;

import java.io.Serializable;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.Location;
import org.bukkit.World;

public class SerializableLocation implements Serializable {
	private static final long serialVersionUID = 1156633965533767514L;

	private String world;
	private double x, y, z;
	
	SerializableLocation(String w, double x, double y, double z) {
		world = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	SerializableLocation(Location l) {
		world = l.getWorld().getName();
		x = l.getX();
		y = l.getY();
		z = l.getZ();
	}
	
	public World getWorld() {
		return PortRunesPlugin.getInstance().getServer().getWorld(world);
	}
	public String getWorldName() {
		return world;
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}
	
	public Location toLocation() {
		return new Location(getWorld(), x, y, z);
	}
	public boolean equals(Object other) {
		if (other instanceof SerializableLocation) {
			SerializableLocation l = (SerializableLocation) other;
			return x == l.getX() && y == l.getY() && z == l.getZ() && world.equals(l.getWorldName());
		}
		return false;
	}
	public int hashCode() {
		return new HashCodeBuilder(17, 41).append(x).append(y).append(z).append(world).toHashCode();
	}
}
