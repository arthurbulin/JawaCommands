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
package jawamaster.jawacommands.kit;

import net.jawasystems.jawacore.handlers.JSONHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import jawamaster.jawacommands.JawaCommands;
import net.jawasystems.jawacore.handlers.MaterialHandler;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONObject;

/**
 * Handles all kit creations, manipulations, and deletions.
 *
 * @author alexander
 */
public class KitHandler {

    private static final Logger LOGGER = Logger.getLogger("KitHandler");
    private static final String TOOMANYOPTIONS = ChatColor.RED + " > Error: You have too many optiongs for your flag!";
    private static final String TOOFEWOPTIONS = ChatColor.RED + " > Error: You don't have enough options for your flag!";
    private static final String NOTEXISTINGKIT = ChatColor.RED + " > Error: That is not an existing kit! Create it with /kit -c <name>";
    private static final String INVALIDFLAG = ChatColor.RED + " > Error: That isn't a valid flag!";
    
    private static final HashMap<String,Kit> KITS = new HashMap();
    
    private static List<BaseComponent[]> listMessage;
    public static final String KITBASEPERM = "jawacommands.kit";
    public static final String KITADMINPERM = "jawacommands.kit.admin";

    /** Creates a blank kit with default values. This creates the kit object in a 
     * temporary state.It contains no items or configurations, only Metadata, if
     * it is not modified it will be wiped out on next reload.
     * @param player
     * @param kitName
     * @return
     */
    public static boolean createKit(Player player, String kitName) {
        if (KITS.containsKey(kitName)) {
            player.sendMessage(ChatColor.RED + "> Error: That kit already exists! To recreate it remove it first or just modify it!");
            return false;
        } else {
            KITS.put(kitName, new Kit(kitName, player));
            saveKits();
            player.sendMessage(ChatColor.GREEN + "> Kit " + ChatColor.BLUE + kitName + ChatColor.GREEN + " created!");
            return true;
        }
    }
    
    /** Creates a blank kit with default values. This creates the kit object in a 
     * temporary state.It contains no items or configurations, only Metadata, if
     * it is not modified it will be wiped out on next reload.
     * @param player
     * @param kitName
     * @param description
     * @return
     */
    public static boolean createKit(Player player, String kitName, String description) {
        if (KITS.containsKey(kitName)) {
            player.sendMessage(ChatColor.RED + "> Error: That kit already exists! To recreate it remove it first or just modify it!");
            return false;
        } else {
            KITS.put(kitName, new Kit(kitName, player, description));
            saveKits();
            player.sendMessage(ChatColor.GREEN + "> Kit " + ChatColor.BLUE + kitName + ChatColor.GREEN + " created!");
            return true;
        }
    }

    /** Removes a kit from KITS, JSONizes the data and saves the resulting object.
     * TODO move this process into its own thread.
     * @param kitName
     * @param commandSender
     * @return
     */
    public static boolean removeKit(String kitName, CommandSender commandSender) {
        if (!KITS.containsKey(kitName)) {
            commandSender.sendMessage(NOTEXISTINGKIT);
            return false;
        } else {
            KITS.remove(kitName);
            saveKits();
            commandSender.sendMessage(ChatColor.GREEN + " > Kit " + ChatColor.BLUE + kitName + ChatColor.GREEN + " removed!");
            return true;
        }
    }
    
