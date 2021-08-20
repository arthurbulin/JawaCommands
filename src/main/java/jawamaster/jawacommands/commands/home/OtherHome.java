/*
 * Copyright (C) 2021 Jawamaster (Arthur Bulin)
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
package jawamaster.jawacommands.commands.home;

import jawamaster.jawacommands.handlers.HomeHandler;
import net.jawasystems.jawacore.PlayerManager;
import net.jawasystems.jawacore.dataobjects.PlayerDataObject;
import net.jawasystems.jawacore.handlers.StandardMessages;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Jawamaster (Arthur Bulin)
 */
public class OtherHome implements CommandExecutor {
    
    public final String USAGE = ChatColor.GREEN + "> Usage: /otherhome <player name> <[info|homename]> <[homename]>";
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Player player = (Player) commandSender;
        
        if ((args == null) || (args.length == 0)){
            player.sendMessage(USAGE);
        } else {
            PlayerDataObject pdo = PlayerManager.getPlayerDataObject(args[0]);
            if (pdo == null) {
                player.sendMessage(StandardMessages.getMessage(StandardMessages.Message.PLAYERNOTFOUND));
                return true;
            }
            
            switch (args.length) {
                case 1:
                    HomeHandler.sendHomeList(player, pdo);
                    break;
                case 2:
                    HomeHandler.sendToOtherHome(player, args[1], pdo);
                    break;
                default:
                    HomeHandler.sendOtherHomeInfo(player, args[2], pdo);
                    break;
            }
        }
        
        return true;
    }
    
}
