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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jawamaster.jawacommands.Warp;
import jawamaster.jawacommands.handlers.WarpHandler;
import net.jawasystems.jawacore.PlayerManager;
import net.jawasystems.jawacore.dataobjects.PlayerDataObject;
import net.jawasystems.jawacore.utils.TargetSelectorProcessor;
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

    private List<UUID> targets;
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String arg0, String[] args) {
        String usage = "/wlwarp <warp name> <add|remove> <player name>";
        Warp warp;
        targets = new ArrayList();
        if (args == null || args.length == 0) {
            commandSender.sendMessage(ChatColor.RED + " > Usage: " + usage);
            return true;
        } else if (args.length > 0) {
            if (!WarpHandler.warpExists(args[0])) {
                commandSender.sendMessage(WarpHandler.WARPDOESNOTEXIST);
                return true;
            }
            warp = WarpHandler.getWarp(args[0]);
            if (!warp.canWhitelist(commandSender)) {
                commandSender.sendMessage(ChatColor.RED + "> Error: You do not have permission to alter the whitelist on this warp.");
                return true;
            }
            if (!warp.isWhiteListed()) {
                commandSender.sendMessage(ChatColor.RED + "> You must enable the whitelist first! /modwarp " + args[0] + " whitelist");
                return true;
            }
            if (args.length == 1) {
                //print whitelist
            } else if (args.length == 3) {
                if (resolveSelectors(args[2], commandSender)) {
                    if (!args[1].matches("(ad?d?|re?m?o?v?e?)")) {
                        commandSender.sendMessage(ChatColor.RED + "> Error: You must specify add or remove.");
                        return true;
                    }
                    targets.forEach((playerUUID) -> {
                        if (args[1].matches("ad?d?")) {
                            warp.addToWhiteList(playerUUID);
                            commandSender.sendMessage(ChatColor.GREEN + "> Adding " + targets.size() + " uuid(s) to the warp whitelist.");
                        } else if (args[1].matches("re?m?o?v?e?")) {
                            warp.removeFromWhitelist(playerUUID.toString());
                            commandSender.sendMessage(ChatColor.GREEN + "> Removing " + targets.size() + " uuid(s) from the warp whitelist.");
                        }
                    });
                    
                    WarpHandler.updateWarp(warp);
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "> Error: Invalid number of arguments.");
            }
            
        }
        
//        Player target = JawaCommands.getPlugin().getServer().getPlayer(args[1]);
//        
//        if (target == null){
//            commandSender.sendMessage(ChatColor.RED + "> " + args[1] + " must be online to add them to the blacklist!");
//            return true;
//        }
//                
//        if (warp.playerIsWhiteListed(target)){
//            warp.addToWhiteList(target.getUniqueId());
//        } else {
//            warp.removeFromWhitelist(target.getUniqueId().toString());
//        }
        
        
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
        
        
        //commandSender.sendMessage(ChatColor.GREEN + " > Adding " + args[1] + " to whitelist.");
        return true;
        
    }
    
        private boolean resolveSelectors(String selector, CommandSender commandSender){
        if (!selector.matches("@[p|P|a|A].*")) {
            PlayerDataObject target = PlayerManager.getPlayerDataObject(selector);
            if (target == null) {
                commandSender.sendMessage(PlayerManager.NOPLAYERERROR);
            } else {
                targets.add(target.getUniqueID());
            }
        } else if (selector.matches("@[p|P].*")) {
            Player target = TargetSelectorProcessor.getAtP(selector, commandSender);
            if (target != null) {
                targets.add(target.getUniqueId());
            }
        } else if (selector.matches("@[a|A].*")) {
            List<Player> tmptargets = TargetSelectorProcessor.getAtA(selector, commandSender);
            if (tmptargets != null && !tmptargets.isEmpty()) {
                tmptargets.forEach((target) -> {
                    targets.add(target.getUniqueId());
                });
            }
        }
        return !targets.isEmpty();
    }
    
}
