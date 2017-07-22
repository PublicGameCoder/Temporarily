package listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import managers.ArenaManager;
import objects.Arena;
import shkitpvp.SHKitPVP;

public class GameListener implements Listener{

    static List<String> players = new ArrayList<String>();
    public static SHKitPVP plugin;

    public GameListener(SHKitPVP plugin){
        GameListener.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player && players.contains(((Player) e.getEntity()).getName())){
        	( (Player) e.getEntity()).sendMessage(ChatColor.RED + "You got damaged!");
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        if(ArenaManager.getManager().isInGame(e.getEntity())){
            ArenaManager.getManager().removePlayer(e.getEntity());
        }
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
    	Player p = e.getPlayer();
    	if (p == null) return;
    	if (ArenaManager.getManager().isInGame(p)) {
    		e.setCancelled(true);
    	}
    }
    
    @EventHandler
    public void onlootContainerClick(PlayerInteractEvent e) {
    	if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
    		return;
    	}
    	Player player = e.getPlayer();
    	if (!ArenaManager.getManager().isInGame(player)) return;
    	Arena a = ArenaManager.getManager().findPlayer(player);
    	a.OpenInventoryFromLootContainer(player, e.getClickedBlock().getLocation());
    }

    public static void add(Player p){
        final String name = p.getName();
        players.add(name);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
            @Override
            public void run(){
                players.remove(name);
            }
        }, 100L);
    }
}
