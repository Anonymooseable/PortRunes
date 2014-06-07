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
	
	private static final File teleportersPath = new File("plugins/PortRunes/teleporters.dat");
	private static final File waypointsPath = new File("plugins/PortRunes/waypoints.dat");
	
	private TeleporterStorage teleporters;
	private WaypointStorage waypoints;

	@Override
	public void onEnable() {
		_instance = this;
		saveDefaultConfig();
		createTeleCost = getConfig().getInt("createTeleporterCost");
		useTeleCost = getConfig().getInt("useTeleporterCost");
		
		teleporters = new TeleporterStorage(teleportersPath);
		waypoints = new WaypointStorage(waypointsPath);
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
