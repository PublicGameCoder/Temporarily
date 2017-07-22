package nl.spacehorizons.minecitypolice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

public class Listeners implements Listener {

	public static HashMap<String, ItemStack> ItemList = new HashMap<String, ItemStack>();
	public static Inventory PoliceMainInventory, PoliceWeaponInventory, PoliceClothInventory;
	Map<String,Player> shacklePlayers = new HashMap<String,Player>();
	Map<String,Player> unshacklePlayers = new HashMap<String,Player>();
	private String displayName;

	public Listeners() {
		createPoliceMainInventory();
		createPoliceWeaponInventory();
		createPoliceClothInventory();
	}
	
	@EventHandler
	public void onCowKill(EntityDeathEvent e) {
		if (e.getEntityType() == EntityType.COW) {
			Entity cow = e.getEntity();
		    cow.getLocation().add(new Vector(0,1,0));
		    Bukkit.getServer().getWorld(cow.getWorld().getName()).playEffect(cow.getLocation(), Effect.HAPPY_VILLAGER, 1,3);
		}
	}
	
	private void createPoliceClothInventory() {
		PoliceClothInventory = Bukkit.createInventory(null, 54, ChatColor.GOLD + "" + ChatColor.BOLD + "Politie Kleren");
		
		displayName = ChatColor.GREEN + " ";
		CreateSlotItem(PoliceClothInventory, Material.STAINED_GLASS_PANE, 1, new int[] { 6, 1 }, null, new String[] { }, null, displayName, "BorderTopCloth", new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 }, true);
		CreateSlotItem(PoliceClothInventory, Material.STAINED_GLASS_PANE, 1, new int[] { 9 }, null, new String[] { }, null, displayName, "BorderSidesCloth", new Integer[] { 9, 17, 18, 26, 27, 35, 36, 44, 45, 53 }, true);
		
		displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "Politie kleren";
		CreateSlotItem(PoliceClothInventory, Material.WOOL, 1, new int[] { 11 }, null, new String[] { }, null, displayName, "PoliceClothes", new Integer[] { 20 }, false);
		
		displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "Terug";
		CreateSlotItem(PoliceClothInventory, Material.BARRIER, 1, new int[] { 0 }, null, new String[] { }, null, displayName, "BackFromCloth", new Integer[] { 49 }, false);
	}

	private void createPoliceWeaponInventory() {
		PoliceWeaponInventory = Bukkit.createInventory(null, 54, ChatColor.GOLD + "" + ChatColor.BOLD + "Politie Wapens en Munutie");
		
		displayName = ChatColor.GREEN + " ";
		CreateSlotItem(PoliceWeaponInventory, Material.STAINED_GLASS_PANE, 1, new int[] { 14, 11 }, null, new String[] { }, null, displayName, "BorderTopWeapon", new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 }, true);
		CreateSlotItem(PoliceWeaponInventory, Material.STAINED_GLASS_PANE, 1, new int[] { 9 }, null, new String[] { }, null, displayName, "BorderSidesWeapon", new Integer[] { 9, 17, 18, 26, 27, 35, 36, 44, 45, 53 }, true);
		
		displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "Tazer";
		CreateSlotItem(PoliceWeaponInventory, Material.GOLD_HOE, 1, new int[] { 0 }, null, new String[] {"",ChatColor.GRAY+"==============================",ChatColor.GREEN+ "Rightclick  om iemand te tazeren!",ChatColor.GRAY+"==============================",""}, null, displayName, "Tazer", new Integer[] { 20 }, false);
		
		displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "Handboeien";
		CreateSlotItem(PoliceWeaponInventory, Material.RED_ROSE, 1, new int[] { 8 }, null, new String[] {"",ChatColor.GRAY+"==============================",ChatColor.GREEN+ "Leftclick om iemand te boeien!",ChatColor.GOLD+ "Rightclick om iemand te onboeien!",ChatColor.GRAY+"==============================",""}, null, displayName, "Shackle", new Integer[] { 21 }, false);
		
		displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "M16";
		CreateSlotItem(PoliceWeaponInventory, Material.SPECKLED_MELON, 1, new int[] { 0 }, null, new String[] {"",ChatColor.AQUA+"Je hebt "+ChatColor.GOLD+"0"+" kogels over!",ChatColor.GRAY+"==============================",ChatColor.GREEN+ "Leftclick om te reloaden!",ChatColor.GOLD+ "Rightclick om te schieten!",ChatColor.GRAY+"==============================",""}, null, displayName, "M16", new Integer[] { 28 }, false);
		
		displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "M16 Ammo";
		CreateSlotItem(PoliceWeaponInventory, Material.INK_SACK, 1, new int[] { 5 }, null, new String[] {"",ChatColor.GRAY+"==============================",ChatColor.GOLD+ "Ammo voor een M16 geweer!",ChatColor.GRAY+"==============================",""}, null, displayName, "M16 Ammo", new Integer[] { 37 }, false);
		
		displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "Terug";
		CreateSlotItem(PoliceWeaponInventory, Material.BARRIER, 1, new int[] { 0 }, null, new String[] { }, null, displayName, "BackFromWeapon", new Integer[] { 49 }, false);
	}

	private void createPoliceMainInventory() {
		PoliceMainInventory = Bukkit.createInventory(null, 54, ChatColor.GOLD + "" + ChatColor.BOLD + "Politie Centrum");
		
		displayName = ChatColor.GREEN + " ";
		CreateSlotItem(PoliceMainInventory, Material.STAINED_GLASS_PANE, 1, new int[] { 4, 5 }, null, new String[] { }, null, displayName, "BorderTopMain", new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 }, true);
		CreateSlotItem(PoliceMainInventory, Material.STAINED_GLASS_PANE, 1, new int[] { 9 }, null, new String[] { }, null, displayName, "BorderSidesMain", new Integer[] { 9, 17, 18, 26, 27, 35, 36, 44, 45, 53 }, true);

		displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "Wapens";
		CreateSlotItem(PoliceMainInventory, Material.IRON_SWORD, 1, new int[] { 0 }, null, new String[] { }, null, displayName, "Weapons", new Integer[] { 20 }, false);
		
		displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "Kleren";
		CreateSlotItem(PoliceMainInventory, Material.IRON_CHESTPLATE, 1, new int[] { 0 }, null, new String[] { }, null, displayName, "Clothes", new Integer[] { 24 }, false);
	}

	private void CreateSlotItem(Inventory inventory, Material material, int amount, int[] data, Color color, String[] lores,HashMap<Enchantment,Integer> enchantments, String displayName, String getterName, Integer[] SlotNumbers, boolean Repeater) {
		for (int i = 0; i < data.length; i++) {
			if (data[i] < 0) {
				data[i] = 0;
			}
			if (data[i] > 127) {
				data[i] = 127;
			}
		}
		ItemStack item = new ItemStack(material, amount, (byte) data[0]);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		if (lores.length > 0) {
			meta.setLore(new ArrayList<String>(Arrays.asList(lores)));
		}
		item.setItemMeta(meta);
		if (item.getType() == Material.LEATHER_BOOTS || item.getType() == Material.LEATHER_CHESTPLATE || item.getType() == Material.LEATHER_LEGGINGS || item.getType() == Material.LEATHER_HELMET) {
			LeatherArmorMeta armourmeta = (LeatherArmorMeta) item.getItemMeta();
			armourmeta.setColor(color);
			item.setItemMeta(armourmeta);
		}
		if (enchantments != null) {
			for (Map.Entry<Enchantment, Integer> compiledmap : enchantments.entrySet()) {
				Enchantment ench = compiledmap.getKey();
				int level = compiledmap.getValue();
				item.addUnsafeEnchantment(ench, level);
			}
		}
		if (Repeater) {
			int Counter = 0;
			for (int i = 0; i < SlotNumbers.length; i++) {
				if (SlotNumbers[i] > 53) {
					SlotNumbers[i] = 53;
				}
				item = new ItemStack(material, amount, (byte) data[Counter]);
				inventory.setItem(SlotNumbers[i], item);
				if (getterName != null) {
					ItemList.put(getterName + i, item);
				}
				Counter++;
				if (Counter >= data.length) {
					Counter = 0;
				}
			}
		}
		if (SlotNumbers[0] > 53) {
			SlotNumbers[0] = 53;
		}
		inventory.setItem(SlotNumbers[0], item);
		if (getterName != null) {
			ItemList.put(getterName, item);
		}
	}
	
	@EventHandler
	public void onClickItem(InventoryClickEvent e) {
		if (e.getInventory().equals(PoliceMainInventory)) {
			e.setCancelled(true);
		}
		if (e.getInventory().equals(PoliceWeaponInventory)) {
			e.setCancelled(true);
		}
		if (e.getInventory().equals(PoliceClothInventory)) {
			e.setCancelled(true);
		}
		
		if (!(e.getWhoClicked() instanceof Player)) return;
		Player player = (Player) e.getWhoClicked();
		mainInventoryHandler(e,player);
		weaponInventoryHandler(e,player);
		clothInventoryHandler(e,player);
	}

	private void clothInventoryHandler(InventoryClickEvent e, Player player) {
		if (e.getCurrentItem().equals(ItemList.get("BackFromCloth"))) {
			player.openInventory(PoliceMainInventory);
			return;
		}
		
		if (e.getCurrentItem().equals(ItemList.get("PoliceClothes"))) {
			
			HashMap<Enchantment, Integer> enchantmentList = new HashMap<Enchantment,Integer>();
			enchantmentList.put(Enchantment.DURABILITY, 5);
			enchantmentList.put(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			
			displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "Politie schoenen";
			CreateSlotItem(player.getInventory(), Material.LEATHER_BOOTS, 1, new int[] { 0 }, Color.BLUE, new String[] { }, enchantmentList, displayName, "PoliceShoes", new Integer[] { 36 }, false);
			displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "Politie broek";
			CreateSlotItem(player.getInventory(), Material.LEATHER_LEGGINGS, 1, new int[] { 0 }, Color.BLUE, new String[] { }, enchantmentList, displayName, "PoliceLeggings", new Integer[] { 37 }, false);
			displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "Politie shirt";
			CreateSlotItem(player.getInventory(), Material.LEATHER_CHESTPLATE, 1, new int[] { 0 }, Color.BLUE, new String[] { }, enchantmentList, displayName, "PoliceChestplate", new Integer[] { 38 }, false);
			displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "Politie helm";
			CreateSlotItem(player.getInventory(), Material.LEATHER_HELMET, 1, new int[] { 0 }, Color.BLUE, new String[] { }, enchantmentList, displayName, "PoliceHelmet", new Integer[] { 39 }, false);
			player.sendMessage(ChatColor.BLUE + "Je hebt politie kleren aangetrokken!");
			player.closeInventory();
			return;
		}
	}
	
	@SuppressWarnings("unused")
	private void createAndAddScoreBoard(Player player, String objectivename, String type,String displayname, DisplaySlot displayslot) {
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective objective = sb.registerNewObjective(objectivename, type);
		objective.setDisplayName(displayname);
		objective.setDisplaySlot(displayslot);
		player.setScoreboard(sb);
	}

	private void weaponInventoryHandler(InventoryClickEvent e, Player player) {
		if (e.getCurrentItem().equals(ItemList.get("BackFromWeapon"))) {
			player.openInventory(PoliceMainInventory);
			return;
		}
		
		if (e.getCurrentItem().equals(ItemList.get("Tazer"))) {
			if (e.getClickedInventory().equals(PoliceWeaponInventory)) {
				player.getInventory().addItem(ItemList.get("Tazer"));
			}
			return;
		}
		
		if (e.getCurrentItem().equals(ItemList.get("Shackle"))) {
			if (e.getClickedInventory().equals(PoliceWeaponInventory)) {
				player.getInventory().addItem(ItemList.get("Shackle"));
			}
			return;
		}
		
		if (e.getCurrentItem().equals(ItemList.get("M16"))) {
			if (e.getClickedInventory().equals(PoliceWeaponInventory)) {
				player.getInventory().addItem(ItemList.get("M16"));
			}
			return;
		}
		
		if (e.getCurrentItem().equals(ItemList.get("M16 Ammo"))) {
			if (e.getClickedInventory().equals(PoliceWeaponInventory)) {
				player.getInventory().addItem(ItemList.get("M16 Ammo"));
			}
			return;
		}
	}

	private void mainInventoryHandler(InventoryClickEvent e, Player player) {
		if (e.getCurrentItem().equals(ItemList.get("Weapons"))) {
			player.openInventory(PoliceWeaponInventory);
			return;
		}
		
		if (e.getCurrentItem().equals(ItemList.get("Clothes"))) {
			player.openInventory(PoliceClothInventory);
			return;
		}
	}
	
	@EventHandler
	public void onRightclick(PlayerInteractEvent e) {
		if (!(e.getPlayer() instanceof Player)) return;
		if (e.getItem().equals(null)) return;
		Player player = (Player) e.getPlayer();
		TazerHandler(e,player);
		Boei(e,player);
		M16(e,player);
		return;
	}

	@SuppressWarnings("deprecation")
	private void M16(PlayerInteractEvent e, Player player) {
		if (!(e.getItem().equals(ItemList.get("Shackle")))) return;
		boolean leftclicked,rightclicked;
		if (!(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
			leftclicked = true;
			rightclicked = false;
		}else {
			leftclicked = false;
			rightclicked = true;
		}
		if (rightclicked){
			if (checkHasEnoughAmmo(player)){

				ItemStack currentItem = player.getItemInHand();
				ItemMeta meta = currentItem.getItemMeta();
				for (int i = 0; i < 12; i++) {
					if (meta.equals(new String[] {"",ChatColor.AQUA+"Je hebt "+ChatColor.GOLD+""+i+" kogels over!",ChatColor.GRAY+"==============================",ChatColor.GREEN+ "Leftclick om te reloaden!",ChatColor.GOLD+ "Rightclick om te schieten!",ChatColor.GRAY+"==============================",""}));
					String[] setlore = new String[] {"",ChatColor.AQUA+"Je hebt "+ChatColor.GOLD+""+(i-1)+" kogels over!",ChatColor.GRAY+"==============================",ChatColor.GREEN+ "Leftclick om te reloaden!",ChatColor.GOLD+ "Rightclick om te schieten!",ChatColor.GRAY+"==============================",""};
					meta.setLore(new ArrayList<String>(Arrays.asList(setlore)));
				}
				Location playerloc = player.getLocation();
				Vector targetLoc = player.getLocation().getDirection();
				player.playSound(player.getPlayer().getLocation(), Sound.ENTITY_ENDERDRAGON_FIREBALL_EXPLODE, 1F, 1F);
				targetLoc.normalize();
				for (int i = 0; i <= 20; i++) {
					for(Player p : Bukkit.getOnlinePlayers()) {
						if (p.getLocation().getBlock().getLocation().equals(playerloc.getBlock().getLocation().add(0, 1, 0)) && !p.isDead() && p != player) {
							p.damage(4);
						}
					}
					playerloc.add(targetLoc);
				}
			}else {
				player.sendMessage(ChatColor.RED+"Je M16 is leeg!");
			}
		}
		else if (leftclicked) {
			player.getInventory().contains(ItemList.get("M16 Ammo"), 1);
			int slot = player.getInventory().first(ItemList.get("M16 Ammo"));
			int itemAmount = player.getInventory().getItem(slot).getAmount();
			ItemStack newItem = ItemList.get("M16 Ammo");
			newItem.setAmount(itemAmount - 1);
			if (newItem.getAmount() <= 0) {
				newItem.setType(Material.AIR);
			}
			player.getInventory().setItem(slot, newItem);
			ItemStack currentItem = player.getItemInHand();
			ItemMeta meta = currentItem.getItemMeta();
			String[] setlore = new String[] {"",ChatColor.AQUA+"Je hebt "+ChatColor.GOLD+"12"+" kogels over!",ChatColor.GRAY+"==============================",ChatColor.GREEN+ "Leftclick om te reloaden!",ChatColor.GOLD+ "Rightclick om te schieten!",ChatColor.GRAY+"==============================",""};
			meta.setLore(new ArrayList<String>(Arrays.asList(setlore)));
		}
		return;
	}

	@SuppressWarnings("deprecation")
	private boolean checkHasEnoughAmmo(Player player) {
		ItemStack currentItem = player.getItemInHand();
		ItemMeta meta = currentItem.getItemMeta();
		if (!meta.getLore().equals(ChatColor.AQUA+"Je hebt "+ChatColor.GOLD+""+0+" kogels over!")){
			return true;
		}else {
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	private void TazerHandler(PlayerInteractEvent e, Player player) {
		if (!(e.getItem().equals(ItemList.get("Tazer")))) return;
		if (!(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
			return;
		}
		Location playerloc = player.getLocation();
		Vector targetLoc = player.getLocation().getDirection();
		targetLoc.normalize();
		List<Player> tazePlayers = new ArrayList<Player>();
		for (int i = 0; i <= 3; i++) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				if (p.getLocation().getBlock().getLocation().equals(playerloc.getBlock().getLocation().add(0, 1, 0)) && !p.isDead() && p != player) {
					tazePlayers.add(tazePlayers.size(), p);
				}
			}
			playerloc.add(targetLoc);
		}
		String tazed = "Je hebt";
		for (Player p : tazePlayers) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 61, 1,false,false));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 7,false,false));
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 200, 128,false,false));
			p.damage(3);
			p.playSound(p.getPlayer().getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1F, 1F);
			Location particleLoc = p.getLocation().getBlock().getLocation().add(0, 2, 0);
			p.spawnParticle(Particle.FLAME, particleLoc, 20, 0, 0, 0, 0);
			
			if (p.isDead()) {
				player.getServer().broadcastMessage(ChatColor.GRAY+""+ChatColor.UNDERLINE+""+p.getName()+" is dood getazerd door: "+ player.getName());
			}else {
			p.sendMessage(ChatColor.RED+ "Je bent getazerd door: "+ player.getName());
			p.sendTitle(ChatColor.RED+"Getazerd!", ChatColor.GRAY+"Getazerd door: "+player.getName());
			}
			tazed +=  " "+p.getName() + " &";
		}
		if (tazePlayers.size() > 0) {
			tazed = tazed.substring(0, (tazed.length() - 2));
		}
		tazed += " getazerd!";
		if (tazePlayers.size() > 0) {
			player.playSound(player.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1F, 1F);
			player.sendMessage(ChatColor.GOLD + tazed);
		}
		return;
	}
	
	@SuppressWarnings("deprecation")
	private void Boei(PlayerInteractEvent e, Player player) {
		if (!(e.getItem().equals(ItemList.get("Shackle")))) return;
		boolean leftclicked,rightclicked;
		if (!(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
			leftclicked = true;
			rightclicked = false;
		}else {
			leftclicked = false;
			rightclicked = true;
		}
		Location playerloc = player.getLocation();
		Vector targetLoc = player.getLocation().getDirection();
		targetLoc.normalize();
		for (int i = 0; i <= 3; i++) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				if (p.getLocation().getBlock().getLocation().equals(playerloc.getBlock().getLocation().add(0, 1, 0)) && !p.isDead() && p != player) {
					if (shacklePlayers.containsValue(p) && rightclicked){
						shacklePlayers.remove(p.getName());
						unshacklePlayers.put(p.getName(), p);
					}
					
					if (!shacklePlayers.containsValue(p) && leftclicked ){
						shacklePlayers.put(p.getName(), p);
					}
				}
			}
			playerloc.add(targetLoc);
		}
		String shackle = "Je hebt";
		for (Player p : shacklePlayers.values()) {
			if (p.isDead()) return;
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 61, 1,false,false));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 4,false,false));
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 300, 128,false,false));
			p.damage(3);
			p.playSound(p.getPlayer().getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1F, 1F);
			Location particleLoc = p.getLocation().getBlock().getLocation().add(0, 2, 0);
			p.spawnParticle(Particle.CRIT, particleLoc, 20, 0, 0, 0, 1);
			
			if (p.isDead()) {
				player.getServer().broadcastMessage(ChatColor.GRAY+""+ChatColor.UNDERLINE+""+p.getName()+" is dood geboeid door: "+ player.getName());
			}else {
			p.sendMessage(ChatColor.RED+ "Je bent geboeid door: "+ player.getName());
			p.sendTitle(ChatColor.RED+"Geboeid!", ChatColor.GRAY+"Geboeid door: "+player.getName());
			}
			shackle +=  " "+p.getName() + " &";
		}
		if (shacklePlayers.size() > 0) {
			shackle = shackle.substring(0, (shackle.length() - 2));
		}
		shackle += " geboeid!";
		if (shacklePlayers.size() > 0) {
			player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1F, 1F);
			player.sendMessage(ChatColor.GOLD + shackle);
		}
		
		String unshackle = "Je hebt";
		for (Player p : unshacklePlayers.values()) {
			p.removePotionEffect(PotionEffectType.CONFUSION);
			p.removePotionEffect(PotionEffectType.SLOW);
			p.removePotionEffect(PotionEffectType.JUMP);
			p.playSound(p.getPlayer().getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1F, 1F);
			Location particleLoc = p.getLocation().getBlock().getLocation().add(0, 2, 0);
			p.spawnParticle(Particle.CRIT, particleLoc, 20, 0, 0, 0, 1);
			
			if (p.isDead()) {
				player.getServer().broadcastMessage(ChatColor.GRAY+""+ChatColor.UNDERLINE+""+p.getName()+" is dood geboeid door: "+ player.getName());
			}else {
			p.sendMessage(ChatColor.RED+ "Je bent onboeid door: "+ player.getName());
			p.sendTitle(ChatColor.RED+"Onboeid!", ChatColor.GRAY+"Onboeid door: "+player.getName());
			
			}
			unshackle +=  " "+p.getName() + " &";
		}
		if (unshacklePlayers.size() > 0) {
			unshackle = unshackle.substring(0, (unshackle.length() - 2));
		}
		unshackle += " onboeid!";
		if (unshacklePlayers.size() > 0) {
			player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1F, 1F);
			player.sendMessage(ChatColor.GOLD + unshackle);
			for (Player p : unshacklePlayers.values()) {
				unshacklePlayers.remove(p.getName());
			}
		}
		return;
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		e.setDeathMessage(null);
	}
}
