package main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;

import manager.GatesManager;
import manager.NPCHandler;

public class Gamebuilder {
	
	private static Gamebuilder gb;
	private List<ArmorStand> ArmorStandList = new ArrayList<ArmorStand>();

	public static Gamebuilder GetBuilder() {
		// TODO Auto-generated method stub
		if (gb == null) {
			gb = new Gamebuilder();
		}
		return gb;
	}

	public void BuildGame() {
		// TODO Auto-generated method stub
		CreateYoutubeStand();
		CreateSiteStand();
		CreateIssueStand();
		CreateInfoeStand();
		CreateDeadBody1();
		CreateGates();
	}
	
	private void CreateGates() {
		GatesManager.getManager().createGate("gate1","game", 990, 50, 2231);
	}

	private void CreateDeadBody1() {
		World world = Bukkit.getServer().getWorld("game");
		Location loc = new Location(world, 1002.931, 50, 2227.337);
		NPCHandler.getHandler().CreateNPC("Keeper", loc);
	}

	private void CreateYoutubeStand() {
		ArmorStand stand = Bukkit.getWorld("world").spawn(new Location(Bukkit.getWorld("world"), -682.5, 51.2, 2234.5), ArmorStand.class);
		stand.setCustomName(ChatColor.RED+"You"+ChatColor.WHITE+"tube");
		stand.setCustomNameVisible(true);
		stand.setGravity(false);
		stand.setVisible(true);
		ArmorStandList.add(stand);
	}
	
	private void CreateSiteStand() {
		ArmorStand stand = Bukkit.getWorld("world").spawn(new Location(Bukkit.getWorld("world"), -662.5, 51.2, 2254.5), ArmorStand.class);
		stand.setCustomName(ChatColor.GREEN+"Our website");
		stand.setCustomNameVisible(true);
		stand.setGravity(false);
		stand.setVisible(true);
		ArmorStandList.add(stand);
	}
	
	private void CreateIssueStand() {
		ArmorStand stand = Bukkit.getWorld("world").spawn(new Location(Bukkit.getWorld("world"), -702.5, 51.2, 2254.5), ArmorStand.class);
		stand.setCustomName(ChatColor.GOLD+"Issue's?");
		stand.setCustomNameVisible(true);
		stand.setGravity(false);
		stand.setVisible(true);
		ArmorStandList.add(stand);
	}
	
	private void CreateInfoeStand() {
		ArmorStand stand = Bukkit.getWorld("world").spawn(new Location(Bukkit.getWorld("world"), -682.5, 51.2, 2274.5), ArmorStand.class);
		stand.setCustomName(ChatColor.AQUA+"Info");
		stand.setCustomNameVisible(true);
		stand.setGravity(false);
		stand.setVisible(true);
		ArmorStandList.add(stand);
	}
	
	public void remover() {
		removeArmourStands();
		removeNPCs();
	}

	private void removeNPCs() {
		List<NPC> list = NPCHandler.getHandler().getNPCList();
		for (NPC npc : list) {
			npc.unrender();
		}
		
	}

	private void removeArmourStands() {
		for (ArmorStand stand : ArmorStandList) {
			stand.remove();
		}
	}

}
