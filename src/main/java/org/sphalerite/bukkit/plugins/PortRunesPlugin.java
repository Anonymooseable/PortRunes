package org.sphalerite.bukkit.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PortRunesPlugin extends JavaPlugin implements Listener {
	private static PortRunesPlugin _instance;
	private static int createTeleCost;
	private static int useTeleCost;
	
	private Map<PortSignature, SerializableLocation> waypoints;
	private static final File teleportersPath = new File("plugins/PortRunes/teleporters.dat");
	private static final File waypointsPath = new File("plugins/PortRunes/waypoints.dat");

	@Override
	public void onEnable() {
		_instance = this;
		saveDefaultConfig();
		createTeleCost = getConfig().getInt("createTeleporterCost");
		useTeleCost = getConfig().getInt("useTeleporterCost");
		
		loadTeleporters();
		loadWaypoints();
	}
	
	private void loadTeleporters() {
		InputStreamReader reader;
		char[] text = new char[(int) teleportersPath.length()];
		try {
			reader = new InputStreamReader(new FileInputStream(teleportersPath));
		}
		catch (FileNotFoundException e) {
			getLogger().warning("Teleporters file not found.");
			return;
		}
		try {
			reader.read(text);
		}
		catch (IOException e) {
			getLogger().warning("Could not read teleporters file.");
			try {reader.close();} catch (IOException e1) {}
			return;
		}
		
		try {reader.close();} catch (IOException e) {}
	}
	
	@Override
	public void onDisable() {
		getLogger().log(Level.INFO, "Unloaded PortRunes.");
	}
	
	@EventHandler
	public void onPlayerInteractBlock(PlayerInteractEvent event) {
		// TODO
	}
	
	public static PortRunesPlugin getInstance() {return _instance;}
}
