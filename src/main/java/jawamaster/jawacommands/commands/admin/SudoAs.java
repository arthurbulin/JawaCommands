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
package jawamaster.jawacommands.commands.admin;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import jawamaster.jawacommands.JawaCommands;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author alexander
 */
public class SudoAs implements CommandExecutor{
    private static final Logger LOGGER = Logger.getLogger("SudoAs");
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        String usage = "/sudo <player> <command>";
        Player target = JawaCommands.getPlugin().getServer().getPlayer(args[0]);
        
        if ((args == null) || (args.length < 2)) {
            commandSender.sendMessage(ChatColor.RED + " > Error! You must specify a player and a command!");
            return true;
        }
        
        if (target == null ) {
            commandSender.sendMessage(ChatColor.RED + " > " + args[0] + " is not online!");
            return true;
        } else {
            String newCommand = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            LOGGER.log(Level.INFO, "[SUDO] {0} used sudo on {1} with command: {2}", new Object[]{((Player) commandSender).getName(), target.getName(), newCommand});
            commandSender.sendMessage(ChatColor.GREEN + " > " + args[0] + " is now executing command: " + ChatColor.WHITE + newCommand);
            target.performCommand(newCommand);
            return true;
        }
        
        
        
    }
    
}
