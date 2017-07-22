package program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Stats {
	
	private static final String INSERT = "INSERT INTO "+Startup.getInstance().getName()+" VALUES(?,?,?,?,?) ON DUPLICATE KEY UPDATE name=?";
	private static final String SELECTKILLS = "SELECT kills FROM "+Startup.getInstance().getName()+" WHERE uuid=?";
    private static final String SAVEKILLS = "UPDATE "+Startup.getInstance().getName()+" SET kills=? WHERE uuid=?";
    private static final String SELECTWINS = "SELECT wins FROM "+Startup.getInstance().getName()+" WHERE uuid=?";
    private static final String SAVEWINS = "UPDATE "+Startup.getInstance().getName()+" SET wins=? WHERE uuid=?";
    private static final String SELECTLOSES = "SELECT loses FROM "+Startup.getInstance().getName()+" WHERE uuid=?";
    private static final String SAVEKLOSES = "UPDATE "+Startup.getInstance().getName()+" SET loses=? WHERE uuid=?";

    private static Map<UUID, Integer> kills = new HashMap<UUID, Integer>();
    private static Map<UUID, Integer> wins = new HashMap<UUID, Integer>();
    private static Map<UUID, Integer> loses = new HashMap<UUID, Integer>();

    public static void removePlayer(Player p) {
    	if (kills.containsKey(p.getUniqueId())){
    		kills.remove(p.getUniqueId(), getKills(p));
    	}else if (wins.containsKey(p.getUniqueId())){
    		wins.remove(p.getUniqueId(), getKills(p));
    	}else if (loses.containsKey(p.getUniqueId())){
    		loses.remove(p.getUniqueId(), getKills(p));
    	}
    }
  //Kills
    public static int getKills(Player p) {
    	if (!kills.containsKey(p.getUniqueId()))return 0;
        return kills.get(p.getUniqueId());
    }

    public static void addKills(Player p, int amount) {
    	if (kills.containsKey(p.getUniqueId())) {
    		amount += getKills(p);
    		kills.remove(p.getUniqueId(),kills.get(p.getUniqueId()));
    	}
    	kills.put(p.getUniqueId(), amount);
    }

    public static void removeKills(Player p, int amount) {
    	if (kills.containsKey(p.getUniqueId())) {
    		amount += getKills(p);
    		kills.remove(p.getUniqueId(),kills.get(p.getUniqueId()));
    		kills.put(p.getUniqueId(), amount);
    	}else return;
    }
  //Wins
    public static int getWins(Player p) {
    	if (!wins.containsKey(p.getUniqueId()))return 0;
        return wins.get(p.getUniqueId());
    }

    public static void addWins(Player p, int amount) {
    	if (wins.containsKey(p.getUniqueId())) {
    		amount += getWins(p);
    		wins.remove(p.getUniqueId(),wins.get(p.getUniqueId()));
    	}
    	kills.put(p.getUniqueId(), amount);
    }

    public static void removeWins(Player p, int amount) {
    	if (wins.containsKey(p.getUniqueId())) {
    		amount += getWins(p);
    		wins.remove(p.getUniqueId(),wins.get(p.getUniqueId()));
    		wins.put(p.getUniqueId(), amount);
    	}else return;
    }
  //Loses
    public static int getLoses(Player p) {
    	if (!loses.containsKey(p.getUniqueId()))return 0;
        return loses.get(p.getUniqueId());
    }

    public static void addLoses(Player p, int amount) {
    	if (loses.containsKey(p.getUniqueId())) {
    		amount += getLoses(p);
    		loses.remove(p.getUniqueId(),loses.get(p.getUniqueId()));
    	}
    	loses.put(p.getUniqueId(), amount);
    }

    public static void removeLoses(Player p, int amount) {
    	if (loses.containsKey(p.getUniqueId())) {
    		int result = getLoses(p) - amount;
    		if (result < 0)result = 0;
    		loses.remove(p.getUniqueId(),loses.get(p.getUniqueId()));
    		loses.put(p.getUniqueId(), result);
    	}else return;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, Integer> getTop3() {
    	Map<String, Integer> map = new HashMap<String, Integer>();
    	for (Entry<UUID, Integer> set : kills.entrySet()) {
    		map.put(Bukkit.getPlayer(set.getKey()).getName(), set.getValue());
		}
		if (map == null || map.isEmpty())return map;
		Object[] a = map.entrySet().toArray();
		Arrays.sort(a, new Comparator() {
		    public int compare(Object o1, Object o2) {
		        return ((Map.Entry<String, Integer>) o2).getValue()
		                   .compareTo(((Map.Entry<String, Integer>) o1).getValue());
		    }
		});
		Map<String, Integer> topMap = new HashMap<String, Integer>(3);
		for (Entry<String, Integer> entry :map.entrySet()) {
			topMap.put(entry.getKey(), entry.getValue());
		}
		return topMap;
	}
    
    public static void loadPlayer(final Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(Startup.getInstance(), new Runnable() {
            @Override
            public void run() {
            	try {
					if (!Startup.getInstance().MySQL.checkConnection()) {
						try {
							Startup.getInstance().MySQL.openConnection();
						} catch (ClassNotFoundException | SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                try (Connection connection = Startup.getInstance().MySQL.getConnection();
                        PreparedStatement insert = connection.prepareStatement(INSERT);
                		PreparedStatement killselect = connection.prepareStatement(SELECTKILLS);
                		PreparedStatement winsselect = connection.prepareStatement(SELECTWINS);
                		PreparedStatement losesselect = connection.prepareStatement(SELECTLOSES)) {
                    insert.setString(1, p.getUniqueId().toString());
                    insert.setString(2, p.getName());
                    insert.setInt(3, 0);
                    insert.setInt(4, 0);
                    insert.setInt(5, 0);
                    insert.setString(6, p.getName());
                    insert.execute();

                    killselect.setString(1, p.getUniqueId().toString());
                    winsselect.setString(1, p.getUniqueId().toString());
                    losesselect.setString(1, p.getUniqueId().toString());
                    ResultSet killsresult = killselect.executeQuery();
                    ResultSet winsresult = winsselect.executeQuery();
                    ResultSet losesresult = losesselect.executeQuery();
                    if (killsresult.next()) addKills(p, killsresult.getInt("kills"));
                    if (winsresult.next()) addWins(p, winsresult.getInt("wins"));
                    if (losesresult.next()) addLoses(p, losesresult.getInt("loses"));
                    killsresult.close();
                    winsresult.close();
                    losesresult.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public static void savePlayer(final Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(Startup.getInstance(), new Runnable() {
            @Override
            public void run() {
            	try {
					if (!Startup.getInstance().MySQL.checkConnection()) {
						try {
							Startup.getInstance().MySQL.openConnection();
						} catch (ClassNotFoundException | SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                try (Connection connection = Startup.getInstance().MySQL.getConnection();
                		PreparedStatement killstatement = connection.prepareStatement(SAVEKILLS);
                		PreparedStatement winsstatement = connection.prepareStatement(SAVEWINS);
                		PreparedStatement losesstatement = connection.prepareStatement(SAVEKLOSES)){
                	killstatement.setInt(1, getKills(p));
                	killstatement.setString(2, p.getUniqueId().toString());
                	killstatement.execute();
                	
                	winsstatement.setInt(1, getWins(p));
                	winsstatement.setString(2, p.getUniqueId().toString());
                	winsstatement.execute();
                	
                	losesstatement.setInt(1, getLoses(p));
                	losesstatement.setString(2, p.getUniqueId().toString());
                	losesstatement.execute();
                	
                    removePlayer(p);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
