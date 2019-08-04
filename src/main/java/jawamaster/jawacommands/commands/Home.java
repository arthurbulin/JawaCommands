/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package jawamaster.jawacommands.commands;

import jawamaster.jawacommands.handlers.ESHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Arthur Bulin
 */
public class Home implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command arg1, String arg2, String[] arg3) {
        Player player = (Player) commandSender;
        switch(arg3.length){
            case 1:
                //check for command or home name
                if (arg3[0].startsWith("-")){
                    //Perform list command
                    if (arg3[0].startsWith("-l")) { //action 1
                        ESHandler.getHomeData(player, null, 1);
                        return true;
                    } else {
                        //Command not understood
                    }
                } else { //Action 0
                    //TODO assume this is a homename and take appropriate action
                    ESHandler.getHomeData(player, arg3[0], 0);
                }
                break;
                
            case 2:
                //TODO check for command AND home name
                if (arg3[0].startsWith("-d")) { //action 3
                    //perform delete command
                    ESHandler.getHomeData(player, arg3[1], 3);
                } else if (arg3[0].startsWith("-a")){ //Action 2
                    //add home to list
                    ESHandler.getHomeData(player, arg3[1], 2);
                } else {
                    //Command not understood
                }
                break;
            
            case 0: //Action 4
                ESHandler.getHomeData(player, "home", 4);
                break;
                
            default:
                //TODO they did something wierd
                break;
                
        }
        return true;
    }
    
}
