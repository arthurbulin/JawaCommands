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
package jawamaster.jawacommands.listeners;

import java.util.logging.Level;
import java.util.logging.Logger;
import jawamaster.jawacommands.JawaCommands;
import jawamaster.jawacommands.handlers.WorldHandler;
import net.jawasystems.jawacore.PlayerManager;
import net.jawasystems.jawacore.dataobjects.PlayerDataObject;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

/**
 *
 * @author alexander
 */
public class FirstSpawnListener implements Listener {
    private static final Logger LOGGER = Logger.getLogger("onPlayerSpawnLocationEvent");
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerSpawnLocationEvent(PlayerSpawnLocationEvent event) {
        Player player = event.getPlayer();
        PlayerDataObject pdObject = PlayerManager.getPlayerDataObject(player);
        World world = event.getPlayer().getWorld();
        

        if (JawaCommands.isDebug()){
            LOGGER.log(Level.INFO, "groupHasGlobalSpawn:{0} worldHasGroupSpawn:{1} worldspawn:{2} isNewPlayer:{3} rank:{4} distance:{5}", 
                    new Object[]{WorldHandler.groupHasGlobalSpawn(pdObject.getRank()), WorldHandler.worldHasGroupSpawn(world.getName(), pdObject.getRank()),event.getSpawnLocation().equals(world.getSpawnLocation()),player.hasPlayedBefore(),pdObject.getRank(),event.getSpawnLocation().distance(world.getSpawnLocation())< 12});
                    LOGGER.log(Level.INFO, "default spawn location:{0} player spawn location:{1}", new Object[]{world.getSpawnLocation(), event.getSpawnLocation()});

        }
        //player, world, bed, custom
        //System.out.println("onPlayerSpawnLocationEvent Player: " + event.getPlayer().getName() 
         //       + ","+world.getName() 
           //     + ","+ player.getBedSpawnLocation()
             //   + "," + JawaCommands.worldSpawns.has(world.getName()));
        
        //If player is new and there is an applicaple place in the world
//        if (!player.hasPlayedBefore() && JawaCommands.worldSpawns.has(world.getName())) {
        //If world has spawn set AND if worldspawn has one named after their rank, send them to the one named after their rank
        //if (WorldHandler.worldSpawns.has(world.getName()) && WorldHandler.worldSpawns.getJSONObject(world.getName()).has(pdObject.getRank()) && event.getSpawnLocation().distance(world.getSpawnLocation()) < 25) {
        if (WorldHandler.groupHasGlobalSpawn(pdObject.getRank()) && event.getSpawnLocation().distance(world.getSpawnLocation()) < 12) {
            event.setSpawnLocation(WorldHandler.getGlobalSpawn(pdObject.getRank()));
        } else if (WorldHandler.worldHasGroupSpawn(event.getSpawnLocation().getWorld().getName(), pdObject.getRank())) {
            event.setSpawnLocation(WorldHandler.getWorldSpawn(event.getSpawnLocation().getWorld().getName(), pdObject.getRank()));
        }
    }
        } //else ignore event
