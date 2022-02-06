/*
 * Copyright (C) 2021 Arthur Bulin
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
package jawamaster.jawacommands.commands.admin;

import net.jawasystems.jawacore.PlayerManager;
import net.jawasystems.jawacore.dataobjects.PlayerDataObject;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Arthur Bulin
 */
public class SafeTeleportBypass implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String arg1, String[] args) {
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage("Error: Only players may use this command");
            return true;
        }
        
        PlayerDataObject player = PlayerManager.getPlayerDataObject((Player) commandSender);
        player.toggleSafeTeleport();
        if (player.safeTeleportEnabled()) {
            commandSender.sendMessage(ChatColor.GREEN + "> Safe teleport bypass has been set to: " + ChatColor.BLUE + "On" + ChatColor.GREEN + " Have a pleasent teleport!");
        } else {
            commandSender.sendMessage(ChatColor.GREEN + "> Safe teleport bypass has been set to: " + ChatColor.RED + "Off" + ChatColor.YELLOW + " Good luck player...good luck...");
        }
        
        return true;
    }
    
}
