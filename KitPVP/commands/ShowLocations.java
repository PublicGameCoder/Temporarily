package commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import managers.ArenaManager;
import shkitpvp.SHKitPVP;
import utils.ChatUtil;

public class ShowLocations {

	private static SHKitPVP plugin = (SHKitPVP)Bukkit.getPluginManager().getPlugin("SHKitPVP");
	private static int times = -1;
	
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
		
		final List<Location> locs = ArenaManager.getManager().getArena(num).getLocations();
		locs.add(p.getLocation());
		
		times = -1;
		final Player player = p;
		BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
        	
			@Override
            public void run() {
				times++;
        		player.teleport(locs.get(times));
        		
        		if (times == (locs.size()-1)) {
        			ChatUtil.Message(player, "Showing Locations done!");
        			return;
        		}
            }
        }, locs.size(), 100L);
	}

}
