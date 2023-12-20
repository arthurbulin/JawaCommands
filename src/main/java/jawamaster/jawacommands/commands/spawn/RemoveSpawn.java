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

import jawamaster.jawacommands.handlers.WorldHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author alexander
 */
public class RemoveSpawn implements CommandExecutor {
    
    public static final String[] USAGE = new String[]{
        ChatColor.GREEN + "> Remove a global spawn - "+ChatColor.GOLD+"/removespawn global <group>" +
        ChatColor.GREEN + "> Remove a world spawn - "+ChatColor.GOLD+"/removespawn world <world> <group>"+
        ChatColor.GREEN + "> To list spawns - "+ChatColor.GOLD+"/removespawn list"};


    @Override
    public boolean onCommand(CommandSender commandSender, Command cmnd, String string, String[] args) {
        if (! (commandSender instanceof Player)){
            commandSender.sendMessage("This command can only be used by players");
            return true;
        }
        
        Player player = (Player) commandSender;
        
        if (args == null || args.length == 0){
            commandSender.sendMessage(ChatColor.GREEN + " > Command Usage: ");
            commandSender.sendMessage(USAGE);
//            commandSender.sendMessage(ChatColor.GOLD + " > The following spawns exist. Repeat the command with /removespawn <world> to see only that world's spawns.");
            
//            for (String world : WorldHandler.worldSpawns.keySet()){
//                commandSender.sendMessage(ChatColor.GREEN + " > World: " + ChatColor.WHITE + world + ChatColor.GREEN + "[" + ChatColor.WHITE + String.join(",", Arrays.toString(WorldHandler.worldSpawns.getJSONObject(world).keySet().toArray())) + ChatColor.GREEN + "]");
//            }
//        } else if (args.length == 1){
//            World world = Bukkit.getServer().getWorld(args[0]);
//            if (world == null){
//                commandSender.sendMessage(ChatColor.RED + " > Invalid world!");
//            } else if (WorldHandler.worldSpawns.has(args[0])) {
//                commandSender.sendMessage(ChatColor.GREEN + " > World: " + ChatColor.WHITE + world.getName() + ChatColor.GREEN + "[" + ChatColor.WHITE + String.join(",", Arrays.toString(WorldHandler.worldSpawns.getJSONObject(world.getName()).keySet().toArray())) + ChatColor.GREEN + "]");
//            }
        } else if (args.length == 1 && args[0].matches("li?s?t?")){
          WorldHandler.listSpawns(player);
        } else if (args.length == 2 && args[0].matches("gl?o?b?a?l?")){ //GLOBAL group spawn
            if (WorldHandler.groupHasGlobalSpawn(args[1])) {
                WorldHandler.removeGlobalSpawn(args[1]);
                player.sendMessage(ChatColor.GREEN + "> Global spawn for " + args[1] + " removed");
            } else {
                player.sendMessage(ChatColor.RED + "> Error: " + args[1] + " does not have a global spawn");
            }
//            if (!WorldHandler.worldSpawns.getJSONObject(args[0]).has(args[1])) {
//                commandSender.sendMessage(ChatColor.RED + " > " + args[1] + " is not a valid spawn within " + args[0]);
//                return true;
//            } else {
//                WorldHandler.worldSpawns.getJSONObject(args[0]).remove(args[1]);
//                if (WorldHandler.worldSpawns.getJSONObject(args[0]).isEmpty()) WorldHandler.worldSpawns.remove(args[0]); //Remove world if it has no custom spawns
//                JSONHandler.WriteJSONToFile(JawaCommands.getPlugin(), "/worldspawns.json", WorldHandler.worldSpawns);
//                commandSender.sendMessage(ChatColor.GREEN + " > " + args[1] + " has been removed from " + args[0]);
//            }
        }else if (args.length == 3 && args[0].matches("wo?r?l?d?")){
            if (!WorldHandler.worldHasSpawnsDefined(args[1])) {
                player.sendMessage(ChatColor.RED + "> Error: " + args[1] + " is not a valid world to remove a spawn from");
                return true;
            }
            if (!WorldHandler.worldHasGroupSpawn(args[1], args[2])) {
                player.sendMessage(ChatColor.RED + "> Error: a spawn for " + args[2] + " is not set in " + args[1]);
                return true;
            }
            
            WorldHandler.removeWorldSpawn(args[1], args[2]);
            player.sendMessage(ChatColor.GREEN + "> " + args[2] + " spawn removed from " + args[1]);
//            commandSender.sendMessage(ChatColor.RED + " > " + args[0] + " has no custom spawns.");
        } else if (args.length > 3) {
            player.sendMessage(ChatColor.RED + " > You have too many arguments! Usage: " );
            player.sendMessage(USAGE);
        } 
        
        return true;
    }
    
}
