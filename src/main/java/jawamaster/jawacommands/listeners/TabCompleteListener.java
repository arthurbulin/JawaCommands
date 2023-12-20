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
import java.util.LinkedList;
import java.util.List;
import jawamaster.jawacommands.commands.playeraugmentation.PlayerTime;
import jawamaster.jawacommands.commands.playeraugmentation.PlayerWeather;
import jawamaster.jawacommands.commands.home.Home;
import jawamaster.jawacommands.commands.warps.ModWarp;
import jawamaster.jawacommands.handlers.WarpHandler;
import jawamaster.jawacommands.kit.KitCommand;
import net.jawasystems.jawacore.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.util.StringUtil;

/**
 *
 * @author alexander
 */
public class TabCompleteListener implements Listener {

    @EventHandler
    public void onTabCompleteEvent(TabCompleteEvent event) {
        event.getBuffer();

        //For the home command
        if (event.getBuffer().matches("/home\\s.*")) {
            //System.out.println("tab event entry");
            List<String> homes = PlayerManager.getPlayerDataObject((Player) event.getSender()).getHomeList();

            String buffer;
            List<String> completes = new ArrayList();
            if (event.getBuffer().matches("/home\\s" + "(" + String.join("|", Home.SECONDS) + String.join("|", Home.NOSECONDS) + ").*")) {
                buffer = rebuildBuffer(event.getBuffer(), "/home\\s" + "(" + String.join("|", Home.SECONDS) + String.join("|", Home.NOSECONDS) + String.join("|", homes) + ")\\s");
                completes.addAll(homes);
                completes.addAll(Home.SECONDS);
                completes.addAll(Home.NOSECONDS);
            } else if (event.getBuffer().matches("/home\\s" + "(" + String.join("|", Home.SECONDS) + ")\\s.*")) {
                buffer = rebuildBuffer(event.getBuffer(), "/home\\s" + "(" + String.join("|", Home.SECONDS) + ")\\s");
                completes.addAll(homes);
            } else if (event.getBuffer().matches("/home\\s" + "(" + String.join("|", Home.NOSECONDS) + ")\\s.*")) {
                buffer = rebuildBuffer(event.getBuffer(), "/home\\s" + "(" + String.join("|", Home.NOSECONDS) + ")\\s");
            }else {
                buffer = rebuildBuffer(event.getBuffer(), "/home\\s");
                completes.addAll(homes);
                completes.addAll(Home.SECONDS);
                completes.addAll(Home.NOSECONDS);
            }
            //FIXME With this below do I even need the stuff above?
            event.setCompletions(StringUtil.copyPartialMatches(buffer, completes, new ArrayList()));

        } //For the ptime command
        else if (event.getBuffer().matches("/ptime\\s.*")) {
            //event.setCompletions(PlayerTime.TABCOMPLETES);
            String buffer;
            List<String> completes;
            if (event.getBuffer().matches("/ptime\\s(morning|noon|sunset|night|midnight|sunrise|reset)\\s.*")) {
                buffer = rebuildBuffer(event.getBuffer(), "/ptime\\s(morning|noon|sunset|night|midnight|sunrise|reset)\\s");
                completes = getOnlinePlayers();
            } else {
                buffer = rebuildBuffer(event.getBuffer(), "/ptime\\s");
                completes = PlayerTime.TABCOMPLETES;
            }

            event.setCompletions(StringUtil.copyPartialMatches(buffer, completes, new ArrayList()));
        } //For the pweather command
        else if (event.getBuffer().matches("/pweather\\s.*")) {
            String buffer;
            List<String> completes;
            if (event.getBuffer().matches("/pweather\\s(clear|rain|reset)\\s.*")) {
                buffer = rebuildBuffer(event.getBuffer(), "/pweather\\s(clear|rain|reset)\\s");
                completes = getOnlinePlayers();
            } else {
                buffer = rebuildBuffer(event.getBuffer(), "/pweather\\s");
                completes = PlayerWeather.TABCOMPLETES;
            }

            event.setCompletions(StringUtil.copyPartialMatches(buffer, completes, new ArrayList()));
        } 
        
        else if (event.getBuffer().matches("/modwarp\\s.*") && event.getSender().hasPermission(ModWarp.PERMISSION)) {
            /*
            /modwarp <warpname> <[whitelist|hidden|location|owner|type]> <[owner|type]>
             */
            if (event.getBuffer().matches("(/modwarp\\s|/modwarp\\s[a-zA-Z0-9]+)")) {
                event.setCompletions(StringUtil.copyPartialMatches(rebuildBuffer(event.getBuffer(), "/modwarp\\s"), WarpHandler.getWarpNames(), new ArrayList()));
            } else if (event.getBuffer().matches("/modwarp\\s" + "(" + String.join("|", WarpHandler.getWarpNames()) + ")\\s.*?$")) {
                event.setCompletions(StringUtil.copyPartialMatches(rebuildBuffer(event.getBuffer(), "/modwarp\\s\\b[a-zA-Z0-9]+\\b\\s"), new ArrayList(Arrays.asList("hidden", "whitelist", "location", "owner", "type")), new ArrayList()));
                if (event.getBuffer().matches("/modwarp\\s" + "(" + String.join("|", WarpHandler.getWarpNames()) + ")\\sowner\\s.*?")) {
                    event.setCompletions(StringUtil.copyPartialMatches(rebuildBuffer(event.getBuffer(), "/modwarp\\s" + "(" + String.join("|", WarpHandler.getWarpNames()) + ")\\sowner\\s"), PlayerManager.getOnlinePlayerList(), new ArrayList()));
                } else if (event.getBuffer().matches("/modwarp\\s" + "(" + String.join("|", WarpHandler.getWarpNames()) + ")\\stype\\s.*?")) {
                    event.setCompletions(StringUtil.copyPartialMatches(rebuildBuffer(event.getBuffer(), "/modwarp\\s" + "(" + String.join("|", WarpHandler.getWarpNames()) + ")\\stype\\s"), new ArrayList(Arrays.asList("public", "private", "permission", "game")), new ArrayList()));
                } 
            }
        } 
        
        else if (event.getBuffer().matches("/warp\\s.*")){
            event.setCompletions(StringUtil.copyPartialMatches(rebuildBuffer(event.getBuffer(), "/warp\\s"), WarpHandler.getVisibleWarpNames((Player) event.getSender()), new ArrayList()));
        } 
        
        else if (event.getBuffer().matches("/makewarp\\s.*?")) {
            event.setCompletions(new ArrayList(Arrays.asList("<[warpname]>")));
            if (event.getBuffer().matches("/makewarp\\s.*?\\s.*"))
            event.setCompletions(StringUtil.copyPartialMatches(rebuildBuffer(event.getBuffer(), "/makewarp\\s.*?\\s"), new ArrayList(Arrays.asList("private","public","game","permission")), new ArrayList()));
        } 
        
        else if (event.getBuffer().matches("/wlwarp\\s.*")) {
            event.setCompletions(StringUtil.copyPartialMatches(rebuildBuffer(event.getBuffer(), "/wlwarp\\s"), WarpHandler.getVisibleWarpNames((Player) event.getSender()), new ArrayList()));
            if (event.getBuffer().matches("/wlwarp\\s.*?\\s.*?")) {
                event.setCompletions(StringUtil.copyPartialMatches(rebuildBuffer(event.getBuffer(), "/wlwarp\\s.*?\\s"), new ArrayList(Arrays.asList("add", "remove")), new ArrayList()));
                if (event.getBuffer().matches("/wlwarp\\s.*?\\s(add|remove)\\s.*?")) {
                    event.setCompletions(StringUtil.copyPartialMatches(rebuildBuffer(event.getBuffer(), "/wlwarp\\s.*?\\s(add|remove)\\s"), PlayerManager.getOnlinePlayerList(), new ArrayList()));
                }
            }
        }
        
        else if (event.getBuffer().matches("/flyspeed\\s.*")) {
            event.setCompletions(StringUtil.copyPartialMatches(rebuildBuffer(event.getBuffer(), "/flyspeed\\s"), new LinkedList(Arrays.asList("1","2","3","4","6","8","10")), new ArrayList()));
        }
        
        else if (event.getBuffer().matches("/walkspeed\\s.*")) {
            event.setCompletions(StringUtil.copyPartialMatches(rebuildBuffer(event.getBuffer(), "/walkspeed\\s"), new LinkedList(Arrays.asList("2","3","4","5","6","7","8","9","10")), new ArrayList()));
        }
        
        else if (event.getBuffer().matches("/kit\\s.*")) {
            event.setCompletions(StringUtil.copyPartialMatches(rebuildBuffer(event.getBuffer(), "/kit\\s"), KitCommand.tabs, new ArrayList()));
            if (event.getBuffer().matches("/kit\\s.*?\\s.*?")){
                event.setCompletions(StringUtil.copyPartialMatches(rebuildBuffer(event.getBuffer(), "/kit\\s*\\s*\\s"), KitCommand.tabsTwo, new ArrayList()));
            }
        }


    }

    private String rebuildBuffer(String buffer, String regex) {
        return buffer.replaceFirst(regex, "");
    }

    private List<String> getOnlinePlayers() {
        List<String> completes = new ArrayList();
        Bukkit.getOnlinePlayers().forEach((player) -> {
            completes.add(player.getName());
        });

        return completes;
    }

}
