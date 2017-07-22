package nl.spacehorizons.minecitypolice;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	@Override
	public void onLoad() {
		// TODO Instert logic to be performed before plugin runs
	}

	@Override
    public void onEnable() {
        // TODO Insert logic to be performed when the plugin is enabled
		getCommand("mcp").setExecutor(new CommandHandler());
		Bukkit.getServer().getPluginManager().registerEvents(new Listeners(), this);
		getLogger().info("===================================");
		getLogger().info("minecitypolice Has been Enabled!");
		getLogger().info("======This Plugin Does Nothing======");
		getLogger().info("===================================");
    }
    
    @Override
    public void onDisable() {
        // TODO Insert logic to be performed when the plugin is disabled
    	getLogger().info("minecitypolice Has been Disabled!");
    }
}
