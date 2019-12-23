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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import jawamaster.jawacommands.JawaCommands;
import jawamaster.jawacommands.WarpObject;
import jawamaster.jawacommands.handlers.WarpHandler;
import jawamaster.jawapermissions.handlers.ESHandler;
import jawamaster.jawapermissions.utils.ArgumentParser;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author alexander
 */
public class ModWarp implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String arg1, String[] args) {
        String usage = "/modwarp <-[w|h|l]> <warpname> <-[o|t]> [arguments(owner or type only)]...";
        String redSlug = ChatColor.RED + " > ";
        String greenSlug = ChatColor.GREEN + " > ";
        String warpName;
        String flags = null;
        int start = 0;

        if ((args== null ) || (args.length < 2)) {
            //Whatever if less than 2 args
        }
        
        //Evaluate if there are flags
        if (args[0].startsWith("-")) {
            start = 1;
            flags = args[0].replace("-", "");
            //TODO resolve bad flags
            warpName = args[start];

        } else {
            warpName = args[start];
        }
        
        //Warp doesnt exist in loaded memory?
        if (!JawaCommands.getWarpIndex().containsKey(warpName)){
            commandSender.sendMessage(redSlug + warpName + " does not exist! Create it first to modify it's parameters!");
            return true;
        }
        
        //Get the warp object
        WarpObject warp = JawaCommands.getWarpIndex().get(warpName);
        
        if (!warp.playerCanModify((Player) commandSender)) {
            commandSender.sendMessage(redSlug + "You do not have permission to modify that warp! If you belive that is in error please speak to an admin!");
            return true;
        }
        
        //More args than flags?
        if (args.length > 2) {
            String[] argss = Arrays.copyOfRange(args, start + 1, args.length);
            HashMap<String, String> parsedArguments = ArgumentParser.getArgumentValues(argss);
            for (String arg : parsedArguments.keySet()){
                switch (arg) {
                    case "o":{
                        //If the player is online just get the UUID from them
                        Player warpOwner = JawaCommands.getPlugin().getServer().getPlayer(parsedArguments.get("o"));
                        
                        //If they were online set it, done. If not run an ES search.
                        if (warpOwner != null) {
                            warp.setOwner(warpOwner.getUniqueId());
                        } else {
                            UUID targetUUID = UUID.fromString(ESHandler.findOfflinePlayer(parsedArguments.get("o")).getId());
                            warp.setOwner(targetUUID);
                        }
                        break;
                    }
                    case "t": {
                        boolean worked = warp.setType(parsedArguments.get("t"));
                        if (!worked){
                            commandSender.sendMessage(redSlug + parsedArguments.get("t") + " is not a valid type!");
                            return true;
                        } else {
                            commandSender.sendMessage(greenSlug + "Type changed to : " +parsedArguments.get("t") );
                        }
                        break;
                    }
                    default:{
                        commandSender.sendMessage(redSlug + "Unknown argument: " + arg);
                        commandSender.sendMessage(redSlug + "Usage: " + usage);
                        return true;
                    }
                }
            };
        }
        
        if (flags != null) {
            for (char flag: flags.toCharArray()){
                switch (flag){
                    case 'w': { //Switch whitelist mode
                        warp.whitelisted(!warp.isWhiteListed());
                        commandSender.sendMessage(greenSlug + "Whitelist mode switched.");
                        break;
                    }
                    case 'h': { //Switch hidden state
                        warp.setHiddenState(!warp.isHidden());
                        commandSender.sendMessage(greenSlug + "Hidden mode switched.");
                        break;
                    }
                    case 'l': { //Changes the warp location to the location of the player
                        commandSender.sendMessage(greenSlug + "New location set.");
                        warp.setLocation(((Player) commandSender).getLocation());
                        break;
                    }
                    default: {
                        commandSender.sendMessage(redSlug + flag + " is an incorrect flag!");
                        return true;
                    }
                }
            }
        }

        //Push update to databse and modify working config
        WarpHandler.updateWarp(warp);

        
        return true;
        
    }
    
}
