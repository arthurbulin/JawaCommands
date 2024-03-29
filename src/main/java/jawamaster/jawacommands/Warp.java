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
package jawamaster.jawacommands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import jawamaster.jawacommands.commands.warps.ModWarp;
import jawamaster.jawacommands.handlers.TPHandler;
import net.jawasystems.jawacore.handlers.LocationDataHandler;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author alexander
 */
public class Warp {
    private JSONObject warpData;
    private String warpName;
    private Location location;
    private final Set WARPTYPES = new HashSet(Arrays.asList("public", "private", "permission","game"));
    
    private static final Logger LOGGER = Logger.getLogger("Kit");
    
    public Warp(String warpName, JSONObject warpData){
        this.warpData = warpData;
        this.warpName = warpName;
        location = LocationDataHandler.unpackLocation(warpData.getJSONObject("location"));
    }
    
    public Warp(String warpName, String type, UUID createdBy){
        this.warpName = warpName;
        this.warpData = new JSONObject();
        warpData.put("created-by", createdBy.toString());
        warpData.put("owner", createdBy.toString());
        setDefaults(type);
        
    }
    
    private void setDefaults(String type){
        switch (type) {
            case "public": {
                setType("public");
                whitelisted(false);
                warpData.put("whitelist", new JSONArray());
                warpData.put("blacklist", new JSONArray());
                setHiddenState(false);
                break;
            }
            case "private": {
                setType("private");
                whitelisted(false);
                warpData.put("whitelist", new JSONArray());
                warpData.put("blacklist", new JSONArray());
                setHiddenState(false);
                break;
            }
            case "permission": {
                setType("permission");
                whitelisted(false);
                warpData.put("whitelist", new JSONArray());
                warpData.put("blacklist", new JSONArray());
                setHiddenState(false);
                setAccessPermission("warps.permission." + warpName);
                break;
            }
            case "game": {
                setType("game");
                whitelisted(true);
                warpData.put("whitelist", new JSONArray());
                warpData.put("blacklist", new JSONArray());
                setHiddenState(true);
                break;
            }
        }
        
    }

//###############################################################################
// Get Warp data
//###############################################################################
    public String getWarpName(){
        return warpName;
    }
    
    public String getType(){
        return warpData.getString("type");
    }
    
    public Location getLocation(){
        return location;
    }
    
    public String getAccessPermission(){
        if (warpData.getString("type").equalsIgnoreCase("permission")) return warpData.getString("access-permission");
        else return null;
    }
    
    public UUID getOwner(){
        return UUID.fromString(warpData.getString("owner"));
    }
    
    public JSONArray getWhiteList(){
        if (warpData.optBoolean("whitelisted")) return warpData.getJSONArray("whitelist");
        else return null;
    }
    
    public JSONArray getBlackList(){
        return warpData.getJSONArray("blacklist");
    }
    
    public JSONObject getWarpData(){
        return warpData;
    }
    
    public boolean isHidden(){
        return warpData.optBoolean("hidden");
    }
    
    public boolean isWhiteListed(){
        return warpData.optBoolean("whitelisted", false);
    }
    
    public boolean playerIsWhiteListed(Player player){
        if (isWhiteListed()){
            for (Object uuid : getWhiteList()){
                if (((String) uuid).equals(player.getUniqueId().toString())){
                    return true;
                }
            } 
            return false;
            //return .toList().contains(player.getUniqueId().toString());
        } else {
            return false;
        }
    }
    public boolean playerIsWhiteListed(UUID uuid){
        if (!isWhiteListed()){
            return getWhiteList().toList().contains(uuid.toString());
        } else {
            return false;
        }
    }
    
    /** If a given CommandSender can visit. This evaluates if the CommandSender
     * is a player or a command block. For any other type of entity this will return false
     * because what else is running the command? The player portion of this is backed
     * by playerCanVisit(Player player)
     * @param commandSender the sender executing the command
     * @return 
     */
    public boolean canVisit(CommandSender commandSender){
        if (commandSender instanceof Player) return playerCanVisit((Player) commandSender);
        else if (commandSender instanceof BlockCommandSender) return true; //This allows command blocks to send players using the same logic as the warp other option
        else return false;
    }
    
