/*
 * Copyright (C) 2020 Jawamaster (Arthur Bulin)
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

import jawamaster.jawacommands.Warp;
import jawamaster.jawacommands.handlers.WarpHandler;
import net.jawasystems.jawacore.PlayerManager;
import net.jawasystems.jawacore.dataobjects.PlayerDataObject;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Jawamaster (Arthur Bulin)
 */
public class WarpInfo implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args == null || args.length == 0){
            //send usage
        } else if (args.length > 0){
            if (WarpHandler.warpExists(args[0])){
                commandSender.sendMessage(ChatColor.GREEN + "> Warp info for " + args[0]);
                commandSender.sendMessage(ChatColor.GREEN + " > Name: " + ChatColor.BLUE + args[0]);
                Location warpLocation = WarpHandler.getWarp(args[0]).getLocation();
                commandSender.sendMessage(ChatColor.GREEN + " > Location: " + ChatColor.BLUE 
                        + "X:" + warpLocation.getBlockX() 
                        + " Y:"+ warpLocation.getBlockY() 
                        + " Z:"+warpLocation.getBlockZ() 
                        + ChatColor.YELLOW + " World:"+warpLocation.getWorld().getName());
                if (WarpHandler.canAdministrate((Player) commandSender, args[0])) {
                    Warp warp = WarpHandler.getWarp(args[0]);
                    PlayerDataObject warpOwner = PlayerManager.getPlayerDataObject(warp.getOwner());
                    commandSender.sendMessage(ChatColor.GREEN + " > Type: " + ChatColor.YELLOW + warp.getType());
                    commandSender.sendMessage(ChatColor.GREEN + " > Hidden: " + ChatColor.YELLOW + String.valueOf(warp.isHidden()));
                    commandSender.sendMessage(ChatColor.GREEN + " > Whitelisted: " + ChatColor.YELLOW + String.valueOf(warp.isWhiteListed()));
                    commandSender.sendMessage(ChatColor.GREEN + " > Owner: " + ChatColor.YELLOW + warpOwner.getFriendlyName());
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "> Error: " + args[0] + " is not a valid warp.");
            }
        }
        return true;   
    }
    
}
