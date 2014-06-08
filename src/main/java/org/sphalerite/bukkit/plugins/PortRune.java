package org.sphalerite.bukkit.plugins;

import java.io.Serializable;

import org.bukkit.Location;
import org.bukkit.Material;

abstract class PortRune implements Serializable {
	private static final long serialVersionUID = -8618238791440850556L;
	abstract protected Material getCentreBlock();
	abstract protected Material getOutsideBlock();
	private SerializableLocation location;
	private PortSignature sig;
	
	public PortRune(SerializableLocation location, PortSignature sig) {
		this.location = location;
		this.sig = sig;
	}
	public PortRune(Location location) {
		this.location = new SerializableLocation(location);
		this.sig = new PortSignature(location);
	}
	
	public boolean isValid() {
		return (
				location.toLocation().getBlock().getType() == getCentreBlock() &&
				location.toLocation().add(Misc.NORTH).getBlock().getType() == sig.getNorth() &&
				location.toLocation().add(Misc.SOUTH).getBlock().getType() == sig.getSouth() &&
				location.toLocation().add(Misc.EAST).getBlock().getType() == sig.getEast() &&
				location.toLocation().add(Misc.WEST).getBlock().getType() == sig.getWest() &&
				location.toLocation().add(Misc.NORTHEAST).getBlock().getType() == getOutsideBlock() &&
				location.toLocation().add(Misc.NORTHWEST).getBlock().getType() == getOutsideBlock() &&
				location.toLocation().add(Misc.SOUTHEAST).getBlock().getType() == getOutsideBlock() &&
				location.toLocation().add(Misc.SOUTHWEST).getBlock().getType() == getOutsideBlock()
				);
	}
	
	PortSignature getSig() {
		return sig;
	}
	SerializableLocation getLocation() {
		return location;
	}
	public boolean equals(Waypoint other) {
		return sig.equals(other.getSig()) && location.equals(other.getLocation()) && this.getClass() == other.getClass();
	}
}
