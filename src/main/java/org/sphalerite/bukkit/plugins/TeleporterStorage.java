package org.sphalerite.bukkit.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class TeleporterStorage {
	private ArrayList<Teleporter> teleporters;
	public TeleporterStorage() {}
	
	private void failLoad() {
		PortRunesPlugin.getInstance().getLogger().warning("Could not read teleporter storage file, proceeding with empty teleporter list");
	}
	@SuppressWarnings("unchecked")
	public TeleporterStorage(File f) {
		try {
			FileInputStream in = new FileInputStream(f);
			ObjectInputStream ostream = new ObjectInputStream(in);
			teleporters = (ArrayList<Teleporter>)ostream.readObject();
			ostream.close();
			in.close();
		} catch (IOException e) {
			failLoad();
		} catch (ClassNotFoundException e) {
			failLoad();
		}
		for (@SuppressWarnings("unused") Teleporter t : teleporters); // Check validity of list
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
		teleporters.add(t);
	}
}
