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
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author alexander
 */
public class MakeWarp implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        String usage = "/makewarp <name> <[public|private|permission|game]>";
        
        if ((args == null) || (args.length < 1)){
            commandSender.sendMessage(ChatColor.RED + "> Error: Not enough arguments. Usage:" + ChatColor.GREEN + usage);
            return true;
        } else if (args.length == 1) { //By default create a public warp
            WarpHandler.createWarp((Player) commandSender, args[0], "public");
        } 
        else if (args.length == 2){ 
            WarpHandler.createWarp((Player) commandSender, args[0], args[1]);
            return true;
        } else {
            commandSender.sendMessage(ChatColor.RED + "> Error: Too many arguments. Usage:" + ChatColor.GREEN + usage);
        }
        
        
        return true;
    }
    
}
