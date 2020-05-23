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
package jawamaster.jawacommands.listeners;

import jawamaster.jawacommands.handlers.FreezeHandler;
import jawamaster.jawacommands.handlers.TPHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 *
 * @author alexander
 */
public class PlayerMovementListener implements Listener {

    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent event) {
        if (FreezeHandler.isFrozen(event.getPlayer()) || TPHandler.safeTP(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}

