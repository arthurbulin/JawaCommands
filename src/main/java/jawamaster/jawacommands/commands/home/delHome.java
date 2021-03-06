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
package jawamaster.jawacommands.commands.home;

import jawamaster.jawacommands.handlers.HomeHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class delHome implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Player player = (Player) commandSender;
        String usage = ChatColor.WHITE + "/delhome <homename>";
        
        if ((args == null) || (args.length == 0)) {
            player.sendMessage(ChatColor.RED + " > You must specify a home name to remove! Usage: " + usage);
        } else if (args.length == 1){
            HomeHandler.removeHome(player, args[0]);
        } else if (args.length > 1) {
            player.sendMessage(ChatColor.RED + " > Too many arguments!! Usage: " + usage);
        }
        return true;
    }
    
}
