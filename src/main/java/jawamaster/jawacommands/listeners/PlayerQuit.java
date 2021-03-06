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

import jawamaster.jawacommands.JawaCommands;
import jawamaster.jawacommands.handlers.FreezeHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author alexander
 */
public class PlayerQuit implements Listener{
    @EventHandler
    public static void onPlayerQuit(PlayerQuitEvent event) {
        //TODO move this into the static BackHandler
        if (JawaCommands.backStack.containsKey(event.getPlayer().getUniqueId())){
            JawaCommands.backStack.remove(event.getPlayer().getUniqueId());
        }
        FreezeHandler.playerQuit(event.getPlayer());
    }
    
}
