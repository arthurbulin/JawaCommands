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
package jawamaster.jawacommands.commands.warps;

import jawamaster.jawacommands.handlers.WarpHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author alexander
 */
public class DelWarp implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String arg1, String[] args) {
        String usage = "/delwarp <warpname>";
        
        if ((args == null) || (args.length < 1)){
            commandSender.sendMessage(ChatColor.RED + "> Usage: " + usage);
            return true;
        }
        
        //Warp doesnt exist in loaded memory?
        if (!WarpHandler.warpExists(args[0])){
            commandSender.sendMessage(ChatColor.RED + "> "+ args[0] + " does not exist! Create it first to modify it's parameters!");
            return true;
        } else {
            boolean worked = WarpHandler.deleteWarp(args[0]);
            if (worked) {
                commandSender.sendMessage(ChatColor.GREEN + "> " + args[0] + " has been deleted!");
            } else {
                commandSender.sendMessage(ChatColor.RED + "> " + args[0] + " failed to be deleted!");
            }
            return true;
        }
        
        
        
    }
    
}
