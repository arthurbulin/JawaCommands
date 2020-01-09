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

import jawamaster.jawacommands.handlers.HomeHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

/**
 *
 * @author alexander
 */
public class BedListener implements Listener{
    @EventHandler
    public void onPlayerTeleportEvent(PlayerBedEnterEvent event) {
        if (event.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.OK) && event.getPlayer().hasPermission("jawapermissions.home.bed")){
            HomeHandler.addHome(event.getPlayer(), "bed", true);
        }
        
    }
}
