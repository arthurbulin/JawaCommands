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
package jawamaster.jawacommands.commands.development;

import java.util.HashMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import jawamaster.jawapermissions.utils.ArgumentParser;

/**
 *
 * @author alexander
 */
public class MOTD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        String usage = "/motd -edit <message number> <message>"
                + "/motd -add <message>"
                + "/motd -del <message number>"
                + "/motd";   
        
        HashMap<String,String> parsed = ArgumentParser.getArgumentValues(args);
        //evaluate arguments
        //check permissions
        //execute values
        return true;
    }
    
}
