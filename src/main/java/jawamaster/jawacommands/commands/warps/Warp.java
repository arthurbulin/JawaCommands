package jawamaster.jawacommands.commands.warps;

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


import jawamaster.jawacommands.JawaCommands;
import jawamaster.jawacommands.handlers.WarpHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author alexander
 */
public class Warp implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        //if no args print list of warps
        if ((args == null) || (args.length < 1)) {
            WarpHandler.listWarps((Player) commandSender);
        } else if (args.length == 1) {
            if (WarpHandler.warpExists(args[0])) {
                boolean worked = JawaCommands.getWarpIndex().get(args[0]).sendPlayer((Player) commandSender);
                if (worked) {
                    commandSender.sendMessage(ChatColor.GREEN + " > Warping!");
                } else {
                    commandSender.sendMessage(ChatColor.RED + " > You don't have access to that!");
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + " > That warp doesn't exist!");
            }
        } else if(args.length == 2) {
            if (commandSender.hasPermission("jawacommands.warp.other") && JawaCommands.getWarpIndex().containsKey(args[0])){
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null){
                    boolean worked = JawaCommands.getWarpIndex().get(args[0]).sendPlayer(target);
                } else {
                    commandSender.sendMessage(ChatColor.RED + " > Error: That is not a valid player!");
                }
            } else if(!commandSender.hasPermission("jawacommands.warp.other")){
                commandSender.sendMessage(ChatColor.RED + " > Error: You do not have permission to warp another player!");
            }
        }
        else {
            commandSender.sendMessage(ChatColor.RED + " > Too many arguments!");
        }
        
        return true;
    }
    
}
