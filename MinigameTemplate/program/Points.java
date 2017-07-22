package program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Points {
	
	private static final String INSERT = "INSERT INTO Points VALUES(?,?,?) ON DUPLICATE KEY UPDATE name=?";
    private static final String SELECT = "SELECT points FROM Points WHERE uuid=?";
    private static final String SAVE = "UPDATE Points SET points=? WHERE uuid=?";

    private static Map<UUID, Integer> points = new HashMap<UUID, Integer>();

    public static void removePlayer(Player p) {
    	if (!points.containsKey(p.getUniqueId()))return;
    	points.remove(p.getUniqueId(), getPoints(p));
    }

    public static int getPoints(Player p) {
    	if (!points.containsKey(p.getUniqueId()))return 0;
        return points.get(p.getUniqueId());
    }

    public static void addPoints(Player p, int amount) {
    	if (points.containsKey(p.getUniqueId())) {
    		amount += getPoints(p);
    		points.remove(p.getUniqueId(),points.get(p.getUniqueId()));
    	}
    	points.put(p.getUniqueId(), amount);
    }

    public static void removePoints(Player p, int amount) {
    	if (points.containsKey(p.getUniqueId())) {
    		amount += getPoints(p);
    		points.remove(p.getUniqueId(),points.get(p.getUniqueId()));
    		points.put(p.getUniqueId(), amount);
    	}else return;
    }
    
    public static void loadPlayer(final Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(Startup.getInstance(), new Runnable() {
            @Override
            public void run() {
            	try {
					if (!Startup.getInstance().MySQL.checkConnection()) {
						try {
							System.out.println("Connecting to database....");
							Startup.getInstance().MySQL.openConnection();
							System.out.println("Connected");
						} catch (ClassNotFoundException | SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("Failed to connecting to database!");
						}
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                try (Connection connection = Startup.getInstance().MySQL.getConnection();
                        PreparedStatement insert = connection.prepareStatement(INSERT);
                        PreparedStatement select = connection.prepareStatement(SELECT)) {
                    insert.setString(1, p.getUniqueId().toString());
                    insert.setString(2, p.getName());
                    insert.setInt(3, 0);
                    insert.setString(4, p.getName());
                    insert.execute();

                    select.setString(1, p.getUniqueId().toString());
                    ResultSet result = select.executeQuery();
                    if (result.next())
                    	addPoints(p, result.getInt("points"));
                    result.close();
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
                        PreparedStatement statement = connection.prepareStatement(SAVE)){
                    statement.setInt(1, getPoints(p));
                    statement.setString(2, p.getUniqueId().toString());
                    statement.execute();
                    removePlayer(p);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
	
	
}
