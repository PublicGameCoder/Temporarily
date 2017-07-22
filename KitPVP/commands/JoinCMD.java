package commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import managers.ArenaManager;
import utils.ChatUtil;

public class JoinCMD {

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
        ArenaManager.getManager().addPlayer(p, num);
        p.setGameMode(GameMode.SURVIVAL);
        ChatUtil.Message(p, "You have successful joined arena: ID "+ num);

	}

}