    public static void modifyKit(Player player, String kitName, String flag, String flagValue){
        if (validateKit(kitName, player)) {
            switch (flag) {
                case "COOLDOWN":
                    try {
                        int value = Integer.valueOf(flagValue);
                        KITS.get(kitName).setCoolDown(value);
                        completeModify(player, kitName, flag, flagValue);
                    } catch (NumberFormatException ex) {
                        player.sendMessage(ChatColor.RED + "> Error: The value for COOLDOWN must be an integer!");
                    }
                    break;
                case "DESCRIPTION":
                    KITS.get(kitName).setDescription(flagValue);
                    completeModify(player, kitName, flag, flagValue);
                    break;
                case "SINGLEUSE":
                    KITS.get(kitName).setSingleUse(flagValue);
                    completeModify(player, kitName, flag, flagValue);
                    break;
                case "MESSAGE":
                    KITS.get(kitName).setMessage(flagValue);
                    completeModify(player, kitName, flag, flagValue);
                    break;
                case "ANNOUNCE":
                    KITS.get(kitName).setAnnounce(flagValue);
                    completeModify(player, kitName, flag, flagValue);
                    break;
                case "PERMISSION":
                    KITS.get(kitName).setPermission(flagValue);
                    completeModify(player, kitName, flag, flagValue);
                    break;
                case "DELETE":
                    // TODO add confirmation logic
                    KITS.remove(kitName);
                    saveKits();
                    //completeModify(player, kitName, flag, flagValue);
                    break;
                case "ENABLE":
                    KITS.get(kitName).enable();
                    KITS.get(kitName).editedBy(player);
                    saveKits();
                    player.sendMessage(ChatColor.GREEN + "> " + kitName + " has been enabled");
                    //completeModify(player, kitName, flag, flagValue);
                    break;
                case "DISABLE":
                    KITS.get(kitName).disable();
                    KITS.get(kitName).editedBy(player);
                    saveKits();
                    player.sendMessage(ChatColor.GREEN + "> " + kitName + " has been disabled");
                    break;
                default:
                    player.sendMessage(INVALIDFLAG);
                    break;
            }
        }
    }
    
    public static void addItemsToKit(Player player, String kitName, String item, String count){
        Material material = MaterialHandler.getMaterial(item);
        if (material == null){
            player.sendMessage(ChatColor.RED + "> Error: That is not a valid material!");
        } else {
            try {
                int quantity = Integer.valueOf(count);
                KITS.get(kitName).modifyItems(material, quantity);
                KITS.get(kitName).editedBy(player);
                saveKits();
                if (quantity <= 0){
                    player.sendMessage(ChatColor.GREEN + "> " + material.toString() + " removed from kit " + kitName);
                } else {
                    player.sendMessage(ChatColor.GREEN + "> " + material.toString() + " quantity set to " + count);
                }
            } catch (NumberFormatException e){
                player.sendMessage(ChatColor.RED + "> Error: The item quantity must be an integer!");
            }
            
        }
    }
    
    private static void completeModify(Player player, String kitName, String flag, String flagValue){
        KITS.get(kitName).editedBy(player);
        saveKits();
        if (flag.equalsIgnoreCase("cooldown")) {
            if (flagValue.equals("0")){
                player.sendMessage(ChatColor.GREEN + "> COOLDOWN for " + kitName + " has been removed");
            } else {
                player.sendMessage(ChatColor.GREEN + "> COOLDOWN for " + kitName + " has been set to: " + flagValue + " minutes");
            }
        } else if (flagValue.equals("")){
            player.sendMessage(ChatColor.GREEN + "> " + flag.toUpperCase() + " for " + kitName + " has been removed");
        } else {
            player.sendMessage(ChatColor.GREEN + "> " + flag.toUpperCase() + " for " + kitName + " has been set to: " + ChatColor.translateAlternateColorCodes('&',flagValue));
        }
        
    }

    /** Loads the kits.json file and builds a Kit object for each entry.
     */
    public static void loadKits() {
        JSONObject kits = JSONHandler.LoadJSONConfig(JawaCommands.getPlugin(), "/kits.json");
        if (!kits.isEmpty()) {
            for (String key : kits.keySet()) {
                KITS.put(key, new Kit(kits.getJSONObject(key)));
            }
            
            LOGGER.log(Level.INFO, "{0} kits have been loaded.", KITS.size());
        } else
            LOGGER.log(Level.INFO, "No kits were found to load");
    }

    /** Triggers the kits JSONObject to be saved to file. Each Kit object is JSONized
     * and written to kits.json.
     */
    public static void saveKits() {
        JSONObject kits = new JSONObject();
        for (String kitname : KITS.keySet()){
            kits.put(kitname, KITS.get(kitname).jsonizeKit());
        }
        JSONHandler.WriteJSONToFile(JawaCommands.getPlugin(), "/kits.json", kits);
        Logger.getLogger(JSONHandler.class.getName()).log(Level.INFO, "{0} kits saved to file", KITS.size());
    }
    
    /** Retrieve a set with the kit names.
     * @return 
     */
    public Set<String> getKitKeys(){
        return KITS.keySet();
    }
    
    /** If the kit exists this will return true. If not it will send a message to the player
     * that it does not exist and return false.
     * @param kitName
     * @param player
     * @return 
     */
    private static boolean validateKit(String kitName, Player player){
        if (kitExists(kitName)) return true;
        else {
            kitDoesntExist(player);
            return false;
        }
    }
    
