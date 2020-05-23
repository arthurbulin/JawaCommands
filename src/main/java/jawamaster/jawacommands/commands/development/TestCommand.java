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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author alexander
 */
public class TestCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command arg1, String arg2, String[] args) {
    
        
//        JSONObject obj = new JSONObject();
//        
//        Material[] mats = Material.values();
//        for (Material mat : mats){
//            JSONObject tmp = new JSONObject();
//            tmp.put("name", mat.name());
//            tmp.put("tostring", mat.toString());
//            tmp.put("isitem", mat.isItem());
//            tmp.put("key", mat.getKey());
//            tmp.put("issolid", mat.isSolid());
//            tmp.put("record", mat.isRecord());
//            tmp.put("occluding", mat.isOccluding());
//            tmp.put("interactable", mat.isInteractable());
//            tmp.put("edible", mat.isEdible());
//            tmp.put("fuel", mat.isFuel());
//            tmp.put("flamable", mat.isFlammable());
//            tmp.put("burnable", mat.isBurnable());
//            tmp.put("block", mat.isBlock());
//            tmp.put("air", mat.isAir());
//            if (mat.isBlock()){
//            tmp.put("hardness", mat.getHardness());
//            tmp.put("blastresistance", mat.getBlastResistance());
//            }               
//            
//            obj.put(mat.name(), tmp);
//        }
//        System.out.println("test");
//        File JSONFile = new File(JawaCommands.getPlugin().getDataFolder() + "/material_burger.json");
//
//        if (!JSONFile.exists()){
//            try {
//                JSONFile.createNewFile();
//            } catch (IOException ex) {
//                Logger.getLogger(TestCommand.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        System.out.println("test");
//        try ( //open our writer and write the player file
//                PrintWriter writer = new PrintWriter(JSONFile)) {
//            //System.out.println(obj);
//            writer.print(obj.toString(4));
//            writer.close();
//            //Logger.getLogger(MOTDHandler.class.getName()).log(Level.INFO, "Successfully updated motd.json");
//            return true;
//        } catch (FileNotFoundException ex) {
//
//            //Logger.getLogger(MOTDHandler.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return true;
    }
    
}
