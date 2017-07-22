package main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;

import manager.NPCHandler;
import net.minecraft.server.v1_11_R1.EntityPlayer;
import net.minecraft.server.v1_11_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_11_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_11_R1.PlayerConnection;
import net.minecraft.server.v1_11_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

public class Eventshandler implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void OnPlayerJoin(PlayerJoinEvent e) {
		e.getPlayer().sendTitle(ChatColor.GOLD+"Clock Hunters", ChatColor.AQUA+"Made by PublicGameCoder");
		for (NPC npcTypeNPC : NPCHandler.getHandler().getNPCList()) {
			EntityPlayer npcTypeEntityPlayer = npcTypeNPC.getNPC();
            PlayerConnection connection = ((CraftPlayer) e.getPlayer()).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, npcTypeEntityPlayer));
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npcTypeEntityPlayer));
		}
	}
	
	@EventHandler
	public void OnPlayerInteract(PlayerInteractEvent e) {
		testForButtons(e);
	}
	
	private void testForButtons(final PlayerInteractEvent e) {
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(Main.plugin, new Runnable() {
            @Override
            public void run() {
                // Do something
            	if (new Location(Bukkit.getWorld("world"), -682, 51, 2234).getBlock().getBlockPower() == 15 ) { YTButton(e); }
            	if (new Location(Bukkit.getWorld("world"), -662, 51, 2254).getBlock().getBlockPower() == 15 ) { SiteButton(e); }
            	if (new Location(Bukkit.getWorld("world"), -702, 51, 2254).getBlock().getBlockPower() == 15 ) { IssueButton(e); }
            	if (new Location(Bukkit.getWorld("world"), -682, 51, 2274).getBlock().getBlockPower() == 15 ) { InfoButton(e); }
            }
        }, 5L);
		
	}

	private void InfoButton(PlayerInteractEvent e) {
		e.getPlayer().sendMessage(" \n"+ChatColor.AQUA+"Info\n"+ChatColor.GOLD+
				"This map doesn't use commandblocks!\n"+
				"We are using plugins instead.\n"+
				"Some are free to download.\n"+
				"But the most are custom made!\n"+
				"I hope you enjoy.\n ");
		
	}

	private void IssueButton(PlayerInteractEvent e) {
		e.getPlayer().sendMessage(ChatColor.GOLD+"\nGot an issue with the map already?");
		e.getPlayer().sendMessage(ChatColor.GOLD+"Maybe try to typ "+ ChatColor.AQUA +"/map "+ChatColor.GOLD + "to see what you can do!");
		e.getPlayer().sendMessage(ChatColor.GRAY+"for other issue's please send me an image and explanation on the Planet Minecraft website!");
		e.getPlayer().sendMessage(ChatColor.GREEN+"Planet Minecraft: "+ChatColor.GRAY+": http://www.planetminecraft.com/project/clock-hunters/");
		
	}

	private void SiteButton(PlayerInteractEvent e) {
		e.getPlayer().sendMessage(ChatColor.GREEN+"Our website"+ChatColor.GRAY+": SpaceHorizons.nl!");
		
	}

	private void YTButton(PlayerInteractEvent e) {
		e.getPlayer().sendMessage(ChatColor.RED+"You"+ChatColor.WHITE+"tube"+ChatColor.GRAY+": link goes here!");
		
	}
}
