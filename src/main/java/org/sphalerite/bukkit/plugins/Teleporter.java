package org.sphalerite.bukkit.plugins;

import org.bukkit.Location;
import org.bukkit.Material;

public class Teleporter extends PortRune {
	private static final long serialVersionUID = 7243429873329918434L;
	public Teleporter(Location location) {
		super(location);
	}
	public Teleporter(SerializableLocation location, PortSignature sig) {
		super(location, sig);
	}
	protected final Material getOutsideBlock() {return Material.LAPIS_BLOCK;}
	protected final Material getCentreBlock() {return Material.DIAMOND_BLOCK;}
}
