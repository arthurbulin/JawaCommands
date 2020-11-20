package jawamaster.jawacommands.commands.warps;

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
import java.util.ArrayList;
import java.util.List;
import jawamaster.jawacommands.handlers.WarpHandler;
import net.jawasystems.jawacore.PlayerManager;
import net.jawasystems.jawacore.utils.TargetSelectorProcessor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author alexander
 */
public class WarpCommand implements CommandExecutor {

    public final String OTHERPERMISSION = "jawacommands.warp.other";
    public final String USAGE = ChatColor.GREEN + "> /warp <warp> [other|@p|@a] [override]. Run without any arguments to list the warps you have access to.";
    private List<Player> targets = new ArrayList();
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        //if no args print list of warps

        if (args == null || args.length == 0) {
            WarpHandler.listWarps((Player) commandSender);
            return true;
        } else if (args.length == 1) {
            if (WarpHandler.warpExists(args[0]) && WarpHandler.canVisit(commandSender, args[0])){
                WarpHandler.sendPlayer((Player) commandSender, args[0]);
            } else {
                commandSender.sendMessage(ChatColor.RED + "> Error: That warp does not exist or you do not haver permission to visit it.");
            }
        } else if (args.length == 2 && commandSender.hasPermission(OTHERPERMISSION)){ // Send a player to a warp with respect to the player's permissions
            if (resolveSelectors(args[1], commandSender)) {            
                if (WarpHandler.warpExists(args[0])) {
                    targets.forEach((target) -> {
                        if (WarpHandler.canVisit(target, args[0])) {
                            WarpHandler.sendPlayer(target, args[0]);
                        } else {
                            target.sendMessage(ChatColor.RED + " Error: You do not have permission to visit this warp.");
                        }
                    });
                }
            }
        } else if (args.length > 3 && commandSender.hasPermission(OTHERPERMISSION)) { // send a player to a warp with no respect to the player's permissions
            if (args[2].equalsIgnoreCase("override")) {
                if (resolveSelectors(args[1], commandSender)) {
                    if (WarpHandler.warpExists(args[0])) {
                        targets.forEach((target) -> {
                            WarpHandler.sendPlayer(target, args[0]);
                        });
                    }
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + " Error: Invalid argument. The final argument must be 'override' for warp commands of this length.");
            }
        } else {
            commandSender.sendMessage(ChatColor.RED + " Error: You do not have permission to perform this command in this manner.");
        }
        
        
//        
//        if (args == null || args.length == 0) {
//            WarpHandler.listWarps((Player) commandSender);
//        } else if (args.length == 1 || (args.length > 1 && commandSender.hasPermission(OTHERPERMISSION))) {
//
//            //If the warp exists
//            if (!WarpHandler.warpExists(args[0])) {
//                commandSender.sendMessage(ChatColor.RED + "> Error: That is not a valid warp!");
//                return true;
//            } 
//            else if (args.length >= 1) {
//                List<Player> targets = new ArrayList();
//
//                if (args.length == 1) {
//                    targets.add((Player) commandSender);
//                } 
//                //Resolving @p functions. There is really no reason to ever do this as a player so I will assume this is a CommandBlock if need be
//                else if (args.length > 1 && args[1].matches("@[p|P].*")) {
//                    Player target = TargetSelectorProcessor.getAtP(args[1], commandSender);
//                    if (target != null) targets.add(target);
//                } 
//                
//                else if (args.length > 1 && args[1].matches("@[a|A].*")){
//                    targets = TargetSelectorProcessor.getAtA(args[1], commandSender);
//                }
//                //Assuming that this is a player if need be
//                else {
//                    for (String ply : Arrays.copyOfRange(args, 1, args.length + 1)) {
//                        Player target = PlayerManager.getPlayer(ply);
//                        if (target != null) {
//                            targets.add(target);
//                        } else {
//                            commandSender.sendMessage(PlayerManager.NOPLAYERERROR + " " + ply);
//                        }
//                    }
//                }
//
//                if (!targets.isEmpty()) {
//                    targets.forEach((targeted) -> {
//                        WarpHandler.sendPlayer(targeted, args[0]);
//                    });
//                }
//                return true;
//            }
//        } else {
//            commandSender.sendMessage(ChatColor.RED + "> You do not have permission to warp another player.");
//            return true;
//        }

//        if ((args == null) || (args.length == 1)) {
//            WarpHandler.listWarps((Player) commandSender);
//        } else if (args.length == 1) {
//            if (WarpHandler.warpExists(args[0])) {
//                boolean worked = JawaCommands.getWarpIndex().get(args[0]).sendPlayer((Player) commandSender);
//                if (worked) {
//                    commandSender.sendMessage(ChatColor.GREEN + " > Warping!");
//                } else {
//                    commandSender.sendMessage(ChatColor.RED + " > You don't have access to that!");
//                }
//            } else {
//                commandSender.sendMessage(ChatColor.RED + " > That warp doesn't exist!");
//            }
//        } else if(args.length == 2) {
//            if (commandSender.hasPermission(OTHERPERMISSION) && JawaCommands.getWarpIndex().containsKey(args[0])){
//                Player target = Bukkit.getPlayer(args[1]);
//                if (target != null){
//                    boolean worked = JawaCommands.getWarpIndex().get(args[0]).sendPlayer(target);
//                } else {
//                    commandSender.sendMessage(ChatColor.RED + " > Error: That is not a valid player!");
//                }
//            } else if(!commandSender.hasPermission("jawacommands.warp.other")){
//                commandSender.sendMessage(ChatColor.RED + " > Error: You do not have permission to warp another player!");
//            }
//        }
//        else {
//            commandSender.sendMessage(ChatColor.RED + " > Too many arguments!");
//        }
        return true;
    }
    
    private boolean resolveSelectors(String selector, CommandSender commandSender){
        if (!selector.matches("@[p|P|a|A].*")) {
            Player target = PlayerManager.getPlayer(selector);
            if (target == null) {
                commandSender.sendMessage(PlayerManager.NOPLAYERERROR);
            } else {
                targets.add(target);
            }
        } else if (selector.matches("@[p|P].*")) {
            Player target = TargetSelectorProcessor.getAtP(selector, commandSender);
            if (target != null) {
                targets.add(target);
            }
        } else if (selector.matches("@[a|A].*")) {
            targets = TargetSelectorProcessor.getAtA(selector, commandSender);
        }
        return !targets.isEmpty();
    }

}
