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

import jawamaster.jawacommands.JawaCommands;
import jawamaster.jawacommands.Warp;
import jawamaster.jawacommands.handlers.WarpHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author alexander
 */
public class WarpWhitelist implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String arg0, String[] args) {
        String usage = "/wlwarp <warp name> <player name>";
        Warp warp;
        
        if ((args == null) || (args.length < 2)) {
            commandSender.sendMessage(ChatColor.RED + " > Usage: " + usage);
            return true;
        } 
        
        if (!WarpHandler.warpExists(args[0])){
            commandSender.sendMessage(ChatColor.RED + " > "+ args[0] + " does not exist! Create it first to modify it's parameters!");
            return true;
        }
        
        warp = WarpHandler.getWarp(args[0]);
        
        if (!warp.isWhiteListed()){
            commandSender.sendMessage(ChatColor.RED + " > You must enable the whitelist first! /modwarp -w " + args[0]);
            return true;
        }
        Player target = JawaCommands.getPlugin().getServer().getPlayer(args[1]);
        
        if (target == null){
            commandSender.sendMessage(ChatColor.RED + " > " + args[1] + " must be online to add them to the blacklist!");
            return true;
        }
                
        if (warp.playerIsWhiteListed(target)){
            warp.addToWhiteList(target.getUniqueId());
        } else {
            warp.removeFromWhitelist(target.getUniqueId().toString());
        }
        
        
//        String[] playerNames = Arrays.copyOfRange(args, 1, args.length);
//        
//        
//        
//        MultiSearchRequest msRequest = new MultiSearchRequest();
//        
//        for (String name : playerNames) {
//            ESRequestBuilder.addToMultiSearchRequest(msRequest, "players", "_id", name);
//        }
//        
//        MultiSearchResponse response = ESHandler.runMultiSearchRequest(msRequest);
//        Set failed = new HashSet();
//        Set<UUID> addToWL = new HashSet();
//        
//        for(Item item : response.getResponses()) {
//            if (item.isFailure()){
//                failed.add(item.getResponse());
//            } else {
//                addToWL.add(UUID.fromString(item.getResponse().getHits().getHits()[0].getId()));
//            }
//        }
//        
//        warp.addToWhiteList(addToWL);
        
        WarpHandler.updateWarp(warp);
        commandSender.sendMessage(ChatColor.GREEN + " > Adding " + args[1] + " to whitelist.");
        return true;
        
    }
    
}
