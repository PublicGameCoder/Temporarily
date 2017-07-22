package shparkour;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {
	
	private SHParkour shpk;

	public CommandHandler(SHParkour shParkour) {
		this.shpk = shParkour;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))return false;
		Player player = (Player) sender;
		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("create")){
				if (args.length <= 1) {
					player.sendMessage(ChatColor.RED+"You need to insert a name.");
					player.sendMessage(ChatColor.GREEN+"Usage:"+ChatColor.GOLD+" /"+label+" create <name>");
					return true;
				}else {
					ParkourManager.getManager(shpk).startEditMode(player, args[1]);
				}
				return true;
			}else if (args[0].equalsIgnoreCase("finish")){
				ParkourManager.getManager(shpk).finishArena(player);
				return true;
			}else if (args[0].equalsIgnoreCase("remove")){
				if (args.length <= 1) {
					player.sendMessage(ChatColor.RED+"You need to insert a name.");
					player.sendMessage(ChatColor.GREEN+"Usage:"+ChatColor.GOLD+" /"+label+" remove <name>");
					return true;
				}else {
					boolean state = ParkourManager.getManager(shpk).removeArenaByName(args[1]);
					if (state) {
						player.sendMessage(ChatColor.GREEN+"You have successfully removed "+args[1]+"!");
						return true;
					}else {
						player.sendMessage(ChatColor.RED+"You have failed removed "+args[1]+"!");
						player.sendMessage(ChatColor.RED+"Maybe "+args[1]+" is not a arena?");
						return false;
					}
				}
			}else if (args[0].equalsIgnoreCase("list")){
				return PrintArenaList(player);
			}else if (args[0].equalsIgnoreCase("quit")){
				return quitPlayer(player);
			}else if (args[0].equalsIgnoreCase("checkpoint")){
				ParkourManager.getManager(shpk).teleportPlayerToLastCheckPoint(player);
				return true;
			}
		}else {
			player.sendMessage("");
			player.sendMessage(ChatColor.RED + "======================[ "+ ChatColor.AQUA +shpk.getName()+ ChatColor.RED +" ]=====================");
			player.sendMessage("");
			player.sendMessage(ChatColor.BLUE + "List of Commands:");
			player.sendMessage(ChatColor.GOLD + "   /"+label+" " + ChatColor.GREEN + shpk.getName()+"'s main command!");
			player.sendMessage(ChatColor.GOLD + "   /"+label+" create <name> " + ChatColor.GREEN + "Creates a new arena!");
			player.sendMessage(ChatColor.GOLD + "   /"+label+" finish " + ChatColor.GREEN + "Finish editing the arena and setting lobby to player position!");
			player.sendMessage(ChatColor.GOLD + "   /"+label+" remove <name> " + ChatColor.GREEN + "Removes the selected arena!");
			player.sendMessage(ChatColor.GOLD + "   /"+label+" list " + ChatColor.GREEN + "Gets a list of all the arenas!");
			player.sendMessage(ChatColor.GOLD + "   /"+label+" quit " + ChatColor.GREEN + "To quit the arena!");
			player.sendMessage(ChatColor.GOLD + "   /"+label+" checkpoint " + ChatColor.GREEN + "Teleports you back to your last checkpoint!");
			player.sendMessage(ChatColor.RED + "=====================================================");
			player.sendMessage("");
			return true;
		}
		
		return false;
	}

	private boolean quitPlayer(Player player) {
		ParkourManager.getManager(shpk).removePlayerFromHisArena(player);
		return false;
	}

	private boolean PrintArenaList(Player player) {
		List<PKArena> arenas = ParkourManager.getManager(shpk).getArenaList();
		if (arenas.isEmpty()) {
			player.sendMessage("No arenas found!");
			return false;
		}
		player.sendMessage("");
		player.sendMessage(ChatColor.RED + "======================[ "+ ChatColor.AQUA +shpk.getName()+ ChatColor.RED +" ]=====================");
		player.sendMessage("");
		player.sendMessage(ChatColor.BLUE + "List of arenas:");
		for (PKArena a : arenas) {
			player.sendMessage(ChatColor.GOLD + "- "+a.getname());
		}
		player.sendMessage(ChatColor.RED + "=====================================================");
		player.sendMessage("");
		return true;
	}

}
