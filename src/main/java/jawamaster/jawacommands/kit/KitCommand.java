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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    /kit modify <name> delete
    /kit modify <name> <enable|disable>
    */
    
    public static final List<String> tabs = new ArrayList(Arrays.asList("list","help","info","items","create","modify"));
    public static final List<String> tabsTwo = new ArrayList(Arrays.asList("description","message","announce","cooldown","singleuse","permission","add","delete","enable","disable"));
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command cmnd, String string, String[] args) {
        Player player = (Player) commandSender;
        boolean admin = player.hasPermission(KitHandler.KITADMINPERM);
        if (args.length == 0){
            sendHelpMessage(commandSender, 1);
        } else if (args[0].matches("(?i)he?l?p?")) {
            //help message
            if ( admin && args.length > 1) {
                try {
                sendHelpMessage(commandSender, Integer.valueOf(args[1]));
                } catch (NumberFormatException e){
                    sendHelpMessage(commandSender, 1);
                }
            } else {
                sendHelpMessage(commandSender, 1);
            }
            
        } else if (args[0].matches("(?i)li?s?t?")) {
            //list
            KitHandler.sendListMessage((Player) commandSender);
        } else if (admin && args[0].matches("(?i)inf?o?")) {
            //info
            if (args.length > 1) {
                KitHandler.sendKitInfo(player, args[1]);
            } else {
                commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit info <name> " + ChatColor.YELLOW + "- See a description of <name>.");
            }
        } else if (args[0].matches("(?i)ite?m?s?")) {
            //list items in kit
            if (args.length > 1) {
                KitHandler.sendKitItemsInfo(player, args[1]);
            } else {
                commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit items <name> " + ChatColor.YELLOW + "- See the items in <name>.");
            }
        } else if (admin && args[0].matches("(?i)cr?e?a?t?e?")) {
            //create kit
            if (args.length > 2) {
                KitHandler.createKit(player, args[1], String.join(" ", Arrays.copyOfRange(args, 2, args.length)));
            } else {
                KitHandler.createKit(player, args[1]);
            }
            
        } else if (admin && args[0].matches("(?i)mo?d?i?f?y?")) {
            //modify kit
            if (args.length <= 2) {
                sendHelpMessage(commandSender, 1);
            } else {
                //KitHandler.modifyKit(player, args[1], args[2], String.join(" ", Arrays.copyOfRange(args, 2, args.length)));
                if (args[2].matches("(?i)desc?r?i?p?t?i?o?n?")){
                    modKit(player, args, "DESCRIPTION");
                } else if (args[2].matches("(?i)me?s?s?a?g?e?")){
                    modKit(player, args, "MESSAGE");
                } else if (args[2].matches("(?i)ann?o?u?n?c?e?")){
                    modKit(player, args, "ANNOUNCE");
                } else if (args[2].matches("(?i)si?n?g?l?e?u?s?e?")){
                    modKit(player, args, "SINGLEUSE");
                } else if (args[2].matches("(?i)co?o?l?d?o?w?n?")){
                    modKit(player, args, "COOLDOWN");
                } else if (args[2].matches("(?i)pe?r?m?i?s?s?i?o?n?")){
                    modKit(player, args, "PERMISSION");
                } else if (args[2].matches("(?i)add?")){
                    if (args.length >= 5) {
                        KitHandler.addItemsToKit(player, args[1], args[3], args[4]);
                    } else {
                        commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit modify <name> add <item> <#> ");
                    }
                } else if (args[2].matches("(?i)del?e?t?e?")){
                    modKit(player, args, "DELETE");
                } else if (args[2].matches("(?i)(en?a?b?l?e?)")){
                    modKit(player, args, "ENABLE");
                } else if (args[2].matches("(?i)dis?a?b?l?e?")){
                    modKit(player, args, "DISABLE");
                } else {
                    player.sendMessage(ChatColor.RED + "> Error: " + args[2] + " is not an understood modify term");
                }
            }
        } else {
            
            if (KitHandler.kitExists(args[0])){
                KitHandler.useKit(player, args[0]);
            } else {
                sendHelpMessage(commandSender, 1);
            }
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
    
    private static void modKit(Player player, String[] args, String flag){
        if (args.length == 3) {
            KitHandler.modifyKit(player, args[1], flag, "");
        } else {
            KitHandler.modifyKit(player, args[1], flag, String.join(" ", Arrays.copyOfRange(args, 3, args.length)));
        }
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
                //commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit modify <name> remove <item>");
                commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit modify <name> delete ");
                commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit modify <name> <enable|disable> ");
                break;
            default:
                commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit <name> " + ChatColor.YELLOW + "- Get this kit.");
                commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit help " + ChatColor.YELLOW + "- This help message.");
                commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit info <name> " + ChatColor.YELLOW + "- See a description of <name>.");
                commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + "/kit items <name> " + ChatColor.YELLOW + "- See the items in <name>.");
                break;
        }
    }
}