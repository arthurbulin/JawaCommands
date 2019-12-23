/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package jawamaster.jawacommands.commands.home;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import jawamaster.jawacommands.handlers.HomeHandler;
import jawamaster.jawacommands.handlers.LocationDataHandler;
import jawamaster.jawacommands.handlers.WorldHandler;
import org.bukkit.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/** This command will allow a user to create, delete, list, and view detailed information
 * about their home data. It contains only help data and if-else logic for logical resolution.
 * All actions are carried out in their respective helper class.
 * @author Arthur Bulin
 */
public class Home implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command arg1, String arg2, String[] args) {
        Player player = (Player) commandSender;
        String[] usage = {
            ChatColor.GREEN + " > " + ChatColor.WHITE +  "/home <[d|sr|s|i|l|h]> <homename>",
            ChatColor.GREEN + " > " + ChatColor.WHITE + "Running this command without arguments will teleport you to your default home (named \"home\")",
            ChatColor.GREEN + " > " + ChatColor.WHITE + "Running /home bed will teleport you to your bed. You " + ChatColor.RED + "CANNOT" + ChatColor.WHITE + " set 'bed' as a home except by sleeping in a bed!",
            ChatColor.GREEN + " > " + ChatColor.WHITE + "Running /home <homename> will send you right to that home.",
            ChatColor.GREEN + " > " + ChatColor.WHITE + "d removes the specified home",
            ChatColor.GREEN + " > " + ChatColor.WHITE + "s creates a new specified home",
            ChatColor.GREEN + " > " + ChatColor.WHITE + "sr creates a new home by replacing an existing",
            ChatColor.GREEN + " > " + ChatColor.WHITE + "i gives information about a specific home",
            ChatColor.GREEN + " > " + ChatColor.WHITE + "l lists the available homes",
            ChatColor.GREEN + " > " + ChatColor.WHITE + "h prints this help"};
        
        //Evaluate input
        if ((args == null) || (args.length == 0)){
            boolean worked = HomeHandler.sendToHome(player, "home");
            if (!worked) player.sendMessage(usage);
        } else if (args.length == 1){
            Set nots = new HashSet(Arrays.asList("-d","-sr","-s","-i","d","sr","s","i"));
            //-h print help
            if (args[0].equals("-h") || args[0].equals("h")){
                player.sendMessage(usage);
            }//-l list homes 
            else if (args[0].equals("-l") || args[0].equals("l")){
                HomeHandler.sendHomeList(player);
            } else if (nots.contains(args[0])) { //Make sure it's not one of the other flags
                player.sendMessage(ChatColor.DARK_RED + " > That flag requires an argument! See /home -h");
            } else { //Attempt to tp them to the location and deal with special bed case
                if (args[0].equalsIgnoreCase("bed")) {
                    boolean worked = LocationDataHandler.sendToBed(player);
                    if (worked) player.sendMessage(ChatColor.GREEN + " > Sleep well.");
                    else player.sendMessage(ChatColor.RED + " > You don't have a bed location set!");
                } else {
                    boolean worked = HomeHandler.sendToHome(player, args[0]);
                    if (!worked) player.sendMessage(ChatColor.RED + " > " + args[0] + " does not exist!! /home -l for a list of homes!");
                }
            }
            
        } else if (args.length > 2) {
            player.sendMessage(ChatColor.RED + " > Invalid argument length! Usage " + usage[0]);
        } else if (args.length == 2) {
            //evaluate flags
            //ONLY ONE flag allowed at a time except sr (r is the replace flag on sethome)
                //-d delete home
                if (args[0].equals("-d") || args[0].equals("d")) {
                    HomeHandler.removeHome(player, args[1]);
                } //-s set
                else if (args[0].equals("-s") || args[0].equals("s")) {
                    if (WorldHandler.isAllowedInWorld(player.getWorld(), "homes") || player.hasPermission("homes.admin.override")) HomeHandler.addHome(player, args[1], false);
                    else player.sendMessage(ChatColor.RED + " > You are not allwoed to add homes in this world.");
                } //-sr set and replace
                else if (args[0].equals("-sr") || args[0].equals("sr")) {
                    if (WorldHandler.isAllowedInWorld(player.getWorld(), "homes") || player.hasPermission("homes.admin.override")) HomeHandler.addHome(player, args[1], true);
                    else player.sendMessage(ChatColor.RED + " > You are not allwoed to add homes in this world.");
                } //-i info
                else if (args[0].equals("-i") || args[0].equals("i")) {
                    HomeHandler.sendHomeInfo(player, args[1]);
                } else { //Something trippy
                    player.sendMessage(ChatColor.DARK_RED + " > Error! " + args[0] + " is not a valid flag! Run /home -h for help!");
                }
        } else {
            //wtf? idk 
            System.out.println("wtf?");
        }
        
        return true;
    }
    
}
