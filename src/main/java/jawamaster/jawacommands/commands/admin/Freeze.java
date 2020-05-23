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
package jawamaster.jawacommands.commands.admin;

import jawamaster.jawacommands.JawaCommands;
import jawamaster.jawacommands.handlers.FreezeHandler;
import net.jawasystems.jawacore.PlayerManager;
import net.jawasystems.jawacore.dataobjects.PlayerDataObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author alexander
 */
public class Freeze implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command arg1, String arg2, String[] args) {
        String usage = "/freeze <player>";
        if (args == null || args.length == 0) {
            commandSender.sendMessage(ChatColor.GREEN + "> " + usage);
        } else if (args.length > 0) {
            PlayerDataObject target = PlayerManager.getPlayerDataObject(args[0]);
            if (target == null) {
                commandSender.sendMessage(ChatColor.RED + " > Error: That player is not found! Try their actual minecraft name instead of nickname.");
                return true;
            }

            if (!FreezeHandler.isFrozen(target.getUniqueID())) {
                FreezeHandler.freeze(target);
//                BaseComponent[] baseComp = new ComponentBuilder("[").color(ChatColor.DARK_GRAY)
//                    .italic(true)
//                    .append(target.getPlainNick() + "has been unmuted by ")
//                    .append(admin.getPlainNick())
//                    .append("]")
//                    .create();
//                
                //ChatHandler.opBroadcast(baseComp);
                if (JawaCommands.getConfiguration().getBoolean("freeze-public-messages", true)) {
                    String publicMessage = JawaCommands.getConfiguration().getString("freeze-public-freeze", "&3> A sudden chill passes over {p}");
                    publicMessage = publicMessage.replace("{p}", target.getFriendlyName());
                    Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', publicMessage));
                } else {
                    commandSender.sendMessage(ChatColor.GREEN + "> " + target.getFriendlyName() + ChatColor.GREEN + " has been " + ChatColor.AQUA + "frozen.");
                }
            } else {
                FreezeHandler.thaw(target);
                if (JawaCommands.getConfiguration().getBoolean("freeze-public-messages", true)) {
                    String publicMessage = JawaCommands.getConfiguration().getString("freeze-public-thaw", "&6> {p} is warmed by the light");
                    publicMessage = publicMessage.replace("{p}", target.getFriendlyName());
                    Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', publicMessage));
                } else {
                    commandSender.sendMessage(ChatColor.GREEN + "> " + target.getFriendlyName() + ChatColor.GREEN + " has been " + ChatColor.GOLD + "thawed.");
                }
            }
        }
        return true;
    }
}
