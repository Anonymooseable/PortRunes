package org.sphalerite.bukkit.plugins;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class PortRunesPlugin extends JavaPlugin implements Listener {
	private static PortRunesPlugin _instance;
	private static int createTeleCost;
	private static int useTeleCost;

	private static final File teleportersPath = new File("plugins/PortRunes/teleporters.dat");
	private static final File waypointsPath = new File("plugins/PortRunes/waypoints.dat");

	private TeleporterStorage teleporters;
	private WaypointStorage waypoints;
	
	private static long teleportDelay;
	private static int maxShunt;

	private enum ActionResult {
		TELEPORTER_DAMAGED,
		TELEPORTER_ACTIVATE_NO_WAYPOINT,
		TELEPORTER_USE_NO_WAYPOINT,
		TELEPORTER_OBSTRUCTED,
		WAYPOINT_ALREADY_ACTIVE,
		WAYPOINT_SIG_OCCUPIED,
		TELEPORT_SUCCESS,
		WAYPOINT_CREATE_SUCCESS,
		TELEPORTER_ACTIVATED,
		NOT_ENOUGH_XP,
	};

	@Override
	public void onEnable() {
		_instance = this;
		saveDefaultConfig();
		createTeleCost = getConfig().getInt("PortRunes.createTeleporterCost");
		useTeleCost = getConfig().getInt("PortRunes.useTeleporterCost");
		teleportDelay = (long) (getConfig().getDouble("PortRunes.teleportDelay") * 20);
		maxShunt = getConfig().getInt("PortRunes.maxShunt");
		
		teleporters = new TeleporterStorage(teleportersPath);
		waypoints = new WaypointStorage(waypointsPath);

		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {
		teleporters.save(teleportersPath);
		waypoints.save(waypointsPath);
		getLogger().log(Level.INFO, "Unloaded PortRunes.");
	}


	private void reportResult(ActionResult r, Player p, Location l) {
		if (p.getGameMode() == GameMode.CREATIVE) {
			switch(r) {
			case TELEPORTER_ACTIVATE_NO_WAYPOINT: p.sendMessage("No matching waypoint found."); break;
			case TELEPORTER_DAMAGED: p.sendMessage("Teleporter has been damaged."); break;
			case TELEPORTER_USE_NO_WAYPOINT: p.sendMessage("Waypoint destroyed."); break;
			case TELEPORTER_OBSTRUCTED: p.sendMessage("Waypoint obstructed."); break;
			case WAYPOINT_ALREADY_ACTIVE: p.sendMessage("Waypoint already active."); break;
			case WAYPOINT_SIG_OCCUPIED: p.sendMessage("This signature is taken."); break;
			case TELEPORT_SUCCESS: p.sendMessage("Teleport succeeded."); break;
			case WAYPOINT_CREATE_SUCCESS: p.sendMessage("Waypoint created."); break;
			case TELEPORTER_ACTIVATED: p.sendMessage("Teleporter activated."); break;
			default: break;
			}
		}
		switch(r) {
		case TELEPORTER_ACTIVATE_NO_WAYPOINT:
			Location cur;
			for (int x = -1; x<=1; x++) {
				for (int z = -1; z<=1; z++) {
		        	cur = l.clone().add(x, 1, z);
		        	l.getWorld().playEffect(cur, Effect.SMOKE, BlockFace.UP, 16);
		        }
			}
			l.getWorld().playEffect(l, Effect.EXTINGUISH, null, 16);
			break;
		case TELEPORT_SUCCESS: break;
		case WAYPOINT_ALREADY_ACTIVE: break;
		case WAYPOINT_SIG_OCCUPIED:
			l.getWorld().playEffect(l.clone().add(0, 1, 0), Effect.POTION_BREAK, new Potion(PotionType.STRENGTH, 0), 16);
			break;
		case TELEPORTER_USE_NO_WAYPOINT:
		case TELEPORTER_OBSTRUCTED:
			l.getWorld().playEffect(l.clone().add(0, 1, 0), Effect.SMOKE, BlockFace.UP, 16);
			l.getWorld().playEffect(l, Effect.EXTINGUISH, null, 16);
			break;
		case TELEPORTER_DAMAGED:
			break;
		case WAYPOINT_CREATE_SUCCESS:
		case TELEPORTER_ACTIVATED:
			for (int x = -1; x<=1; x++) {
				for (int z = -1; z<=1; z++) {
		        	cur = l.clone().add(x, 1, z);
		        	l.getWorld().playEffect(cur, Effect.MOBSPAWNER_FLAMES, null, 16);
		        }
			}
			break;
		case NOT_ENOUGH_XP:
			p.playEffect(p.getLocation(), Effect.SMOKE, BlockFace.UP);
			p.playEffect(p.getLocation(), Effect.EXTINGUISH, null);
			break;
		}
	}
	
	@EventHandler
	public void onPlayerInteractBlock(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if (event.isBlockInHand()) return;
		
		Block selected = event.getClickedBlock();
		Player player = event.getPlayer();
		Teleporter t = new Teleporter(selected.getLocation());
		if (t.isValid()) {
			Teleporter existing = teleporters.get(selected.getLocation()); 
			if (existing != null) { // Teleporter already active -- use it
				if (existing.isValid()) {
					tryTeleport(event.getPlayer(), existing);
				} else {
					reportResult(ActionResult.TELEPORTER_DAMAGED, player, selected.getLocation());
					destroyTeleporter(existing);
				}
				return;
			}
			else {
				tryActivateTeleporter(event.getPlayer(), t);
				return;
			}
		}

		boolean createWaypoint = false;
		Waypoint toCreate = new Waypoint(selected.getLocation());
		if (toCreate.isValid()) {
			Waypoint existing = waypoints.get(toCreate.getSig());
			if (existing != null) {
				if (existing.equals(toCreate)) { // Waypoint already active
					reportResult(ActionResult.WAYPOINT_ALREADY_ACTIVE, player, selected.getLocation());
				} else { // Signature already taken
					if (existing.isValid()) {
						reportResult(ActionResult.WAYPOINT_SIG_OCCUPIED, player, selected.getLocation());
					} else {
						waypoints.remove(toCreate.getSig());
						createWaypoint = true;
					}
				}
			} else {
				createWaypoint = true;
			}
		}
		if (createWaypoint) {
			Block aboveCentre = selected.getRelative(0, 1, 0);
			if (aboveCentre.getType() == Material.AIR) {
				aboveCentre.setType(Material.SIGN_POST);
				Sign s = (Sign) aboveCentre.getState();
				s.setLine(1, ChatColor.BLUE + "WAYPOINT");
				s.setLine(2, "ACTIVATED");
				s.update();
			}
			waypoints.add(toCreate);
			reportResult(ActionResult.WAYPOINT_CREATE_SUCCESS, player, selected.getLocation());
		}
	}

	private void tryActivateTeleporter(Player player, Teleporter t) {
		Waypoint w = waypoints.get(t.getSig());
		if (w != null) {
			if (!w.isValid()) {
				waypoints.remove(w.getSig());
			} else {
				if (player.getLevel() >= createTeleCost || player.getGameMode() == GameMode.CREATIVE) {
					player.setLevel(player.getLevel() - createTeleCost);
					teleporters.add(t);
					Block aboveCentre = t.getLocation().toLocation().add(0, 1, 0).getBlock();
					if (aboveCentre.getType() == Material.AIR) {
						aboveCentre.setType(Material.SIGN_POST);
						Sign s = (Sign) aboveCentre.getState();
						s.setLine(1, ChatColor.BLUE + "TELEPORTER");
						s.setLine(2, "ACTIVATED");
						s.update();
					}
					reportResult(ActionResult.TELEPORTER_ACTIVATED, player, t.getLocation().toLocation());
				} else {
					reportResult(ActionResult.NOT_ENOUGH_XP, player, t.getLocation().toLocation());
				}
			}
		}
		else {
			reportResult(ActionResult.TELEPORTER_ACTIVATE_NO_WAYPOINT, player, t.getLocation().toLocation());
		}
	}

	
	private class DoTeleportRunnable implements Runnable {
		Location dest;
		Location origin;
		Player player;
		public DoTeleportRunnable(Player p, Location dest) {
			this.dest = dest;
			this.origin = p.getLocation().clone();
			this.player = p;
		}
		public void run() {
			origin.getWorld().playSound(origin, Sound.ENDERMAN_TELEPORT, 200, 0.2f);
			dest.getWorld().playSound(dest, Sound.ENDERMAN_TELEPORT, 200, 5.0f);
			player.teleport(dest);
		}
		
	}
	private void tryTeleport(Player player, Teleporter teleporter) {
		Waypoint w = waypoints.get(teleporter.getSig());
		if (w == null) {
			if (getConfig().getBoolean("PortRunes.slightlyGratuitousTeleporterDestruction")) destroyTeleporter(teleporter);
			return;
		}
		if (!w.isValid()) {
			if (getConfig().getBoolean("PortRunes.gratuitousTeleporterDestruction")) destroyTeleporter(teleporter);
			waypoints.remove(teleporter.getSig());
			return;
		}
		for (int height = 0; height <= maxShunt+1; height++) {
			Location dest = w.getLocation().toLocation();
			Location origin = player.getLocation();
			dest.add(0.5, height, 0.5);
			if (!dest.getBlock().getType().isSolid() && !dest.clone().add(0, 1, 0).getBlock().getType().isSolid()) {
				if (player.getLevel() >= useTeleCost || player.getGameMode() == GameMode.CREATIVE) {
					player.setLevel(player.getLevel() - useTeleCost);
					reportResult(ActionResult.TELEPORT_SUCCESS, player, teleporter.getLocation().toLocation());
					origin.getWorld().playEffect(origin, Effect.ENDER_SIGNAL, null, 16);
					dest.getWorld().playEffect(dest, Effect.ENDER_SIGNAL, null, 16);
					getServer().getScheduler().scheduleSyncDelayedTask(this, new DoTeleportRunnable(player, dest), teleportDelay);
					return;
				} else {
					reportResult(ActionResult.NOT_ENOUGH_XP, player, teleporter.getLocation().toLocation());
					return;
				}
			}
		}
		reportResult(ActionResult.TELEPORTER_OBSTRUCTED, player, teleporter.getLocation().toLocation());
	}

	public static PortRunesPlugin getInstance() {return _instance;}
	private void destroyTeleporter(Teleporter t) {
		teleporters.remove(t.getLocation());
		t.getLocation().getWorld().createExplosion(
				t.getLocation().getX()+0.5,
				t.getLocation().getY(),
				t.getLocation().getZ()+0.5,
				5.0f, // power
				false, // fire
				false // break blocks
		);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("portrunes")) {
			if (args.length >= 1) {
				/*if (args[0].equalsIgnoreCase("list")) {

				}*/
				if (args[0].equalsIgnoreCase("clear")) {
					if (args.length >= 2) {
						if (args[1].equalsIgnoreCase("waypoints") || args[1].equalsIgnoreCase("w")) {
							this.waypoints = new WaypointStorage();
							return true;
						}
						if (args[1].equalsIgnoreCase("teleporters") || args[1].equalsIgnoreCase("t")) {
							this.teleporters = new TeleporterStorage();
							return true;
						}
					} else {
						this.waypoints = new WaypointStorage();
						this.teleporters = new TeleporterStorage();
						return true;
					}
				}
			}
		}
		return false;
	}
}
