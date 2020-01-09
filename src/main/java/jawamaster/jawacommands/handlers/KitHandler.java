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
package jawamaster.jawacommands.handlers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import jawamaster.jawacommands.JawaCommands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.JSONArray;
import org.json.JSONObject;


/** Handles all kit creations, manipulations, and deletions.
 *
 * @author alexander
 */
public class KitHandler {
    
    private static final String TOOMANYOPTIONS = ChatColor.RED + " > Error! You have too many optiongs for your flag!";
    private static final String TOOFEWOPTIONS = ChatColor.RED + " > Error! You don't have enough options for your flag!";
    private static final String NOTEXISTINGKIT = ChatColor.RED + " > Error: That is not an existing kit! Create it with /kit -c <name>";
    
    /** Creates a kit with default values.
     * @param kitName 
     * @param commandSender 
     * @return  
     */
    public static boolean createKit(String kitName, CommandSender commandSender){
        if (JawaCommands.kits.has(kitName)) {
            commandSender.sendMessage(ChatColor.RED + " Error: That kit already exists! To recreate it remove it first!");
            return false;
        } else {

            JSONObject newKit = new JSONObject();

            newKit.put("items", new JSONObject());
            newKit.put("single_use", false);
            newKit.put("cooldown", 0);

            JawaCommands.kits.put(kitName, newKit);
            saveKits();
            commandSender.sendMessage(ChatColor.GREEN + " > Kit "  + ChatColor.BLUE + kitName + ChatColor.GREEN + " created!");
            return true;
        }
    }
    
    /** Removes a kit from JawaCommands.kits and saves the list to file.
     * @param kitName 
     * @param commandSender 
     * @return  
     */
    public static boolean removeKit(String kitName, CommandSender commandSender){
        if (!JawaCommands.kits.has(kitName)) {
            commandSender.sendMessage(NOTEXISTINGKIT);
            return false;
        } else {
            JawaCommands.kits.remove(kitName);
            saveKits();
            commandSender.sendMessage(ChatColor.GREEN + " > Kit "  + ChatColor.BLUE + kitName + ChatColor.GREEN + " removed!");
            return true;
        }
    }

    /** Loads the /kits.json file into JawaCommands.kits.
     * @return 
     */
    public static JSONObject loadKits(){
        JSONObject kits = JSONHandler.LoadJSONConfig("/kits.json");
        if (kits == null) return new JSONObject();
        else return kits;
    }
    
    /** Triggers the JawaCommands.kits JSONObject to be saved to file.
     */
    public static void saveKits(){
        JSONHandler.WriteJSONToFile("/kits.json", JawaCommands.kits);
        Logger.getLogger(JSONHandler.class.getName()).log(Level.INFO, "kits saved to file");
    }
    
    /** Validates a material. Will return a Material if valid or null if invalid.
     * @param material
     * @return 
     */
    private static Material validateMinecraftItem(String materialString){
        
        Material[] materials = Material.values();
        for (Material mat : materials){
            System.out.println("Name:" + mat.toString());
        }
        
        Material material = Material.matchMaterial(materialString);
        
        if (material == null) {
            material = Material.getMaterial(materialString);
        }
        
        return material;
    }
    
    /** Modifies the flags on a kit as well as handles error returning. Valid flags are:
     * cooldown: minutes, single_use: boolean, message: String, announce: String.
     * @param kitName
     * @param flags
     * @param commandSender
     * @return 
     */
    public static boolean modifyKitFlags(String kitName, String flags, CommandSender commandSender){
        String[] input = flags.split(" ");
        /*Flags:
        cooldown: minutes
        single_use: boolean
        message: String
        anounce: String
        */
        HashSet<String> acceptedFlags = new HashSet(Arrays.asList("cooldown","single_use","message","announce"));
        
        //Error evaluation of input
        if (!JawaCommands.kits.has(kitName)) {
            commandSender.sendMessage(NOTEXISTINGKIT);
            return false;
        }
        if (input.length < 2) {
            commandSender.sendMessage(TOOFEWOPTIONS);
            return false;
        }

        //Determine which flag is being modified
        switch (input[0]) {
            
            case "cooldown": {
                
                if (input.length > 2) { //Length Evaluation
                    commandSender.sendMessage(TOOMANYOPTIONS);
                    return false;
                }
                try { //Verify that it is an integer
                    Integer.valueOf(input[1]);
                } catch (NumberFormatException ex) {
                    commandSender.sendMessage(ChatColor.RED + " > Error: Cooldown time must be an integer number!");
                    return false;
                }
                
                
                //Perform the modification
                JawaCommands.kits.getJSONObject(kitName).put("cooldown", Integer.valueOf(input[1]));
                //If not already tracking who is using add the JSONObject otherwise continue
                if (!JawaCommands.kits.getJSONObject(kitName).has("used_by")) JawaCommands.kits.getJSONObject(kitName).put("used_by", new JSONObject());
                break;
            }
            
            case "single_use": {
                
                if (input.length > 2) { //Length Evaluation
                    commandSender.sendMessage(TOOMANYOPTIONS);
                    return false;
                }
                if (!input[1].equalsIgnoreCase("true") && !input[1].equalsIgnoreCase("false")){ //verify that it is a string that can be evaluated to a boolean
                    commandSender.sendMessage(ChatColor.RED + " > Error: single_use must be a boolean, either true or false.");
                    return false;
                }
                
                //Perfrom the modification
                JawaCommands.kits.getJSONObject(kitName).put("single_use", Boolean.valueOf(input[1]));
                if (!JawaCommands.kits.getJSONObject(kitName).has("used_by")) JawaCommands.kits.getJSONObject(kitName).put("used_by", new JSONObject());
                break;
            }
            
            case "message" : {
                if (input[1].equalsIgnoreCase("none") && JawaCommands.kits.getJSONObject(kitName).has("message")) JawaCommands.kits.getJSONObject(kitName).remove("message");
                else if (!input[1].equalsIgnoreCase("none")) {
                    //put message
                    JawaCommands.kits.getJSONObject(kitName).put("message", Arrays.copyOfRange(input, 1, input.length));
                }
                break;
            }
            
            case "announce" : {
                if (input[1].equalsIgnoreCase("none") && JawaCommands.kits.getJSONObject(kitName).has("announce")) JawaCommands.kits.getJSONObject(kitName).remove("announce");
                else if (!input[1].equalsIgnoreCase("none")) {
                    //put message
                    JawaCommands.kits.getJSONObject(kitName).put("announce", Arrays.copyOfRange(input, 1, input.length));
                }
                break;
            }
            default: {
                commandSender.sendMessage(ChatColor.RED + " > Error! That flag isnt understood!");
                return false;
            }
         }
        
        saveKits();
        return true;
    }

