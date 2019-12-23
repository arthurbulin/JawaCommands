/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jawamaster.jawacommands.commands.home;

import jawamaster.jawacommands.JawaCommands;
import jawamaster.jawacommands.handlers.HomeHandler;
import jawamaster.jawacommands.handlers.LocationDataHandler;
import jawamaster.jawapermissions.PlayerDataObject;
import jawamaster.jawapermissions.handlers.ESHandler;
import jawamaster.jawapermissions.utils.ESRequestBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONObject;

/**
 *
 * @author Arthur Bulin
 */
public class setHome implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command arg1, String arg2, String[] args) {

        Player player = (Player) commandSender;
        boolean replace = false;
        String homeName = "";
        String usage = "/sethome -[r] <homename>";

        if (args.length == 0) {
            player.sendMessage(ChatColor.GREEN + " > Usage: " + usage);
            return true;
        } else if (args.length > 2) {
            player.sendMessage(ChatColor.RED + " > Error! Too many arguments! Usage: " + usage);
            return true;
        } else if (("-r".equals(args[0])) && (args.length == 2)) {
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + " > Error! Too few arguments! Usage: " + usage);
                return true;
            } else {
                replace = true;
                homeName = args[1];
            }
        } else {
            homeName = args[0];
        }
        
        return HomeHandler.addHome(player, homeName, replace);
        
    }

}


