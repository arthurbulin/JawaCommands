/*
 * Copyright (C) 2020 Jawamaster (Arthur Bulin)
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
package jawamaster.jawacommands.commands;

import java.util.Arrays;
import java.util.List;
import net.jawasystems.jawacore.PlayerManager;
import net.jawasystems.jawacore.dataobjects.PlayerDataObject;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Jawamaster (Arthur Bulin)
 */
public class PlayerWeather implements CommandExecutor {

    private final String PERMISSION = "jawacommands.pweather";
    private final String OTHERPERMISSION = "jawacommands.pweather.other";
    private final String USAGE = "/pweather [clear|rain|reset] [player]. Run without arguments to reset your own weather.";
    public static final List<String> TABCOMPLETES = Arrays.asList("rain","clear","reset");
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
    
        if (!(commandSender instanceof Player)) return true; //if not player cancel
        
        if (args == null || args.length == 0){
            ((Player) commandSender).resetPlayerWeather();
            commandSender.sendMessage(ChatColor.GREEN + "> Your weather has been reset");
        } else if (args.length >= 1) {
            PlayerDataObject target;
            String who;
            if (args.length >= 2 && commandSender.hasPermission(OTHERPERMISSION)) {
                target = PlayerManager.getPlayerDataObject(args[1]);
                if (target == null || !target.isOnline()) {
                    commandSender.sendMessage(ChatColor.RED + "> Error: That player is not found or is not online! Try their actual minecraft name instead of nickname.");
                    return true;
                }
                who = target.getFriendlyName()+ "'s" + ChatColor.GREEN;
            }
            else {
                target = PlayerManager.getPlayerDataObject((Player) commandSender);
                who = "Your";              
            }
            
            if (args[0].matches("cl?e?a?r?")) {
                target.getPlayer().setPlayerWeather(WeatherType.CLEAR);
                commandSender.sendMessage(ChatColor.GREEN + "> "+ who + " weather has been set to clear");
            } else if (args[0].matches("rai?n?")) {
                target.getPlayer().setPlayerWeather(WeatherType.DOWNFALL);
                commandSender.sendMessage(ChatColor.GREEN + "> "+ who + " weather has been set to downfall");
            } else if (args[0].matches("res?e?t?")) {
                target.getPlayer().resetPlayerWeather();
                commandSender.sendMessage(ChatColor.GREEN + "> "+ who + " weather has been reset");
            } else {
                commandSender.sendMessage(ChatColor.RED + "> " + USAGE);
            }
            
        } 
        
        return true;
    }
    
}
