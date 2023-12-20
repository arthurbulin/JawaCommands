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

import net.jawasystems.jawacore.handlers.JSONHandler;
import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import jawamaster.jawacommands.JawaCommands;
import net.jawasystems.jawacore.handlers.LocationDataHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.json.JSONObject;

/** Controls world features such as world spawn.
 *
 * @author alexander
 */

public class WorldHandler {

    private static final Logger LOGGER = Logger.getLogger("WorldHandler");
    
    private static final HashMap<String, Location> GLOBALSPAWNS = new HashMap();
    private static final HashMap<String, HashMap<String,Location>> WORLDSPAWNS = new HashMap();
   
    /** *  Loads custom spawn points for the worlds and stores them in local hashmaps.
     * If the worldspawns.txt doesn't exist or is empty it will do nothing. 
     */
    public static void loadWorldSpawns(){
        File worldspawns = new File(JawaCommands.getPlugin().getDataFolder() + "/worldspawns.json");

        if (worldspawns.exists()){ //Load the jsonobject
            JSONObject spawnJSON = JSONHandler.LoadJSONConfig(JawaCommands.getPlugin(), "/worldspawns.json");
            if (spawnJSON == null) { //verify that the JSONHandler could load the data in the file.
                LOGGER.log(Level.INFO, "Unable to load custom spawn points even though a file exists, it may just be empty.");
            } else {
                LOGGER.log(Level.INFO, "Loading custom spawn points.");
                for (String key : spawnJSON.keySet()) {
                    if (key.equals("GLOBAL")) { //key is global
                        for (String group : spawnJSON.getJSONObject(key).keySet()){
                            JSONObject tmpJSON = spawnJSON.getJSONObject(key).getJSONObject(group);
                            GLOBALSPAWNS.put(group, new Location(Bukkit.getWorld(tmpJSON.getString("world")), tmpJSON.getDouble("X"), tmpJSON.getDouble("Y"), tmpJSON.getDouble("Z"), 0, 0));
                        }
                    } else { //Key is world
                        HashMap<String,Location> loadedWorldSpawns = new HashMap();
                        for (String group : spawnJSON.getJSONObject(key).keySet()) {
                            JSONObject tmpJSON = spawnJSON.getJSONObject(key).getJSONObject(group);
                            loadedWorldSpawns.put(group, new Location(Bukkit.getWorld(tmpJSON.getString("world")), tmpJSON.getDouble("X"), tmpJSON.getDouble("Y"), tmpJSON.getDouble("Z"), 0, 0));
                        }
                        WORLDSPAWNS.put(key, loadedWorldSpawns);
                        LOGGER.log(Level.INFO, "{0} world spawns loaded for {1}", new Object[]{WORLDSPAWNS.size(), key});
                    }
                }
                LOGGER.log(Level.INFO, "{0} global spawns loaded", GLOBALSPAWNS.size());
                LOGGER.log(Level.INFO, "{0} total worlds have world spawns", WORLDSPAWNS.size());
            }
            
        } else { //No file exists, this is ok
            LOGGER.log(Level.INFO, "No custom spawn points exist.");
        }
    }
    
    /** Add a group spawn to a world.
     * @param group The group/rank that will spawn here
     * @param spawn The location of the spawn
     */
    public static void addWorldSpawn(String group, Location spawn){
        if (WORLDSPAWNS.containsKey(spawn.getWorld().getName())) {
            WORLDSPAWNS.get(spawn.getWorld().getName()).put(group, spawn) ;
        } else {
            HashMap<String,Location> tmp = new HashMap();
            tmp.put(group, spawn);
            WORLDSPAWNS.put(spawn.getWorld().getName(), tmp);
        }
        saveWorldSpawns();
    }
    
    /** Returns true if the list of worldspawns contains a the world worldName.
     * @param worldName The string name of the world
     * @return 
     */
    public static boolean worldHasSpawnsDefined(String worldName){
        return WORLDSPAWNS.containsKey(worldName);
    }
    
    /** Returns true if a specific world, worldName, has a spawn for group
     * @param worldName World being checked
     * @param group Group being checked
     * @return 
     */
    public static boolean worldHasGroupSpawn(String worldName, String group){
        return WORLDSPAWNS.containsKey(worldName) && WORLDSPAWNS.get(worldName).containsKey(group);
    }
    
    /** Returns true if a specific group, group, has a global spawn set
     * @param group The group being tested
     * @return 
     */
    public static boolean groupHasGlobalSpawn(String group){
        return GLOBALSPAWNS.containsKey(group);
    }
    
