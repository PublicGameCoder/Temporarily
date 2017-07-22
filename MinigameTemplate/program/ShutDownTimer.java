package program;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class ShutDownTimer extends BukkitRunnable {

	private int counter;
	
	public ShutDownTimer(int counter) {
        if (counter < 1) {
            throw new IllegalArgumentException("counter must be greater than 1");
        } else {
            this.counter = counter;
        }
	}

	@Override
	public void run() {
		if (counter <= 0) {
			Bukkit.shutdown();
			this.cancel();
        }
	}

}
