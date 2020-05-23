/*
 * Copyright (C) 2020 alexander
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

import jawamaster.jawacommands.handlers.TPHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author alexander
 */
public class RandomTP implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command cmnd, String string, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            Player target;

            if (args.length > 0) {
                target = Bukkit.getPlayer(args[0]);
            } else {
                target = player;
            }

            if (player.hasPermission("jawamaster.foxcommands.randomtp") || !(player instanceof Player)) {
                //player.sendMessage(ChatColor.DARK_GREEN + "Teleporting: " + target.getDisplayName());
                executeTP(target);
            } else {
                player.sendMessage(ChatColor.RED + "You do not have permission to execute that command.");
            }
        } else {
            if (args.length > 0) {
                Player target = Bukkit.getPlayer(args[0]);
                executeTP(target);
                System.out.println("Teleporting: " + target.getDisplayName() + " via console or command block.");
            }
        }
        return true;
    }

    public static void executeTP(Player target) {
        int worldborder = 29900;
        Location loc = TPHandler.randomLocation(target, worldborder);
        
        boolean safe = true; //TPHandler.safeToTP(loc);
        int iter = 0;

        while (!safe && (iter < 8)) {
            loc = TPHandler.randomLocation(target, worldborder);
            safe = TPHandler.safeToTP(loc);
            iter++;
            //System.out.println(iter);
        }

        target.sendMessage("Randomly Teleporting you");
        //target.setVelocity(new Vector(0, 0, 0));
        //target.teleport(loc, PlayerTeleportEvent.TeleportCause.COMMAND);
        TPHandler.performSafeTeleport(target, loc);
    }

}