    /** This is backed by modifyKitItems(String kitName, Material material, int count). It will validate the String values
     * passed to it and return an error message to the commandSender if it is not parsable.
     * @param kitName
     * @param values
     * @param commandSender
     * @return 
     */
    public static boolean modifyKitItems(String kitName, String values, CommandSender commandSender){
        //Format some things
        //Should be of the form <item name/id> <count>
        String[] input = values.split(" ");
        System.out.println("material: " + input[0]);
        
        Material.values();
        
        Material material = validateMinecraftItem(input[0]);
        
        if (material == null) {
            commandSender.sendMessage(ChatColor.RED + " > Error: Invalid material id or name!");
            return false;
        }
        
        //Error handling 
        if (input.length != 2){
            commandSender.sendMessage(TOOFEWOPTIONS);
            return false;
        }
        if (!JawaCommands.kits.has(kitName)) {
            commandSender.sendMessage(NOTEXISTINGKIT);
            return false;
        }
        try {
            Integer.valueOf(input[1]);
        } catch (NumberFormatException ex) {
            commandSender.sendMessage(ChatColor.RED + " > Error: Item quantity must be an integer number!");
            return false;
        }
        if (Integer.valueOf(input[1])< 0){
            commandSender.sendMessage(ChatColor.RED + " > Error: Item quantity must be an integer number 0 or greater!");
            return false;
        }
        
        modifyKitItems(commandSender, kitName, material, Integer.valueOf(input[1]));
        return true;
        
    }
    
