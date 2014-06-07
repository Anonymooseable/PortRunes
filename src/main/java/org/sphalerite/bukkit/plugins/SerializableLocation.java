package org.sphalerite.bukkit.plugins;

import java.io.Serializable;

import org.bukkit.Location;
import org.bukkit.World;

public class SerializableLocation implements Serializable {
	private static final long serialVersionUID = 1156633965533767514L;

	String world;
	double x, y, z;
	
	SerializableLocation(String w, double x, double y, double z) {
		world = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	SerializableLocation(Location l) {
		world = l.getWorld().toString();
		x = l.getX();
		y = l.getY();
		z = l.getZ();
	}
	
	public World getWorld() {
		return PortRunesPlugin.getInstance().getServer().getWorld(world);
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
}
