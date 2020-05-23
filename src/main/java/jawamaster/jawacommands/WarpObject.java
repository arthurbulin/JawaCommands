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
import jawamaster.jawacommands.handlers.TPHandler;
import net.jawasystems.jawacore.handlers.LocationDataHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author alexander
 */
public class WarpObject {
    private JSONObject warpData;
    private String warpName;
    private Location location;
    private final Set WARPTYPES = new HashSet(Arrays.asList("public", "private", "permission"));
    
    public WarpObject(String warpName, JSONObject warpData){
        this.warpData = warpData;
        this.warpName = warpName;
        location = LocationDataHandler.unpackLocation(warpData.getJSONObject("location"));
    }
    
    public WarpObject(String warpName, String type, UUID createdBy){
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
        }
        
    }

//###############################################################################
// Get WarpObject data
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
        if (warpData.getBoolean("whitelisted")) return warpData.getJSONArray("whitelist");
        else return null;
    }
    
    public JSONArray getBlackList(){
        return warpData.getJSONArray("blacklist");
    }
    
    public JSONObject getWarpData(){
        return warpData;
    }
    
    public boolean isHidden(){
        return warpData.getBoolean("hidden");
    }
    
    public boolean isWhiteListed(){
        return warpData.getBoolean("whitelisted");
    }
    
    public boolean playerIsWhiteListed(Player player){
        if (!isWhiteListed()){
            return getWhiteList().toList().contains(player.getUniqueId().toString());
        } else {
            return false;
        }
    }
    
    public boolean playerCanVisit(Player player){
        //On all
            //restrict if on blacklist
            //Allow if on whitelist
            //Allow if admin
            //Allow if owner
        //if permission
            //Allow if permission

        
        if (player.hasPermission("warps.admin.visitall") || getOwner().equals(player.getUniqueId())) return true; //If a player is admin OR is the owner
        else if (getBlackList().toList().contains(player.getUniqueId())) return false; //instantly say no if the player is blacklisted. Type does not matter.
        else if (isWhiteListed() && getWhiteList().toList().contains(player.getUniqueId())) return true; //If the whitelist is enabled AND contains the player
        else if (warpData.getString("type").equals("permission") && player.hasPermission(warpData.getString("access-permission"))) return true; //if permission type and player has permission
        else return true; //Public unrestricted
    }
    
    
    public boolean playerCanSee(Player player){
        if (isHidden()){
            if (getType().equals("private") && getWhiteList().toList().contains(player.getUniqueId())) return true; // If it is a private warp AND they are whitelisted
            else if (getType().equals("permission") && player.hasPermission(getAccessPermission())) return true; //If it is a permission warp AND they have permission
            else return !(!getOwner().equals(player.getUniqueId()) && !player.hasPermission("warps.admin.viewall")); //They just can't even
            //If they are the warp owner or an admin
            
        } else { //If it isn't hidden users can always see it
            return true;
        }
    }
    
    public boolean playerCanModify(Player player){
        if (player.hasPermission("warps.admin.modify")) return true; //if player has universal modify command
        else if (getOwner().equals(player.getUniqueId())) return true; //if player is warp owner
        else return false;
    }

//###############################################################################
// Set warp data
//###############################################################################
    
    public boolean setType(String type){
        if (WARPTYPES.contains(type)){
            warpData.put("type", type);
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
    
    public boolean sendPlayer(Player target){
        return sendPlayer(target, false);
        
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
}
