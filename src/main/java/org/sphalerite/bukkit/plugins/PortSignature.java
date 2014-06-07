package org.sphalerite.bukkit.plugins;

import java.io.Serializable;

import org.bukkit.Location;
import org.bukkit.Material;

public class PortSignature implements Serializable {
	private static final long serialVersionUID = -5186396378561256593L;
	
	Material north;
	Material south;
	Material east;
	Material west;

	PortSignature(Material n, Material s, Material e, Material w) {
		north = n;
		south = s;
		east = e;
		west = w;
	}
	
	PortSignature(Location loc) {
		north = loc.clone().add(Misc.NORTH).getBlock().getType();
		south = loc.clone().add(Misc.SOUTH).getBlock().getType();
		east = loc.clone().add(Misc.EAST).getBlock().getType();
		west = loc.clone().add(Misc.WEST).getBlock().getType();
	}
	
	public boolean equals(PortSignature other) {
		return (north == other.north && south == other.south && east == other.east && west == other.west);
	}
	
}
