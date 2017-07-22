package nl.spacehorizons.minecitypolice;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String Label, String[] args) {
		// TODO Auto-generated method stub
		if (!(sender instanceof Player)) return false;
		Player player = (Player) sender;
		
		if (args.length <= 1 ) {
		player.openInventory(Listeners.PoliceMainInventory);
		}
		
		return false;
	}

}
