/*
 * Copyright (C) 2021 Jawamaster (Arthur Bulin)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jawamaster.jawacommands.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

/**
 *
 * @author Jawamaster (Arthur Bulin)
 */
public class RepairCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
        ItemMeta meta = ((Player) sender).getInventory().getItemInMainHand().getItemMeta();
        if (meta instanceof Damageable && item.getType().isItem() && !item.getType().isBlock()){
            Damageable gmmeta = (Damageable)((Player) sender).getInventory().getItemInMainHand().getItemMeta();
            Repairable repairTest = (Repairable)((Player) sender).getInventory().getItemInMainHand().getItemMeta();
            System.out.println(repairTest.getRepairCost());
//            ItemMeta gmmeta = ((Player) sender).getInventory().getItemInMainHand().getItemMeta();
            System.out.println(gmmeta);
            System.out.println(Bukkit.getServer().getRecipesFor(((Player) sender).getInventory().getItemInMainHand()));
            gmmeta.setDamage(0);
//            item.getType().getMaxDurability();
            //meta.
            System.out.println(gmmeta);
//            item.setItemMeta((ItemMeta) gmmeta);
            
//            System.out.println(item.getData());
            sender.sendMessage("Repairing");
        } else
            sender.sendMessage("That item is not repairable");
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return true;
    }
    
}
