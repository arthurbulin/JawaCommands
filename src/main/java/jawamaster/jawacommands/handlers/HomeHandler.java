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
import java.util.Set;
import jawamaster.jawapermissions.PlayerDataObject;
import jawamaster.jawapermissions.utils.ESRequestBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.elasticsearch.action.update.UpdateRequest;
import org.json.JSONObject;
import jawamaster.jawapermissions.handlers.ESHandler;

/**
 *
 * @author alexander
 */
public class HomeHandler {
    
    private static String red = ChatColor.RED + " > ";
    private static String green = ChatColor.GREEN + " > ";

    /** Returns a PlayerDataObject from JawaPermissions. This object will contain
     * any "homes" index data the user has. This information call is private and
     * is only a worker for this class.
     * @param player
     * @return 
     */
    private static PlayerDataObject getPDO(Player player) {
        PlayerDataObject pdObject = new PlayerDataObject(player.getUniqueId());
        pdObject = ESHandler.runMultiIndexSearch(ESRequestBuilder.buildSingleMultiSearchRequest("homes", "_id", player.getUniqueId().toString()), pdObject);
        return pdObject;
    }

    /** Adds a home entry to the user's "homes" index entry. If replace == true
     * an existing home will be overwritten. Otherwise the player will be warned
     * and the home will not be overwritten.
     * @param player
     * @param homeName
     * @param replace
     * @return 
     */
    public static boolean addHome(Player player, String homeName, boolean replace) {
        PlayerDataObject pdObject = getPDO(player);
        
        //Check that name doesnt exist
        if (pdObject.containsHome(homeName) && !replace) {
            player.sendMessage(red + "Error: That home already exists! Remove it first or rerun with the -r flag!");
            return true;
        }

        //Collect player location data
        //Create home data
        JSONObject topLevel = LocationDataHandler.createTopLevelHomeObject(player.getLocation(), homeName);

        boolean success = ESHandler.singleUpdateRequest(ESRequestBuilder.updateRequestBuilder(topLevel, "homes", player.getUniqueId().toString(), true));

        if (success) {
            player.sendMessage(green + homeName + " has been successfully saved!");
        } else {
            player.sendMessage(red + homeName + " failed to be saved!");
        }
        return true;
    }

    /** Removes an entry from the user's "homes" index entry. If the home does not
     * exist the user will be notified.
     * @param player
     * @param homeName
     * @return 
     */
    public static boolean removeHome(Player player, String homeName) {
        PlayerDataObject pdObject = getPDO(player);
        
        if (!pdObject.containsHome(homeName)) {
            player.sendMessage(red + homeName + " is not in your home's list and cannot be removed!"  );
            return true;
        }
        UpdateRequest request = ESRequestBuilder.requestFieldRemoval(player, homeName);
        
        boolean status = ESHandler.singleUpdateRequest(request);
        
        if (status == true) {
            player.sendMessage(green + homeName + " has been successfully " + ChatColor.DARK_RED + " removed" + ChatColor.GREEN + "!");
        } else {
            player.sendMessage(red + homeName + " failed to be deleted!");
        }
        
        return true;
        
    }

    /** Sends a list of homes to the player in a viewable format.
     * @param player
     * @return 
     */
    public static boolean sendHomeList(Player player) {
        Set homeList = getHomeList(player);
        if (homeList != null){
            player.sendMessage(green + "Your homes: " + ChatColor.WHITE + String.join(", ", Arrays.toString(homeList.toArray())));
            return true;
        } else {
            player.sendMessage(red + "You don't have any homes set!");
            return false;
        }
        
    }
    
    /** Returns a Set containing the home names for a player. If a user has no homes
     * returns null;
     * @param player
     * @return 
     */
    public static Set getHomeList(Player player){
        PlayerDataObject pdObject = getPDO(player);
        if (pdObject.containsHomeData()) return pdObject.getHomeEntries();
        else return null;
    }

    /** Gets detailed and formatted home information for the player.
     * @param player
     * @param homeName 
     */
    public static void sendHomeInfo(Player player, String homeName) {

        JSONObject home = getHome(player, homeName);
        if (home != null) {
            player.sendMessage(green + "Name: " + ChatColor.WHITE + homeName);
            player.sendMessage(green + "World: " + ChatColor.WHITE + home.getString("world"));
            player.sendMessage(green + "(X,Y,Z): " + ChatColor.WHITE + "(" + (int) Math.round(home.getDouble("X")) + "," + (int) Math.round(home.getDouble("Y")) + "," + (int) Math.round(home.getDouble("Z")) + ")");
        } else {
            player.sendMessage(red + homeName + " is not in your home's list and cannot be removed!"  );

        }
    }
    
    /** Returns a user's specified home JSONObject containing the same data as a
     * Bukkit Location.
     * @param player
     * @param homeName
     * @return 
     */
    public static JSONObject getHome(Player player, String homeName) {
        PlayerDataObject pdObject = getPDO(player);
        if (pdObject.containsHomeData()) return pdObject.getHome(homeName);
        else return null;
    }
    
    /** Sends a player to the specified home location. Returns true on a successful transport
     * otherwise returns false.
     * @param player
     * @param homeName
     * @return 
     */
    public static boolean sendToHome(Player player, String homeName){
        PlayerDataObject pdObject = getPDO(player);
        
        if (pdObject.containsHome(homeName)) {
            player.sendMessage(ChatColor.GREEN + " > Welcome home.");
            player.teleport(LocationDataHandler.unpackLocation(pdObject.getHome(homeName)));
            return true;
        } else {
            return false;
        }
    }

}
