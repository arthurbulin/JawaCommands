/*
 * Copyright (C) 2020 alexander
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
package jawamaster.jawacommands.commands.development;

import jawamaster.jawacommands.JawaCommands;
import jawamaster.jawacommands.handlers.TPHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.json.JSONArray;

/**
 *
 * @author alexander
 */
public class BackHandler {
    public static final String BACKPERMISSION = "jawacommands.back";
    public static final String ADMINBACKPERMISSION = "jawacommands.back.admin";
    /** Add a location to the user's back stack.
     * @param target
     * @param loc 
     */
    public static void addUserBackLocation(Player target, Location loc){
        if (JawaCommands.backStack.containsKey(target.getUniqueId())){
            JawaCommands.backStack.get(target.getUniqueId()).put(loc);
        } else {
            JSONArray newStack = new JSONArray();
            newStack.put(loc);
            JawaCommands.backStack.put(target.getUniqueId(), newStack);
        }
    }
    
    /** Remove a location from ther user's back stack.
     * @param target 
     */
    public static void removeUserBackLocation(Player target) {
        if (JawaCommands.backStack.get(target.getUniqueId()).length() == 1) {
            JawaCommands.backStack.remove(target.getUniqueId());

        } else {
            JawaCommands.backStack.get(target.getUniqueId()).remove(JawaCommands.backStack.get(target.getUniqueId()).length() - 1);
        }
    }
    
    /** Get a back location from the top of the stack.
     * @param target
     * @return 
     */
    public static Location getUserBackLocation(Player target){
        if (JawaCommands.backStack.containsKey(target.getUniqueId())){
            return (Location) JawaCommands.backStack.get(target.getUniqueId()).get(JawaCommands.backStack.get(target.getUniqueId()).length()-1);
        } else {
            return null;
        }
    }
    
    /** Returns true if /back is allowed in a world for users. This checks the permission
     * of the format 'jawacommands.back.WORLD' and if the user has that permission it
     * returns true.
     * @return 
     */
    public static boolean backAllowedInWorld(Player target, World world){
        if (target.hasPermission(BACKPERMISSION + "." + world.getName())){
            return true;
        } else return false;
    }
    
    /** Sends a player to their top back location using safe freezing teleportation.
     * @param target
     * @param topBack 
     */
    public static void sendBack(Player target, Location topBack){
        if (topBack.getWorld().getBlockAt(topBack.subtract(0, 1, 0)).getType().equals(Material.AIR)) {
            topBack.setY(topBack.getWorld().getHighestBlockYAt(topBack) + 2);
        }
        
        TPHandler.performSafeTeleport(target, topBack, PlayerTeleportEvent.TeleportCause.UNKNOWN);
        BackHandler.removeUserBackLocation(target);
    }
    
    
    
}
