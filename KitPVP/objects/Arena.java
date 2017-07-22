package objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import shkitpvp.SHKitPVP;
import utils.ChatUtil;

public class Arena {

    //you want some info about the arena stored here
	private static SHKitPVP plugin = (SHKitPVP)Bukkit.getPluginManager().getPlugin("SHKitPVP");
    public int id = 0;//the arena id
    public Location spawn = null;//spawn location for the arena
    List<String> players = new ArrayList<String>();//list of players
    List<Location> LootContainerLocations = new ArrayList<Location>();
    List<Inventory> LootContainerInventories = new ArrayList<Inventory>();
    Map<Location, Inventory> LootContainers = new HashMap<Location, Inventory>();

    //now let's make a few getters/setters, and a constructor
    public Arena(Location loc, int id){
        this.spawn = loc;
        this.id = id;
        plugin.getLogger().info(""+ plugin);
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
        	
			@Override
            public void run() {
        		if (LootContainerLocations.size() <= 0 || LootContainerInventories.size() <= 0 || LootContainers.size() >= LootContainerLocations.size()) {
        			return;
        		}
        		//Create and Spawn lootContainer
        		Random random = new Random();
        		int locindex = random.nextInt(LootContainerLocations.size());
        		Location loc = LootContainerLocations.get(locindex);
        		
        		int invindex = random.nextInt(LootContainerInventories.size());
        		Inventory inv = LootContainerInventories.get(invindex);
        		
        		LootContainers.put(loc, inv);
        		Bukkit.getWorld(loc.getWorld().getName()).getBlockAt(loc).setType(Material.ENDER_PORTAL_FRAME);
            }
        }, 0L, 60L);
    }
    
    public void addLootContainerLocation(Location loc){
    	LootContainerLocations.add(loc);
    }
    
    public void addLootContainerInventorie(Inventory inv) {
    	LootContainerInventories.add(inv);
    }
    
    public boolean removeLootContainerLocation(Location loc) {
		if (!LootContainerLocations.remove(loc)) {
			return false;
		}
		return true;
	}

	public boolean removeLootContainerInventorie(int invnum) {
		if (invnum > (LootContainerInventories.size()-1) || invnum < 0) {
			return false;
		}
		Inventory inv = LootContainerInventories.get(invnum);
		if (!LootContainerInventories.remove(inv)) {
			return false;
		}
		return true;
	}
    public int getId(){
        return this.id;
    }

    public List<String> getPlayers(){
        return this.players;
    }

	public List<Location> getLocations() {
		return this.LootContainerLocations;
	}

	public List<Inventory> getInventories() {
		return this.LootContainerInventories;
	}
	
	public void OpenInventoryFromLootContainer(Player player,Location loc) {
		if (!LootContainers.containsKey(loc)) {
			if (loc.getBlock().getType() == Material.ENDER_PORTAL_FRAME) {
				ChatUtil.Message(player, ChatColor.RED + "Sorry we couldn't open that LootContainer");
			}
			return;
		}
		Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST);
		ItemStack[] items = LootContainers.get(loc).getContents();
		inv.setContents(items);
		loc.getBlock().setType(Material.AIR);
		LootContainers.remove(loc);
		player.openInventory(inv);
	}
	
}
