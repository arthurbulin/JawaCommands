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
package jawamaster.jawacommands.commands.spawn;

import java.util.logging.Logger;
import jawamaster.jawacommands.handlers.TPHandler;
import jawamaster.jawacommands.handlers.WorldHandler;
import net.jawasystems.jawacore.PlayerManager;
import net.jawasystems.jawacore.dataobjects.PlayerDataObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author alexander
 */
public class Spawn implements CommandExecutor{

    private static final Logger LOGGER = Logger.getLogger("spawn");
    private static final String USAGE = "/spawn [world name]";
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        
        if (!(commandSender instanceof Player)){
            System.out.println("Only players can user this command!");
            return true;
        }
        
        Player player = (Player) commandSender;
        PlayerDataObject pdObject = PlayerManager.getPlayerDataObject(player);
        
        if ((args == null) || (args.length == 0)) {
            
            if (WorldHandler.groupHasGlobalSpawn(pdObject.getRank())) {
                TPHandler.performSafeTeleport(player, WorldHandler.getGlobalSpawn(pdObject.getRank()));
            } else if (WorldHandler.worldHasGroupSpawn(player.getWorld().getName(), pdObject.getRank())){
                TPHandler.performSafeTeleport(player, WorldHandler.getWorldSpawn(player.getWorld().getName(), pdObject.getRank()));
            } else {
                player.spawnAt(player.getWorld().getSpawnLocation());
            }
        } else if ((args.length > 0) && player.hasPermission("jawacommands.spawn." + args[0])){
            if (WorldHandler.groupHasGlobalSpawn(args[0])) {
                TPHandler.performSafeTeleport(player, WorldHandler.getGlobalSpawn(args[0]));
            } else if (WorldHandler.worldHasGroupSpawn(args[0], pdObject.getRank())){
                TPHandler.performSafeTeleport(player, WorldHandler.getWorldSpawn(args[0], pdObject.getRank()));
            } else if (Bukkit.getServer().getWorld(args[0]) != null) {
                TPHandler.performSafeTeleport(player, Bukkit.getServer().getWorld(args[0]).getSpawnLocation());
            }
//            TPHandler.performSafeTeleport(player, Bukkit.getServer().getWorld(args[0]).getSpawnLocation());
            //player.teleport(Bukkit.getServer().getWorld(args[0]).getSpawnLocation(),PlayerTeleportEvent.TeleportCause.COMMAND);
        } else {
            player.sendMessage(ChatColor.RED + " > You do not have permission to spawn in this world!");
        }
        return true;
    }
    
}
