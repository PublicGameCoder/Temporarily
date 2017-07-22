package threads;

import java.util.concurrent.Callable;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import managers.MessageManager;

public class TeleportThread<T, V> extends Thread {
	
	private Entity[] entities;
	private Location destination;
	private int time;
	private Particle particle;
	private Callable<V> ExecuteDone;
	
	
	/**
	 * @param entity : the entity to teleport
	 * @param destination : the destination
	 * @param time : the time in seconds bevore teleport
	 * @param particle : the particle to display while waiting for teleport (null means no particle)
	 */
	public TeleportThread(Entity[] entities, Location destination, int time , Particle particle) {
		this.entities = entities;
		this.destination = destination;
		this.time = time;
		this.particle = particle;
	}
	
	public void run() {
		
		for (Entity e : entities) {
			if (e instanceof Player) {
				
				MessageManager.sendMessage(new Player[] { (Player) e }, new String[] {"Starting Teleportation!"} );
			
			}
		}
		
		while (true) {
			for (Entity e : entities) {
				Location loc = e.getLocation();
				loc.setY( loc.getY() + 2 );
				e.getWorld().spawnParticle(particle, loc, 5);
			}
			
			if (time <= 0) {
				break;
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			time--;
		}
		
		for (Entity e : entities) {
			e.teleport(destination);
		}
		
		try {
			ExecuteDone.call();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }

	/**
	* @param ExecuteDone : the function to execute when finished 
	*/
	public void CallWhenFinished(Callable<V> ExecuteDone) {
		this.ExecuteDone = ExecuteDone;
	}
}
