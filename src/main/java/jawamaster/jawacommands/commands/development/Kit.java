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
package jawamaster.jawacommands.commands.development;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import jawamaster.jawacommands.handlers.KitHandler;
import net.jawasystems.jawacore.utils.ArgumentParser;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author alexander
 */
public class Kit implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command cmnd, String string, String[] args) {

        if (args == null || args.length == 0){
            KitHandler.sendHelpMessage(commandSender);
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("-h")) {
                KitHandler.sendHelpMessage(commandSender);
                return true;
            } else if (args[0].equalsIgnoreCase("-l") && commandSender.hasPermission("jawacommands.kit.user")){
                KitHandler.listKits(commandSender, null);
                return true;
            } else {
                commandSender.sendMessage(ChatColor.RED + " Error: That flag is not understood!");
                return true;
            }
        } 
        
        
        /*args: 
        -c: create - <kit name>
        -m: modify this kit - <kit name>
        -i: add/rem items from kit - <item id> <#>
        -f: add flags - <flag> [flag options]
        -h: display help
        -r: remove a kit - <kit name>
        -d: description - <kit name> <description>
        */
        HashSet<String> acceptedArgs = new HashSet(Arrays.asList("c","m","i","f","h","g","r","l"));
        
        HashMap<String, String> parsedArgs = ArgumentParser.getArgumentValues(args);
        
        if (!ArgumentParser.validateArguments(commandSender, parsedArgs, acceptedArgs)){
            commandSender.sendMessage(ChatColor.RED + " > Use /kit -h for the command help.");
            return true;
        }
        
        String kitName;
        if (parsedArgs.containsKey("l")){
            kitName = parsedArgs.get("l");
            KitHandler.listKits(commandSender, kitName);
            return true;
        }
        if (parsedArgs.containsKey("g")){
            kitName = parsedArgs.get("g").split(" ")[0];
            String[] players = Arrays.copyOfRange(parsedArgs.get("g").split(" "),1,parsedArgs.get("g").split(" ").length);
            KitHandler.givePlayersKit(commandSender, kitName, players);
            return true;
        }
        if (parsedArgs.containsKey("r")){
            boolean removed = KitHandler.removeKit(parsedArgs.get("r"), commandSender);
            return true;
        }
        
        
//==============================================================================
//          ADMIN FUNCTIONS BELOW - user function should return before this
//==============================================================================
        if (!commandSender.hasPermission("jawacommands.kit.admin")){
                commandSender.sendMessage(ChatColor.RED + " > You do not have permission to create a kit!");
                return true;
        }
        
        boolean created;
        if (parsedArgs.containsKey("c")) {
            created = KitHandler.createKit(parsedArgs.get("c"), commandSender);
            kitName = parsedArgs.get("c");
            if (!created) return true;
            //else continue with execution
        } else if (parsedArgs.containsKey("m")){
            kitName = parsedArgs.get("m");
        } else {
            commandSender.sendMessage(ChatColor.RED + " > Error: You must specify a kit argument: -c,m,g,r");
            return true;
        }
        
        
        if (parsedArgs.containsKey("i")){
            KitHandler.modifyKitItems(kitName, parsedArgs.get("i"), commandSender);
        } else if (parsedArgs.containsKey("f")){
            KitHandler.modifyKitFlags(kitName, parsedArgs.get("f"), commandSender);
        }
        
        
        
        
        return true;
        
    }
    
}
