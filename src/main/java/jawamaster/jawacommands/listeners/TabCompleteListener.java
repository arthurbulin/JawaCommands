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
import java.util.List;
import jawamaster.jawacommands.commands.PlayerTime;
import jawamaster.jawacommands.commands.PlayerWeather;
import jawamaster.jawacommands.commands.home.Home;
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
            System.out.println("tab event entry");
            List<String> complete = Home.TABCOMPLETES;
            List<String> homes = PlayerManager.getPlayerDataObject((Player) event.getSender()).getHomeList();

            String buffer;
            List<String> completes = new ArrayList();
            if (event.getBuffer().matches("/home\\s" + "(" + String.join("|", Home.SECONDS) + String.join("|", Home.NOSECONDS) + ").*")) {
                System.out.println("tab event entry regex 1");
                buffer = rebuildBuffer(event.getBuffer(), "/home\\s" + "(" + String.join("|", Home.SECONDS) + String.join("|", Home.NOSECONDS) + String.join("|", homes) + ")\\s");
                completes.addAll(homes);
                completes.addAll(Home.SECONDS);
                completes.addAll(Home.NOSECONDS);
            } else if (event.getBuffer().matches("/home\\s" + "(" + String.join("|", Home.SECONDS) + ")\\s.*")) {
                System.out.println("tab event entry regex 2");
                buffer = rebuildBuffer(event.getBuffer(), "/home\\s" + "(" + String.join("|", Home.SECONDS) + ")\\s");
                completes.addAll(homes);
            } else if (event.getBuffer().matches("/home\\s" + "(" + String.join("|", Home.NOSECONDS) + ")\\s.*")) {
                System.out.println("tab event entry regex 3");
                buffer = rebuildBuffer(event.getBuffer(), "/home\\s" + "(" + String.join("|", Home.NOSECONDS) + ")\\s");
            }else {
                buffer = rebuildBuffer(event.getBuffer(), "/home\\s");
                System.out.println("tab event entry regex 4");
                completes.addAll(homes);
                completes.addAll(Home.SECONDS);
                completes.addAll(Home.NOSECONDS);
            }
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
