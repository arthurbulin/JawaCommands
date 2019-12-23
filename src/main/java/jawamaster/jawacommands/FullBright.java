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
package jawamaster.jawacommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * @author alexander
 */
public class FullBright implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command arg1, String arg2, String[] arg3) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                player.sendMessage(ChatColor.DARK_GREEN + " > Do not go gentle into that good night player.");
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            } else {
                player.sendMessage(ChatColor.DARK_GREEN + " > Night vision activate!");
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
            }

        } else {
            System.out.println("Console cannot execute this command");
        }
        return true;
    }
}
