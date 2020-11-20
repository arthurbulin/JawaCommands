/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package jawamaster.jawacommands.commands.home;

import java.util.Arrays;
import java.util.List;
import jawamaster.jawacommands.handlers.HomeHandler;
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
    
    public static final List<String> TABCOMPLETES = Arrays.asList("delete","set","replace","info","list","help");
    public static final List<String> SECONDS = Arrays.asList("delete","set","replace","info");
    public static final List<String> NOSECONDS = Arrays.asList("list","help");
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command arg1, String arg2, String[] args) {
        Player player = (Player) commandSender;
        String[] usage = {
            ChatColor.GREEN + " > " + ChatColor.WHITE +  "/home <[d|sr|s|i|l|h]> <homename>",
            ChatColor.GREEN + " > " + ChatColor.WHITE + "Running this command without arguments will teleport you to your default home (named \"home\")",
            ChatColor.GREEN + " > " + ChatColor.WHITE + "Running /home bed will teleport you to your bed. You " + ChatColor.RED + "CANNOT" + ChatColor.WHITE + " set 'bed' as a home except by sleeping in a bed!",
            ChatColor.GREEN + " > " + ChatColor.WHITE + "Running /home <homename> will send you right to that home.",
            ChatColor.GREEN + " > " + ChatColor.WHITE + "delete (d) removes the specified home",
            ChatColor.GREEN + " > " + ChatColor.WHITE + "set (s) creates a new specified home",
            ChatColor.GREEN + " > " + ChatColor.WHITE + "replace (r) creates a new home by replacing an existing",
            ChatColor.GREEN + " > " + ChatColor.WHITE + "info (i) gives information about a specific home",
            ChatColor.GREEN + " > " + ChatColor.WHITE + "list (l) lists the available homes",
            ChatColor.GREEN + " > " + ChatColor.WHITE + "help (h) prints this help"};
        if ((args == null) || (args.length == 0)){
            boolean worked = HomeHandler.sendToHome(player, "home");
            if (!worked) player.sendMessage(ChatColor.RED + "> Error: That home doesn't exist. Run /home help for help.");
        } else {
            int action = 0;
            if (args[0].equalsIgnoreCase("delete")) action = 1;
            else if (args[0].equalsIgnoreCase("set")) action = 2;
            else if (args[0].equalsIgnoreCase("replace")) action = 3;
            else if (args[0].equalsIgnoreCase("info")) action = 4;
            else if (args[0].equalsIgnoreCase("list")) action = 5;
            else if (args[0].equalsIgnoreCase("help")) action = 6;

            switch (action) {
                case 0:
                    HomeHandler.sendToHome(player, args[0]);
                    break;
                case 1:
                    HomeHandler.removeHome(player, args[1]);
                    break;
                case 2:
                    HomeHandler.addHome(player, args[1], false);
                    break;
                case 3:
                    HomeHandler.addHome(player, args[1], true);
                    break;
                case 4:
                    HomeHandler.sendHomeInfo(player, args[1]);
                    break;
                case 5:
                    HomeHandler.sendHomeList(player);
                    break;
                case 6:
                    player.sendMessage(usage);
                    break;
            }
        }
        
//        //Evaluate input
//        if ((args == null) || (args.length == 0)){
//            boolean worked = HomeHandler.sendToHome(player, "home");
//            if (!worked) player.sendMessage(usage);
//        } else if (args.length == 1){
//            Set nots = new HashSet(Arrays.asList("-d","-sr","-s","-i","d","sr","s","i"));
//            //-h print help
//            if (args[0].equals("-h") || args[0].equals("h") || args[0].equalsIgnoreCase("help")){
//                player.sendMessage(usage);
//            }//-l list homes 
//            else if (args[0].equals("-l") || args[0].equals("l")|| args[0].equalsIgnoreCase("list")){
//                HomeHandler.sendHomeList(player);
//            } else if (nots.contains(args[0])) { //Make sure it's not one of the other flags
//                player.sendMessage(ChatColor.DARK_RED + " > That flag requires an argument! See /home -h");
//            } else { //Attempt to tp them to the location and deal with special bed case
//                if (args[0].equalsIgnoreCase("bed")) {
//                    boolean worked = LocationDataHandler.sendToBed(player);
//                    if (worked) player.sendMessage(ChatColor.GREEN + " > Sleep well.");
//                    else player.sendMessage(ChatColor.RED + " > You don't have a bed location set!");
//                } else {
//                    boolean worked = HomeHandler.sendToHome(player, args[0]);
//                    if (!worked) player.sendMessage(ChatColor.RED + " > " + args[0] + " does not exist!! /home -l for a list of homes!");
//                }
//            }
//            
//        } else if (args.length > 2) {
//            player.sendMessage(ChatColor.RED + " > Invalid argument length! Usage " + usage[0]);
//        } else if (args.length == 2) {
//            //evaluate flags
//            //ONLY ONE flag allowed at a time except sr (r is the replace flag on sethome)
//                //-d delete home
//                if (args[0].equals("-d") || args[0].equals("d")|| args[0].equalsIgnoreCase("delete")) {
//                    HomeHandler.removeHome(player, args[1]);
//                } //-s set
//                else if (args[0].equals("-s") || args[0].equals("s") || args[0].equalsIgnoreCase("set")) {
//                    if (args[1].equalsIgnoreCase("bed")) {
//                        commandSender.sendMessage(ChatColor.DARK_RED + " > You cannot set a bed home. You must sleep in a bed to do so.");
//                        return true;
//                    }
//                    if (WorldHandler.isAllowedInWorld(player.getWorld(), "homes") || player.hasPermission("homes.admin.override")) HomeHandler.addHome(player, args[1], false);
//                    else player.sendMessage(ChatColor.RED + " > You are not allowed to add homes in this world.");
//                } //-sr set and replace
//                else if (args[0].equals("-sr") || args[0].equals("sr") || args[0].equalsIgnoreCase("setreplace")) {
//                    if (WorldHandler.isAllowedInWorld(player.getWorld(), "homes") || player.hasPermission("homes.admin.override")) HomeHandler.addHome(player, args[1], true);
//                    else player.sendMessage(ChatColor.RED + " > You are not allwoed to add homes in this world.");
//                } //-i info
//                else if (args[0].equals("-i") || args[0].equals("i") || args[0].equalsIgnoreCase("info")) {
//                    HomeHandler.sendHomeInfo(player, args[1]);
//                } else { //Something trippy
//                    player.sendMessage(ChatColor.DARK_RED + " > Error! " + args[0] + " is not a valid flag! Run /home -h for help!");
//                }
//        } else {
//            //wtf? idk 
//            System.out.println("wtf?");
//        }
        
        return true;
    }
    
}
