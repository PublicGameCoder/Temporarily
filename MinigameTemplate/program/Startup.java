package program;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

import database.MySQL;

public class Startup extends JavaPlugin {
	
	private static Startup instance;
	String[] requiredPlugins = {"Vault","WorldEdit","WorldGuard"};
	private EventsHandler eventHandler;
	MySQL MySQL;
    Connection c = null;
	private String arenaName;
	private ProtectedCuboidRegion region;
	private Map<Flag<?>, Object> flags;
	private boolean countdown = false;
	private int countdownTime = 5;
	private ItemStack KitsMenuItem;
	static int MinPlayers;
	static int MaxPlayers;
	private static boolean respawnTimer = false;
	private static int respawnTime = 5;
	private static boolean kits;
	private static Map<String, Map< Map< ItemStack, Map<String,ItemStack>> , List<ItemStack> >> kitsList; //Map { KitName , Map<DisplayItem, Map< ArmourName , ArmourItem >, List<KitItems> > }
	private static List<String> finishMessage;
	private String[] ArmorList;
	
	@Override
	public void onEnable() {
		checkForRequiredPlugins();
		instance = this;
		Team.clearTeams();
		flags = new HashMap<Flag<?>, Object>();
		createConfig();
		loadConfig();
		loadDatabase();
		for (Player p : Bukkit.getOnlinePlayers()) {
			Points.loadPlayer(p);
		}
		instance.eventHandler = new EventsHandler(instance);
		getServer().getPluginManager().registerEvents(eventHandler, instance);
		ArmorList = new String[] {"Helmet","Chestplate","Leggings","Boots"};
	}
	
