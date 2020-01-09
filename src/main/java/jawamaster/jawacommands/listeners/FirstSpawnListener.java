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

import jawamaster.jawacommands.JawaCommands;
import jawamaster.jawacommands.handlers.LocationDataHandler;
import jawamaster.jawapermissions.JawaPermissions;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.json.JSONObject;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

/**
 *
 * @author alexander
 */
public class FirstSpawnListener implements Listener {

    @EventHandler
    public void onPlayerSpawnLocationEvent(PlayerSpawnLocationEvent event) {
        Player player = event.getPlayer();
        World world = event.getPlayer().getWorld();

        System.out.println("onPlayerSpawnLocationEvent");
        System.out.println("player:" + event.getPlayer().getName() + " world:"+event.getPlayer().getWorld());
        System.out.println("has player played before: "+ player.hasPlayedBefore());
        System.out.println("world has custom spawn: " + JawaCommands.worldSpawns.has(world.getName()));
        
        //If player is new and there is an applicaple place in the world
//        if (!player.hasPlayedBefore() && JawaCommands.worldSpawns.has(world.getName())) {
        //If world has spawn set AND if worldspawn has one named after their rank, send them to the one named after their rank
        if (JawaCommands.worldSpawns.has(world.getName()) 
                && !player.hasPlayedBefore()
                && JawaCommands.worldSpawns.getJSONObject(world.getName()).has(JawaPermissions.playerRank.get(player.getUniqueId())) 
                && event.getSpawnLocation().equals(world.getSpawnLocation())) {
            //if that world has spawns
            //if that world has a spawn named that rank
            //if the player spawn location equals the world default spawn
            
//            JSONObject worldSpawns = JawaCommands.worldSpawns.getJSONObject(world.getName());        
//            for (String perm : worldSpawns.keySet()) {
//                System.out.println("checking for jawacommands.spawn."+perm);
//                if (player.hasPermission("jawacommands.spawn." + perm)) {
//                    System.out.println("user has that permission");
                    event.setSpawnLocation(LocationDataHandler.unpackLocation(
                            JawaCommands.worldSpawns.getJSONObject(world.getName()).getJSONObject(JawaPermissions.playerRank.get(player.getUniqueId()))));
//                    break;
//                } else {
//                    System.out.println("user doesn't have that permission");
              }
            }
        } //else ignore event
