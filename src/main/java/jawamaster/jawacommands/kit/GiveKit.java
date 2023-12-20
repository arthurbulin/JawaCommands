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
package jawamaster.jawacommands.kit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.jawasystems.jawacore.PlayerManager;
import net.jawasystems.jawacore.utils.TargetSelectorProcessor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Jawamaster (Arthur Bulin)
 */
public class GiveKit implements CommandExecutor {

    public static final String[] USAGE = {ChatColor.GREEN + "> /givekit useage",
            ChatColor.GREEN + " > /givekit <kitname> <player|players|@p|@a>" + ChatColor.BLUE + " - Gives <kitname> to <player|players|@p|@a>. This respects the kit configured rules.",
            ChatColor.GREEN + " > /givekit <kitname> override <message,announce,cooldown,singleuse,permission,all> <player|players|@p|@a>" + ChatColor.BLUE + " - Gives a kit and overrides the listed configurations.",
            ChatColor.GREEN + "  > " + ChatColor.BLUE + "Override values can be put into a list. Ex: message,announce or cooldown"
            //ChatColor.GREEN + " > /givekit help" + ChatColor.BLUE + " - Prints this help message."
            };
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        
        if (args.length <= 1){
            for (String msg : USAGE){
                commandSender.sendMessage(msg);
            }
        } else {
            //If kit exists
            if (KitHandler.validateKit(args[0], commandSender)) {
                List<String> correctedOverrides = new ArrayList();
                List<Player> players;
                //If overriding
                if (args[1].equalsIgnoreCase("override")){
                    
                    String[] overrides = args[2].split(",");
//                    if (args[2].contains(",")){
//                        overrides.addAll(Arrays.asList(args[2].split(",")));
//                    } else {
//                        overrides.add(args[2]);
//                    }
                    
                    //List<String> correctedOverrides = new ArrayList();
                    for (String override : overrides) {
                        if (override.matches("(?i)mes?s?a?g?e?"))
                            correctedOverrides.add("MESSAGE");
                        else if (override.matches("(?i)ann?o?u?n?c?e?"))
                            correctedOverrides.add("ANNOUNCE");
                        else if (override.matches("(?i)co?o?l?d?o?w?n?"))
                            correctedOverrides.add("COOLDOWN");
                        else if (override.matches("(?i)si?n?g?l?e?u?s?e?"))
                            correctedOverrides.add("SINGLEUSE");
                        else if (override.matches("(?i)pe?r?m?i?s?s?i?o?n?"))
                            correctedOverrides.add("PERMISSION");
                        else if (override.matches("(?i)all"))
                            correctedOverrides.add("ALL");
                        else {
                            commandSender.sendMessage(ChatColor.RED + "> Error: " + override + " is not a valid override flag");
                            return true;
                        }
                        
                    }
                    players = resolvePlayers(commandSender, Arrays.copyOfRange(args, 3, args.length));
                } else {
                    players = resolvePlayers(commandSender, Arrays.copyOfRange(args, 1, args.length));
                }
                
                if (!players.isEmpty()) {
                    KitHandler.giveKit(commandSender, args[0], players, correctedOverrides);
                }
                
            }
            //Nothing else to do since validateKit sends message
        }
        
        return true;
    }
    
    private List<Player> resolvePlayers(CommandSender commandSender, String[] args){
        List<Player> targets = new ArrayList();
        for (String target : args){
            if (target.matches("@[p|P]*")) {
                Player ply = TargetSelectorProcessor.getAtP(target, commandSender);
                if (ply != null) targets.add(ply);
                else commandSender.sendMessage(ChatColor.RED + "> Error: " + target + " is not valid");
            } else if (target.matches("@[a|A]*")) {
                List<Player> plys = TargetSelectorProcessor.getAtA(target, commandSender);
                if (plys != null) targets.addAll(plys);
                else commandSender.sendMessage(ChatColor.RED + "> Error: " + target + " is not valid");
            } else {
                Player ply = PlayerManager.getPlayer(target);
                if (ply != null) targets.add(ply);
                else commandSender.sendMessage(ChatColor.RED + "> Error: " + target + " is not valid");
            }
                
        }
        return targets;
    }
    
}
