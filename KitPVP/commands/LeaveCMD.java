package commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import managers.ArenaManager;
import utils.ChatUtil;

public class LeaveCMD {

	public static void HandleCommand(Player p, Command cmd, String label, String[] args) {
		ArenaManager.getManager().removePlayer(p);
        ChatUtil.Message(p, "You have left the arena!");
		
	}

}
