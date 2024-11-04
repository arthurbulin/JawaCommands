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
import jawamaster.jawacommands.handlers.MessageHandler;
import net.jawasystems.jawacore.PlayerManager;
import net.jawasystems.jawacore.dataobjects.PlayerDataObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author alexander
 */
public class Freeze implements CommandExecutor {

    private static final TextComponent USAGE = Component.text(" > ", NamedTextColor.GREEN)
            .append(Component.text("/freeze <player>", NamedTextColor.WHITE));
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command arg1, String arg2, String[] args) {
        if (args == null || args.length == 0) {
            commandSender.sendMessage(USAGE);
        } else if (args.length > 0) {
            PlayerDataObject target = PlayerManager.getPlayerDataObject(args[0]);
            if (target == null) {
                commandSender.sendMessage(MessageHandler.getMessage("player-not-found"));
                return true;
            }

            if (!FreezeHandler.isFrozen(target.getUniqueID())) {
                FreezeHandler.freeze(target);
                TextComponent message = MessageHandler.runReplace("{p}", MessageHandler.getMessage("freeze-public-freeze"), target.getFriendlyName());
                if (JawaCommands.getConfiguration().getBoolean("freeze-public-messages", true)) {
                    Bukkit.getServer().broadcast(message);
                } else {
                    commandSender.sendMessage(message);
                }
                
            } else {
                FreezeHandler.thaw(target);
                TextComponent message = MessageHandler.runReplace("{p}", MessageHandler.getMessage("freeze-public-thaw"), target.getFriendlyName());
                if (JawaCommands.getConfiguration().getBoolean("freeze-public-messages", true)) {
                    Bukkit.getServer().broadcast(message);
                } else {
                    commandSender.sendMessage(message);
                }
            }
        }
        return true;
    }
}
