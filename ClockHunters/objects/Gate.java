package objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

public class Gate {

	private String name;
	public Gate(Location loc, String gateName) {
		this.name = gateName;
		int ypos = loc.getBlockY() + 3;
		int xpos = loc.getBlockX() - 4;
		for (int yval = 0; yval < 4; yval++) {
			for (int xval = 0; xval < 9; xval++) {
				
				if (xval == 4) {
					Bukkit.getServer().getWorld(loc.getWorld().getName()).getBlockAt(xpos,ypos,loc.getBlockZ()).setType(Material.LAPIS_BLOCK);
				}else {
					Bukkit.getServer().getWorld(loc.getWorld().getName()).getBlockAt(xpos,ypos,loc.getBlockZ()).setType(Material.REDSTONE_BLOCK);
				}
				
				xpos++;
			}
			xpos = loc.getBlockX() - 4;
			ypos--;
		}
	}
	public String getName() {
		return this.name;
	}
}
