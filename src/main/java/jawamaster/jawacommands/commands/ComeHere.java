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
package jawamaster.jawacommands.commands;

import jawamaster.jawacommands.JawaCommands;
import jawamaster.jawacommands.handlers.TPHandler;
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
public class ComeHere implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command arg1, String arg2, String[] arg3) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (arg3.length == 0) {
                player.sendMessage("You must specify a player to summon");
                return true;
            }

            final Player target = Bukkit.getServer().getPlayer(arg3[0]);
            TPHandler.tpRequest(1, player, target);

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(JawaCommands.getPlugin(), () -> {
                TPHandler.killRequest(target);
            }, 600);
        } else {
            commandSender.sendMessage(" > You must be a player to use this command!");
        }
        return true;

    }
}
