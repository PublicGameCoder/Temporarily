package commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import managers.ArenaManager;
import utils.ChatUtil;

public class CreateCMD {

	public static void HandleCommand(Player p, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		ArenaManager.getManager().createArena(p.getLocation());
		String world = p.getLocation().getWorld().getName();
		String cords = p.getLocation().getBlockX() + " " + p.getLocation().getBlockY() + " " + p.getLocation().getBlockZ();
		ChatUtil.Message(p, "Created arena at: "+cords+" in world: "+ world);
	}

	
}
