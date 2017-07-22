package manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import main.NPC;
import net.minecraft.server.v1_11_R1.EntityPlayer;
import net.minecraft.server.v1_11_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_11_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_11_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_11_R1.PlayerConnection;

public class NPCHandler {

	public static NPCHandler npch;
	private List<NPC> NPCList = new ArrayList<NPC>();
	
	public static NPCHandler getHandler() {
		if (npch == null) {
			npch = new NPCHandler();
		}
		return npch;
	}
	
	public void CreateNPC(String playername, Location location) {
		NPC npc = new NPC(playername, location);
		NPCList.add(npc);
		npc.getNPC().sleeping = true;
	}

	public List<NPC> getNPCList() {
		// TODO Auto-generated method stub
		return NPCList;
	}
	
	public NPC getNPCByName(String NPCName){
        for (NPC npc : this.NPCList) {
            if (npc.getName().equalsIgnoreCase(NPCName)) {
                return npc;
            }
        }

        return null; // Not found
    }
	
	public void RenderAllNPCSForAllPlayers() {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			for (NPC npcTypeNPC : NPCHandler.getHandler().getNPCList()) {
				EntityPlayer npcTypeEntityPlayer = npcTypeNPC.getNPC();
	            PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
	            connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, npcTypeEntityPlayer));
	            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npcTypeEntityPlayer));
			}
		}
	}
}
