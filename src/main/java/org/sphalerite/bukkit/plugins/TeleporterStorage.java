package org.sphalerite.bukkit.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.bukkit.Location;

public class TeleporterStorage {
	private HashMap<SerializableLocation, Teleporter> teleporters;
	public TeleporterStorage() {
		teleporters = new HashMap<SerializableLocation, Teleporter>();
	}

	private void failLoad() {
		PortRunesPlugin.getInstance().getLogger().warning("Could not read teleporter storage file, proceeding with no teleporters");
		teleporters = new HashMap<SerializableLocation, Teleporter>();
	}
	@SuppressWarnings("unchecked")
	public TeleporterStorage(File f) {
		try {
			FileInputStream in = new FileInputStream(f);
			ObjectInputStream ostream = new ObjectInputStream(in);
			teleporters = (HashMap<SerializableLocation, Teleporter>)ostream.readObject();
			ostream.close();
			in.close();
		} catch (IOException e) {
			failLoad();
		} catch (ClassNotFoundException e) {
			failLoad();
		}
		// Check validity of map
		for (@SuppressWarnings("unused") SerializableLocation l: teleporters.keySet());
		for (@SuppressWarnings("unused") Teleporter t: teleporters.values());
	}
	
	private void failSave() {
		PortRunesPlugin.getInstance().getLogger().warning("Could not save teleporter storage file!");
	}
	public void save(File f) {
		try {
			FileOutputStream out = new FileOutputStream(f);
			ObjectOutputStream ostream = new ObjectOutputStream(out);
			ostream.writeObject(teleporters);
			ostream.close();
			out.close();
		} catch (IOException e) {
			failSave();
		}
	}
	
	public void add(Teleporter t) {
		teleporters.put(t.getLocation(), t);
	}
	public Teleporter get(SerializableLocation l) {
		return teleporters.get(l);
	}
	public Teleporter get(Location l) {
		return teleporters.get(new SerializableLocation(l));
	}
	public void remove(SerializableLocation l) {
		teleporters.remove(l);
	}
}
