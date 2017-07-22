package program;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class CountdownTimer extends BukkitRunnable {
	
    private int counter;
    public boolean isCanceled;
    
    final List<String> playernames;

    public CountdownTimer(int counter) {
        playernames = Team.getAllPlayersInTeams();
        if (counter < 1) {
            throw new IllegalArgumentException("counter must be greater than 1");
        } else {
            this.counter = counter;
            isCanceled = false;
        }
    }

    @Override
    public void run() {
    	while (!isCanceled) {
	        if (counter > 0) { 
	        	for (String name : playernames) {
					if (Bukkit.getPlayer(name) == null)continue;
					Bukkit.getPlayer(name).sendTitle(ChatColor.RED+""+counter--, "", 10, 5, 10);
				}
	        } else {
	        	isCanceled = true;
	            this.cancel();
	        }
	    }
    	this.cancel();
    }

}
