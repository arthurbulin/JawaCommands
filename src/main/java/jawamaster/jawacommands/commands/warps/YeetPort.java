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
package jawamaster.jawacommands.commands.warps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jawamaster.jawacommands.JawaCommands;
import jawamaster.jawacommands.commands.development.BackHandler;
import jawamaster.jawacommands.handlers.WarpHandler;
import net.jawasystems.jawacore.PlayerManager;
import net.jawasystems.jawacore.handlers.LocationDataHandler;
import net.jawasystems.jawacore.utils.TargetSelectorProcessor;
import net.jawasystems.jawacore.utils.VectorParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.json.JSONObject;

/**
 *
 * @author alexander
 */
public class YeetPort implements CommandExecutor {
    
    private static final Logger LOGGER = Logger.getLogger("YeetPort");
    
    private static final String[] USAGE = new String[]{
        ChatColor.GREEN + "> Yeet yourself to a warp - " + ChatColor.GOLD + "/yeetport <warp>",
        ChatColor.GREEN + ""};
    
    private static final HashMap<Player,Location> LOCATIONS = new HashMap();
    private static final HashMap<Player,Integer> COUNTS = new HashMap();
    private static final HashMap<Player,ArrayList<JSONObject>> VECTORS = new HashMap();
    private static final HashMap<Player,Integer> TASK = new HashMap();
    
    private static final Vector NONE = new Vector(0, 0, 0);
    private static final Vector UP = new Vector(0, 30, 0);
    private static final JSONObject DEFAULTVECTOR = new JSONObject().put("vector", UP).put("timeout", 6).put("distance", 100);
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        //Should ignore warp permissions
        //really just a command block command
        // /yeetport <warp> <playername> <vectors:x,y,z,d,to:x2,y2,z2,d2,to2...> <start:x,y,z,yaw,pitch>
        // /yeetport <warp> @p
        // /yeetport <warp> @a
        // /yeetport <warp>
        
//        if (!commandSender.hasPermission("jawacommands.yeetport")){
//            
//            return true;
//        }

