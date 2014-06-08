package org.sphalerite.bukkit.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class WaypointStorage {
	private HashMap<PortSignature, Waypoint> waypoints;

	public WaypointStorage() {
		waypoints = new HashMap<PortSignature, Waypoint>();
	}

	private void failLoad() {
		PortRunesPlugin.getInstance().getLogger().warning("Could not read waypoint storage file, proceeding with empty waypoint map");
		waypoints = new HashMap<PortSignature, Waypoint>();
	}

	@SuppressWarnings("unchecked")
	public WaypointStorage(File f) {
		try {
			FileInputStream in = new FileInputStream(f);
			ObjectInputStream ostream = new ObjectInputStream(in);
			waypoints = (HashMap<PortSignature, Waypoint>)ostream.readObject();
			ostream.close();
			in.close();
		} catch (IOException e) {
			failLoad();
		} catch (ClassNotFoundException e) {
			failLoad();
		}
		// Check validity of map
		for (@SuppressWarnings("unused") PortSignature s: waypoints.keySet());
		for (@SuppressWarnings("unused") Waypoint t: waypoints.values());
	}
	
	private void failSave() {
		PortRunesPlugin.getInstance().getLogger().warning("Could not save waypoint storage file!");
	}
	public void save(File f) {
		try {
			FileOutputStream out = new FileOutputStream(f);
			ObjectOutputStream ostream = new ObjectOutputStream(out);
			ostream.writeObject(waypoints);
			ostream.close();
			out.close();
		} catch (IOException e) {
			failSave();
		}
	}
	
	public void add(Waypoint w) {
		waypoints.put(w.getSig(), w);
	}

	public Waypoint get(PortSignature sig) {
		return waypoints.get(sig);
	}
	public void remove(PortSignature sig) {
		waypoints.remove(sig);
	}
}
