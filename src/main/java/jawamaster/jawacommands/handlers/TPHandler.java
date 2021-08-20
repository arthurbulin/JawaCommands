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
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import jawamaster.jawacommands.JawaCommands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

/**
 *
 * @author alexander
 */
public class TPHandler {

    static Map<String, String> currentTPRequest = new HashMap<String, String>();
    static Map<String, String> currentSummonRequest = new HashMap<String, String>();
    static HashMap<UUID, Integer> playerTPTask = new HashMap();
    private static HashSet<Player> safeTeleport = new HashSet();
    public static long keepAlive = 1200;
    public static long coolDown = 1200;

    //Person the request is being sent to is the target
    public static void tpRequest(int type, Player player, Player target) {
        String playerName = ChatColor.translateAlternateColorCodes('&', player.getDisplayName());
        String targetName = ChatColor.translateAlternateColorCodes('&', target.getDisplayName());
        player.sendMessage(ChatColor.GREEN + " > Sending request to " + targetName);

        if (type == 0) { //To tpa to a player
            target.sendMessage(ChatColor.GREEN + "> " + playerName + ChatColor.RESET + ChatColor.GREEN + " has requested to teleport to you.");
            currentTPRequest.put(target.getName(), player.getName());
        } else if (type == 1) { //to summona a player to you
            target.sendMessage(ChatColor.GREEN + "> " + playerName + ChatColor.RESET + ChatColor.GREEN + " has requested YOU teleport to them.");
            currentSummonRequest.put(target.getName(), player.getName());
        }

        target.sendMessage(ChatColor.GREEN + " > Type /accept to accept the request.");
        //target.sendMessage("Type /deny to deny.");
    }

    public static void killRequest(Player target) {
        if (currentTPRequest.containsKey(target.getName())) {
            Player player = Bukkit.getServer().getPlayer(currentTPRequest.get(target.getName()));
            player.sendMessage(ChatColor.RED + "> Your teleport request timed out.");
            currentTPRequest.remove(target.getName());
        } else if (currentSummonRequest.containsKey(target.getName())) {
            Player player = Bukkit.getServer().getPlayer(currentSummonRequest.get(target.getName()));
            player.sendMessage(ChatColor.RED + "> Your summon request timed out.");
            currentSummonRequest.remove(target.getName());
        }

    }

    public static boolean tpAccept(Player player) {
        if (currentTPRequest.containsKey(player.getName())) {
            Player target = Bukkit.getServer().getPlayer(currentTPRequest.get(player.getName()));
            currentTPRequest.remove(player.getName());
            player.sendMessage(ChatColor.GREEN + "> Teleporting " + target.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + " to you.");
            target.sendMessage(ChatColor.GREEN + "> Teleporting you to " + player.getDisplayName());
            target.teleport(player, PlayerTeleportEvent.TeleportCause.COMMAND);
            return true;
        } else if (currentSummonRequest.containsKey(player.getName())) {
            Player target = Bukkit.getServer().getPlayer(currentSummonRequest.get(player.getName()));
            currentSummonRequest.remove(player.getName());
            player.sendMessage(ChatColor.GREEN + "> Teleporting you to " + target.getDisplayName());
            target.sendMessage(ChatColor.GREEN + "> Teleporting " + player.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + " to you.");
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

    public static boolean safeToTP(Location loc) {
        //Block block = loc.getWorld().getHighestBlockAt(loc);
        Block blk = loc.getBlock();
        boolean safe = true;
        if (!blk.isPassable() || blk.isLiquid()) {
            safe = false;
        }
        Block blkD = loc.subtract(0, 1, 0).getBlock();
        if (blkD.isPassable() || blkD.isLiquid() || blkD.isEmpty()) {
            safe = false;
        }
        
        //System.out.print("Block is safe. Block:" + blk.getType().name());
        return safe;
        //Material type = block.getType();

        
//        Block feet = location.getBlock();
//        if (!feet.getType().isTransparent() && !feet.getLocation().add(0, 1, 0).getBlock().getType().isTransparent()) {
//            return false; // not transparent (will suffocate)
//        }
//        Block head = feet.getRelative(BlockFace.UP);
//        if (!head.getType().isTransparent()) {
//            return false; // not transparent (will suffocate)
//        }
//        Block ground = feet.getRelative(BlockFace.DOWN);
//        if (!ground.getType().isSolid()) {
//            return false; // not solid
//        }
//        return true;
    
//        if ((type == Material.LAVA) || (type == Material.WATER)
//                || (type == Material.AIR)
//                || (type == Material.LAVA)
//                || (type == Material.CACTUS)
//                || (type == Material.MAGMA_BLOCK)) {
//
//            return false;
//        } else {
//            return true;
//        }
    }
    
    /** Performs a teleport that tries to keep the player from falling through the ground
     * by freezing them in place for 5 seconds and sets their velocity to 0 in all
     * vectors.
     * @param target
     * @param loc 
     * @param cause 
     */
    public static void performSafeTeleport(Player target, Location loc, PlayerTeleportEvent.TeleportCause cause) {
        
        //Inform the target of freezing
        if (JawaCommands.getConfiguration().getBoolean("safe-tp-messages", true)) {
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', JawaCommands.getConfiguration().getConfigurationSection("messages").getString("safe-tp-freeze", "&e> You are being frozen for teleport.")));
            target.setInvulnerable(true);
            //target.sendMessage(ChatColor.GREEN + "> You are being frozen for teleport.");
        }

        //Teleport the player
        target.teleport(loc, cause);
        
        //Cancel all velocity if need be
        target.setVelocity(new Vector(0, 0, 0));
        
        //Freeze them
        safeTeleport.add(target);

        Bukkit.getServer().getScheduler().runTaskLater(JawaCommands.getPlugin(), () -> {
            //Unfreeze the player
            safeTeleport.remove(target);
            if (JawaCommands.getConfiguration().getBoolean("safe-tp-messages", true)) {
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', JawaCommands.getConfiguration().getConfigurationSection("messages").getString("safe-tp-thaw", "&e> You have been thawed.")));
                target.setInvulnerable(false);
                //target.sendMessage(ChatColor.GREEN + "> You have been thawed.");
            }
            
        }, 100);
    }
    
    /** Performs a teleport that tries to keep the player from falling through the ground
     * by freezing them in place for 5 seconds and sets their velocity to 0 in all
     * vectors. This will teleport with the cause COMMAND. This is backed by 
     * performSageTeleport(Player, Location, TeleportCause).
     * @param target
     * @param loc 
     */
    public static void performSafeTeleport(Player target, Location loc) {
        performSafeTeleport(target, loc, PlayerTeleportEvent.TeleportCause.COMMAND);
        
    }
    
    public static boolean safeTP(Player player){
        return safeTeleport.contains(player);
    }

}
