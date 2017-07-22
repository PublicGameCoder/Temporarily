package program;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class EventsHandler implements Listener {

	@SuppressWarnings("unused")
	private Startup pl; // plugin variable
	private boolean joinable;
	
	public EventsHandler(Startup startup) {
		this.pl = startup;
		joinable = true;
	}
	
	public void setJoinable(boolean b) {
		this.joinable = b;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		
		if (!joinable || pl.getServer().getOnlinePlayers().size() > pl.MaxPlayers) {
			e.getPlayer().kickPlayer("Game is not joinable right now!");
			return;
		}
		Points.loadPlayer(e.getPlayer());
		e.getPlayer().setGameMode(GameMode.SURVIVAL);
		e.setJoinMessage(ChatColor.GREEN+"+ " + ChatColor.GRAY + e.getPlayer().getName());
		
		if (pl.getServer().getOnlinePlayers().size() >= pl.MinPlayers) {
			pl.getServer().broadcastMessage(ChatUtil.markdown + "Minimum players reached!");
		}else {
			int currentAmount = pl.getServer().getOnlinePlayers().size();
			int remainingAmount = pl.MinPlayers - currentAmount;
			pl.getServer().broadcastMessage(ChatUtil.markdown + remainingAmount + "players till countdown!");
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if (!Team.isInTeam(p)) {
			p.kickPlayer("You've been removed from the game!");
			return;
		}
		p.sendTitle(ChatColor.RED+"You Died", null, 2, 5, 2);
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		if (!Team.isInTeam(p)) {
			p.kickPlayer("You've been removed from the game!");
			return;
		}
		if (Team.isPermanentDead(p)) {
			p.setGameMode(GameMode.SPECTATOR);
		}else if (Startup.getRespawnTimer()){
			new CountdownTimer(Startup.getRespawnTime()).run();
		}
		p.teleport(Team.getTeamSpawn(Team.getTeamType(p)));
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		if (Team.isInTeam(e.getPlayer())) {
			Team.removePlayer(e.getPlayer());
			Points.savePlayer(e.getPlayer());
		}
		e.getPlayer().setGameMode(GameMode.SURVIVAL);
		e.setQuitMessage(ChatColor.RED+"- "+ChatColor.GRAY+e.getPlayer().getName());
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent e) {
		if (Team.isInTeam(e.getPlayer())) {
			Team.removePlayer(e.getPlayer());
			Points.savePlayer(e.getPlayer());
		}
		e.getPlayer().setGameMode(GameMode.SURVIVAL);
		e.setLeaveMessage(null);
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if (!Team.isInTeam(e.getPlayer()))return;
		String message = "[Team] "+ e.getPlayer().getName()+" : "+ e.getMessage();
		e.setCancelled(true);
		sendMessageThroughTeam(Team.getTeamType(e.getPlayer()), message);
	}
	
	public void sendMessageThroughTeam(TeamType type, String message) {
		switch (type) {
		case RED:
			for (String playername : Team.getRedTeam()) {
				if (Bukkit.getPlayer(playername) == null)continue;
				Bukkit.getPlayer(playername).sendMessage(message);
			}
			break;
		case BLUE:
			for (String playername : Team.getBlueTeam()) {
				if (Bukkit.getPlayer(playername) == null)continue;
				Bukkit.getPlayer(playername).sendMessage(message);
			}
			break;
		}
		
	}
}
