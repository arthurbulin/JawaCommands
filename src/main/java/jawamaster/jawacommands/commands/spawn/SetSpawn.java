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

import java.util.logging.Logger;
import jawamaster.jawacommands.handlers.WorldHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author alexander
 */
public class SetSpawn implements CommandExecutor {
    private static final Logger LOGGER = Logger.getLogger("SetSpawn");

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        String[] usage = new String[]{
            ChatColor.YELLOW + "> /setspawn world <group>",
            ChatColor.YELLOW + "> /setspawn global <group>"
        };
        
        if (!(commandSender instanceof Player)){
            LOGGER.warning("Only a player can set a spawn location");
            return true;
        }
        
        Location newSpawn = ((Player) commandSender).getLocation();
        World world = newSpawn.getWorld();
        
        if (args == null || args.length == 0) { //if no flags
            world.setSpawnLocation(newSpawn);
            commandSender.sendMessage(ChatColor.GREEN + "> New world spawn set for " + ChatColor.BLUE + world.getName() + ChatColor.GREEN 
                    + " at X:" + newSpawn.getX() 
                    + " Y:" + newSpawn.getY() 
                    + " Z:" + newSpawn.getZ());
        } else if (args.length < 2){
            commandSender.sendMessage(ChatColor.RED + "> Error: You don't have enough arguments!");
            commandSender.sendMessage(usage);
            return true;
        } else if (args[0].matches("(?i)wo?r?l?d?$")) {
            WorldHandler.addWorldSpawn(args[1], newSpawn);
            commandSender.sendMessage(ChatColor.GREEN + "> New group spawn " + ChatColor.BLUE + args[1] + ChatColor.GREEN 
                    + " set for " + ChatColor.BLUE + world.getName() + ChatColor.GREEN 
                    + " at X:" + newSpawn.getX() 
                    + " Y:" + newSpawn.getY() 
                    + " Z:" + newSpawn.getZ());
        } else if (args[0].matches("(?i)glo?b?a?l?$")) {
            WorldHandler.addGlobalSpawn(args[1], newSpawn);
            commandSender.sendMessage(ChatColor.GREEN + "> New group global spawn " + ChatColor.BLUE + args[1] + ChatColor.GREEN 
                    + " set for " + ChatColor.BLUE + world.getName() + ChatColor.GREEN 
                    + " at X:" + newSpawn.getX() 
                    + " Y:" + newSpawn.getY() 
                    + " Z:" + newSpawn.getZ());
            commandSender.sendMessage(ChatColor.GOLD + "> Remember a global spawn overrides world spawns for that rank!" );
        } else {
            commandSender.sendMessage(ChatColor.RED + "> Error! Usage:");
            commandSender.sendMessage(usage);
        }
            return true;
    
    }
    
}
