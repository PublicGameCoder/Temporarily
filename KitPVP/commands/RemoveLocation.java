package commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import managers.ArenaManager;
import utils.ChatUtil;

public class RemoveLocation {

	public static void HandleCommand(Player p, Command cmd, String label, String[] args) {
		if(args.length != 1){
            ChatUtil.Message(p, "Insuffcient arguments!");
            return;
        }
		int num = 0;
		try{
            num = Integer.parseInt(args[0]);
        }catch(NumberFormatException e){
            ChatUtil.Message(p, "Invalid arena ID");
        }
		Location loc = p.getLocation().getBlock().getLocation();
		if (!ArenaManager.getManager().getArena(num).removeLootContainerLocation(loc)) {
			ChatUtil.Message(p, "Location not found in our library");
			return;
		}
		ChatUtil.Message(p, "Removed Location successful from arena: ID "+ num);
	}

}