        if ( args == null || args.length == 0) {
            commandSender.sendMessage(ChatColor.GREEN + "> Usage:");
            commandSender.sendMessage(USAGE);
        } else {
            if (!WarpHandler.warpExists(args[0])){
                commandSender.sendMessage(ChatColor.RED + "> Error: Warp " + args[0] + " does not exist");
            }
            
            List<Player> playerList;
            Location start = null;
            
            if (args.length == 1){
                ArrayList defaultArrayList = new ArrayList();
                defaultArrayList.add(DEFAULTVECTOR);
                VECTORS.put((Player) commandSender, defaultArrayList);
                yeet((Player) commandSender, args[0] , null);
            } else {
                //parse for arguments
                if (args[1].matches("@[p|P].*")){
                    Player player = TargetSelectorProcessor.getAtP(args[1], commandSender);
                    if (player != null){
                        playerList = new ArrayList();
                        playerList.add(player);
                    } else {
                        commandSender.sendMessage(ChatColor.RED + "> Error: No player was found within the @p paramaters. Please check your paramaters.");
                        return true;
                    }
                } else if (args[1].matches("@[a|A].*")){
                    playerList = TargetSelectorProcessor.getAtA(args[1], commandSender);
                    if (playerList == null || playerList.isEmpty()){
                        commandSender.sendMessage(ChatColor.RED + "> Error: No player was found within the @a paramaters. Please check your paramaters.");
                        return true;
                    }
                } else { //assume it is a player name
                    Player player = PlayerManager.getPlayer(args[1]);
                    if (player != null){
                        playerList = new ArrayList();
                        playerList.add(player);
                    } else {
                        commandSender.sendMessage(ChatColor.RED + "> Error: No player was found with that name. Please be sure you typed it correctly or that the player is still online.");
                        return true;
                    }
                }
                
                if (args.length > 2){
                    for (String argument : Arrays.copyOfRange(args, 2, args.length)){
                        if (argument.matches("(?i)ve?c?t?o?r?\\:.*")) {
                            String[] tmp = argument.replaceFirst("(?i)ve?c?t?o?r?\\:", "").split("\\:");
                            ArrayList<JSONObject> parsedVectors = VectorParser.parseVectors(tmp);
                            if (parsedVectors.size() != tmp.length){
                                commandSender.sendMessage(ChatColor.RED + "> Error: Your vectors are not properly formatted.");
                                return true;
                            } else {
                                for (Player play : playerList){
                                    VECTORS.put(play, parsedVectors);
                                }
                            }
                        } else if (argument.matches("(?i)st?a?r?t?\\:.*")){
//                            LOGGER.log(Level.INFO, "Start at: " + argument);
                            if (commandSender instanceof BlockCommandSender ) {
                                start = LocationDataHandler.getLocation(argument.replaceFirst("(?i)st?a?r?t?\\:", "").split(","), ((BlockCommandSender) commandSender).getBlock().getWorld());
//                                LOGGER.log(Level.INFO, "CB Start at: " + start);
                            } else if (commandSender instanceof Player){
                                start = LocationDataHandler.getLocation(argument.replaceFirst("(?i)st?a?r?t?\\:", "").split(","), ((Player) commandSender).getWorld());
//                                LOGGER.log(Level.INFO, "PL Start at: " + start);
                            } else {
                                commandSender.sendMessage("You are not a valid command sender for this command and cannot set a start position");
                            }
                        }
                    }
                }
                
                               
                for (Player player : playerList){
                    if (!VECTORS.containsKey(player)){
                        ArrayList defaultArrayList = new ArrayList();
                        defaultArrayList.add(DEFAULTVECTOR);
                        VECTORS.put(player, defaultArrayList);
                    }
                    yeet(player, args[0], start);
                }
            }
        }

//        if ((args != null) && args.length == 2) {
//            String warp = args[0];
//            Player target = null;
//            if (!args[1].equals("@p")) {
//                target = Bukkit.getPlayer(args[1]);
//                //System.out.println("player");
//            } else {
//                //System.out.println("area");
//                //BoundingBox bb = new BoundingBox();
//                //System.out.println(commandSender.getClass());
//                BlockCommandSender commandBlock = ((BlockCommandSender) commandSender);
//                //System.out.println("cast");
//                
//                List<Player> players = (commandBlock.getBlock().getLocation().getWorld().getPlayers());
//                //System.out.println("players");
//                
//                double dist = 100.0;
//                
//                for (Player player : players){
//                    double pDist = player.getLocation().distanceSquared(commandBlock.getBlock().getLocation());
//                    if (pDist < dist) {
//                        dist = pDist;
//                        target = player;
//                    }
//                }
//                
//
//            }
//
//            if (target == null) {
//                System.out.println("didnt find player");
//                return true;
//            }
//
//            Player player = target;
//            int startY = target.getLocation().getBlockY();
//            Vector up = new Vector(0, 30, 0);
//            Vector none = new Vector(0, 0, 0);
//
//            target.sendMessage(ChatColor.YELLOW + " > YEEET!!!");
//
//            new BukkitRunnable() {
//                @Override
//                public void run() {
//                    player.setVelocity(up);
//                    if (player.getLocation().getBlockY() > (startY + 100)) {
//                        player.setVelocity(none);
//                        WarpHandler.getWarp(warp).sendPlayer(player, true);
//                        this.cancel();
//                    }
//                }
//            }.runTaskTimer(JawaCommands.getPlugin(), 5, 5);
//
//        }
        return true;
    }
    
    private static void yeet(Player player, String warpName, Location start){
        LOCATIONS.put(player, player.getLocation());
        COUNTS.put(player, 0);
//        VECTORS.put(player, vectors);
        
        if (start != null){
            player.teleport(start, PlayerTeleportEvent.TeleportCause.UNKNOWN);
            BackHandler.addUserBackLocation(player, start);
        } else {
            BackHandler.addUserBackLocation(player, player.getLocation());
        }

        

        player.sendMessage(ChatColor.YELLOW + "> YEET!!!!!");
        int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(JawaCommands.getPlugin(), ()->{
            //if timeout is exceeded
            if (COUNTS.get(player) > VECTORS.get(player).get(0).getInt("timeout")*4){
//                LOGGER.log(Level.INFO, "Expired counts:{0} timeout:{1}", new Object[]{COUNTS.get(player), VECTORS.get(player).get(0).getInt("timeout")*4});
                LOCATIONS.remove(player);
                COUNTS.remove(player);
                VECTORS.remove(player);
                Bukkit.getScheduler().cancelTask(TASK.get(player));
                player.setVelocity(NONE);
                WarpHandler.getWarp(warpName).sendPlayerWithUnknownCause(player, true);
            } else {
                //if the distance has been exceeded
                if (VECTORS.get(player).get(0).getInt("distance") < player.getLocation().distance(LOCATIONS.get(player))){
//                    LOGGER.log(Level.INFO, "Distance exceeded. Configured distance:{0} Calculated distance:{1}", new Object[]{VECTORS.get(player).get(0).getInt("distance"), player.getLocation().distance(LOCATIONS.get(player))});
                    //move on to the next vector
                    VECTORS.get(player).remove(0);
                    //Reset the location to check for the next vector
                    LOCATIONS.remove(player);
                    //Reset counts
                    COUNTS.remove(player);
                    
                    //if that was the last vector then clean up, send the player, and cancel the task
                    if (VECTORS.get(player).isEmpty()) {
//                        LOGGER.log(Level.INFO,"No more vectors");
                        VECTORS.remove(player);
                        Bukkit.getScheduler().cancelTask(TASK.get(player));
                        player.setVelocity(NONE);
                        
                        WarpHandler.getWarp(warpName).sendPlayerWithUnknownCause(player, true);
                    } else { //If there are more vectors keep punting the player around
//                        LOGGER.log(Level.INFO,"Keep Punting");
                        LOCATIONS.put(player, player.getLocation());
                        COUNTS.put(player, 0);
                        player.setVelocity((Vector) VECTORS.get(player).get(0).get("vector"));
                    }   
                } else { //If the we haven't exceeded the distance
//                    LOGGER.log(Level.INFO,"Not there yet");
                    player.setVelocity((Vector) VECTORS.get(player).get(0).get("vector"));
                    COUNTS.put(player, COUNTS.get(player)+1);
                }
            }
        }, 1, 5);
        TASK.put(player, task);
    }

}
