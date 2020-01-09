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
package jawamaster.jawacommands.commands.development;

import jawamaster.jawacommands.JawaCommands;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.json.JSONArray;

/**
 *
 * @author alexander
 */
public class BackHandler {
    
    public static void addUserBackLocation(Player target, Location loc){
        if (JawaCommands.backStack.containsKey(target.getUniqueId())){
            JawaCommands.backStack.get(target.getUniqueId()).put(loc);
        } else {
            JSONArray newStack = new JSONArray();
            newStack.put(loc);
            JawaCommands.backStack.put(target.getUniqueId(), newStack);
        }
    }
    
    public static void removeUserBackLocation(Player target) {
        if (JawaCommands.backStack.get(target.getUniqueId()).length() == 1) {
            JawaCommands.backStack.remove(target.getUniqueId());

        } else {
            JawaCommands.backStack.get(target.getUniqueId()).remove(JawaCommands.backStack.get(target.getUniqueId()).length() - 1);
        }
    }
    
    public static Location getUserBackLocation(Player target){
        if (JawaCommands.backStack.containsKey(target.getUniqueId())){
            return (Location) JawaCommands.backStack.get(target.getUniqueId()).get(JawaCommands.backStack.get(target.getUniqueId()).length()-1);
        } else {
            return null;
        }
    }
    
    
    public static boolean backAllowedInWorld(){
        //TODO fix this
        return true;
    }
    
    
    
}
