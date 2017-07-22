package shkitpvp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import commands.AddInventory;
import commands.AddLocation;
import commands.CreateCMD;
import commands.JoinCMD;
import commands.LeaveCMD;
import commands.RemoveCMD;
import commands.RemoveInventory;
import commands.RemoveLocation;
import commands.ShowInventories;
import commands.ShowLocations;
import listeners.GameListener;
import managers.ArenaManager;
import utils.ChatUtil;

public class SHKitPVP extends JavaPlugin{

	@Override
    public void onEnable(){
        if(!getDataFolder().exists())
            getDataFolder().mkdir();

        if(getConfig() == null)
            saveDefaultConfig();

        new ArenaManager(this);
        ArenaManager.getManager().loadGames();
        
        getServer().getPluginManager().registerEvents(new GameListener(this), this);
    }

    @Override
    public void onDisable(){
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(!(sender instanceof Player)){
            ChatUtil.Message(sender, "Whoa there buddy, only players may execute this!");
            return true;
        }

        Player p = (Player) sender;

        if(cmd.getName().equalsIgnoreCase("create")){
        	
            CreateCMD.HandleCommand(p, cmd, label, args);

            return true;
        }
        if(cmd.getName().equalsIgnoreCase("join")){
        	
            JoinCMD.HandleCommand(p, cmd, label, args);
            
            return true;
        }
        if(cmd.getName().equalsIgnoreCase("leave")){

            LeaveCMD.HandleCommand(p, cmd, label, args);
            
            return true;
        }
        if(cmd.getName().equalsIgnoreCase("remove")){
            
            RemoveCMD.HandleCommand(p, cmd, label, args);
            
            return true;
        }
        if(cmd.getName().equalsIgnoreCase("addinventory")){
        	
        	AddInventory.HandleCommand(p, cmd, label, args);
        	
            return true;
        }
        if(cmd.getName().equalsIgnoreCase("removeinventory")){
        	
        	RemoveInventory.HandleCommand(p, cmd, label, args);
        	
            return true;
        }
        if(cmd.getName().equalsIgnoreCase("addlocation")){
        	
        	AddLocation.HandleCommand(p, cmd, label, args);
        	
            return true;
        }
        if(cmd.getName().equalsIgnoreCase("removelocation")){
        	
        	RemoveLocation.HandleCommand(p, cmd, label, args);
        	
            return true;
        }
        if(cmd.getName().equalsIgnoreCase("showinventories")){
        	
        	ShowInventories.HandleCommand(p, cmd, label, args);
	
        	return true;
        }
        if(cmd.getName().equalsIgnoreCase("showlocations")){
	
        	ShowLocations.HandleCommand(p, cmd, label, args);
	
        	return true;
        }

        return false;
    }
}
