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
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 *
 * @author alexander
 */
public class SpawnListener implements Listener {

    private static final Logger LOGGER = Logger.getLogger("onPayerRespawnEvent");

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPayerRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        PlayerDataObject pdObject = PlayerManager.getPlayerDataObject(player);
        World world = event.getPlayer().getWorld();

        //"player, world, bed, customspawn"
        //System.out.println("onPlayerRespawnEvent." + event.getPlayer().getName() + ","+event.getPlayer().getWorld() + ","+ player.getBedSpawnLocation() + "," + JawaCommands.worldSpawns.has(world.getName()));
        if (JawaCommands.isDebug()) {
            LOGGER.log(Level.INFO, "bed:{0} anchor:{1} worldspawn:{2} groupHasGlobal:{3} groupHasThisWorld:{4}",
                    new Object[]{event.isBedSpawn(), event.isAnchorSpawn(), event.getRespawnLocation().equals(world.getSpawnLocation()), WorldHandler.groupHasGlobalSpawn(pdObject.getRank()), WorldHandler.worldHasGroupSpawn(world.getName(), pdObject.getRank())});
        }
        //If player doesn't have a bed location and there is an applicaple place in the world
        if ( //Don't interrupt bed spawns
                !event.isBedSpawn()
                //Don't interrupt spawn anchors
                && !event.isAnchorSpawn()
                //Interrupt default world spawns
                && event.getRespawnLocation().distance(world.getSpawnLocation()) < 3) {
            if (WorldHandler.groupHasGlobalSpawn(pdObject.getRank())) {
                event.setRespawnLocation(WorldHandler.getGlobalSpawn(pdObject.getRank()));
            } else if (WorldHandler.worldHasGroupSpawn(event.getRespawnLocation().getWorld().getName(), pdObject.getRank())) {
                event.setRespawnLocation(WorldHandler.getWorldSpawn(event.getRespawnLocation().getWorld().getName(), pdObject.getRank()));
            }
        }
    } //else ignore event

}
