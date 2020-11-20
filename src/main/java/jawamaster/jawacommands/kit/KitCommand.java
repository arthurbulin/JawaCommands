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
package jawamaster.jawacommands.kit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import net.jawasystems.jawacore.utils.ArgumentParser;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author alexander
 */
public class KitCommand implements CommandExecutor {

    /* Usage:
    /kit
    /kit list
    /kit help
    /kit info <name>
    /kit items <name>
    /kit create <name> [description <multi word description>]
    /kit modify <name> description [<multi word description>]
    /kit modify <name> message [<message>]
    /kit modify <name> announce [<announce>]
    /kit modify <name> cooldown [<mintues>]
    /kit modify <name> singleuse [<LIFE|ONCE>]
    /kit modify <name> permission [<permission>]
    /kit modify <name> add <item> <#>
    /kit modify <name> remove <item>
    /kit modify <name> delete
    /kit modify <name> <enable|disable>
    */
    @Override
    public boolean onCommand(CommandSender commandSender, Command cmnd, String string, String[] args) {
        String matchString = String.join(" ", args);
        
        if (args[0].matches("(?i)li?s?t?")) {
            //list
            KitHandler.sendListMessage((Player) commandSender);
        } else if (args[0].matches("(?i)he?l?p?")) {
            //help message
            if (args.length > 1 && args[1].matches("^[0-3]$")) {
                sendHelpMessage(commandSender, Integer.valueOf(args[1]));
            } else {
                sendHelpMessage(commandSender, 1);
            }
        } else if (args[0].matches("(?i)inf?o?")) {
            //info
        } else if (args[0].matches("(?i)ite?m?s?")) {
            //list items in kit
        } else if (args[0].matches("(?i)cr?e?a?t?")) {
            //create kit
        } else if (args[0].matches("(?i)mo?d?i?f?y?")) {
            //modify kit
        } else {
            //help message
        }
        
        
        
        
//        if (args == null || args.length == 0){
//            KitHandler.sendHelpMessage(commandSender);
//            return true;
//        } else if (args.length == 1) {
//            if (args[0].equalsIgnoreCase("-h")) {
//                KitHandler.sendHelpMessage(commandSender);
//                return true;
//            } else if (args[0].equalsIgnoreCase("-l") && commandSender.hasPermission("jawacommands.kit.user")){
//                KitHandler.listKits(commandSender, null);
//                return true;
//            } else {
//                commandSender.sendMessage(ChatColor.RED + " Error: That flag is not understood!");
//                return true;
//            }
//        } 
//        
//        
//        /*args: 
//        -c: create - <kit name>
//        -m: modify this kit - <kit name>
//        -i: add/rem items from kit - <item id> <#>
//        -f: add flags - <flag> [flag options]
//        -h: display help
//        -r: remove a kit - <kit name>
//        -d: description - <kit name> <description>
//        */
//        HashSet<String> acceptedArgs = new HashSet(Arrays.asList("c","m","i","f","h","g","r","l"));
//        
//        HashMap<String, String> parsedArgs = ArgumentParser.getArgumentValues(args);
//        
//        if (!ArgumentParser.validateArguments(commandSender, parsedArgs, acceptedArgs)){
//            commandSender.sendMessage(ChatColor.RED + " > Use /kit -h for the command help.");
//            return true;
//        }
//        
//        String kitName;
//        if (parsedArgs.containsKey("l")){
//            kitName = parsedArgs.get("l");
//            KitHandler.listKits(commandSender, kitName);
//            return true;
//        }
//        if (parsedArgs.containsKey("g")){
//            kitName = parsedArgs.get("g").split(" ")[0];
//            String[] players = Arrays.copyOfRange(parsedArgs.get("g").split(" "),1,parsedArgs.get("g").split(" ").length);
//            KitHandler.givePlayersKit(commandSender, kitName, players);
//            return true;
//        }
//        if (parsedArgs.containsKey("r")){
//            boolean removed = KitHandler.removeKit(parsedArgs.get("r"), commandSender);
//            return true;
//        }
//        
//        
////==============================================================================
////          ADMIN FUNCTIONS BELOW - user function should return before this
////==============================================================================
//        if (!commandSender.hasPermission("jawacommands.kit.admin")){
//                commandSender.sendMessage(ChatColor.RED + " > You do not have permission to create a kit!");
//                return true;
//        }
//        
//        boolean created;
//        if (parsedArgs.containsKey("c")) {
//            created = KitHandler.createKit(parsedArgs.get("c"), commandSender);
//            kitName = parsedArgs.get("c");
//            if (!created) return true;
//            //else continue with execution
//        } else if (parsedArgs.containsKey("m")){
//            kitName = parsedArgs.get("m");
//        } else {
//            commandSender.sendMessage(ChatColor.RED + " > Error: You must specify a kit argument: -c,m,g,r");
//            return true;
//        }
//        
//        
//        if (parsedArgs.containsKey("i")){
//            KitHandler.modifyKitItems(kitName, parsedArgs.get("i"), commandSender);
//        } else if (parsedArgs.containsKey("f")){
//            KitHandler.modifyKitFlags(kitName, parsedArgs.get("f"), commandSender);
//        }
//        
//        
//        
//        
        return true;
        
    }
    
    private static void sendHelpMessage(CommandSender commandSender, int page) {
        if (!commandSender.hasPermission(KitHandler.KITBASEPERM + ".admin")) {
            commandSender.sendMessage(ChatColor.GREEN + "> Kit Command Help:");
        } else {
            commandSender.sendMessage(ChatColor.GREEN + "> Kit Command Help " + page + " of 3:");
        }

        switch (page) {
            case 1:
                commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit <name> " + ChatColor.YELLOW + "- Get this kit.");
                commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit help " + ChatColor.YELLOW + "- This help message.");
                commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit info <name> " + ChatColor.YELLOW + "- See a description of <name>.");
                commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit items <name> " + ChatColor.YELLOW + "- See the items in <name>.");
                break;
            case 2:
                commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit create <name> [description <multi word description>] ");
                commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit modify <name> description [<multi word description>] ");
                commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit modify <name> announce [<announce>] ");
                commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit modify <name> message [<message>] ");
                commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit modify <name> cooldown [<mintues>] ");
                break;
            case 3:
                commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit modify <name> singleuse [<LIFE|ONCE>] ");
                commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit modify <name> permission [<permission>] ");
                commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit modify <name> add <item> <#> ");
                commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit modify <name> remove <item>");
                commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit modify <name> delete ");
                commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit modify <name> <enable|disable> ");
                break;
            default:
                break;
        }
    }
}