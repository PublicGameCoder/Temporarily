package main;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.CraftServer;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.v1_11_R1.EntityPlayer;
import net.minecraft.server.v1_11_R1.MinecraftServer;
import net.minecraft.server.v1_11_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_11_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_11_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import objects.UUIDFetcher;
import net.minecraft.server.v1_11_R1.PlayerConnection;
import net.minecraft.server.v1_11_R1.PlayerInteractManager;
import net.minecraft.server.v1_11_R1.WorldServer;
 
public class NPC implements Listener {
	
        private EntityPlayer npc;
       
        public NPC(String playername, Location location) {
        	
        	@SuppressWarnings("unused")
			UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(playername));
        	UUID uuid = null;
        	try {
        		uuid = UUIDFetcher.getUUIDOf(playername);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
        	MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        	WorldServer nmsWorld = ((CraftWorld) location.getWorld() ).getHandle();
        	
        	npc = new EntityPlayer(nmsServer, nmsWorld, new GameProfile(uuid, playername), new PlayerInteractManager(nmsWorld));
        	npc.setLocation(location.getX(), location.getY(), location.getZ(), 0, 0);
        }
        
        public void unrender() {
        	for(Player p: Bukkit.getServer().getOnlinePlayers()) {
	        	PlayerConnection connection = ((CraftPlayer)p).getHandle().playerConnection;
	        	connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, npc));
	            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
        	}
        }

		public EntityPlayer getNPC() {
			return npc;
		}

		public String getName() {
			// TODO Auto-generated method stub
			return this.npc.displayName;
		}
}
