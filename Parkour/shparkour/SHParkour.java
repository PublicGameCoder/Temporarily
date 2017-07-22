package shparkour;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

public class SHParkour extends JavaPlugin{
	
	@Override
    public void onEnable() {
        // TODO Insert logic to be performed when the plugin is enabled
		createConfig();
		getCommand("shparkour").setExecutor(new CommandHandler(this));
		getCommand("shpk").setExecutor(new CommandHandler(this));
		getServer().getPluginManager().registerEvents(ParkourManager.getManager(this), this);
		ParkourManager.getManager(this).loadAllArenasFromArenaFile();
    }
    
    private void createConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info("config.yml not found, creating!");
                getConfig().addDefault("ArenaNamesList", new ArrayList<String>());
                getConfig().options().copyDefaults(true);
                saveConfig();
            } else {
                getLogger().info("config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