    /** Removes the global spawn of group and saves a copy of the current spawns
     * @param group Group who's global spawn is being removed
     */
    public static void removeGlobalSpawn(String group){
        GLOBALSPAWNS.remove(group);
        saveWorldSpawns();
    }
    
    /** Removes the world spawn of group from world and saves a copy of the current spawns.
     * This assumes the world has checked to exist using worldHasSpawnsDefined(String worldName)
     * @param worldName The world from which the group spawn needs removed
     * @param group The group being removed
     */
    public static void removeWorldSpawn(String worldName, String group){
        WORLDSPAWNS.get(worldName).remove(group);
        if (WORLDSPAWNS.get(worldName).isEmpty()) {
            WORLDSPAWNS.remove(worldName);
        }
        saveWorldSpawns();
    }
    
    /** Adds a group spawn to the global list.
     * @param group Group to add a spawn for
     * @param spawn The location of the spawn
     */
    public static void addGlobalSpawn(String group, Location spawn){
        GLOBALSPAWNS.put(group, spawn);
        saveWorldSpawns();
    }
    
    /** Get the location of a global spawn. this assumes existence has been checked
     * before calling or this may return null.
     * @param group The group global spawn
     * @return the location object of the group global spawn
     */
    public static Location getGlobalSpawn(String group){
        return GLOBALSPAWNS.get(group);
    }
    
    /** Get the location object of a group spawn in a specific world.
     *  This assumed existence has already been checked or this may return null
     * @param world The string name of the world being requested
     * @param group The group spawn that is being retrieved
     * @return the location where that group should spawn if no other viable location exists
     */
    public static Location getWorldSpawn(String world, String group){
        return WORLDSPAWNS.get(world).get(group);
    }
    
    /** Commit the hashmaps to JSONObjects and save them in a file.
     */
    private static void saveWorldSpawns(){
        JSONObject spawns = new JSONObject();
        for (String world : WORLDSPAWNS.keySet()){
            JSONObject tmpGroup = new JSONObject();
            for (String group : WORLDSPAWNS.get(world).keySet()){
                tmpGroup.put(group, LocationDataHandler.packLocation(WORLDSPAWNS.get(world).get(group)));
            }
            spawns.put(world, tmpGroup);
        }
        
        JSONObject tmpGroup = new JSONObject();
        for (String group : GLOBALSPAWNS.keySet()){
            tmpGroup.put(group, LocationDataHandler.packLocation(GLOBALSPAWNS.get(group)));
        }
        if (!tmpGroup.isEmpty()) spawns.put("GLOBAL", tmpGroup);
            
//        spawns.put("GLOBAL", new JSONObject(GLOBALSPAWNS));
        JSONHandler.WriteJSONToFile(JawaCommands.getPlugin(), "/worldspawns.json", spawns);
    }
    
    /** Send a list of spawns to the indicated player.
     * TODO this should later be wrapped up in an async event that sync to send the messages so that the parsing isn't in main
     * @param player The player who should receive the list
     */
    public static void listSpawns(Player player){
        if (!WORLDSPAWNS.isEmpty()) {
            BaseComponent[] msg = new ComponentBuilder()
                    .append("> World group spawns:")
                    .color(ChatColor.GREEN)
                    .create();

            player.spigot().sendMessage(msg);

            for (String world : WORLDSPAWNS.keySet()) {
                player.sendMessage(ChatColor.GREEN + "> Spawns for " + ChatColor.GOLD + world);
                for (String group : WORLDSPAWNS.get(world).keySet()) {
                    player.sendMessage(ChatColor.GREEN + " > " + group + " " + ChatColor.GREEN + WORLDSPAWNS.get(world).get(group).getBlockX() + "," + WORLDSPAWNS.get(world).get(group).getBlockY() + "," + WORLDSPAWNS.get(world).get(group).getBlockZ());
                }
            }
        }

        if (!GLOBALSPAWNS.isEmpty()) {
            BaseComponent[] globalMSG = new ComponentBuilder()
                    .append("> Global groups spawns:")
                    .color(ChatColor.GREEN)
                    .create();

            player.spigot().sendMessage(globalMSG);

            for (String group : GLOBALSPAWNS.keySet()) {
                player.sendMessage(ChatColor.GREEN + " > " + group + " " + ChatColor.GREEN + GLOBALSPAWNS.get(group).getBlockX() + "," + GLOBALSPAWNS.get(group).getBlockY() + "," + GLOBALSPAWNS.get(group).getBlockZ());
            }
        }
        
    }
}
