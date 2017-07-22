package utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatUtil {
	
	public static void Message(CommandSender sender,String message) {
		sender.sendMessage(ChatColor.BLUE+"["+ChatColor.RED+"SHKitPVP"+ChatColor.BLUE+"] "+ChatColor.GRAY+ message);
	}
	
	public static void Message(Player p,String message) {
		p.sendMessage(ChatColor.BLUE+"["+ChatColor.RED+"SHKitPVP"+ChatColor.BLUE+"] "+ChatColor.GRAY+ message);
	}
}
