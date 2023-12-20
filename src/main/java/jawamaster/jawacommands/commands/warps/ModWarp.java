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
package jawamaster.jawacommands.commands.warps;

import jawamaster.jawacommands.Warp;
import jawamaster.jawacommands.handlers.WarpHandler;
import net.jawasystems.jawacore.PlayerManager;
import net.jawasystems.jawacore.dataobjects.PlayerDataObject;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author alexander
 */
public class ModWarp implements CommandExecutor{

    public static final String PERMISSION = "jawacommands.warps.modify";
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String arg1, String[] args) {
        /*
            /modwarp <warpname> <[whitelist|blacklist|location|owner|type]> <[owner|type]>
        */
        
        String usage = "/modwarp <warpname> [whitelist|location|hidden|owner|type] <[owner|type]>";
        String redSlug = ChatColor.RED + "> ";
        String greenSlug = ChatColor.GREEN + "> ";
//        String warpName;
//        String flags = null;
//        int start = 0;

        if (args == null || args.length == 0) {
            commandSender.sendMessage(greenSlug + usage);
            return true;
        } else if (WarpHandler.warpExists(args[0]) && (commandSender instanceof BlockCommandSender || WarpHandler.canAdministrate((Player) commandSender, args[0]))) {
            Warp workingWarp = WarpHandler.getWarp(args[0]);
            if (args[1].matches("wh?i?t?e?l?i?s?t?")){
                workingWarp.whitelisted(!workingWarp.isWhiteListed());
                WarpHandler.updateWarp(workingWarp);
                commandSender.sendMessage(greenSlug + "The whitelist has been toggled to: " + String.valueOf(workingWarp.isWhiteListed()));
            //} else if (args[1].matches("bl?a?c?k?l?i?s?t?")) {
            } else if (args[1].matches("lo?c?a?t?i?o?n?")) {
                workingWarp.setLocation(((Player) commandSender).getLocation());
                WarpHandler.updateWarp(workingWarp);
                commandSender.sendMessage(greenSlug + "The location has been updated.");
            } else if (args[1].matches("ow?n?e?r?")) {
                if (args.length >=3 ){
                    PlayerDataObject owner = PlayerManager.getPlayerDataObject(args[2]);
                    if (owner == null){
                        commandSender.sendMessage(ChatColor.RED + " > Error: That player is not found! Try their actual minecraft name instead of nickname.");
                        return true;
                    } else {
                        workingWarp.setOwner(owner.getUniqueID());
                        WarpHandler.updateWarp(workingWarp);
                        commandSender.sendMessage(greenSlug + "The owner has been set to " + owner.getFriendlyName());
                        return true;
                    }
                } else {
                    commandSender.sendMessage(redSlug + "Error: You must provide a user name for the owner!");
                    return true;
                }
                
            } else if (args[1].matches("hi?d?d?e?n?")) {
                workingWarp.setHiddenState(!workingWarp.isHidden());
                commandSender.sendMessage(greenSlug + "Toggling hidden to: " + String.valueOf(workingWarp.isHidden()));
                WarpHandler.updateWarp(workingWarp);
                return true;
            } else if (args[1].matches("ty?p?e?")) {
                if (args.length >= 3) {
                    if (args[2].matches("per?m?i?s?s?i?o?n?")){
                        workingWarp.setType("permission");
                        commandSender.sendMessage(greenSlug + "Warp type set to 'permission' for " + args[0]);
                        WarpHandler.updateWarp(workingWarp);
                        return true;
                    } else if (args[2].matches("pri?v?a?t?e?")) {
                        workingWarp.setType("private");
                        commandSender.sendMessage(greenSlug + "Warp type set to 'private' for " + args[0]);
                        WarpHandler.updateWarp(workingWarp);
                        return true;
                    } else if (args[2].matches("pub?l?i?c?")) {
                        workingWarp.setType("public");
                        commandSender.sendMessage(greenSlug + "Warp type set to 'public' for " + args[0]);
                        WarpHandler.updateWarp(workingWarp);
                        return true;
                    } else if (args[2].matches("ga?m?e?")) {
                        workingWarp.setType("game");
                        commandSender.sendMessage(greenSlug + "Warp type set to 'game' for " + args[0]);
                        WarpHandler.updateWarp(workingWarp);
                        return true;
                    } else {
                        commandSender.sendMessage(redSlug + "Error: " + args[2] + " is not a valid warp type.");
                        return true;
                    }
                } else {
                    commandSender.sendMessage(redSlug + "Error: You must provide a type!");
                    return true;
                }
            } else {
                commandSender.sendMessage(redSlug + "Error: " + args[1] + " is not a valid argument.");
                commandSender.sendMessage(greenSlug + usage);
            }
            return true;
        } else {
            commandSender.sendMessage(redSlug + "Error: " + args[0] + " does not appear to be a valid warp or you do not have permission to administer it.");
            return true;
        }
        
//        
//        if ((args== null ) || (args.length < 2)) {
//            commandSender.sendMessage(ChatColor.GREEN + " > Usage: " + usage);
//            return true;
//        }
//        
//        //Evaluate if there are flags
//        if (args[0].startsWith("-")) {
//            start = 1;
//            flags = args[0].replace("-", "");
//            //TODO resolve bad flags
//            warpName = args[start];
//
//        } else {
//            warpName = args[start];
//        }
//        
//        //Warp doesnt exist in loaded memory?
//        if (!WarpHandler.warpExists(warpName)){
//            commandSender.sendMessage(redSlug + warpName + " does not exist! Create it first to modify it's parameters!");
//            return true;
//        }
//        
//        //Get the warp object
//        Warp warp = WarpHandler.getWarp(warpName);
//        
//        if (!warp.playerCanModify((Player) commandSender)) {
//            commandSender.sendMessage(redSlug + "You do not have permission to modify that warp! If you belive that is in error please speak to an admin!");
//            return true;
//        }
//        
//        //More args than flags?
//        if (args.length > 2) {
//            String[] argss = Arrays.copyOfRange(args, start + 1, args.length);
//            HashMap<String, String> parsedArguments = ArgumentParser.getArgumentValues(argss);
//            for (String arg : parsedArguments.keySet()){
//                switch (arg) {
//                    case "o":{
//                        //If the player is online just get the UUID from them
//                        Player warpOwner = JawaCommands.getPlugin().getServer().getPlayer(parsedArguments.get("o"));
//                        
//                        //If they were online set it, done. If not run an ES search.
//                        if (warpOwner != null) {
//                            warp.setOwner(warpOwner.getUniqueId());
//                        } else {
//                            UUID targetUUID = UUID.fromString(ESHandler.findOfflinePlayer(parsedArguments.get("o")).getId());
//                            warp.setOwner(targetUUID);
//                        }
//                        break;
//                    }
//                    case "t": {
//                        boolean worked = warp.setType(parsedArguments.get("t"));
//                        if (!worked){
//                            commandSender.sendMessage(redSlug + parsedArguments.get("t") + " is not a valid type!");
//                            return true;
//                        } else {
//                            commandSender.sendMessage(greenSlug + "Type changed to : " +parsedArguments.get("t") );
//                        }
//                        break;
//                    }
//                    default:{
//                        commandSender.sendMessage(redSlug + "Unknown argument: " + arg);
//                        commandSender.sendMessage(redSlug + "Usage: " + usage);
//                        return true;
//                    }
//                }
//            };
//        }
//        
//        if (flags != null) {
//            for (char flag: flags.toCharArray()){
//                switch (flag){
//                    case 'w': { //Switch whitelist mode
//                        warp.whitelisted(!warp.isWhiteListed());
//                        commandSender.sendMessage(greenSlug + "Whitelist mode switched.");
//                        break;
//                    }
//                    case 'h': { //Switch hidden state
//                        warp.setHiddenState(!warp.isHidden());
//                        commandSender.sendMessage(greenSlug + "Hidden mode switched.");
//                        break;
//                    }
//                    case 'l': { //Changes the warp location to the location of the player
//                        commandSender.sendMessage(greenSlug + "New location set.");
//                        warp.setLocation(((Player) commandSender).getLocation());
//                        break;
//                    }
//                    default: {
//                        commandSender.sendMessage(redSlug + flag + " is an incorrect flag!");
//                        return true;
//                    }
//                }
//            }
//        }
//
//        //Push update to databse and modify working config
//        WarpHandler.updateWarp(warp);
//
//        
//        return true;
        
    }
    
}
