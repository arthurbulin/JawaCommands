/*
 * Copyright (C) 2020 alexander
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
import java.util.LinkedList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author alexander
 */
public class Colors implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        int partitionSize = 3;
        List<List<ChatColor>> partitions = new LinkedList<List<ChatColor>>();
        List<ChatColor> ccs = Arrays.asList(ChatColor.values());
        for (int i = 0; i < ccs.size(); i += partitionSize) {
            partitions.add(ccs.subList(i, Math.min(i + partitionSize, ccs.size())));
        }
        
        commandSender.sendMessage(ChatColor.GREEN + " > Server chat color codes as they are in the code:");
        for (List<ChatColor> colorList : partitions){
            String msg = ChatColor.GREEN + " > ";
            for(ChatColor cc : colorList){
                msg += cc + cc.name() + ChatColor.WHITE + ":" + cc.getChar() + " ";
            }
            commandSender.sendMessage(msg);
        }
            
        return true;
        
    }
    
    
}
