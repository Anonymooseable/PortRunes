package org.sphalerite.bukkit.plugins;

import org.bukkit.Location;
import org.bukkit.Material;

public class Waypoint extends PortRune {
	private static final long serialVersionUID = 1326898931837537251L;
	public Waypoint(Location location) {
		super(location);
	}
	public Waypoint(SerializableLocation location, PortSignature sig) {
		super(location, sig);
	}
	protected final Material getOutsideBlock() {return Material.REDSTONE_BLOCK;}
	protected final Material getCentreBlock() {return Material.DIAMOND_BLOCK;}
}
