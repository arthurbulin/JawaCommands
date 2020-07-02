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
package jawamaster.jawacommands.handlers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import jawamaster.jawacommands.JawaCommands;
import jawamaster.jawacommands.Warp;
import net.jawasystems.jawacore.utils.ESRequestBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.elasticsearch.action.search.SearchRequest;
import net.jawasystems.jawacore.handlers.ESHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.search.SearchHit;
import org.json.JSONObject;

/**
 *
 * @author alexander
 */
public class WarpHandler {
    
    private static HashMap<String, Warp> warpIndex;
            
    public WarpHandler(){
        warpIndex = WarpHandler.loadWarpObjects();
        Logger.getLogger("WarpHandler").log(Level.INFO, "Warps loaded:{0}. {1}", new Object[]{warpIndex.size(), warpIndex.keySet()});

    }

    
    public static HashMap<String,Warp> loadWarpObjects(){ //TODO if check for malformed warps to prevent errors
        HashMap<String, Warp> warps = new HashMap();
        
        SearchRequest request = ESRequestBuilder.getAllOfIndex("warps");
        SearchHit[] hits = ESHandler.runSearchRequest(request);
        
        if ((hits != null) && (hits.length > 0)) {
            Arrays.asList(hits).forEach((hit) -> {
                warps.put(hit.getId(), new Warp(hit.getId(), new JSONObject(hit.getSourceAsMap())));
            });
            
        } else {
        }
        
        return warps;
    }
    
    /** Creates a warp object from the supplied arguments and returns it.
     * @param warpName
     * @param type
     * @param location
     * @param whiteListed
     * @param hidden
     * @return 
     */
    private static Warp buildWarpObject(String warpName, String type, Location location, UUID createdBy){
        Warp warp = new Warp(warpName, type, createdBy);
        warp.setLocation(location);
        return warp;
    }
    
    public static boolean createWarp(Player player, String warpName, String type){
        //make sure this isnt an existing warp
        if (warpIndex.containsKey(warpName)) {
            player.sendMessage(ChatColor.RED + " > " + warpName + " already exists in the warp database! Use a differnet name or delete/modify the exisint one!");
            return false;
        }
        
        //Generate new warpObject
        Warp newWarp = buildWarpObject(warpName, type, player.getLocation(), player.getUniqueId());
        
        //Create the index request
        IndexRequest indexRequest = ESRequestBuilder.createIndexRequest("warps", warpName, newWarp.getWarpData());
        
        //Index and return if success
        boolean esWorked = ESHandler.runSingleIndexRequest(indexRequest);
        
        //If it worked tell the player if it didnt also tell the player!
        if (esWorked) {
            warpIndex.put(newWarp.getWarpName(), newWarp);
            player.sendMessage(ChatColor.GREEN + " > " + warpName + " has been indexed! You can modify it with /modwarp.");
            return true;
        } else {
            player.sendMessage(ChatColor.RED + " > " + warpName + " was not able to be indexed!");
            return false;
        }
    }
    
    public static void listWarps(Player player){
        Set<String> hiddenVisible = new HashSet();
        Set<String> regularVisible = new HashSet();
        
        warpIndex.values().forEach((warp) -> {
            if (warp.isHidden() && warp.playerCanSee(player)){
                if (warp.playerCanVisit(player)) hiddenVisible.add(ChatColor.GREEN + warp.getWarpName() + ChatColor.WHITE);
                else hiddenVisible.add(ChatColor.RED + warp.getWarpName()  + ChatColor.WHITE);
            } else if (!warp.isHidden()) {
                if (warp.playerCanVisit(player)) regularVisible.add(ChatColor.GREEN + warp.getWarpName() + ChatColor.WHITE);
                else regularVisible.add(ChatColor.RED + warp.getWarpName() + ChatColor.WHITE);
            }
        });
        //player.sendMessage("(" + ChatColor.GREEN + "Accessible" + ChatColor.WHITE + ") (" +ChatColor.RED + "Unaccessible" + ChatColor.WHITE + ")");
        player.sendMessage(ChatColor.GREEN + " > These are warps you can see:" );
        player.sendMessage(String.join(", ", Arrays.toString(regularVisible.toArray())));
        if (!hiddenVisible.isEmpty()) {
            player.sendMessage(ChatColor.GREEN + " > These are hidden warps you can see:");
            player.sendMessage(String.join(", ", Arrays.toString(hiddenVisible.toArray())));
        }
    }
    
    public static void updateWarp(Warp warp){
        //Push update to current list
        warpIndex.put(warp.getWarpName(), warp);
        
        //Update the ES index
        JSONObject warpData = warp.getWarpData();
        //System.out.println(warpData);
        UpdateRequest request = ESRequestBuilder.updateRequestBuilder(warpData, "warps", warp.getWarpName(), true);
        ESHandler.singleUpdateRequest(request);
    }
    
    public static boolean deleteWarp(String warp){
        //remove from ES        
        boolean worked = ESHandler.runSingleDocumentDeleteRequest(ESRequestBuilder.deleteDocumentRequest("warps", warpIndex.get(warp).getWarpName()));
        if (worked) {
            warpIndex.remove(warp);
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean warpExists(String warp){
        return warpIndex.containsKey(warp);
    }
    
    public static boolean canVisit(CommandSender commandSender, String warp){
        return warpIndex.get(warp).canVisit(commandSender);
    }
    
    public static boolean canVisit(Player player, String warp){
        return warpIndex.get(warp).playerCanVisit((Player) player);
    }
    
    //TODO build administration options
    public static boolean canAdministrate(Player player, String warp){
        return false;
    }
    
    public static void sendPlayer(Player target, String warp){
        warpIndex.get(warp).sendPlayer(target);
    }
    
    public static Warp getWarp(String warp){
        return warpIndex.get(warp);
    }
    
    
}