    /** *  Adds or removes specified items.The String kitName is the name of the kit, 
 the material should already be validated, and the int count should be positive
 or 0. If these things are not done then there may be unknown behavior.
     * @param commandSender
     * @param kitName
     * @param material
     * @param count 
     */
    public static void modifyKitItems(CommandSender commandSender, String kitName, Material material, int count){
        if (count == 0) { //remove the items
            JawaCommands.kits.getJSONObject(kitName).getJSONObject("items").remove(material.toString());
            commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + material.toString() + ChatColor.GREEN + " has been removed from " + ChatColor.BLUE + kitName);
        } else {
            JawaCommands.kits.getJSONObject(kitName).getJSONObject("items").put(material.toString(), count);
            commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + material.toString() + ChatColor.GREEN + " has been added to " + ChatColor.BLUE + kitName);
        }
        saveKits();
    }
    
    /** gives the specified kit to the players listed if they are online. If they are
     * not online an error message is sent for each player not online. Those that are online
     * are given the kit.
     * @param commandSender
     * @param kitName
     * @param players
     * @return 
     */
    public static boolean giveKit(CommandSender commandSender, String kitName, String[] players){
        
        if (!JawaCommands.kits.has(kitName)) {
            commandSender.sendMessage(NOTEXISTINGKIT);
            return false;
        }
        
        HashSet<Player> playerSet = new HashSet();
        for (String playerString : players){
            Player player = Bukkit.getPlayer(playerString);
            if (player != null) {
                playerSet.add(player);
            } else {
                commandSender.sendMessage(ChatColor.RED + " Error: " + playerString + " is not a valid online player.");
            }
        }
        
        if (!playerSet.isEmpty()){
            playerSet.forEach((player) -> {
                giveKit(kitName, commandSender, player, false);
            });
            return true;
        } else return false;
             
    }
    
    /** Give a player a specific kit. The String kitName will point to the specified kit,
     * commandSender is the one sending the command (player, command block, console), 
     * and the target is the player it is being given too. The boolean override allows the
     * single_use flag to overridden.
     * @param kitName
     * @param commandSender
     * @param target
     * @param overrideSingleUse
     * @return 
     */
    public static boolean giveKit(String kitName, CommandSender commandSender, Player target, boolean overrideSingleUse) {
        
        if (!JawaCommands.kits.has(kitName)) {
            commandSender.sendMessage(NOTEXISTINGKIT);
            return false;
        }
        
        if (JawaCommands.kits.getJSONObject(kitName).getJSONObject("items").isEmpty()) {
            commandSender.sendMessage(ChatColor.RED + " > Error: That kit has no items in it!! Please add items first!");
        }
        
        if (JawaCommands.kits.getJSONObject(kitName).getBoolean("single_use") && JawaCommands.kits.getJSONObject(kitName).getJSONObject("used_by").has(target.getUniqueId().toString()) && !overrideSingleUse) {
            if ((commandSender instanceof CommandBlock) || (commandSender instanceof Player && ((Player) commandSender).getUniqueId().equals(target.getUniqueId()))){
                target.sendMessage(ChatColor.RED + " > This kit is one time only! You already used it!");
            } else {
                commandSender.sendMessage(ChatColor.RED + " > That player has already used this one time kit!");
            }
            return false;
        }
        
        if ((JawaCommands.kits.getJSONObject(kitName).getInt("cooldown") > 0) && JawaCommands.kits.getJSONObject(kitName).getJSONObject("used_by").has(target.getUniqueId().toString())) {
            LocalDateTime coolDownAt = LocalDateTime.parse(JawaCommands.kits.getJSONObject(kitName).getJSONObject("used_by").getString(target.getUniqueId().toString())).plusMinutes(JawaCommands.kits.getJSONObject(kitName).getInt("cool_down"));
            if (!coolDownAt.isBefore(LocalDateTime.now())) {
                if ((commandSender instanceof CommandBlock) || (commandSender instanceof Player && ((Player) commandSender).getUniqueId().equals(target.getUniqueId()))) {
                    target.sendMessage(ChatColor.RED + " > This kit is one time only! You already used it!");
                } else {
                    commandSender.sendMessage(ChatColor.RED + " > That player has already used this one time kit!");
                }
                return false;
            }
        }
                
        ItemStack[] itemStack = generateItemStack(JawaCommands.kits.getJSONObject(kitName).getJSONObject("items"));
        itemStack = giveItemsToPlayer(itemStack, target);
        handleNotGivenItems(itemStack, target);
        return true;
    }
    
    /** Creates an item stack from a JSONObject containing item names as the keys
     * and quantities as the values. This returns an ItemStack[]
     * @param items
     * @return 
     */
    private static ItemStack[] generateItemStack(JSONObject items){
        HashSet<ItemStack> itemsArray = new HashSet();
        ItemStack[] itemStack = new ItemStack[items.length()];
        items.keySet().forEach((item) -> {
            itemsArray.add(new ItemStack(Material.matchMaterial(item),items.getInt(item)));
        });
        
        return itemsArray.toArray(itemStack);
    }
    
    /** Give items to a player. The items that will not fit into a player's inventory
     * are returned as an ItemStack[].
     * @param items
     * @param target
     * @return 
     */
    private static ItemStack[] giveItemsToPlayer(ItemStack[] items, Player target){
        HashMap<Integer,ItemStack> notGiven = target.getInventory().addItem(items);
        return notGiven.values().toArray(new ItemStack[notGiven.size()]);
    }
    
    /** Will spawn items that won't fit in a player's inventory at their feet.
     * @param notGiven
     * @param target 
     */
    private static void handleNotGivenItems(ItemStack[] notGiven, Player target){
        Location loc = target.getLocation();
        for (ItemStack stack : notGiven){
            target.getWorld().dropItem(loc, stack);
        }
    }
    
    public static void sendHelpMessage(CommandSender commandSender){
        String[] usage = new String[]{
            ChatColor.GREEN + "To get a kit:" + ChatColor.WHITE + " /kit <kit name>",
            ChatColor.GREEN + "To create a kit:" + ChatColor.WHITE + " /kit -c <kit name>",
            ChatColor.GREEN + "To add items to a kit:" + ChatColor.WHITE + " /kit -m <kit name> -a <item id> <count to give>",
            ChatColor.GREEN + "To add flags to a kit:" + ChatColor.WHITE + " /kit -m <kit name> -f <flag> [flag options]",
            ChatColor.GREEN + "To display kit help:" + ChatColor.WHITE + " /kit -h",
            ChatColor.GREEN + "To give a kit to someone:" + ChatColor.WHITE + " /kit -g <kit> <player1 player2 ...>",
            ChatColor.GREEN + "To list kits:" + ChatColor.WHITE + " /kit -l"};
        commandSender.sendMessage(usage);
    }
    
    public static void listKits(CommandSender sender){
        
    }
    
    
    

    
}
