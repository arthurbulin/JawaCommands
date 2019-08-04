/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package jawamaster.jawacommands.handlers;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 *
 * @author Arthur Bulin
 */
public class DataHandler {

    
    public static Map<String, Object> disassembleLocation(Location location){
        
        Map<String, Object> homeLoc = new HashMap();
        
        homeLoc.put("world", location.getWorld().getName());
        homeLoc.put("x", location.getX());
        homeLoc.put("y", location.getY());
        homeLoc.put("z", location.getZ());
        homeLoc.put("yaw", location.getYaw());
        homeLoc.put("pitch", location.getPitch());
        
        return homeLoc;
    }
    
    public static Location assembleLocation(Map<String, Object> location){
        
        return new Location(Bukkit.getWorld((String) location.get("world")), (double) location.get("x"), (double) location.get("y"), (double) location.get("z"), (float) ((double) location.get("yaw")), (float) ((double) location.get("pitch")));
    }
    
}
