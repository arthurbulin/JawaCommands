/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jawamaster.jawacommands.handlers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jawamaster.jawacommands.JawaCommands;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 *
 * @author Arthur Bulin
 */
public class ESHandler {
    public static JawaCommands plugin;
    public static RestHighLevelClient restClient;
    public static SearchRequest searchRequest;
    public static SearchSourceBuilder searchSourceBuilder;
    public static SearchHits searchHits;
    public static UpdateRequest updateRequest;
    
    public ESHandler(JawaCommands plugin) {
        ESHandler.plugin = plugin;
        ESHandler.restClient = JawaCommands.restClient;
    }
    
    public static Map<String, Object> getHomeData(Player player, String home, int action) {
        searchRequest = new SearchRequest("mc");
        searchSourceBuilder = new SearchSourceBuilder();
        
        searchSourceBuilder.query(QueryBuilders.matchQuery("_id", player.getUniqueId().toString()));
        searchRequest.source(searchSourceBuilder);
        
        SearchResponse searchResponse;
        try {
            searchResponse = restClient.search(searchRequest);
            searchHits = searchResponse.getHits();
            SearchHit[] hits = searchHits.getHits();
            long totalHits = searchHits.totalHits;
            
        if (totalHits == 0){
            //TODO deal with player not in db, this should never happen
        } else {
            
            Map<String, Object> playerHomes;
            if (hits[0].getSourceAsMap().containsKey("homes")) playerHomes = (Map<String, Object>) hits[0].getSourceAsMap().get("homes");
            else {
                if (action != 2) {
                    player.sendMessage("You do not have any homes");
                    player.sendMessage("To add a home at your current position: /home -a <homename>");
                    return null;
                } else playerHomes = new HashMap();

            }
            
            //System.out.println(playerHomes);
            
            switch (action){
                case 0: //Teleporting to home
                    Map<String, Object> loc = (Map<String, Object>) playerHomes.get(home);
                    System.out.println(loc);
                    player.teleport(DataHandler.assembleLocation(loc));
                    player.teleport(new Location((World) Bukkit.getWorld((String) loc.get("world")), (double) loc.get("x"), (double) loc.get("y"), (double) loc.get("z"), (float) ((double) loc.get("yaw")), (float) ((double) loc.get("pitch"))));
                    System.out.println("home teleport");
                    
                    if (!playerHomes.isEmpty() && playerHomes.containsKey(home)) {
                        System.out.println("teleporting");
                        //player.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
                    }
                    else player.sendMessage(home + " is not in your saved home list.");
                    break;
                    
                case 1: //listing homes
                    if (!playerHomes.isEmpty()) player.sendMessage("These are your current homes: " + playerHomes.keySet());
                    else player.sendMessage("You do not have any homes set. /home -a <homename> to set a home to the current location.");
                    break;
                    
                case 2: //Adding homes
                    Map<String, Object> homeLoc = DataHandler.disassembleLocation(player.getLocation());
                    if (playerHomes.containsKey(home)) {
                        player.sendMessage(home + " already exists within your homelist! Please remove it first!");
                    } else {
                        playerHomes.put(home, homeLoc); //Just overwrite for now. //TODO deal with overwrite warning
                        updateHomeData(player, playerHomes);
                    }
                    break;
                    
                case 3: //Deleting homes
                    if (!playerHomes.isEmpty() && playerHomes.containsKey(home)){
                        //playerHomes.remove(home);
                        //updateHomeData(player, playerHomes);
                        deleteHome(player, home);
                    }
                    else player.sendMessage(home + " is not in your homelist.");
                    break;
                    
                case 4:
                    if (!playerHomes.isEmpty() && playerHomes.containsKey("home")) player.teleport((Location) playerHomes.get("home"), PlayerTeleportEvent.TeleportCause.PLUGIN);
                    else player.sendMessage("You do not have a default home set. Run /home -h for information.");
                    break;
                    
                default: //Something called the wrong action
                    System.out.println("I dont even know what you did to get this message.");
                    break;
            }
        }
        } catch (IOException ex) {
            Logger.getLogger(ESHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static void updateHomeData(Player player, Map<String, Object> playerHomes){
        Map<String, Object> playerDataUpdate = new HashMap();
        playerDataUpdate.put("homes", playerHomes);
        updateRequest = new UpdateRequest("mc", "players", player.getUniqueId().toString()).doc(playerDataUpdate);
        
        restClient.updateAsync(updateRequest, new ActionListener<UpdateResponse>() {
            @Override
            public void onResponse(UpdateResponse response) {
                
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        
    });
    }
    
    public static void deleteHome(Player player, String home){
        
        Script script = new Script(ScriptType.INLINE, "painless", "ctx._source.homes.remove(\"" + home + "\")", new HashMap());
        
        updateRequest = new UpdateRequest("mc", "players", player.getUniqueId().toString()).script(script);
        
        restClient.updateAsync(updateRequest, new ActionListener<UpdateResponse>() {
            @Override
            public void onResponse(UpdateResponse response) {
                player.sendMessage(home + " has been removed from your home locations.");
            }

            @Override
            public void onFailure(Exception e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
    
}
