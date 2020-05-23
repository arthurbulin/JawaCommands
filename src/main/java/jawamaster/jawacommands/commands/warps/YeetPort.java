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

import java.util.List;
import jawamaster.jawacommands.JawaCommands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.bukkit.command.BlockCommandSender;

/**
 *
 * @author alexander
 */
public class YeetPort implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        //Should ignore warp permissions
        //really just a command block command
        
        if (!commandSender.hasPermission("jawacommands.yeetport")){
            
            return true;
        }
        
        if ((args != null) && args.length == 2) {
            String warp = args[0];
            Player target = null;
            if (!args[1].equals("@p")) {
                target = Bukkit.getPlayer(args[1]);
                //System.out.println("player");
            } else {
                //System.out.println("area");
                //BoundingBox bb = new BoundingBox();
                //System.out.println(commandSender.getClass());
                BlockCommandSender commandBlock = ((BlockCommandSender) commandSender);
                //System.out.println("cast");
                
                List<Player> players = (commandBlock.getBlock().getLocation().getWorld().getPlayers());
                //System.out.println("players");
                
                double dist = 100.0;
                
                for (Player player : players){
                    double pDist = player.getLocation().distanceSquared(commandBlock.getBlock().getLocation());
                    if (pDist < dist) {
                        dist = pDist;
                        target = player;
                    }
                }
                

            }

            if (target == null) {
                System.out.println("didnt find player");
                return true;
            }

            Player player = target;
            int startY = target.getLocation().getBlockY();
            Vector up = new Vector(0, 30, 0);
            Vector none = new Vector(0, 0, 0);

            target.sendMessage(ChatColor.YELLOW + " > YEEET!!!");

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.setVelocity(up);
                    if (player.getLocation().getBlockY() > (startY + 100)) {
                        player.setVelocity(none);
                        JawaCommands.getWarpIndex().get(warp).sendPlayer(player, true);
                        this.cancel();
                    }
                }
            }.runTaskTimer(JawaCommands.getPlugin(), 5, 5);

        }
        return true;
    }

}
