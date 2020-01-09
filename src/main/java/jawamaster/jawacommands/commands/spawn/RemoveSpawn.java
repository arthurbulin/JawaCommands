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

import java.util.Arrays;
import jawamaster.jawacommands.JawaCommands;
import jawamaster.jawacommands.handlers.JSONHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author alexander
 */
public class RemoveSpawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command cmnd, String string, String[] args) {
        String usage = "/removespawn <world> <spawn group>";
        
        if (args == null || args.length == 0){
            commandSender.sendMessage(ChatColor.GREEN + " > Command Usage: "+ ChatColor.WHITE + usage);
            commandSender.sendMessage(ChatColor.GOLD + " > The following spawns exist. Repeat the command with /removespawn <world> to see only that world's spawns.");
            
            for (String world : JawaCommands.worldSpawns.keySet()){
                commandSender.sendMessage(ChatColor.GREEN + " > World: " + ChatColor.WHITE + world + ChatColor.GREEN + "[" + ChatColor.WHITE + String.join(",", Arrays.toString(JawaCommands.worldSpawns.getJSONObject(world).keySet().toArray())) + ChatColor.GREEN + "]");
            }
        } else if (args.length == 1){
            World world = Bukkit.getServer().getWorld(args[0]);
            if (world == null){
                commandSender.sendMessage(ChatColor.RED + " > Invalid world!");
            } else if (JawaCommands.worldSpawns.has(args[0])) {
                commandSender.sendMessage(ChatColor.GREEN + " > World: " + ChatColor.WHITE + world.getName() + ChatColor.GREEN + "[" + ChatColor.WHITE + String.join(",", Arrays.toString(JawaCommands.worldSpawns.getJSONObject(world.getName()).keySet().toArray())) + ChatColor.GREEN + "]");
            }
        } else if (args.length == 2 && JawaCommands.worldSpawns.has(args[0])){
            if (!JawaCommands.worldSpawns.getJSONObject(args[0]).has(args[1])) {
                commandSender.sendMessage(ChatColor.RED + " > " + args[1] + " is not a valid spawn within " + args[0]);
                return true;
            } else {
                JawaCommands.worldSpawns.getJSONObject(args[0]).remove(args[1]);
                if (JawaCommands.worldSpawns.getJSONObject(args[0]).isEmpty()) JawaCommands.worldSpawns.remove(args[0]); //Remove world if it has no custom spawns
                JSONHandler.WriteJSONToFile("/worldspawns.json", JawaCommands.worldSpawns);
                commandSender.sendMessage(ChatColor.GREEN + " > " + args[1] + " has been removed from " + args[0]);
            }
        }else if (args.length == 2 && !JawaCommands.worldSpawns.has(args[0])){
            commandSender.sendMessage(ChatColor.RED + " > " + args[0] + " has no custom spawns.");
        } else if (args.length > 2) {
            commandSender.sendMessage(ChatColor.RED + " > You have too many arguments! Usage: " + ChatColor.WHITE + usage);
        } 
        
        return true;
    }
    
}
