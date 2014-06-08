package org.sphalerite.bukkit.plugins;

import java.io.Serializable;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.Location;
import org.bukkit.Material;

public class PortSignature implements Serializable {
	private static final long serialVersionUID = -5186396378561256593L;
	
	private Material north;
	private Material south;
	private Material east;
	private Material west;
	
	public Material getNorth() {return north;}
	public Material getSouth() {return south;}
	public Material getEast() {return east;}
	public Material getWest() {return west;}

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
	
	public boolean equals(Object o) {
		if (!(o instanceof PortSignature)) return false;
		PortSignature other = (PortSignature) o;
		return (north == other.north && south == other.south && east == other.east && west == other.west);
	}
	
	public PortSignature(PortSignature other) {
		north = other.north;
		south = other.south;
		east = other.east;
		west = other.west;
	}
	
	public String toString() {
		String out = "Signature ";
		out.concat(String.valueOf(north)).concat(" ");
		out.concat(String.valueOf(east)).concat(" ");
		out.concat(String.valueOf(south)).concat(" ");
		out.concat(String.valueOf(west)).concat(" ");
		return out;
	}
	
	public int hashCode() {
		return new HashCodeBuilder(19, 41)
				.append(
						north.name().concat(" ")
						.concat(south.name()).concat(" ")
						.concat(east.name()).concat(" ")
						.concat(west.name())
						)
				.toHashCode();
	}
}
