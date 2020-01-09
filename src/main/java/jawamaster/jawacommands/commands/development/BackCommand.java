/*
 * Copyright (C) 2019 alexander
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
package jawamaster.jawacommands.commands.development;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 *
 * @author alexander
 */
public class BackCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        //Get user's backStack
        //send user to back location
        //remove location from backStack
        Player target;
        if (commandSender instanceof Player){
            target = (Player) commandSender;
        } else {
            System.out.println("Error: Only a player may user the /back command.");
            return true;
        }
        Location topBack = BackHandler.getUserBackLocation(target);
        
        if (topBack == null){
            target.sendMessage(ChatColor.RED + "> Error: You do not have any back locations!!");
        } else {
            target.sendMessage(ChatColor.GREEN + " > Sending you back.");
            //Block block = topBack.getWorld().getHighestBlockAt(topBack);
            
            topBack.setY(topBack.getWorld().getHighestBlockYAt(topBack));
            target.teleport(topBack,PlayerTeleportEvent.TeleportCause.UNKNOWN);
            BackHandler.removeUserBackLocation(target);
        }
        
        return true;
    }
    
}
