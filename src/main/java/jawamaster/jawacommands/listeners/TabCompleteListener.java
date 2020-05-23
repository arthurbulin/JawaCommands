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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.jawasystems.jawacore.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

/**
 *
 * @author alexander
 */
public class TabCompleteListener implements Listener {
    
    @EventHandler
    public void onTabCompleteEvent(TabCompleteEvent event){
        event.getBuffer();
        if (event.getBuffer().matches("/home\\s.*")) {
            
            List<String> complete = Arrays.asList("delete", "replace", "set", "info", "list", "help");
            List<String> homes = PlayerManager.getPlayerDataObject((Player) event.getSender()).getHomeList();
            
            if (event.getBuffer().matches("/home\\s")) {
                homes.addAll(complete);
                event.setCompletions(homes);
            } else if (event.getBuffer().matches("/home\\s(delete|replace|info)\\s")) {
                event.setCompletions(new ArrayList<>(homes));
            } else if (event.getBuffer().matches("/home\\s(set|help|list)\\s")) {
                event.setCancelled(true);
            }
        }
        
    }
    
}
