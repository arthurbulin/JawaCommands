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

import jawamaster.jawacommands.commands.development.BackHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

/** This listener access if a user teleport event should be added to the back stack.
 * This allows a user teleport event to have the location saved in the /back stack.
 * @author Jawamaster (Arthur Bulin)
 */
public class TeleportListener implements Listener {

    @EventHandler
    public void onPlayerTeleportEvent(PlayerTeleportEvent event) {

        // If player has permmission to do back
        if ((BackHandler.backAllowedInWorld(event.getPlayer(), event.getFrom().getWorld()) && event.getPlayer().hasPermission(BackHandler.BACKPERMISSION)) || event.getPlayer().hasPermission(BackHandler.ADMINBACKPERMISSION)){
            // Ignore UNKNOWN and NETHER_PORTAL causes and exclude them from the back stack
            if (event.getCause() != PlayerTeleportEvent.TeleportCause.UNKNOWN && (event.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)) {
                //if yes commit location to stack (location handler)
                BackHandler.addUserBackLocation(event.getPlayer(), event.getFrom());
            }
        } 
        
    }
}
