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
package jawamaster.jawacommands.commands.spawn;

import jawamaster.jawacommands.JawaCommands;
import jawamaster.jawacommands.handlers.TPHandler;
import net.jawasystems.jawacore.handlers.LocationDataHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONObject;

/**
 *
 * @author alexander
 */
public class Spawn implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        String usage = "/spawn [world name]";
        Player player;
        //permissions jawa.command.spawn.<world name>
        if (commandSender instanceof Player){
            player = (Player) commandSender;
        } else {
            System.out.println("Only players can user this command!");
            return true;
        }
        if ((args == null) || (args.length == 0)) {
            if (JawaCommands.worldSpawns.has(player.getWorld().getName())) {
                JSONObject worldSpawns = JawaCommands.worldSpawns.getJSONObject(player.getWorld().getName());
                for (String perm : worldSpawns.keySet()) {
                    if (player.hasPermission("jawacommands.spawn." + perm)) {
                        TPHandler.performSafeTeleport(player, LocationDataHandler.unpackLocation(JawaCommands.worldSpawns.getJSONObject(player.getWorld().getName()).getJSONObject(perm)));
                        //player.teleport(LocationDataHandler.unpackLocation(JawaCommands.worldSpawns.getJSONObject(player.getWorld().getName()).getJSONObject(perm)));
                        break;
                    }
                }
            }
            player.teleport(player.getWorld().getSpawnLocation());
            player.sendMessage(ChatColor.GREEN + " > Spawning you in " + player.getWorld().getName());
        } else if ((args.length > 0) && player.hasPermission("jawacommands.spawn." + args[0])){
            TPHandler.performSafeTeleport(player, Bukkit.getServer().getWorld(args[0]).getSpawnLocation());
            //player.teleport(Bukkit.getServer().getWorld(args[0]).getSpawnLocation(),PlayerTeleportEvent.TeleportCause.COMMAND);
        } else {
            player.sendMessage(ChatColor.RED + " > You do not have permission to spawn in this world!");
        }
        return true;
    }
    
}