    /** Returns true is a player can visit this warp. This checks permissions, 
     * admin permissions, ownership, white lists, and black lists.
     * @param player
     * @return 
     */
    public boolean playerCanVisit(Player player){
        //On all
            //restrict if on blacklist
            //Allow if on whitelist
            //Allow if admin
            //Allow if owner
        //if permission
            //Allow if permission
        if (JawaCommands.isDebug()){
            LOGGER.log(Level.INFO, "HasPermission:{0}IsOwner:{1}", new Object[]{player.hasPermission(ModWarp.PERMISSION), getOwner().equals(player.getUniqueId())});
            LOGGER.log(Level.INFO, "IsBlacklisted:{0}", getBlackList().toList().contains(player.getUniqueId().toString()));
            LOGGER.log(Level.INFO, "IsWhitelisted:{0}", playerIsWhiteListed(player));
            //LOGGER.log(Level.INFO, "Permission:{0}HasPermission:{1}", new Object[]{warpData.getString("type").equals("permission"),player.hasPermission(warpData.getString("access-permission"))});
            LOGGER.log(Level.INFO, "PublicType:{0}", getType().equalsIgnoreCase("PUBLIC"));
        }
        if (player.hasPermission(ModWarp.PERMISSION) || getOwner().equals(player.getUniqueId())) return true; //If a player is admin OR is the owner
        else if (getBlackList().toList().contains(player.getUniqueId().toString())) return false; //instantly say no if the player is blacklisted. Type does not matter.
        else if (playerIsWhiteListed(player)) return true; //If the whitelist is enabled AND contains the player
        //else if (warpData.getString("type").equals("permission") && player.hasPermission(warpData.getString("access-permission"))) return true; //if permission type and player has permission
        else return (getType().equalsIgnoreCase("PUBLIC"));
 //Public unrestricted
    }
    
    /** Will return true or false depending on if a player can see a specified warp.
     * This will evaluate warp type, hidden state, permissions, admin permissions, 
     * ownership, and white lists.
     * @param player
     * @return 
     */
    public boolean playerCanSee(Player player){
        if (isHidden()){
            return (player.hasPermission(ModWarp.PERMISSION) || playerCanModify(player)) //if admin or can modify
                    || (getType().equals("private") && getWhiteList().toList().contains(player.getUniqueId())) //if private and whitelisted
                    || (getType().equals("permission") && player.hasPermission(getAccessPermission())); //if permission and they have the permission
//            if (player.hasPermission("warps.admin.viewall") || playerCanModify(player)) return true;
//            else if (getType().equals("private") && getWhiteList().toList().contains(player.getUniqueId())) return true; // If it is a private warp AND they are whitelisted
//            else if (getType().equals("permission") && player.hasPermission(getAccessPermission())) return true; //If it is a permission warp AND they have permission
//            else return !(!getOwner().equals(player.getUniqueId()) && !player.hasPermission("warps.admin.viewall")); //They just can't even
            //If they are the warp owner or an admin
            
        } else { //If it isn't hidden users can always see it
            return true;
        }
    }
    
    /** Returns true if a player has the permission to modify a warp.
     * @param player
     * @return 
     */
    public boolean playerCanModify(Player player){
        return player.hasPermission(ModWarp.PERMISSION) || getOwner().equals(player.getUniqueId());
//        if (player.hasPermission("jawacommands.warps.modify")) return true; //if player has universal modify command
//        else if (getOwner().equals(player.getUniqueId())) return true; //if player is warp owner
//        else return false;
    }
    
    public boolean canModify(CommandSender commandSender){
        if (commandSender instanceof Player) return playerCanModify((Player) commandSender);
        else return true;
    }
    
