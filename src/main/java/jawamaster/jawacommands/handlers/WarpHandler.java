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
import jawamaster.jawacommands.JawaCommands;
import jawamaster.jawacommands.WarpObject;
import net.jawasystems.jawacore.utils.ESRequestBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.elasticsearch.action.search.SearchRequest;
import net.jawasystems.jawacore.handlers.ESHandler;
import org.bukkit.ChatColor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.search.SearchHit;
import org.json.JSONObject;

/**
 *
 * @author alexander
 */
public class WarpHandler {
    
    public static HashMap<String,WarpObject> loadWarpObjects(){ //TODO if check for malformed warps to prevent errors
        HashMap<String, WarpObject> warps = new HashMap();
        
        SearchRequest request = ESRequestBuilder.getAllOfIndex("warps");
        SearchHit[] hits = ESHandler.runSearchRequest(request);
        
        if ((hits != null) && (hits.length > 0)) {
            Arrays.asList(hits).forEach((hit) -> {
                warps.put(hit.getId(), new WarpObject(hit.getId(), new JSONObject(hit.getSourceAsMap())));
            });
            System.out.println(warps.size() + " warps loaded into memory.");
            System.out.println(warps.keySet());
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
    private static WarpObject buildWarpObject(String warpName, String type, Location location, UUID createdBy){
        WarpObject warp = new WarpObject(warpName, type, createdBy);
        warp.setLocation(location);
        return warp;
    }
    
    public static boolean createWarp(Player player, String warpName, String type){
        //make sure this isnt an existing warp
        if (JawaCommands.getWarpIndex().containsKey(warpName)) {
            player.sendMessage(ChatColor.RED + " > " + warpName + " already exists in the warp database! Use a differnet name or delete/modify the exisint one!");
            return false;
        }
        
        //Generate new warpObject
        WarpObject newWarp = buildWarpObject(warpName, type, player.getLocation(), player.getUniqueId());
        
        //Create the index request
        IndexRequest indexRequest = ESRequestBuilder.createIndexRequest("warps", warpName, newWarp.getWarpData());
        
        //Index and return if success
        boolean esWorked = ESHandler.runSingleIndexRequest(indexRequest);
        
        //If it worked tell the player if it didnt also tell the player!
        if (esWorked) {
            JawaCommands.addWarpToIndex(newWarp);
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
        
        JawaCommands.getWarpIndex().values().forEach((warp) -> {
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
    
    public static void updateWarp(WarpObject warp){
        //Push update to current list
        JawaCommands.getWarpIndex().put(warp.getWarpName(), warp);
        
        //Update the ES index
        JSONObject warpData = warp.getWarpData();
        //System.out.println(warpData);
        UpdateRequest request = ESRequestBuilder.updateRequestBuilder(warpData, "warps", warp.getWarpName(), true);
        ESHandler.singleUpdateRequest(request);
    }
    
    public static boolean deleteWarp(WarpObject warp){
        //remove from ES        
        boolean worked = ESHandler.runSingleDocumentDeleteRequest(ESRequestBuilder.deleteDocumentRequest("warps", warp.getWarpName()));
        if (worked) {
            JawaCommands.getWarpIndex().remove(warp.getWarpName());
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean warpExists(String warp){
        return JawaCommands.getWarpIndex().containsKey(warp);
    }
    
}
