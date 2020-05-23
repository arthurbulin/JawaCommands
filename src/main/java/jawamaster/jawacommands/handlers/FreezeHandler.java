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

import java.util.HashSet;
import java.util.UUID;
import net.jawasystems.jawacore.dataobjects.PlayerDataObject;
import org.bukkit.entity.Player;

/**
 *
 * @author alexander
 */
public class FreezeHandler {
    private static HashSet<UUID> frozenPlayers = new HashSet();
    
    /** Freezes a player by adding them to the frozenPlayers set as well as updating
     * their player data to keep data persistance.
     * @param player 
     */
    public static void freeze(PlayerDataObject player){
        frozenPlayers.add(player.getUniqueID());
        player.freeze();
    }
    
    /** Unfreezes a player by removing them from the frozenPlayers set and updating
     * their player data to keep data persistant.
     * @param player 
     */
    public static void thaw(PlayerDataObject player){
        frozenPlayers.remove(player.getUniqueID());
        player.thaw();
    }
    
    /** Remove a player from the frozenPlayer set on quit
     * @param player 
     */
    public static void playerQuit(Player player){
        if (frozenPlayers.contains(player.getUniqueId())){
            frozenPlayers.remove(player.getUniqueId());
        }
    }
    
    /** Add a frozen player to the frozenPlayer set on join.
     * @param target 
     */
    public static void playerJoin(PlayerDataObject target){
        if (target.isFrozen()) frozenPlayers.add(target.getUniqueID());
    }

    /** See if a player is frozen.
     * @param player
     * @return 
     */
    public static boolean isFrozen(Player player){
        return isFrozen(player.getUniqueId());
    }
    
    /** See if a player is frozen.
     * @param uuid
     * @return 
     */
    public static boolean isFrozen(UUID uuid){
        return frozenPlayers.contains(uuid);
    }
}