    public boolean canWhitelist(CommandSender commandSender){
        if (commandSender instanceof Player) return canWhitelist((Player) commandSender);
        else return true;
    }
    
    public boolean canWhitelist(Player player){
        return player.hasPermission(ModWarp.PERMISSION) || getOwner().equals(player.getUniqueId());
    }

//###############################################################################
// Set warp data
//###############################################################################
    /** Returns true if a warp type is valid and can be set.
     * @param type
     * @return 
     */
    public boolean setType(String type){
        if (WARPTYPES.contains(type.toLowerCase())){ // Added the toLowerCase to protect against admins who put don't understand case-sensativity
            warpData.put("type", type.toLowerCase()); // Xaihn im talking about you
            return true;
        } else {
            return false;
        }
    }
    
    public void setLocation(Location loc){
        warpData.put("location", LocationDataHandler.packLocation(loc));
        location = loc;
    }
    
    public boolean setAccessPermission(String permission) { //this will work even if there is no set or incorrect
        if (warpData.getString("type").equals("permission")) {
            warpData.put("access-permission", permission);
            return true;
        }
        else return false;
    }
    
    public boolean setOwner(UUID owner) {
        warpData.put("owner", owner.toString());
        return true;

    }
    
    public void whitelisted(boolean isWhiteListed){
        warpData.put("whitelisted", isWhiteListed);
    }
    
    public void setWhitelist(JSONArray whitelist){
        warpData.put("whitelist", whitelist);
        warpData.put("whitelisted", true);
    }
    
    public void addToWhiteList(UUID target){
        JSONArray whitelist = warpData.getJSONArray("whitelist");
        if (whitelist == null) {
            whitelist = new JSONArray(target.toString());
        } else {
            whitelist.put(target.toString());
        }
        warpData.put("whitelist", whitelist);
    }
    
    public void addToWhiteList(Set<UUID> targets){
        targets.forEach((target) -> {
            addToWhiteList(target);
        });
    }
    
    public void addToBlackList(UUID target) {
        JSONArray blacklist = warpData.getJSONArray("whitelist");
        if (blacklist == null) {
            blacklist = new JSONArray(target.toString());
        } else {
            blacklist.put(target.toString());
        }
        warpData.put("blacklist", blacklist);
    }
    
    public void addToBlackList(Set<UUID> targets){
        targets.forEach((target) -> {
            addToBlackList(target);
        });
    }
    
    public void setHiddenState(boolean hidden){
        warpData.put("hidden", hidden);
    }
    
    public void removeFromWhitelist(String UUID){
        JSONArray wl = warpData.getJSONArray("whitelist");
        for (int i = 0; i < wl.length() ; i++){
            if (wl.getString(i).equals(UUID)){
                wl.remove(i);
                break;
            }
        }
        warpData.put("whitelist", wl);
    }
    
    public void removeFromBlacklist(String UUID){
        JSONArray bl = warpData.getJSONArray("blacklist");
        for (int i = 0; i < bl.length() ; i++){
            if (bl.getString(i).equals(UUID)){
                bl.remove(i);
                break;
            }
        }
        warpData.put("blacklist", bl);
    }
    
//###############################################################################
// Player things
//###############################################################################
    
    public void sendPlayer(Player target){
        TPHandler.performSafeTeleport(target, location);
        
    }
    
    public boolean sendPlayer(Player target, boolean override){
        if (override || playerCanVisit(target)){
            TPHandler.performSafeTeleport(target, location);
            //target.teleport(location,PlayerTeleportEvent.TeleportCause.COMMAND);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean sendPlayerWithUnknownCause(Player target, boolean override){
        if (override || playerCanVisit(target)){
            TPHandler.performSafeTeleport(target, location, PlayerTeleportEvent.TeleportCause.UNKNOWN);
            //target.teleport(location,PlayerTeleportEvent.TeleportCause.COMMAND);
            return true;
        } else {
            return false;
        }
    }
}
