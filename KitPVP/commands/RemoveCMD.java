package commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import managers.ArenaManager;
import objects.Arena;
import utils.ChatUtil;

public class RemoveCMD {

	public static void HandleCommand(Player p, Command cmd, String label, String[] args) {
		if(args.length != 1){
            ChatUtil.Message(p, "Insuffcient arguments!");
            return;
        }
        int num = 0;
        try{
        	num = Integer.parseInt(args[0]);
        	if (num < 1) {
        		ChatUtil.Message(p, "Invalid arena ID");
        		return;
        	}
        	Arena a = ArenaManager.getManager().getArena(num);
        	if (a == null) {
        		ChatUtil.Message(p, "Arena doesn't exist!");
        		return;
        	}
        }catch(NumberFormatException e){
            ChatUtil.Message(p, "Invalid arena ID");
            return;
        }
        ArenaManager.getManager().removeArena(num);
        ChatUtil.Message(p, "You have removed the arena with ID "+num+"!");

	}

}
