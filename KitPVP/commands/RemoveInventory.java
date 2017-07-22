package commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import managers.ArenaManager;
import utils.ChatUtil;

public class RemoveInventory {

	public static void HandleCommand(Player p, Command cmd, String label, String[] args) {
		if(args.length != 2){
            ChatUtil.Message(p, "Insuffcient arguments!");
            return;
        }
		int arenanum = 0;
		int invnum = 0;
		try{
			arenanum = Integer.parseInt(args[0]);
			invnum = Integer.parseInt(args[1]);
        }catch(NumberFormatException e){
            ChatUtil.Message(p, "Invalid arena ID and/or inventory number");
        }
		
		if (!ArenaManager.getManager().getArena(arenanum).removeLootContainerInventorie(invnum)) {
			ChatUtil.Message(p, "Location not found in our library");
			return;
		}
		ChatUtil.Message(p, "Created new lootInventory to arena: ID "+ arenanum);
	}

}
