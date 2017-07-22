package manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import objects.Gate;

public class GatesManager {

	public static GatesManager GM;
	private List<Gate> gates = new ArrayList<Gate>();
	
	public static GatesManager getManager() {
		
		if (GM == null) {
			GM = new GatesManager();
		}
		
		return GM;
	}
	
	public void createGate(String gateName, Location loc) {
		Gate gate = new Gate(loc, gateName);
		gates.add(gate);
	}

	public void createGate(String gateName, String world, int x, int y, int z) {
		try {
		Location loc = new Location(Bukkit.getWorld(world), x, y, z);
		Gate gate = new Gate(loc, gateName);
		gates.add(gate);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public Gate getGateByName(String gateName){
        for (Gate gate : this.gates) {
            if (gate.getName().equalsIgnoreCase(gateName)) {
                return gate;
            }
        }

        return null; // Not found
    }
}
