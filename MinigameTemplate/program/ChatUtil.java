package program;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtil {
	
	static String markdown = ChatColor.GOLD+"[ "+ChatColor.AQUA+""+ChatColor.BOLD+Startup.getInstance().getName()+ChatColor.RESET+ChatColor.GOLD+" ] "+ChatColor.GRAY;
	
	public static void sendMessage(Player p,String message) {
		p.sendMessage(markdown + message);
	}
}
