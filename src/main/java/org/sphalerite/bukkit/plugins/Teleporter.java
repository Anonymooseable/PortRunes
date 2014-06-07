package org.sphalerite.bukkit.plugins;

import java.io.Serializable;

import org.bukkit.Location;
import org.bukkit.Material;

public class Teleporter implements Serializable {
	private static final long serialVersionUID = -8618238791440850556L;
	SerializableLocation location;
	PortSignature sig;
	
	public Teleporter(SerializableLocation location, PortSignature sig) {
		this.location = location;
		this.sig = sig;
	}
	public Teleporter(Location location) {
		this.location = new SerializableLocation(location);
		this.sig = new PortSignature(location);
	}
	
	public boolean isValid() {
		return (
				location.toLocation().getBlock().getType() == Material.DIAMOND_BLOCK &&
				location.toLocation().add(Misc.NORTH).getBlock().getType() == sig.north &&
				location.toLocation().add(Misc.SOUTH).getBlock().getType() == sig.south &&
				location.toLocation().add(Misc.EAST).getBlock().getType() == sig.east &&
				location.toLocation().add(Misc.WEST).getBlock().getType() == sig.west &&
				location.toLocation().add(Misc.NORTHEAST).getBlock().getType() == Material.LAPIS_BLOCK &&
				location.toLocation().add(Misc.NORTHWEST).getBlock().getType() == Material.LAPIS_BLOCK &&
				location.toLocation().add(Misc.SOUTHEAST).getBlock().getType() == Material.LAPIS_BLOCK &&
				location.toLocation().add(Misc.SOUTHWEST).getBlock().getType() == Material.LAPIS_BLOCK
				);
	}
}