    /** If the kit exists this will return true. If not it will send a message to the CommandSender
     * that it does not exist and return false.
     * @param kitName
     * @param commandSender
     * @return 
     */
    public static boolean validateKit(String kitName, CommandSender commandSender){
        if (kitExists(kitName)) return true;
        else {
            commandSender.sendMessage(NOTEXISTINGKIT);
            return false;
        }
    }
    
    /** Returns true if a kit exists, false if not.
     * @param kitName
     * @return 
     */
    public static boolean kitExists(String kitName){
        return KITS.containsKey(kitName);
    }
    
    /** Send the player a message that the kit doesn't exist.
     * @param player 
     */
    public static void kitDoesntExist(Player player){
        player.sendMessage(NOTEXISTINGKIT);
    }
    
    public static void sendListMessage(Player player){
        List<Kit> tmpkits = new ArrayList();
        for (Kit kit : KITS.values()){
          if (kit.canUse(player)) tmpkits.add(kit);
        }
        
        int partitionSize = 4;
        List<List<Kit>> partitions = new LinkedList();
        for (int i = 0; i < tmpkits.size(); i += partitionSize) {
            partitions.add(tmpkits.subList(i, Math.min(i + partitionSize, tmpkits.size())));
        }

        player.sendMessage(net.md_5.bungee.api.ChatColor.GREEN + "> These are the kits you have access to:");
        for (List<Kit> kitLine : partitions) {
            ComponentBuilder compBuilder = new ComponentBuilder(" > ").color(net.md_5.bungee.api.ChatColor.GREEN);
            for (Kit kit : kitLine) {
                compBuilder.append("[" + kit.getKitName() + "]").color(net.md_5.bungee.api.ChatColor.BLUE)
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kit " + kit.getKitName()))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Get " + kit.getKitName())))
                        .append("[i]").color(net.md_5.bungee.api.ChatColor.YELLOW)
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kit info " + kit.getKitName()))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(kit.getKitName() + " info")))
                        .append("[l]").color(net.md_5.bungee.api.ChatColor.YELLOW)
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kit items " + kit.getKitName()))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("List items in " + kit.getKitName())))
                        .append(" ");
            }
            BaseComponent[] baseComp = compBuilder.create();
            player.spigot().sendMessage(baseComp);
        }
    }
    
    /** Send kit info message to player for kit specified.
     * @param player
     * @param kitName 
     */
    public static void sendKitInfo(Player player, String kitName){
        if (kitExists(kitName)) {
            for (BaseComponent[] baseComp : KITS.get(kitName).getInfo(player.hasPermission("jawacommands.kit.admin"))){
                player.spigot().sendMessage(baseComp);
            }
        } else {
            kitDoesntExist(player);
        }
    }
    
    /** Send kit info message to player for kit specified.
     * @param player
     * @param kitName 
     */
    public static void sendKitItemsInfo(Player player, String kitName){
        if (kitExists(kitName)) {
            player.sendMessage(ChatColor.GREEN + "> Items for " + ChatColor.BLUE + kitName);
            for (BaseComponent[] baseComp : KITS.get(kitName).getItems(player.hasPermission("jawacommands.kit.admin"))){
                player.spigot().sendMessage(baseComp);
            }
        } else {
            kitDoesntExist(player);
        }
    }
    
    /** Makes the player Use the kit.Kit rules are followed.
     * @param player
     * @param kitName 
     * @return  
     */
    public static boolean useKit(Player player, String kitName){
        if (validateKit(kitName, player)) return KITS.get(kitName).useKit(player);
        else return false;
    }
    
    public static void giveKit(CommandSender commandSender, String kitName, List<Player> targets, List<String> overrides){
        KITS.get(kitName).useKit(targets, overrides);
    }

