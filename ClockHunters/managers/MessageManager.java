package managers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import main.Main;

public class MessageManager {

	private static String str = ChatColor.GRAY+"[ "+ ChatColor.GOLD + Main.plugin.getName() + ChatColor.GRAY + " ] "+ChatColor.BLUE;
	
	public static void sendMessage(Player[] players, String[] strings) {
		String newString = str;
		for (String str : strings) {
			newString += str + "\n";
		}
		for (Player p : players){
			p.sendMessage(newString);
		}
	}

}
