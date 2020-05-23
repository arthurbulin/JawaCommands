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
import net.jawasystems.jawacore.handlers.JSONHandler;
import net.jawasystems.jawacore.handlers.LocationDataHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONObject;

/**
 *
 * @author alexander
 */
public class SetSpawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        String usage = "/setspawn <group>";
        if (!(commandSender instanceof Player)){
            System.out.println("Only a player can set a spawn location");
            return true;
        }
        
        World world = ((Player) commandSender).getWorld();
        Location newSpawn = ((Player) commandSender).getLocation();
        
        if (args == null || args.length == 0) { //if no flags
            world.setSpawnLocation(newSpawn);
            commandSender.sendMessage(ChatColor.GREEN + " > New world spawn set for " + ChatColor.BLUE + world.getName() + ChatColor.GREEN + " at X:" + newSpawn.getX() + " Y:" + newSpawn.getY() + " Z:" + newSpawn.getZ());
          
        } else if (args.length == 1) {
            
            if (JawaCommands.worldSpawns.has(world.getName())) { //if it already exists in the map
                JawaCommands.worldSpawns.getJSONObject(world.getName()).put(args[0], LocationDataHandler.packLocation(newSpawn));
            } else { //if it doesn't exist in the map
                JawaCommands.worldSpawns.put(world.getName(), (new JSONObject()).put(args[0], LocationDataHandler.packLocation(newSpawn)));
            }
            
            commandSender.sendMessage(ChatColor.GREEN + " > New group spawn " + ChatColor.BLUE + args[0] + ChatColor.GREEN + " set for " + ChatColor.BLUE + world.getName() + ChatColor.GREEN + " at X:" + newSpawn.getX() + " Y:" + newSpawn.getY() + " Z:" + newSpawn.getZ());
            JSONHandler.WriteJSONToFile(JawaCommands.getPlugin(), "/worldspawns.json", JawaCommands.worldSpawns);
        } else {
            commandSender.sendMessage(ChatColor.RED + " > Error! You have too many flags! Usage: " + ChatColor.WHITE + usage);
        }
            return true;
    
    }
    
}
