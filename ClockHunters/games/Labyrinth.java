package games;

import java.util.concurrent.Callable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import games.motions.Motions;
import main.Main;
import managers.MessageManager;
import threads.TeleportThread;

@SuppressWarnings("deprecation")
public class Labyrinth implements Listener {

	private static Labyrinth LR;
	private boolean Started = false;;
	
	private Player controller;
	private Player dummy;
	
	private String[] description = {
		ChatColor.GOLD+"Labyrinth",
		"Desc",
		"Desc",
		"Desc"
	};
	
	private Location contolLoc = new Location(null, 0, 0, 0);
	private Location dummyLoc = new Location(null, 0, 0, 0);
	
	public static Labyrinth getManager() {
		if (LR == null) {
			LR = new Labyrinth();
		}
		
		return LR;
	}
	
	public void Build() {
		ConfigureController();
		ConfigureDummy();
	}
	
	private void ConfigureController() {
		if (controller == null || contolLoc.getWorld() == null) {Main.plugin.getLogger().info("controller status: "+ controller.getName() + " | contolLoc world status: "+ contolLoc.getWorld() ); return;}
		@SuppressWarnings("rawtypes")
		TeleportThread teleportThread = new TeleportThread(new Entity[] {controller}, contolLoc, 5, Particle.PORTAL);
		teleportThread.start();
	}
	
	@SuppressWarnings("unchecked")
	private void ConfigureDummy() {
		if (dummy == null || dummyLoc.getWorld() == null) {Main.plugin.getLogger().info("dummy status: "+ dummy.getName() + " | dummyLoc world status: "+ dummyLoc.getWorld() ); return;}
		@SuppressWarnings("rawtypes")
		TeleportThread teleportThread = new TeleportThread(new Entity[] {dummy}, dummyLoc, 5, Particle.PORTAL);
		teleportThread.CallWhenFinished(Labyrinth.getManager().play());
		teleportThread.start();
	}
	
	@SuppressWarnings("rawtypes")
	public Callable play() {
		dummy.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 0, false, false));
		controller.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 0, false, false));
		
		MessageManager.sendMessage(new Player[] {dummy, controller}, description);
		
		Started = true;
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.plugin);
		
		return null;
	}
	
	@SuppressWarnings("unused")
	private void Finished() {
		Started = false;
		
	}


	/**
	 * @return the controller
	 */
	public Player getController() {
		return controller;
	}
	/**
	 * @param controller the controller to set
	 */
	public void setController(Player controller) {
		this.controller = controller;
	}
	
	/**
	 * @return the dummy
	 */
	public Player getDummy() {
		return dummy;
	}
	/**
	 * @param dummy the dummy to set
	 */
	public void setDummy(Player dummy) {
		this.dummy = dummy;
	}
	
	@EventHandler
	public void onMessageSend(PlayerChatEvent e ) {
		if (!Started) return;
		if (e.getMessage().equalsIgnoreCase(" ")) return;
		String str = e.getMessage();
		
		if (str.equalsIgnoreCase(Motions.left.toString())) {;
		}
		else
		if (str.equalsIgnoreCase(Motions.right.toString())) {
			
		}
		else
		if (str.equalsIgnoreCase(Motions.up.toString())) {
			
		}
		else
		if (str.equalsIgnoreCase(Motions.down.toString())) {
			
		}
		
		return;
	}
}
