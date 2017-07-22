package program;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Team {

	private static List<String> redTeam = new ArrayList<String>();
	private static List<String> blueTeam = new ArrayList<String>();
	private static List<String> spectator = new ArrayList<String>();
	private static HashMap<String, Integer> kills = new HashMap<String, Integer>();
	
	private static Location redSpawn;
	private static Location blueSpawn;
	
	public static void addToTeam(TeamType type, Player player) {
		if (isInTeam(player)) {
			ChatUtil.sendMessage(player,"You are already in a team!");
			return;
		}
		switch (type) {
		case RED:
			redTeam.add(player.getName());
			break;
			
		case BLUE:
			blueTeam.add(player.getName());
			break;
		}
		ChatUtil.sendMessage(player,"Added to "+ type.name() +" team!");
	}
	
	public static void addToSpectator(Player p) {
		if (!spectator.contains(p.getName()))
			spectator.add(p.getName());
	}
	
	public static void finishMatch() {
		kills = sortHashMap(kills);
		for (String name : getAllPlayersInTeams()) {
			Player p = Bukkit.getPlayer(name);
			if (p == null) continue;
			List<String> finishMessage = Startup.getFinishMessage();
			for (String line : finishMessage) {
				line = PlaceholderHandler(line);
				p.sendMessage(line);
			}
		}
		new ShutDownTimer(10).run();
	}
	
	private static List<String> getHighScores(HashMap<String, Integer> map) {
		if (map == null || map.isEmpty())return null;
		List<String> scores = new ArrayList<String>(map.size());
		int i = 0;
		for (String value : map.keySet()) {
			scores.set(i, value);
			i++;
		}
		return scores;
	}

	private static String PlaceholderHandler(String message) {
		List<String> highScores = getHighScores(kills);
		message = ReplacePlaceHolder(message, "%First%", kills.get(highScores.get(0)).toString());
		message = ReplacePlaceHolder(message, "%Second%", kills.get(highScores.get(1)).toString());
		message = ReplacePlaceHolder(message, "%Third%", kills.get(highScores.get(2)).toString());
		return message;
		
	}
	
	private static String ReplacePlaceHolder(String str,String placeholder, String replacement) {
		String newstr = str;
		try {
			newstr = str.replaceAll(placeholder, replacement);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return newstr;
	}
	
	public static boolean isInTeam(Player player) {
		return redTeam.contains(player.getName()) || blueTeam.contains(player.getName());
	}
	
	public static void clearTeams() {
		redTeam.clear();
		blueTeam.clear();
	}
	
	public static List<String> getRedTeam() {
		return redTeam;
	}
	
	public static List<String> getBlueTeam() {
		return blueTeam;
	}
	
	public static List<String> getAllPlayersInTeams() {
		List<String> combinedTeams = new ArrayList<String>();
		combinedTeams.addAll(redTeam);
		combinedTeams.addAll(blueTeam);
		return combinedTeams;
	}
	
	public static TeamType getTeamType(Player player) {
		if (!isInTeam(player)) {
			return null;
		}
		return (redTeam.contains(player.getName()) ? TeamType.RED : TeamType.BLUE);
	}
	
	public static Location getTeamSpawn(TeamType team) {
		return (team.equals(TeamType.RED) ? redSpawn : blueSpawn );
	}
	
	public static void setSpawnLocation(TeamType type ,Location loc) {
		switch (type) {
		case RED:
			redSpawn = loc;
			break;
			
		case BLUE:
			blueSpawn = loc;
			break;
		}
	}

	public static void removePlayer(Player player) {
		if (!isInTeam(player))return;
		TeamType type = getTeamType(player);
		switch (type) {
		case RED:
			redTeam.remove(player);
			break;
			
		case BLUE:
			blueTeam.remove(player);
			break;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	private static HashMap<String, Integer> sortHashMap(HashMap<String,Integer> map) {
		if (map == null || map.isEmpty())return map;
		Object[] a = map.entrySet().toArray();
		Arrays.sort(a, new Comparator() {
		    public int compare(Object o1, Object o2) {
		        return ((Map.Entry<String, Integer>) o2).getValue()
		                   .compareTo(((Map.Entry<String, Integer>) o1).getValue());
		    }
		});
		return map;
	}

	public static boolean isPermanentDead(Player p) {
		if (spectator.contains(p))return true;
		return false;
	}
}
