package commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import managers.ArenaManager;
import utils.ChatUtil;

public class AddInventory {

	public static void HandleCommand(Player p, Command cmd, String label, String[] args) {
		if(args.length != 1){
            ChatUtil.Message(p, "Insuffcient arguments!");
            return;
        }
		int num = 0;
		try{
            num = Integer.parseInt(args[0]);
        }catch(NumberFormatException e){
            ChatUtil.Message(p, "Invalid arena ID");
        }
		if (p.getLocation().getBlock().getType() != Material.CHEST) {
			
		}
		Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST);
		ItemStack[] items = ((Chest) p.getLocation().getBlock().getState()).getBlockInventory().getContents();
		inv.setContents(items);
		
		ArenaManager.getManager().getArena(num).addLootContainerInventorie(inv);
		ChatUtil.Message(p, "Created new lootInventory to arena: ID "+ num);
	}

}
