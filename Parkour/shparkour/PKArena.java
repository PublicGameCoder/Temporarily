package shparkour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PKArena implements Listener {
	
	private SHParkour shpk;
	private String arenaName;
	private Location lobbylocation;
	private List<String> players;
	private HashMap<String, Location> currentCheckPoint;
	private HashMap<String, ItemStack[]> inventoryContents;
	private HashMap<String, ItemStack[]> armorContents;
	private boolean printDebug = false;
	private PotionEffect noDieEffect;
	private PotionEffect fireResEffect;
	private List<Material> blacklistedMaterials;
	
	private List<Location> checkPoints;
	private	int checkPointCount;
	
	//################## Arena Methods ##################
	
	public PKArena(String name, List<Location> checkPointList, Location lobbyposition,SHParkour shpk) {
		this.shpk = shpk;
		this.arenaName = name;
		this.checkPoints = checkPointList;
		this.checkPointCount = checkPoints.size();
		this.checkPointCount--;
		System.out.println("Checkpoints count: "+checkPoints.size());
		this.lobbylocation = lobbyposition;
		players = new ArrayList<String>();
		this.currentCheckPoint = new HashMap<String, Location>();
		inventoryContents = new HashMap<String, ItemStack[]>();
		armorContents = new HashMap<String, ItemStack[]>();
		blacklistedMaterials = new ArrayList<Material>();
		
		noDieEffect = new PotionEffect(PotionEffectType.REGENERATION, 999999, 30, false , false);
		fireResEffect = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 30, false , false);
	}
	
	public void addPlayer(Player player) { //adding player to game
		inventoryContents.put(player.getName(), player.getInventory().getStorageContents().clone());
		armorContents.put(player.getName(), player.getInventory().getArmorContents().clone());
		player.getInventory().clear();
		players.add(player.getName());
		currentCheckPoint.put(player.getName(), getStartPoint());
		player.addPotionEffect(noDieEffect);
		player.addPotionEffect(fireResEffect);
		int currentcheckpointnumber = checkPoints.indexOf(currentCheckPoint.get(player))+1;
		player.sendTitle(ChatColor.GREEN+"Starting on "+this.getname()+"!", ChatColor.GOLD+""+currentcheckpointnumber+""+ChatColor.BLUE+" / "+ChatColor.GOLD+""+checkPoints.size(),1, 40, 5);
	}

	public void finishPlayer(Player player) {
		currentCheckPoint.remove(player.getName());
		player.getInventory().setContents(inventoryContents.get(player.getName()));
		player.getInventory().setArmorContents(armorContents.get(player.getName()));
		
		players.remove(player.getName());
		inventoryContents.remove(player.getName());
		armorContents.remove(player.getName());
		player.teleport(lobbylocation);
		player.removePotionEffect(noDieEffect.getType());
		player.removePotionEffect(fireResEffect.getType());
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100.0f, 1f);
		player.sendMessage(ChatColor.GREEN + "You finished the parkour!");
	}
	
	public void quitPlayer(Player player) {
		if (!players.contains(player.getName())) {
			player.sendMessage(ChatColor.GOLD+"You need to be in arena: "+ getname() +" to quit !");
			return;
		}
		player.getInventory().setContents(inventoryContents.get(player.getName()));
		player.getInventory().setArmorContents(armorContents.get(player.getName()));
		
		currentCheckPoint.remove(player.getName());
		inventoryContents.remove(player.getName());
		armorContents.remove(player.getName());
		players.remove(player.getName());
		player.removePotionEffect(noDieEffect.getType());
		player.removePotionEffect(fireResEffect.getType());
		player.teleport(lobbylocation);
		player.sendMessage(ChatColor.GREEN + "You quit the parkour!");
	}
	
	//################## Ingame methods ##################

	@EventHandler
	public void onPlayerStandOnBlacklistedBlock(PlayerMoveEvent e) {
		if (!players.contains(e.getPlayer().getName()))return;
		Player player = e.getPlayer();
		Block onBlock =  player.getLocation().getWorld().getBlockAt(player.getLocation().subtract(0, 1, 0));
		Block inBlock =  player.getLocation().getWorld().getBlockAt(player.getLocation());
		for (Material material : blacklistedMaterials) {
			if (onBlock.getType() == material || inBlock.getType() == material) {
				ParkourManager.getManager(shpk).teleportPlayerToLastCheckPoint(player);
			}
		}
	}
	public void addCheckPoint(Player player, Block block) {
		Location nextCheckPoint = findNextCheckpoint(currentCheckPoint.get(player.getName()));
		if (!isLocationsTheSame(nextCheckPoint , block.getLocation()) ){
			//player.sendMessage(ChatColor.GOLD+"This is not your next checkpoint!");
			player.sendTitle("", ChatColor.GOLD+"This is not your next checkpoint!",1, 40, 5);
			return;
		}else {
			Location oldlocation = currentCheckPoint.get(player.getName());
			if (currentCheckPoint.replace(player.getName(), oldlocation, nextCheckPoint)) { //replacing checkpoints
				//player.sendMessage(ChatColor.GREEN+"CheckPoint!");
				int currentcheckpointnumber = checkPoints.indexOf(currentCheckPoint.get(player.getName()))+1;
				if (currentcheckpointnumber != checkPoints.size()){
				player.sendTitle(ChatColor.GREEN+"CheckPoint!", ChatColor.GOLD+""+currentcheckpointnumber+""+ChatColor.BLUE+" / "+ChatColor.GOLD+""+checkPoints.size(),1, 40, 5);
				}else {
					player.sendTitle(ChatColor.GREEN+"Finished on "+this.getname()+"!", ChatColor.GOLD+""+currentcheckpointnumber+""+ChatColor.BLUE+" / "+ChatColor.GOLD+""+checkPoints.size(),1, 40, 5);
				}
				
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 100.0f, 3f);
				
			}else {
				System.out.println("Error Occurred PKArena:80");
			}
		}
	}
	
	public Location findNextCheckpoint(Location checkPointLocation) {
		if (!checkPoints.contains(checkPointLocation)) {
			System.out.println("finding next checkpoint can't be a non-checkpoint location");
			return null;
		}
		Location loc = null;
		for (int i = 0; i <= checkPointCount; i++) {
			if (checkPoints.get(i) == checkPointLocation) {
				i++;
				loc = checkPoints.get(i);
				break;
			}
		}
		if (loc == null) {
			System.out.println("null Location: "+ checkPointLocation);
		}
		return loc;
	}
	
	//################## Fix object methods ##################
	
	@SuppressWarnings("unused")
	private Location fixLocation(Location loc) {
		World w = loc.getWorld();
		int x = (int) Math.round(loc.getX());
		int y = (int) Math.round(loc.getY());
		int z = (int) Math.round(loc.getZ());
		Location newloc = new Location(w, x, y, z);
		return newloc;
	}
	
	//################## Checkers ##################
	
	public boolean isPlayerFinished(Player player) { // check if player is finished with parkouring
		if (currentCheckPoint.get(player.getName()) == getEndPoint()) {
			return true;
		}else {
			return false;
		}
	}
	
	private boolean isLocationsTheSame(Location one, Location two) {
		if (one.getWorld() != two.getWorld()) {
			if (printDebug) {
				System.out.println("Worlds aren't the same!");
				System.out.println("World one: "+one.getWorld());
				System.out.println("World two: "+two.getWorld());
			}
			return false;
		}else if (one.getX() != two.getX()) {
			if (printDebug) {
				System.out.println("Xpos aren't the same!");
				System.out.println("Xpos one: "+one.getX());
				System.out.println("Xpos two: "+two.getX());
			}
			return false;
		}else if (one.getY() != two.getY()) {
			if (printDebug) {
				System.out.println("Ypos aren't the same!");
				System.out.println("Ypos one: "+one.getY());
				System.out.println("Ypos two: "+two.getY());
			}
			return false;
		}else if (one.getZ() != two.getZ()) {
			if (printDebug) {
				System.out.println("Zpos aren't the same!");
				System.out.println("Zpos one: "+one.getZ());
				System.out.println("Zpos two: "+two.getZ());
			}
			return false;
		}else {
			return true;
		}
	}
	
	//################## Getters ##################

	public String getname() {
		return this.arenaName;
	}

	public Location getLobbyLocation() {
		return this.lobbylocation;
	}

	public List<Location> getCheckPoints() {
		// TODO Auto-generated method stub
		return this.checkPoints;
	}
	
	
	public Location getStartPoint() {
		return this.checkPoints.get(0);
	}
	
	private Location getEndPoint() {
		return checkPoints.get(this.checkPointCount);
	}

	public Location getCheckPointOf(Player player) {
		if (!players.contains(player.getName()) || !currentCheckPoint.containsKey(player.getName())) {
			player.sendMessage(ChatColor.RED+"We can't find your data inside arena: "+this.getname());
			return null;
		}
		return currentCheckPoint.get(player.getName());
	}

	public void setBlackListedMaterials(List<Material> blacklistMaterials) {
		this.blacklistedMaterials = blacklistMaterials;
	}
	
	//################## Setters ##################
}
