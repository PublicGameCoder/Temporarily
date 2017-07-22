package main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {
	
	private static boolean Started = false;
	Player player;
	Command cmd;
	String label;
	String[] args;
	private List<Player> PlayersPlaying = new ArrayList<Player>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) { sender.sendMessage(ChatColor.GOLD + "This command can only be used by a online player!"); return false; }
		Player player = (Player) sender;
		
		this.player = player;
		this.cmd = cmd;
		this.label = label;
		this.args = args;
		
		if (args.length <= 0) {
			BroadcastCommands(player);
			return true;
		}
		//CommandsListeners
		if (args.length >= 1) {
			
			if (args[0].equalsIgnoreCase("start") && Started == false) {
				Started = true;
				for (Player p : Bukkit.getOnlinePlayers()) {
					PlayersPlaying.add(p);
				}
				
				player.sendMessage(ChatColor.BLUE+""+ChatColor.BOLD+"Starting teleportation!");
				Bukkit.getWorld("world").spawnParticle(Particle.PORTAL, player.getLocation(), 50);
				
				for (int i = 0; i < 10; i++) {
					try {
						Thread.sleep(500L);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Bukkit.getWorld("world").spawnParticle(Particle.PORTAL, player.getLocation(), 50);
				}
				try {
					Thread.sleep(2000L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				for (Player p : PlayersPlaying) {
					p.teleport(Bukkit.getWorld("game").getSpawnLocation());
				}
				
			}
			else {
				player.sendMessage(ChatColor.RED+ "Game has already been started!");
			}
			if (args[0].equalsIgnoreCase("goto") && args.length >= 2) {
					for (World w : Bukkit.getServer().getWorlds()) {
						if (w.getName().equalsIgnoreCase(Bukkit.getServer().getWorld(args[1]).getName())) {
							Location gotoLocation = w.getSpawnLocation();
							player.teleport(gotoLocation);
							player.sendMessage(ChatColor.GREEN+"Teleported to: "+ChatColor.GOLD + gotoLocation.getWorld().getName() );
							return false;
						}
					}
					player.sendMessage(ChatColor.RED+ "World '"+ ChatColor.AQUA + args[1] + ChatColor.RED+ "' does not exist!");
				
			}
			
			if (args[0].equalsIgnoreCase("worldinfo")) {
				player.sendMessage(ChatColor.GOLD+"You're currently in world: "+ ChatColor.AQUA + player.getWorld().getName() );
				String str = "\n";
				for (World w : Bukkit.getWorlds()) {
					str += w.getName()+"\n";
				}
				player.sendMessage(ChatColor.YELLOW+"Possible worlds are: "+ ChatColor.GREEN + str );
			}
			
		}
		
		return false;
	}

	private void BroadcastCommands(Player player) {
		String str = "\n"+ ChatColor.GREEN+"Useful commands:\n"+ChatColor.AQUA+
				"  - /"+label+" "+	"start"	+" "+ChatColor.GRAY+	"to start the map!"		+ChatColor.AQUA+"\n"+
				"  - /"+label+" "+	"info"	+" "+ChatColor.GRAY+	"to get the level info!"		+ChatColor.AQUA+"\n"+
				"  - /"+label+" "+	"tip"	+" "+ChatColor.GRAY+	"to get a tip of the level!"		+ChatColor.AQUA+"\n"+
				"  - /"+label+" "+	"restart"	+" "+ChatColor.GRAY+	"restart the level!"		+ChatColor.AQUA+"\n"+
				"  - /"+label+" "+	"goto <WorldName>"	+" "+ChatColor.GRAY+	"go to the world!"		+ChatColor.AQUA+"\n"+
				"  - /"+label+" "+	"worldinfo"	+" "+ChatColor.GRAY+	"get the world name!"		+ChatColor.AQUA+"\n";
		player.sendMessage( str );
	}

}
