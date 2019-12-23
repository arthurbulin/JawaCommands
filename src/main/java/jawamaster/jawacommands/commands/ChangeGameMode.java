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
package jawamaster.jawacommands.commands;

import jawamaster.jawacommands.handlers.WorldHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;

/**
 *
 * @author alexander
 */
public class ChangeGameMode implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        String[] usage = {"/gm [survival|creative|adventure|spectator]",
                          "/gm [0|1|2|3]",
                          "/gm [s|c|a|sp]",
                          "/gm [gamemode] [target]",
                          "/gm"};
        GameMode currentMode = ((Player) commandSender).getGameMode();
        Player target = ((Player) commandSender);
        
        //resolve arguments
        if ((args==null) || (args.length==0)){
            //sWITCH TO CREATIVE IF PLAYER IS IN SURVIVAL AND HAS PERMISSION OR IS ALLOWED TO
            if (currentMode.equals(GameMode.SURVIVAL) && (target.hasPermission("gamemode.admin.override") || WorldHandler.isAllowedInWorld(target.getWorld(), "gamemode", "creative")) ) {
                    target.setGameMode(GameMode.CREATIVE);
            } else if (currentMode.equals(GameMode.CREATIVE) && (target.hasPermission("gamemode.admin.override") || WorldHandler.isAllowedInWorld(target.getWorld(), "gamemode", "survival")) ) {
                target.setGameMode(GameMode.SURVIVAL);
            }
            target.sendMessage(ChatColor.GREEN + " > Your game mode has been toggled to " + target.getGameMode().toString());
            return true;
        } else if (args.length == 2){ //Assumes 1 argument is for player self target, else change the gm targer to the name of the player
            target = Bukkit.getServer().getPlayer(args[1]);
            if (target == null){
                commandSender.sendMessage(ChatColor.RED + " > " + args[1] + " was not found in the online players!");
                return true;
            }
        } else if (args.length > 2) {
            target.sendMessage(ChatColor.RED + " > Too many arguments!");
            return true;
        }
        String[] modesShort = {"s","c","a","sp"};
        String[] modesLong = {"survival","creative","adventure","spectator"};
        String[] modesInt = {"0","1","2","3"};
        //If args.length == 1 or 2
        String toGM = args[0].toLowerCase();
        if (toGM.length() > 1) toGM = toGM.substring(0,2);
        GameMode toMode;
        switch (toGM) {
            case "s" :
                toMode = GameMode.SURVIVAL;
                break;
            case "c":
                toMode = GameMode.CREATIVE;
                break;
            case "a":
                toMode = GameMode.ADVENTURE;
                break;
            case "sp":
                toMode = GameMode.SPECTATOR;
                break;
            case "0":
                toMode = GameMode.SURVIVAL;
                break;
            case "1":
                toMode = GameMode.CREATIVE;
                break;
            case "2":
                toMode = GameMode.ADVENTURE;
                break;
            case "3":
                toMode = GameMode.SPECTATOR;
                break;
            case "su" :
                toMode = GameMode.SURVIVAL;
                break;
            case "cr":
                toMode = GameMode.CREATIVE;
                break;
            case "ad":
                toMode = GameMode.ADVENTURE;
                break;
            default:
                ((Player) commandSender).sendMessage(ChatColor.RED + " > " + args[0] + " is not an understood game type!");
                return true;
        }
        System.out.println("Mode Name: " + toMode.toString());
        boolean allowedInWorld = WorldHandler.isAllowedInWorld(target.getWorld(), "gamemode", toMode.toString().toLowerCase());
        
        if (allowedInWorld || target.hasPermission("gamemode.admin.override")){
            target.setGameMode(toMode);
        }
        
//        //Change game mode
//        if ((toGM.charAt(0) == 'c' || "1".equals(toGM)) && (target.hasPermission("gamemode.admin.override") || WorldHandler.isAllowedInWorld(target.getWorld(), "gamemode", "creative"))) { //Creative
//            target.setGameMode(GameMode.CREATIVE);
//        } else if ((toGM.charAt(0) == 'a' || "2".equals(toGM)) && (target.hasPermission("gamemode.admin.override") || WorldHandler.isAllowedInWorld(target.getWorld(), "gamemode", "adventure"))) { //adventure
//            target.setGameMode(GameMode.ADVENTURE);
//        } else if (((toGM.charAt(0) == 's' && !(toGM.charAt(1) == 'p'))  || "0".equals(toGM)) && (target.hasPermission("gamemode.admin.override") || WorldHandler.isAllowedInWorld(target.getWorld(), "gamemode", "creative"))) {
//            target.setGameMode(GameMode.SURVIVAL);
//        } else if (("sp".equals(toGM)  || "3".equals(toGM)) && (target.hasPermission("gamemode.admin.override") || WorldHandler.isAllowedInWorld(target.getWorld(), "gamemode", "creative"))) {
//            if (!((Player) commandSender).hasPermission("gamemode.admin.override")) {
//                ((Player) commandSender).sendMessage(ChatColor.RED + " > Spectator mode is restricted to admin ranks only.");
//                return true;
//            } else {
//                target.setGameMode(GameMode.SPECTATOR);
//            }
//        } else {
//            ((Player) commandSender).sendMessage(ChatColor.RED + " > " + args[0] + " is not an understood game type!");
//            return true;
//        }
        
        //inform target and commandsender
        if (target.getUniqueId().equals(((Player) commandSender).getUniqueId())){
            if (!target.getGameMode().equals(GameMode.ADVENTURE)) target.sendMessage(ChatColor.GREEN + " > Your game mode has been changed to " + target.getGameMode().toString());
            else target.sendMessage(ChatColor.GREEN + " > Your game mode has been changed to " + target.getGameMode().toString());
        } else {
            target.sendMessage(ChatColor.GREEN + " > Your game mode has been changed to " + target.getGameMode().toString() +" by " + ((Player) commandSender).getDisplayName());
            ((Player) commandSender).sendMessage(ChatColor.GREEN + " > " + target.getDisplayName() + "'s game mode has been changed to " + target.getGameMode().toString());
        }
        
        return true;
    }
    
}
