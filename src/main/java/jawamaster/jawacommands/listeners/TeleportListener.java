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

import jawamaster.jawacommands.handlers.LocationDataHandler;
import jawamaster.jawacommands.handlers.WorldHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 *
 * @author alexander
 */
public class TeleportListener implements Listener {

    @EventHandler
    public void onPlayerTeleportEvent(PlayerTeleportEvent teleportEvent) {
        Player player = teleportEvent.getPlayer();
        boolean isBackAllowed = WorldHandler.isAllowedInWorld(teleportEvent.getFrom().getWorld(), "back");
        
        // If player has permmission to do back
        if ((isBackAllowed && player.hasPermission("jawacommands.back")) || player.hasPermission("jawacommands.back.admin")){
            // Is teleport type valid
            if (teleportEvent.getCause() != PlayerTeleportEvent.TeleportCause.UNKNOWN) {
                //if yes commit location to stack (location handler)
                LocationDataHandler.addToBackStack(player, teleportEvent.getFrom());
            }
                //if no ignore
        } //else nothing happens
        
    }
}
