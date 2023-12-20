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

import net.jawasystems.jawacore.PlayerManager;
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
        String[] usage = {ChatColor.GREEN + " > " + ChatColor.WHITE + "/gm [survival|creative|adventure|spectator]",
                          ChatColor.GREEN + " > " + ChatColor.WHITE + "/gm [gamemode] [target]",
                          ChatColor.GREEN + " > " + ChatColor.WHITE + "/gm"};
        Player target;
        GameMode toMode;
        String toGM = "adventure";
        
        
        //resolve arguments
        if ((args==null) || (args.length==0)){ //Allow toggle
            //sWITCH TO CREATIVE IF PLAYER IS IN SURVIVAL AND HAS PERMISSION OR IS ALLOWED TO
            if (((Player) commandSender).getGameMode().equals(GameMode.SURVIVAL)) {
                toGM = GameMode.CREATIVE.toString().toLowerCase();
            } else if (((Player) commandSender).getGameMode().equals(GameMode.CREATIVE)) {
                toGM = GameMode.SURVIVAL.toString().toLowerCase();
            }
            
            target = ((Player) commandSender);
        } else if (args.length == 1) {
            target = ((Player) commandSender);
            toGM = args[0].toLowerCase();
        } else if (args.length == 2){ //Assumes 1 argument is for player self target, else change the gm targer to the name of the player
            if (!commandSender.hasPermission("jawacommands.gamemode.admin.other")){
                commandSender.sendMessage(ChatColor.RED + " > You do not have permission to change other people's gamemode.");
                return true;
            }
            
            //Using PlayerManager to resolve nicknames
            target = PlayerManager.getPlayer(args[1]);
            if (target == null){
                commandSender.sendMessage(ChatColor.RED + " > " + args[1] + " was not found in the online players!");
                return true;
            }

            toGM = args[0].toLowerCase();
        } else {
            commandSender.sendMessage(ChatColor.RED + " > Too many arguments!");
            commandSender.sendMessage(usage);
            return true;
        }

        if(toGM.equals("s") || toGM.startsWith("su") || toGM.equals("0")){
            toMode = GameMode.SURVIVAL;
        } else if (toGM.equals("c") || toGM.startsWith("cr") || toGM.equals("1")){
            toMode = GameMode.CREATIVE;
        } else if (toGM.equals("a") || toGM.startsWith("ad") || toGM.equals("2")){
            toMode = GameMode.ADVENTURE;
        } else if (toGM.startsWith("sp") || toGM.equals("3")){
            toMode = GameMode.SPECTATOR;
        } else {
            commandSender.sendMessage(ChatColor.RED + " > " + args[0] + " is not an understood game type!");
            return true;
        }
        
        //if (WorldHandler.isAllowedInWorld(target.getWorld(), "gamemode", toMode.toString().toLowerCase()) || target.hasPermission("gamemode.admin.override")){
        //System.out.println("gamemode." + toMode.toString().toLowerCase());
        if (target.hasPermission("jawacommands.gamemode." + toMode.toString().toLowerCase()) || commandSender.hasPermission("jawacommands.gamemode.admin.override")) {
            target.setGameMode(toMode);

            //inform target and commandsender
            if (target.getUniqueId().equals(((Player) commandSender).getUniqueId())) {
                target.sendMessage(ChatColor.GREEN + " > Your game mode has been changed to " + target.getGameMode().toString());
            } else {
                target.sendMessage(ChatColor.GREEN + " > Your game mode has been changed to " + target.getGameMode().toString() + " by " + ((Player) commandSender).getDisplayName());
                commandSender.sendMessage(ChatColor.GREEN + " > " + target.getDisplayName() + ChatColor.GREEN + "'s game mode has been changed to " + target.getGameMode().toString().toLowerCase());
            }
        } else {
            //inform target and commandsender
            if (target.getUniqueId().equals(((Player) commandSender).getUniqueId())) {
                target.sendMessage(ChatColor.RED + " > You do not have permission to change your gamemode to " + toMode.toString().toLowerCase());
            } else {
                commandSender.sendMessage(ChatColor.RED + " > You do not have permission to override a player's gamemode");
            }
        }
        
        return true;
    }
    
}
