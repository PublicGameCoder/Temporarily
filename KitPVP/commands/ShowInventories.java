package commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import managers.ArenaManager;
import shkitpvp.SHKitPVP;
import utils.ChatUtil;

public class ShowInventories {
	
	private static SHKitPVP plugin = (SHKitPVP)Bukkit.getPluginManager().getPlugin("SHKitPVP");
	static int count = 0;
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
		final List<Inventory> invs = ArenaManager.getManager().getArena(num).getInventories();
		
		times = -1;
		final Player player = p;
		count = Bukkit.getScheduler().scheduleSyncRepeatingTask( plugin, new Runnable() {
        	
			@Override
            public void run() {
				times++;
				player.closeInventory();
				ChatUtil.Message(player, "Closed: "+times);
				
				if (times >= invs.size()) {
					ChatUtil.Message(player, "Showing Inventories done!");
        			Bukkit.getScheduler().cancelTask(count);
				}
				
				player.openInventory(invs.get(times));
				
            }
        }, 0L, 60L);
		
		
	}

}