//    /**
//     * Modifies the flags on a kit as well as handles error returning. Valid
//     * flags are: cooldown: minutes, single_use: boolean, message: String,
//     * announce: String.
//     * @param kitName
//     * @param flags
//     * @param commandSender
//     * @return
//     */
//    public static boolean modifyKitFlags(String kitName, String flags, CommandSender commandSender) {
//        String[] input = flags.split(" ");
//        /*Flags:
//        cooldown: minutes
//        single_use: boolean
//        message: String
//        announce: String
//        description: String
//         */
//
//        if (!validateFlagOptions(commandSender, kitName, input)) {
//            return false;
//        }
//        //Determine which flag is being modified
//        if (input[0].equalsIgnoreCase("cooldown") || input[0].equalsIgnoreCase("single_use")) {
//            modifyFlags(input[0].toLowerCase(), commandSender, kitName, input);
//        } else if (input[0].equalsIgnoreCase("message") || input[0].equalsIgnoreCase("announce") | input[0].equalsIgnoreCase("description")) {
//            modifyValues(input[0].toLowerCase(), kitName, input, commandSender);
//        } else {
//            commandSender.sendMessage(ChatColor.RED + " > Error! That flag isnt understood!");
//            return false;
//        }
//
//        saveKits();
//        return true;
//    }
//
//    /**Performs the modifications on flags.
//     * @param key
//     * @param commandSender
//     * @param kitName
//     * @param input 
//     */
//    private static void modifyFlags(String key, CommandSender commandSender, String kitName, String[] input) {
//        //Perform the modification
//        if (key.equals("cooldown")) {
//            kits.getJSONObject(kitName).put(key, Integer.valueOf(input[1]));
//        } else if (key.equals("single_use")) {
//            kits.getJSONObject(kitName).put(key, Boolean.valueOf(input[1]));
//        }
//
//        commandSender.sendMessage(ChatColor.GREEN + " > " + key + " has been update to: " + ChatColor.WHITE + input[1]);
//        //If not already tracking who is using add the JSONObject otherwise continue
//        if (!kits.getJSONObject(kitName).has("used_by")) {
//            kits.getJSONObject(kitName).put("used_by", new JSONObject());
//        }
//    }
//
//    private static void modifyValues(String type, String kitName, String[] input, CommandSender commandSender) {
//        if (input[1].equalsIgnoreCase("none") && kits.getJSONObject(kitName).has(type)) {
//            kits.getJSONObject(kitName).remove(type);
//            commandSender.sendMessage(ChatColor.GREEN + " > " + type + " flag has been removed from the " + ChatColor.BLUE + kitName + ChatColor.GREEN + " kit.");
//        } else if (!input[1].equalsIgnoreCase("none")) {
//            kits.getJSONObject(kitName).put(type, String.join(" ", Arrays.copyOfRange(input, 1, input.length)));
//            commandSender.sendMessage(ChatColor.GREEN + " > " + type + " flag has been added to the " + ChatColor.BLUE + kitName + ChatColor.GREEN + " kit.");
//        }
//
//    }
//
//    /**
//     * This is backed by modifyKitItems(String kitName, Material material, int
//     * count). It will validate the String values passed to it and return an
//     * error message to the commandSender if it is not parsable.
//     *
//     * @param kitName
//     * @param values
//     * @param commandSender
//     * @return
//     */
//    public static void modifyKitItems(String kitName, String values, CommandSender commandSender) {
//        //Format some things
//        //Should be of the form <item name/id> <count>
//        String[] input = values.split(" ");
//
//        Material material = validateMinecraftItem(input[0]);
//
//        if (material != null && validateModifyItemInput(kitName, input, commandSender)) {
//            modifyKitItems(commandSender, kitName, material, Integer.valueOf(input[1]));
//        } else {
//            commandSender.sendMessage(ChatColor.RED + " > Error: Invalid material id or name!");
//        }
//
//    }
//
//    /**
//     * * Adds or removes specified items.The String kitName is the name of the
//     * kit, the material should already be validated, and the int count should
//     * be positive or 0. If these things are not done then there may be unknown
//     * behavior.
//     *
//     * @param commandSender
//     * @param kitName
//     * @param material
//     * @param count
//     */
//    public static void modifyKitItems(CommandSender commandSender, String kitName, Material material, int count) {
//        if (count == 0) { //remove the items
//            kits.getJSONObject(kitName).getJSONObject("items").remove(material.toString());
//            commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + material.toString() + ChatColor.GREEN + " has been removed from " + ChatColor.BLUE + kitName);
//        } else {
//            kits.getJSONObject(kitName).getJSONObject("items").put(material.toString(), count);
//            commandSender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + material.toString() + ChatColor.GREEN + " has been added to " + ChatColor.BLUE + kitName);
//        }
//        saveKits();
//    }
//
//    /**
//     * gives the specified kit to the players listed if they are online. If they
//     * are not online an error message is sent for each player not online. Those
//     * that are online are given the kit.
//     *
//     * @param commandSender
//     * @param kitName
//     * @param players
//     * @return
//     */
//    public static boolean givePlayersKit(CommandSender commandSender, String kitName, String[] players) {
//        HashSet<Player> playerSet = new HashSet();
//        for (String playerString : players) {
//            Player player = Bukkit.getPlayer(playerString);
//            if (player != null) {
//                playerSet.add(player);
//            } else {
//                commandSender.sendMessage(ChatColor.RED + " Error: " + playerString + " is not a valid online player.");
//            }
//        }
//
//        if (!playerSet.isEmpty()) {
//            playerSet.forEach((player) -> {
//                if (giveKitInputValidation(kitName, commandSender, player, true)) {
//                    giveKitToPlayer(kitName, player); //TODO setup override
//                }
//            });
//            return true;
//        } else {
//
//            return false;
//        }
//
//    }
//
//    /**
//     * Give a player a specific kit. The String kitName will point to the
//     * specified kit, commandSender is the one sending the command (player,
//     * command block, console), and the target is the player it is being given
//     * too. The boolean override allows the single_use flag to overridden.
//     *
//     * @param kitName
//     * @param commandSender
//     * @param target
//     * @param overrideSingleUse
//     * @return
//     */
//    private static boolean giveKitToPlayer(String kitName, Player target) {
//        ItemStack[] itemStack = generateItemStack(kits.getJSONObject(kitName).getJSONObject("items"));
//        itemStack = giveItemsToPlayer(itemStack, target);
//        handleNotGivenItems(itemStack, target);
//
//        if (kits.getJSONObject(kitName).has("message")) {
//            target.sendMessage(ChatColor.GREEN + " > " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', kits.getJSONObject(kitName).getString("message")));
//        }
//
//        if (kits.getJSONObject(kitName).has("announce")) {
//            System.out.println(target.hasPermission(Server.BROADCAST_CHANNEL_USERS));
//            plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', kits.getJSONObject(kitName).getString("announce")));
//        }
//        
//        if (kits.getJSONObject(kitName).getInt("cooldown") > 0 || kits.getJSONObject(kitName).getBoolean("single_use")){
//            
//        }
//
//        return true;
//    }
//
//    /**
//     * Creates an item stack from a JSONObject containing item names as the keys
//     * and quantities as the values. This returns an ItemStack[]
//     *
//     * @param items
//     * @return
//     */
//    private static ItemStack[] generateItemStack(JSONObject items) {
//        HashSet<ItemStack> itemsArray = new HashSet();
//        ItemStack[] itemStack = new ItemStack[items.length()];
//        items.keySet().forEach((item) -> {
//            itemsArray.add(new ItemStack(Material.matchMaterial(item), items.getInt(item)));
//        });
//
//        return itemsArray.toArray(itemStack);
//    }
//
//    /**
//     * Give items to a player. The items that will not fit into a player's
//     * inventory are returned as an ItemStack[].
//     *
//     * @param items
//     * @param target
//     * @return
//     */
//    private static ItemStack[] giveItemsToPlayer(ItemStack[] items, Player target) {
//        HashMap<Integer, ItemStack> notGiven = target.getInventory().addItem(items);
//        return notGiven.values().toArray(new ItemStack[notGiven.size()]);
//    }
//
//    /**
//     * Will spawn items that won't fit in a player's inventory at their feet.
//     *
//     * @param notGiven
//     * @param target
//     */
//    private static void handleNotGivenItems(ItemStack[] notGiven, Player target) {
//        Location loc = target.getLocation();
//        for (ItemStack stack : notGiven) {
//            target.getWorld().dropItem(loc, stack);
//        }
//    }
//
//    public static void sendHelpMessage(CommandSender commandSender) {
//        String[] usage = new String[]{
//            ChatColor.GREEN + "To get a kit:" + ChatColor.WHITE + " /kit <kit name>",
//            ChatColor.GREEN + "To create a kit:" + ChatColor.WHITE + " /kit -c <kit name>",
//            ChatColor.GREEN + "To add items to a kit:" + ChatColor.WHITE + " /kit -m <kit name> -a <item id> <count to give>",
//            ChatColor.GREEN + "To add flags to a kit:" + ChatColor.WHITE + " /kit -m <kit name> -f <flag> [flag options]",
//            ChatColor.GREEN + "To display kit help:" + ChatColor.WHITE + " /kit -h",
//            ChatColor.GREEN + "To give a kit to someone:" + ChatColor.WHITE + " /kit -g <kit> <player1 player2 ...>",
//            ChatColor.GREEN + "To list kits:" + ChatColor.WHITE + " /kit -l"};
//        commandSender.sendMessage(usage);
//    }
//
//    public static void listKits(CommandSender sender, String kitName) {
//        if (kitName == null) {
//            HashSet<String> availableKits = new HashSet();
//            for (String kit : kits.keySet()) {
//                if (kits.getJSONObject(kit).has("permission") && !sender.hasPermission(kits.getJSONObject(kit).getString("permission"))) {
//                    continue;
//                }
//                if (kits.getJSONObject(kit).has("description")) {
//                    availableKits.add(ChatColor.GREEN + "  > " + ChatColor.BLUE + kit + ChatColor.WHITE + ": " + ChatColor.GOLD + kits.getJSONObject(kit).getString("description"));
//                } else {
//                    availableKits.add(ChatColor.GREEN + "  > " + ChatColor.BLUE + kit);
//                }
//            }
//
//            if (availableKits.isEmpty()) {
//                sender.sendMessage(ChatColor.RED + " > There are no availble kits that you have permission for");
//            } else {
//                sender.sendMessage(ChatColor.GREEN + " > Here are the kits you have permission to use: ");
//                sender.sendMessage(availableKits.toArray((new String[availableKits.size()])));
//
//            }
//        } else {
//            if (kits.getJSONObject(kitName).has("description")) {
//                sender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + kitName + ChatColor.WHITE + ": " + ChatColor.translateAlternateColorCodes('&', kits.getJSONObject(kitName).getString("description")));
//            } else {
//                sender.sendMessage(ChatColor.GREEN + " > " + ChatColor.BLUE + kitName);
//            }
//
//            sender.sendMessage(ChatColor.GREEN + "  > Cooldown:" + kits.getJSONObject(kitName).getInt("cooldown"));
//            sender.sendMessage(ChatColor.GREEN + "  > Single Use:" + kits.getJSONObject(kitName).getBoolean("single_use"));
//
//            if (kits.getJSONObject(kitName).has("items")) {
//                for (String materialName : kits.getJSONObject(kitName).getJSONObject("items").keySet()) {
//                    sender.sendMessage(ChatColor.GREEN + "  > " + ChatColor.WHITE + kits.getJSONObject(kitName).getJSONObject("items").getInt(materialName) + ": " + ChatColor.YELLOW + materialName);
//                }
//            } else {
//                sender.sendMessage(ChatColor.RED + " > Error: That kit doesn't have any items!");
//            }
//
//        }
//
//    }
//
////==============================================================================
////                            Input Validation Methods
////==============================================================================
//    
//        /**
//     * Validates the input for giving a player a kit. If this returns false the
//     * calling method should terminate without sending a message as this method
//     * has already done so.
//     *
//     * @param kitName
//     * @param commandSender
//     * @param target
//     * @param overrideSingleUse
//     * @return
//     */
//    private static boolean giveKitInputValidation(String kitName, CommandSender commandSender, Player target, boolean overrideSingleUse) {
//        if (!kits.has(kitName)) {
//            commandSender.sendMessage(NOTEXISTINGKIT);
//            return false;
//        }
//
//        if (kits.getJSONObject(kitName).getJSONObject("items").isEmpty()) {
//            commandSender.sendMessage(ChatColor.RED + " > Error: That kit has no items in it!! Please add items first!");
//            return false;
//        }
//
//        if (kits.getJSONObject(kitName).getBoolean("single_use") && kits.getJSONObject(kitName).getJSONObject("used_by").has(target.getUniqueId().toString()) && !overrideSingleUse) {
//            if ((commandSender instanceof CommandBlock) || (commandSender instanceof Player && ((Player) commandSender).getUniqueId().equals(target.getUniqueId()))) {
//                target.sendMessage(ChatColor.RED + " > This kit is one time only! You already used it!");
//            } else {
//                commandSender.sendMessage(ChatColor.RED + " > That player has already used this one time kit!");
//            }
//            return false;
//        }
//
//        if ((kits.getJSONObject(kitName).getInt("cooldown") > 0) && kits.getJSONObject(kitName).getJSONObject("used_by").has(target.getUniqueId().toString())) {
//            LocalDateTime coolDownAt = LocalDateTime.parse(kits.getJSONObject(kitName).getJSONObject("used_by").getString(target.getUniqueId().toString())).plusMinutes(kits.getJSONObject(kitName).getInt("cool_down"));
//            if (!coolDownAt.isBefore(LocalDateTime.now())) {
//                if ((commandSender instanceof CommandBlock) || (commandSender instanceof Player && ((Player) commandSender).getUniqueId().equals(target.getUniqueId()))) {
//                    target.sendMessage(ChatColor.RED + " > This kit is one time only! You already used it!");
//                } else {
//                    commandSender.sendMessage(ChatColor.RED + " > That player has already used this one time kit!");
//                }
//                return false;
//            }
//        }
//        return true;
//    }
//    
//        private static boolean validateModifyItemInput(String kitName, String[] input, CommandSender commandSender) {
//        //Error handling 
//        if (input.length != 2) {
//            commandSender.sendMessage(TOOFEWOPTIONS);
//            return false;
//        }
//        if (!kits.has(kitName)) {
//            commandSender.sendMessage(NOTEXISTINGKIT);
//            return false;
//        }
//        try {
//            Integer.valueOf(input[1]);
//        } catch (NumberFormatException ex) {
//            commandSender.sendMessage(ChatColor.RED + " > Error: Item quantity must be an integer number!");
//            return false;
//        }
//        if (Integer.valueOf(input[1]) < 0) {
//            commandSender.sendMessage(ChatColor.RED + " > Error: Item quantity must be an integer number 0 or greater!");
//            return false;
//        }
//        return true;
//    }
//        
//        
//    /**
//     * Validating user input for modifying flags. This is all error handling and
//     * message sending. if the return is false then the calling method should
//     * terminate without sending a message as this method has already done it.
//     *
//     * @param commandSender
//     * @param kitName
//     * @param input
//     * @return
//     */
//    private static boolean validateFlagOptions(CommandSender commandSender, String kitName, String[] input) {
//        HashSet<String> acceptedFlags = new HashSet(Arrays.asList("cooldown", "single_use", "message", "announce", "description"));
//
//        //Error evaluation of input
//        if (!kits.has(kitName)) {
//            commandSender.sendMessage(NOTEXISTINGKIT);
//            return false;
//        }
//        if (input.length < 2) {
//            commandSender.sendMessage(TOOFEWOPTIONS);
//            return false;
//        }
//
//        if (!acceptedFlags.contains(input[0].toLowerCase())) {
//            commandSender.sendMessage(INVALIDFLAG);
//            commandSender.sendMessage(ChatColor.GREEN + " > Valid flags are: " + acceptedFlags.toString());
//            return false;
//        }
//
//        if (input[0].equalsIgnoreCase("cooldown")) {
//            if (input.length > 2) { //Length Evaluation
//                commandSender.sendMessage(TOOMANYOPTIONS);
//                return false;
//            }
//            try { //Verify that it is an integer
//                Integer.valueOf(input[1]);
//            } catch (NumberFormatException ex) {
//                commandSender.sendMessage(ChatColor.RED + " > Error: Cooldown time must be an integer number!");
//                return false;
//            }
//        } else if (input[0].equalsIgnoreCase("single_use")) {
//            if (input.length > 2) { //Length Evaluation
//                commandSender.sendMessage(TOOMANYOPTIONS);
//                return false;
//            }
//            if (!input[1].equalsIgnoreCase("true") && !input[1].equalsIgnoreCase("false")) { //verify that it is a string that can be evaluated to a boolean
//                commandSender.sendMessage(ChatColor.RED + " > Error: single_use must be a boolean, either true or false.");
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    /**
//     * Validates a material. Will return a Material if valid or null if invalid.
//     *
//     * @param material
//     * @return
//     */
//    private static Material validateMinecraftItem(String materialString) {
//
////        Material[] materials = Material.values();
////        for (Material mat : materials){
////            System.out.println("Name:" + mat.toString());
////        }
//        materialString = materialString.toUpperCase();
//
//        if (materialString.contains("MINECRAFT:")) {
//            materialString = materialString.replace("MINECRAFT:", "");
//        }
//
//        Material material = Material.matchMaterial(materialString);
//
//        if (material == null) {
//            material = Material.getMaterial(materialString);
//        }
//
//        return material;
//    }


}