	private void checkForRequiredPlugins() {
		boolean disablePlugin = false;
		for (String string : requiredPlugins) {
			if (getServer().getPluginManager().getPlugin(string) == null) {
				getLogger().info("Missing "+string+"!");
				disablePlugin = true;
			}
		}
		if (disablePlugin) {
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
	}

	private void loadDatabase() {
        String host = getConfig().getString("Database.host");
        int port = getConfig().getInt("Database.port");
        String database = getConfig().getString("Database.databaseName");
        String username = getConfig().getString("Database.user");
        String password = getConfig().getString("Database.password");
        MySQL = new MySQL(host, port+"", database, username, password);
        try {
			MySQL.openConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        createTable();
	}
	
	public void createTable(){
        try(Connection connection = MySQL.getConnection();
        		Statement pointsStatement = connection.createStatement();
        		Statement minigameStatement = connection.createStatement();){
        	pointsStatement.executeUpdate("CREATE TABLE IF NOT EXISTS Points(UUID varchar(36), name VARCHAR(16), POINTS int)");
        	minigameStatement.executeUpdate("CREATE TABLE IF NOT EXISTS "+instance.getName()+"(UUID varchar(36), name VARCHAR(16), KILLS int, WINS int, LOSES int)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

	@Override
	public void onDisable() {
		Team.clearTeams();
		for (Player p : Bukkit.getOnlinePlayers()) {
			Points.savePlayer(p);
			p.kickPlayer("Game ended!");
		}
		
		if (this.c != null){
            try {
				this.c.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void createConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
	            getLogger().info("config.yml not found, creating!");
	            getConfig().addDefault("ArenaName", "Game1");
	            getConfig().addDefault("EnableCountdown", true);
	            getConfig().addDefault("CountdownTime", (int) 10);
	            //Setting KITS OPTIONS
	            getConfig().addDefault("EnableKits", false);
	            getConfig().addDefault("KitsMenuItem.Material", Material.WATCH.toString());
	            getConfig().addDefault("KitsMenuItem.Data", (byte) 0);
	            getConfig().addDefault("KitAmount", (int) 2);
	            getConfig().addDefault("Kits.1.KitName", "Basic");
	            getConfig().addDefault("Kits.1.DisplayItem.Material", Material.WOOD_SWORD.toString());
	            getConfig().addDefault("Kits.1.DisplayItem.Data", (byte) 0);
	            getConfig().addDefault("Kits.1.Slot.1.Material", Material.WOOD_SWORD.toString());
	            getConfig().addDefault("Kits.1.Slot.1.Amount", (int) 1);
	            getConfig().addDefault("Kits.1.Slot.1.Damage", (short) 0);
	            getConfig().addDefault("Kits.1.Slot.1.Data", (byte) 0);
	            getConfig().addDefault("Kits.1.Slot.2.Material", Material.APPLE.toString());
	            getConfig().addDefault("Kits.1.Slot.2.Amount", (int) 5);
	            getConfig().addDefault("Kits.1.Slot.2.Damage", (short) 0);
	            getConfig().addDefault("Kits.1.Slot.2.Data", (byte) 0);
	            getConfig().addDefault("Kits.1.ArmorContents.Chestplate.Material", Material.CHAINMAIL_CHESTPLATE.toString());
	            getConfig().addDefault("Kits.1.ArmorContents.Chestplate.EnchantmentsAmount", (int) 1);
	            getConfig().addDefault("Kits.1.ArmorContents.Chestplate.Enchantment.1.Name", Enchantment.DURABILITY.toString());
	            getConfig().addDefault("Kits.1.ArmorContents.Chestplate.Enchantment.2.Level", (int) 1);
	            //Setting RED SPAWN
	            getConfig().addDefault("RedSpawnLocation.worldname", Bukkit.getWorld("world").getName());
	            getConfig().addDefault("RedSpawnLocation.x", (int) -5);
	            getConfig().addDefault("RedSpawnLocation.y", (int) 70);
	            getConfig().addDefault("RedSpawnLocation.z", (int) 0);
	            //Setting BLUE SPAWN
	            getConfig().addDefault("BlueSpawnLocation.worldname", Bukkit.getWorld("world").getName());
	            getConfig().addDefault("BlueSpawnLocation.x", (int) 5);
	            getConfig().addDefault("BlueSpawnLocation.y", (int) 70);
	            getConfig().addDefault("BlueSpawnLocation.z", (int) 0);
	            //Setting WORLD BOUND MINIMUM
	            getConfig().addDefault("Arena.BoundsLocation.Min.x", (int) -20);
	            getConfig().addDefault("Arena.BoundsLocation.Min.y", (int) 0);
	            getConfig().addDefault("Arena.BoundsLocation.Min.z", (int) -20);
	            //Setting WORLD BOUND MAXIMUM
	            getConfig().addDefault("Arena.BoundsLocation.Max.x", (int) 20);
	            getConfig().addDefault("Arena.BoundsLocation.Max.y", (int) 120);
	            getConfig().addDefault("Arena.BoundsLocation.Max.z", (int) 20);
	            //Setting FINISH MESSAGE
	            String[] basicFinishMessage = {"==============[ Finish ]==============".toString(), "Results:".toString(), "  1 - %First%".toString(), "  2 - %Second%".toString(), "  3 - %Third%".toString()};
	            getConfig().addDefault("Arena.Finish.Message",(String[]) basicFinishMessage);
	            //Setting ARENA BOUND PROTECTION
	            getConfig().addDefault("Arena.Flags.CanBreakBlocks", false);
	            getConfig().addDefault("Arena.Flags.CanPlaceBlocks", false);
	            getConfig().addDefault("Arena.Flags.CreeperExplosion", false);
	            getConfig().addDefault("Arena.Flags.DamageAnimals", true);
	            getConfig().addDefault("Arena.Flags.DragonCanBreakBlocks", false);
	            getConfig().addDefault("Arena.Flags.DestroyItemframe", false);
	            getConfig().addDefault("Arena.Flags.DestroyPainting", false);
	            getConfig().addDefault("Arena.Flags.FallDamage", true);
	            getConfig().addDefault("Arena.Flags.Invincibility", false);
	            getConfig().addDefault("Arena.Flags.MobSpawning", false);
	            getConfig().addDefault("Arena.Flags.PVP", true);
	            getConfig().addDefault("Arena.Flags.CanSleepInBed", false);
	            getConfig().addDefault("Arena.Flags.TNT", false);
	            //Setting DATABASE DEFAULTS
	            getConfig().addDefault("Database.host", "host");
	            getConfig().addDefault("Database.port", (int) 25565);
	            getConfig().addDefault("Database.databaseName", "Database1");
	            getConfig().addDefault("Database.user", "user");
	            getConfig().addDefault("Database.password", "password");
	            getConfig().options().copyDefaults(true);
	            saveConfig();
            } else {
                getLogger().info("config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	@SuppressWarnings("deprecation")
	private void loadConfig() {
		Location spawn = null;
		String worldname = "";
		int x = 0;
		int y = 0;
		int z = 0;
		this.arenaName = getConfig().getString("ArenaName");
		this.countdown = getConfig().getBoolean("EnableCountdown");
		this.countdownTime = getConfig().getInt("CountdownTime");
		//Getting KITS OPTIONS
        kits = getConfig().getBoolean("EnableKits");
        if (kits) {
        	System.out.println("Kits are enabled! Loading.....");
	        Material mat = Material.getMaterial(getConfig().getString("KitsMenuItem.Material"));
	        byte data = getConfig().getByteList("KitsMenuItem.Data").get(0);
	        this.KitsMenuItem = new ItemStack(mat, 1, (short) 0, (byte)data);
	        int kitAmount = getConfig().getInt("KitAmount");
	        kitsList = new HashMap<String, Map< Map< ItemStack, Map<String,ItemStack>> , List<ItemStack> >>();
	        Material itemMat = null;
	    	int itemAmount = 0;
	    	short itemDamage = 0;
	    	byte itemData = 0;
	    	Enchantment ench = null;
	        int level = 0;
	        for (int i = 0; i < kitAmount; i++) {
	        	String kitName = getConfig().getString("Kits."+i+".KitName");
	        	Material displayMat = Material.getMaterial(getConfig().getString("Kits."+i+".DisplayItem.Material"));
	        	byte displayData = getConfig().getByteList("Kits."+i+".DisplayItem.Data").get(0);
	        	List<ItemStack> items = new ArrayList<ItemStack>();
		        for (int s = 0; s < 54; s++) {
		        	itemMat = Material.getMaterial(getConfig().getString("Kits."+i+".Slot.1.Material"));
		        	itemAmount = getConfig().getInt("Kits."+i+".Slot."+s+".Amount");
		        	itemDamage = getConfig().getShortList("Kits."+i+".Slot."+s+".Damage").get(0);
		        	itemData = getConfig().getByteList("Kits."+i+".Slot."+s+".Data").get(0);
			        items.add(new ItemStack(itemMat,itemAmount,itemDamage,itemData));
		        }
		        Map<String,ItemStack> armorContents = new HashMap<String,ItemStack>();
		        for (int a = 0; a < 4; a++) {
			        itemMat = Material.getMaterial(getConfig().getString("Kits.1.ArmorContents."+ArmorList[a]+".Material"));
			        int enchantmentAmount = getConfig().getInt("Kits.1.ArmorContents."+ArmorList[a]+".EnchantmentsAmount");
			        ItemStack item = new ItemStack(itemMat,enchantmentAmount);
			        for (int e = 0; e < enchantmentAmount; e++) {
			        	ench = Enchantment.getByName(getConfig().getString("Kits.1.ArmorContents.Chestplate.Enchantment.1.Name"));
			        	level = getConfig().getInt("Kits.1.ArmorContents.Chestplate.Enchantment.2.Level");
			        	item.addEnchantment(ench, level);
			        }
			        armorContents.put(ArmorList[a], item);
		        }
		        ItemStack displayitem = new ItemStack(displayMat,1,(short) 0, (byte) displayData);
		        
		        Map< ItemStack, Map<String,ItemStack>> KitContents2 = new HashMap< ItemStack, Map<String,ItemStack>>();
		        KitContents2.put(displayitem, armorContents);
		        Map< Map< ItemStack, Map<String,ItemStack>> , List<ItemStack> > KitContents = new HashMap< Map< ItemStack, Map<String,ItemStack>> , List<ItemStack> >();
		        KitContents.put(KitContents2, items);
		        kitsList.put(kitName, KitContents);
		        // Order of mapping
		        /*
		         * Map < KitName , KitContents>;
		         * 		KitContents< KitContents2 , List<KitItems> >;
		         * 			KitContents2< DisplayItem, Armour>;
		         * 				Armour<ArmourName , ArmourItem>;
		        */
	        }
        }
        System.out.println("Kits has been loaded!");
		//Getting RED SPAWN
		worldname = getConfig().getString("RedSpawnLocation.worldname");
        x = getConfig().getInt("RedSpawnLocation.x");
        y = getConfig().getInt("RedSpawnLocation.y");
        z = getConfig().getInt("RedSpawnLocation.z");
        spawn = new Location(Bukkit.getWorld(worldname), x, y, z);
        Team.setSpawnLocation(TeamType.RED, spawn);
		//Getting BLUE SPAWN
        worldname = getConfig().getString("BlueSpawnLocation.worldname");
        x = getConfig().getInt("BlueSpawnLocation.x");
        y = getConfig().getInt("BlueSpawnLocation.y");
        z = getConfig().getInt("BlueSpawnLocation.z");
        spawn = new Location(Bukkit.getWorld(worldname), x, y, z);
        Team.setSpawnLocation(TeamType.BLUE, spawn);
        //Getting WORLD BOUND MINIMUM
        x = getConfig().getInt("Arena.BoundsLocation.Min.x");
        y = getConfig().getInt("Arena.BoundsLocation.Min.y");
        z = getConfig().getInt("Arena.BoundsLocation.Min.z");
        Vector p1 = new Vector(x, y, z);
        //Getting WORLD BOUND MAXIMUM
        x = getConfig().getInt("Arena.BoundsLocation.Max.x");
        y = getConfig().getInt("Arena.BoundsLocation.Max.y");
        z = getConfig().getInt("Arena.BoundsLocation.Max.z");
        Vector p2 = new Vector(x, y, z);
        region = new ProtectedCuboidRegion(this.arenaName, p1.toBlockVector(), p2.toBlockVector());
        //Getting FINISH MESSAGE
        finishMessage = (List<String>) getConfig().getList("Arena.Finish.Message");
        //Getting ARENA BOUND PROTECTION
        flags.put(DefaultFlag.BLOCK_BREAK, (getConfig().getBoolean("Arena.Flags.CanBreakBlocks")? State.ALLOW : State.DENY));
        flags.put(DefaultFlag.BLOCK_PLACE, (getConfig().getBoolean("Arena.Flags.CanPlaceBlocks")? State.ALLOW : State.DENY));
        flags.put(DefaultFlag.CREEPER_EXPLOSION, (getConfig().getBoolean("Arena.Flags.CreeperExplosion")? State.ALLOW : State.DENY));
        flags.put(DefaultFlag.DAMAGE_ANIMALS, (getConfig().getBoolean("Arena.Flags.DamageAnimals")? State.ALLOW : State.DENY));
        flags.put(DefaultFlag.ENDERDRAGON_BLOCK_DAMAGE, (getConfig().getBoolean("Arena.Flags.DragonCanBreakBlocks")? State.ALLOW : State.DENY));
        flags.put(DefaultFlag.ENTITY_ITEM_FRAME_DESTROY, (getConfig().getBoolean("Arena.Flags.DestroyItemframe")? State.ALLOW : State.DENY));
        flags.put(DefaultFlag.ENTITY_PAINTING_DESTROY, (getConfig().getBoolean("Arena.Flags.DestroyPainting")? State.ALLOW : State.DENY));
        flags.put(DefaultFlag.FALL_DAMAGE, (getConfig().getBoolean("Arena.Flags.FallDamage")? State.ALLOW : State.DENY));
        flags.put(DefaultFlag.INVINCIBILITY, (getConfig().getBoolean("Arena.Flags.Invincibility")? State.ALLOW : State.DENY));
        flags.put(DefaultFlag.MOB_SPAWNING, (getConfig().getBoolean("Arena.Flags.MobSpawning")? State.ALLOW : State.DENY));
        flags.put(DefaultFlag.MOB_DAMAGE, (getConfig().getBoolean("Arena.Flags.MobDamage")? State.ALLOW : State.DENY));
        flags.put(DefaultFlag.PVP, (getConfig().getBoolean("Arena.Flags.PVP")? State.ALLOW : State.DENY));
        flags.put(DefaultFlag.SLEEP, (getConfig().getBoolean("Arena.Flags.CanSleepInBed")? State.ALLOW : State.DENY));
        flags.put(DefaultFlag.TNT, (getConfig().getBoolean("Arena.Flags.TNT")? State.ALLOW : State.DENY));
        
        region.setFlags(flags);
        System.out.println("Loading succeeded!");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))return false;
		Player player = (Player) sender;
		if (label.equalsIgnoreCase("forcestart")) {
			if(player.hasPermission(getName()+".admin")) {
				eventHandler.setJoinable(false);
				int i = 0;
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (i < Bukkit.getOnlinePlayers().size() / 2) {
						Team.addToTeam(TeamType.RED, p);
					} else {
						Team.addToTeam(TeamType.BLUE, p);
					}
					i++;
				}
				
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (!Team.isInTeam(p)) {
						p.kickPlayer("You've been removed from the game!");
						continue;
					}
					//=============================================================================<<<<<<<<<<<<<<<<<<<<
					Points.addPoints(p, 5);
					//=============================================================================<<<<<<<<<<<<<<<<<<<<
				}
				if (countdown){
					new CountdownTimer(countdownTime).run();
				}
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.teleport(Team.getTeamSpawn(Team.getTeamType(p)));
				}
			}else {
				ChatUtil.sendMessage(player,"You do not have permission to do that!");
			}
		}
		return false;
	}
		

	public static Startup getInstance() {
		return instance;
	}

	public static boolean getRespawnTimer() {
		return respawnTimer;
	}

	public static int getRespawnTime() {
		// TODO Auto-generated method stub
		return respawnTime;
	}

	public static List<String> getFinishMessage() {
		return finishMessage;
	}
}
