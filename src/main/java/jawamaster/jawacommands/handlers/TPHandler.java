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
package jawamaster.jawacommands.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 *
 * @author alexander
 */
public class TPHandler {

    static Map<String, String> currentTPRequest = new HashMap<String, String>();
    static Map<String, String> currentSummonRequest = new HashMap<String, String>();
    public static long keepAlive = 1200;
    public static long coolDown = 1200;

    //Person the request is being sent to is the target
    public static void tpRequest(int type, Player player, Player target) {
        String playerName = ChatColor.translateAlternateColorCodes('$', player.getDisplayName());
        String targetName = ChatColor.translateAlternateColorCodes('$', target.getDisplayName());
        player.sendMessage(ChatColor.DARK_GREEN + " > Sending request to " + targetName);

        if (type == 0) { //To tpa to a player
            target.sendMessage(ChatColor.GREEN + " > " + playerName + " has requested to teleport to you.");
            currentTPRequest.put(target.getName(), player.getName());
        } else if (type == 1) { //to summona a player to you
            target.sendMessage(ChatColor.GREEN + " > " + playerName + " has requested YOU teleport to them.");
            currentSummonRequest.put(target.getName(), player.getName());
        }

        target.sendMessage(ChatColor.GREEN + " > Type /accept to accept the request.");
        //target.sendMessage("Type /deny to deny.");
    }

    public static void killRequest(Player target) {
        if (currentTPRequest.containsKey(target.getName())) {
            Player player = Bukkit.getServer().getPlayer(currentTPRequest.get(target.getName()));
            player.sendMessage(ChatColor.RED + " > Your teleport request timed out.");
            currentTPRequest.remove(target.getName());
        } else if (currentSummonRequest.containsKey(target.getName())) {
            Player player = Bukkit.getServer().getPlayer(currentSummonRequest.get(target.getName()));
            player.sendMessage(ChatColor.RED + " > Your summon request timed out.");
            currentSummonRequest.remove(target.getName());
        }

    }

    public static boolean tpAccept(Player player) {
        if (currentTPRequest.containsKey(player.getName())) {
            Player target = Bukkit.getServer().getPlayer(currentTPRequest.get(player.getName()));
            currentTPRequest.remove(player.getName());
            player.sendMessage(ChatColor.DARK_GREEN + " > Teleporting " + target.getDisplayName() + " to you.");
            target.sendMessage(ChatColor.DARK_GREEN + " > Teleporting you to " + player.getDisplayName());
            target.teleport(player, PlayerTeleportEvent.TeleportCause.COMMAND);
            return true;
        } else if (currentSummonRequest.containsKey(player.getName())) {
            Player target = Bukkit.getServer().getPlayer(currentSummonRequest.get(player.getName()));
            currentSummonRequest.remove(player.getName());
            player.sendMessage(ChatColor.DARK_GREEN + " > Teleporting you to " + target.getDisplayName());
            target.sendMessage(ChatColor.DARK_GREEN + " > Teleporting " + player.getDisplayName() + " to you.");
            player.teleport(target, PlayerTeleportEvent.TeleportCause.COMMAND);
            return true;
        }
        return false;
    }

    public static Location randomLocation(Player player, int worldborder) {
        Random r = new Random();

        int x = r.nextInt(worldborder + worldborder) - worldborder;
        int z = r.nextInt(worldborder + worldborder) - worldborder;
        int y = player.getWorld().getHighestBlockYAt(x, z);

        Location loc = new Location(player.getWorld(), x, y, z);

        return loc;
    }

    public static boolean blockCheck(Location loc) {
        Block block = loc.getWorld().getHighestBlockAt(loc);

        Material type = block.getType();

        if ((type == Material.LAVA) || (type == Material.WATER)
                || (type == Material.AIR) || (type == Material.WATER)
                || (type == Material.LAVA) || (type == Material.CACTUS)
                || (type == Material.MAGMA_BLOCK)) {

            return false;
        } else {
            return true;
        }
    }

}
