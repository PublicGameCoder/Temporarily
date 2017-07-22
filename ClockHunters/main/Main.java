package main;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import manager.NPCHandler;

public class Main extends JavaPlugin {
	
	public static Plugin plugin;

	@Override
	public void onLoad() {
		// TODO Instert logic to be performed before plugin runs
	}

	@Override
    public void onEnable() {
		plugin = this;
        // TODO Insert logic to be performed when the plugin is enabled
		getCommand("map").setExecutor(new CommandHandler());
		
		LoadWorlds();
		
		Gamebuilder.GetBuilder().BuildGame();
		Bukkit.getServer().getPluginManager().registerEvents(new Eventshandler(), this);
		getLogger().info("===================================");
		getLogger().info("Clock Hunters is ready to use!!");
		getLogger().info("===================================");
		
		NPCHandler.getHandler().RenderAllNPCSForAllPlayers();
    }
    
    private void LoadWorlds() {
		// TODO Auto-generated method stub
		Bukkit.createWorld(WorldCreator.name("game"));
	}

	@Override
    public void onDisable() {
        // TODO Insert logic to be performed when the plugin is disabled
    	Gamebuilder.GetBuilder().remover();
    	try {
    		Thread.sleep(20L);
    	}catch (Exception e) {
			// TODO: handle exception
    		getLogger().info("Did not sleep!");
		}
    	getLogger().info("Clock Hunters got disabled!");
    }
}
