package shparkour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public class ParkourManager implements Listener {
	
	private SHParkour shpk;
	private static ParkourManager PM;
	private List<PKArena> arenas;
	FileConfiguration config;
	
	private HashMap<String, PKArena> players;
	
	private HashMap<String,String> editors;
	private HashMap<String, List<Block>> editorData;
	private ItemStack editorWand;
	
	public ParkourManager(SHParkour shpk) {
		this.shpk = shpk;
		this.config = shpk.getConfig();
		arenas = new ArrayList<PKArena>();
		players = new HashMap<String, PKArena>();
		editors = new HashMap<String,String>();
		editorData = new HashMap<String, List<Block>>();
		
		editorWand = new ItemStack(Material.BLAZE_ROD,1);
		ItemMeta meta = editorWand.getItemMeta();
		
		meta.setDisplayName(ChatColor.GOLD+""+ChatColor.BOLD+"The SHParkour's Editor Wand");
		meta.addEnchant(Enchantment.DURABILITY, 1, false);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		List<String> lores = new ArrayList<String>();
		
		lores.add("");
		lores.add(ChatColor.GREEN+"RightClick"+ChatColor.GOLD+" to add a new checkpoint!");
		lores.add(ChatColor.GREEN+"LeftClick"+ChatColor.GOLD+" to remove the selected checkpoint!");
		
		meta.setLore(lores);
		editorWand.setItemMeta(meta);
	}

	public static ParkourManager getManager(SHParkour shpk) {
		if (PM == null) {
			PM = new ParkourManager(shpk);
		}
		return PM;
	}
	
	public PKArena findArenaByStartCheckPoint(Location checkPoint) {
		checkPoint = checkPoint.getBlock().getLocation();
		for (PKArena arena : arenas) {
			if (arena.getStartPoint().equals(checkPoint)) {
				return arena;
			}
		}
		return null;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (!editors.containsKey(e.getPlayer().getName()) || !editorData.containsKey(e.getPlayer().getName())){
			onPlateTrigger(e);
			return;
		}
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.PHYSICAL) {
			return;
		}
		if (e.getItem() == null || !e.getItem().equals(editorWand)) {
			e.getPlayer().sendMessage(ChatColor.RED+"you may only use the editor wand to select checkpoints!");
			return;
		}
		if (e.getClickedBlock().getType() != Material.GOLD_PLATE) {
			e.getPlayer().sendMessage(ChatColor.RED+"only golden pressureplates can be a checkpoint!");
			return;
		}
		Player player = e.getPlayer();
		List<Block> checkpoints = editorData.get(player.getName());
		Block interactedBlock = e.getClickedBlock();
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) { //Adding CheckPoint
			if (checkpoints.contains(interactedBlock)) { //if CheckPoint already exists
				player.sendMessage(ChatColor.RED+"You already added this checkpoint!");
				return;
			}
			checkpoints.add(interactedBlock);
			player.sendMessage(ChatColor.GOLD+"successfully created that checkpoint!!");
		}else if (e.getAction() == Action.LEFT_CLICK_BLOCK) { //Removing CheckPoint
			if (checkpoints.contains(interactedBlock)) { //if CheckPoint already exists
				checkpoints.remove(interactedBlock);
				player.sendMessage(ChatColor.RED+"You successfully removed this checkpoint!");
				return;
			}else {
				player.sendMessage(ChatColor.RED+"Only checkpoints that already exists can be removed!!");
				return;
			}
		}
	}
	
	private void onPlateTrigger(final PlayerInteractEvent e) {
		if (e.getAction() != Action.PHYSICAL || e.getClickedBlock().getType() != Material.GOLD_PLATE)return;
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(shpk, new Runnable() {
            @Override
            public void run() {
                // Do something
            	Location location = e.getClickedBlock().getLocation();
            	location.subtract(new Vector(0, 1, 0));
            	if (location.getBlock().getBlockPower() == 15 ) { runGame(e); }
            }
        }, 2L);
		
	}
	//############################# GAMEMODE #####################################
	private void runGame(final PlayerInteractEvent e) {
		if (players.containsKey(e.getPlayer().getName())) {
			gameContinue(e);
		}else {
			gameStart(e);
		}
	}

	private void gameStart(PlayerInteractEvent e) {
		PKArena arena = findArenaByStartCheckPoint(e.getClickedBlock().getLocation());// Check if triggered plate is a startpoint.
		if (arena == null) {
			//e.getPlayer().sendMessage(ChatColor.GOLD+"This is not the startpoint of this arena!");
			return;
		}else{
			Player player = e.getPlayer();
			players.put(player.getName(), arena);
			arena.addPlayer(player);
			e.getPlayer().sendMessage(ChatColor.GREEN+"You have joined arena: "+ arena.getname());
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 100.0f, 1f);
			return;
		}
	}

	private void gameContinue(PlayerInteractEvent e) {
		PKArena arena = players.get(e.getPlayer());// gets arena the player is currently in.
		//Start contents
		
		arena.addCheckPoint(e.getPlayer(), e.getClickedBlock());
		
		//End contents
		if (arena.isPlayerFinished(e.getPlayer())) {
			players.remove(e.getPlayer().getName());
			arena.finishPlayer(e.getPlayer());
		}
	}

	//############################# EDITMODE #####################################
	public void startEditMode(Player player, String arenaName) {
		editors.put(player.getName(), arenaName);
		editorData.put(player.getName(), new ArrayList<Block>());
		player.getInventory().addItem(editorWand);
		player.sendMessage(ChatColor.GOLD+"you now got "+editorWand.getItemMeta().getDisplayName()+"!");
		player.sendMessage(ChatColor.GREEN+"RightClick"+ChatColor.GOLD+" to add a new checkpoint!");
		player.sendMessage(ChatColor.GREEN+"LeftClick"+ChatColor.GOLD+" to remove the selected checkpoint!");
	}

	public void finishArena(Player player) {
		if (!editors.containsKey(player.getName())) {
			player.sendMessage(ChatColor.RED+"You need to be in edit mode to finish a arena!");
			return;
		}else if (!editorData.containsKey(player.getName()) || (editorData.get(player.getName()).size()-1) <= 0) {
			player.sendMessage(ChatColor.RED+"You need to have checkpoints to finish a arena!");
		}else if ((editorData.get(player.getName()).size()-1) >= 1) {
			List<Location> checkpointlist = new ArrayList<Location>();
			List<Block> blocklist = editorData.get(player.getName());
			for (Block b : blocklist) {
				Location loc = b.getLocation();
				checkpointlist.add(loc);
			}
			PKArena arena = new PKArena(editors.get(player.getName()), checkpointlist, player.getLocation().add(0, 0.2, 0),shpk);
			shpk.getServer().getPluginManager().registerEvents(arena, shpk);
			this.arenas.add(arena);
			saveToConfigFile(arena);
			editors.remove(player.getName());
			editorData.remove(player.getName());
			player.getInventory().remove(editorWand);
			player.sendMessage(ChatColor.GREEN+"You have successfully finished a arena!");
		}else {
			player.sendMessage(ChatColor.RED+"error occurred by finishing a arena!");
		}
	}
	//############################# CONFIGMETHODS #####################################
	
	private void saveToConfigFile(PKArena arena) {
		@SuppressWarnings("unchecked")
		List<String> configList = (List<String>)shpk.getConfig().getList("ArenaNamesList");
		configList.add((String)arena.getname().toString());
		shpk.getConfig().set("ArenaNamesList", configList);
		String ArenaDetailList = arena.getname().toString();
		shpk.getConfig().set(ArenaDetailList+".name", arena.getname().toString());
		shpk.getConfig().set(ArenaDetailList+".lobbylocation.world", arena.getLobbyLocation().getWorld().getName().toString());
		shpk.getConfig().set(ArenaDetailList+".lobbylocation.x", (double) arena.getLobbyLocation().getX());
		shpk.getConfig().set(ArenaDetailList+".lobbylocation.y", (double) arena.getLobbyLocation().getY());
		shpk.getConfig().set(ArenaDetailList+".lobbylocation.z", (double) arena.getLobbyLocation().getZ());
		List<Location> blocklist = arena.getCheckPoints();
		shpk.getConfig().set(ArenaDetailList+".checkpoints",blocklist.size());
		for (Integer i = 0; i < blocklist.size(); i++) {
			Location loc = blocklist.get(i);
			shpk.getConfig().set(ArenaDetailList+".checkpoint."+i.toString()+".world", loc.getWorld().getName().toString());
			shpk.getConfig().set(ArenaDetailList+".checkpoint."+i.toString()+".x", loc.getX());
			shpk.getConfig().set(ArenaDetailList+".checkpoint."+i.toString()+".y", loc.getY());
			shpk.getConfig().set(ArenaDetailList+".checkpoint."+i.toString()+".z", loc.getZ());
		}
		List<String> blacklistMaterials = new ArrayList<String>();
		blacklistMaterials.add(Material.WATER.toString());
		blacklistMaterials.add(Material.STATIONARY_WATER.toString());
		blacklistMaterials.add(Material.LAVA.toString());
		blacklistMaterials.add(Material.STATIONARY_LAVA.toString());
		shpk.getConfig().set(ArenaDetailList+".blacklistedMaterials",blacklistMaterials);
		shpk.saveConfig();
	}
	
	@SuppressWarnings("unchecked")
	public void loadAllArenasFromArenaFile() {
		List<String> configList = (List<String>)shpk.getConfig().getList("ArenaNamesList");
		FileConfiguration config = shpk.getConfig();
		for (String arenaconfigname : configList) {
			if (!config.contains(arenaconfigname)){
				System.out.println("Arena: "+arenaconfigname+" not found!");
				continue;
			}
			String arenaName = config.getString(arenaconfigname+".name");
			Location lobbyloc = new Location(shpk.getServer().getWorld(config.getString(arenaconfigname+".lobbylocation.world")), config.getDouble(arenaconfigname+".lobbylocation.x"), config.getDouble(arenaconfigname+".lobbylocation.y"), config.getDouble(arenaconfigname+".lobbylocation.z"));
			List<Location> checkpointlocs = new ArrayList<Location>();
			int checkpointscount = config.getInt(arenaconfigname+".checkpoints");
			if (checkpointscount <= 0) {
				System.out.println("Arena: "+arenaconfigname+" has no checkpoints!");
				continue;
			}
			for (int i = 0; i < checkpointscount; i++) {
				String checkpointConfigIndexString = arenaconfigname+".checkpoint."+i;
				String world = config.getString(checkpointConfigIndexString+".world");
				World w = shpk.getServer().getWorld(world);
				double x = config.getDouble(checkpointConfigIndexString+".x");
				double y = config.getDouble(checkpointConfigIndexString+".y");
				double z = config.getDouble(checkpointConfigIndexString+".z");
				Location checkpointloc = new Location(w, x, y, z);
				checkpointlocs.add(checkpointloc);
			}
			List<String> blacklistMaterialsStrings = config.getStringList(arenaconfigname+".blacklistedMaterials");
			List<Material> blacklistMaterials = new ArrayList<Material>();
			for (String materialstring : blacklistMaterialsStrings) {
				Material mat = Material.getMaterial(materialstring);
				if (mat == null)continue;
				blacklistMaterials.add(mat);
			}
			PKArena a = new PKArena(arenaName, checkpointlocs, lobbyloc,shpk);
			a.setBlackListedMaterials(blacklistMaterials);
			shpk.getServer().getPluginManager().registerEvents(a, shpk);
			this.arenas.add(a);
		}
	}

	public List<PKArena> getArenaList() {
		return this.arenas;
	}

	public boolean removeArenaByName(String name) {
		PKArena arena = findArenaByName(name);
		if (arena == null) {
			return false;
		}
		arenas.remove(arena);
		@SuppressWarnings("unchecked")
		List<String> configList = (List<String>)shpk.getConfig().getList("ArenaNamesList");
		configList.remove(arena.getname());
		config.set("ArenaNamesList", configList);
		config.set(arena.getname(), null);
		shpk.saveConfig();
		return true;
	}

	private PKArena findArenaByName(String name) {
		for (PKArena a : arenas) {
			if (a.getname().equalsIgnoreCase(name)) {
				return a;
			}
		}
		return null;
	}
	
	public void removePlayerFromHisArena(Player player) {
		if (players.containsKey(player.getName())) {
			PKArena a = players.get(player.getName());
			players.remove(player.getName());
			a.quitPlayer(player);
		}else {
			player.sendMessage(ChatColor.GOLD+"You need to be in a arena to quit a game!");
		}
	}

	public void teleportPlayerToLastCheckPoint(Player player) {
		if (!players.containsKey(player.getName())) {
			player.sendMessage(ChatColor.RED+"You need to be in a arena to use this command!");
			return;
		}
		PKArena a = players.get(player.getName());
		Location loc = a.getCheckPointOf(player);
		if (loc == null) {
			player.sendMessage(ChatColor.RED+"No current checkpoint found!");
			return;
		}
		player.teleport(loc);
		player.playSound(player.getLocation(), Sound.ENTITY_IRONGOLEM_HURT, 100.0f, 2f);
		player.sendMessage(ChatColor.GREEN+"successfully teleported you back to your last checkpoint!");
	}
	
}
