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
import java.util.logging.Level;
import java.util.logging.Logger;
import jawamaster.jawacommands.JawaCommands;
import org.bukkit.World;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author alexander
 */

//TODO this whole thing is a mess
public class WorldHandler {
    
//    /** Checks the /plugin/worlds.json file to see if certain actions are allowed or not
//     * world is the world you are checking. Section is the action subsection. Item is what
//     * you are checking for. 
//     * @param world
//     * @param section
//     * @param item
//     * @return 
//     */
//    public static boolean isAllowedInWorld(World world, String section, String item){
//        // FIXME Build resolution to deal with worlds not already configured
//        JSONObject worldConfiguration = JawaCommands.getWorldConfiguration().getJSONObject(world.getName());
//        boolean isStrict = worldConfiguration.getBoolean("strict");
//        if (worldConfiguration.keySet().contains(section)) {
//            JSONObject worldSection = worldConfiguration.getJSONObject(section);
//
//            JSONArray allowed;
//            JSONArray denied;
//            if (worldSection.keySet().contains("allowed")) allowed = worldSection.getJSONArray("allowed");
//            else allowed = new JSONArray();
//            if (worldSection.keySet().contains("allowed")) denied = worldSection.getJSONArray("denied");
//            else denied = new JSONArray();
//
//
//            if (allowed.toList().contains(item)) return true; //If explicitly allowed True
//            else if (denied.toList().contains(item)) return false; //If explicitly not allowed False
//            else return !isStrict; //If not defined and world is strict False
//            //If behavior is not defined, and world is not strict be generally permissive True
//        } else { //If section has not been defined
//            return !isStrict;
//        }
//        
//    }
//    
//    public static boolean isAllowedInWorld(World world, String section){
//        JSONObject worldConfiguration = JawaCommands.getWorldConfiguration().getJSONObject(world.getName());
//        boolean isStrict = worldConfiguration.getBoolean("strict");
//        
//        if (worldConfiguration.keySet().contains(section)) {
//            
//            if (worldConfiguration.keySet().contains(section)) return worldConfiguration.getBoolean(section);
//            else return !isStrict; //If not defined and world is strict False
//            //If behavior is not defined, and world is not strict be generally permissive True
//            
//        } else { //If section has not been defined
//            return !isStrict;
//        }
//    }
//    
//    /** Returns if a world is configured or not.
//     * @param world
//     * @return 
//     */
//    public static boolean worldIsConfigured(World world){
//        JSONObject worldConfiguration = JawaCommands.getWorldConfiguration().getJSONObject(world.getName());
//        return worldConfiguration.getBoolean("configured");
//    }
//    
//    /** Returns true or false depending on if a world is strict.
//     * @param world
//     * @return 
//     */
//    public static boolean worldIsStrict(World world){
//        JSONObject worldConfiguration = JawaCommands.getWorldConfiguration().getJSONObject(world.getName());
//        return worldConfiguration.getBoolean("strict");
//    }
//    
//    public static JSONObject generateWorldConfigs(){
//        JSONObject worldsConfig = new JSONObject();
//        JSONObject worldConfig = new JSONObject();
//        worldConfig.put("strict", false);
//        worldConfig.put("configured", false);
//        worldConfig.put("back count", 10);
//        worldConfig.put("back-allowed", false);
//        JawaCommands.getPlugin().getServer().getWorlds().forEach((world) -> {
//            worldsConfig.put(world.getName(), worldConfig);
//        });
//        return worldsConfig;
//    }
//    
//    public static JSONObject LoadWorldConfigs(){
//        JSONObject worldConfigs = JSONHandler.LoadJSONConfig(JawaCommands.getPlugin(), "/worlds.json");
//        if (worldConfigs == null){
//            worldConfigs = generateWorldConfigs();
//            JSONHandler.WriteJSONToFile(JawaCommands.getPlugin(),"/worlds.json", worldConfigs);
//        }
//        
//        System.out.println("[JawaCommands][WorldHandler] " + worldConfigs.length() + " worlds have been configured and loaded for configuration management");
//        return worldConfigs;
//        
//    }
//    
//    public static int getConfigNumber(World world, String section){
//        JSONObject worldConfiguration = JawaCommands.getWorldConfiguration().getJSONObject(world.getName());
//        if (worldConfiguration.keySet().contains(world.getName()) && worldConfiguration.getJSONObject(world.getName()).keySet().contains(section)){
//            return worldConfiguration.getJSONObject(world.getName()).getInt(section);
//        } else return -1;
//    }
    
    /** Loads custom spawn points for the worlds and returns them as a HashMap<String,JSONObject>.
     * If the worldspawns.txt doesn't exist or is empty it will return an empty map.
     * @return 
     */
    public static JSONObject loadWorldSpawns(){
        JSONObject spawnJSON = new JSONObject();
        File worldspawns = new File(JawaCommands.getPlugin().getDataFolder() + "/worldspawns.json");

        if (worldspawns.exists()){ //Load the jsonobject
            spawnJSON = JSONHandler.LoadJSONConfig(JawaCommands.getPlugin(),"/worldspawns.json");
            if (spawnJSON == null) { //verify that the JSONHandler could load the data in the file.
                Logger.getLogger(JSONHandler.class.getName()).log(Level.INFO, "Unable to load custom spawn points even though a file exists, it may just be empty.");
                return spawnJSON;
            }
            Logger.getLogger(JSONHandler.class.getName()).log(Level.INFO, "Loading custom spawn points.");
            return spawnJSON;
            
        } else { //No file exists, this is ok
            Logger.getLogger(JSONHandler.class.getName()).log(Level.INFO, "No custom spawn points exist.");
            return spawnJSON;
        }
    }
}
