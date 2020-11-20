/*
 * Copyright (C) 2020 Jawamaster (Arthur Bulin)
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import jawamaster.jawacommands.JawaCommands;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;

/**
 *
 * @author Jawamaster (Arthur Bulin)
 */
public class Kit {
    
    private enum flags {
        COOLDOWN,
        DESCRIPTION,
        SINGLEUSE,
        MESSAGE,
        ANNOUNCE,
        PERMISSION
    }
    private final JSONObject USERS;
    private final JSONObject ITEMS;
    private final String KITNAME;
    private final JSONObject FLAGS;
    private final JSONObject METADATA;
    private ItemStack[] itemStacks;
    
    private List<BaseComponent[]> detailsMessage;
    private List<BaseComponent[]> adminDetailMessage;
    
    
    /** Create a kit from preexisting JSON data.
     * @param kit The JSONObject representing the kit.
     */
    public Kit (JSONObject kit){
        this.KITNAME = kit.getString("KITNAME");
        
        //Get kit flags
        if (kit.has("FLAGS")) {
            this.FLAGS = kit.getJSONObject("FLAGS");
        } else {
            this.FLAGS = new JSONObject();
        }
        
        //Process user data
        if (kit.has("USERS")) {
            this.USERS = kit.getJSONObject("USERS");
        } else {
            this.USERS = new JSONObject();
        }
        
        //Process items
        if (kit.has("ITEMS")) {
            this.ITEMS = kit.getJSONObject("ITEMS");
            generateItemStack(ITEMS);
        } else {
            this.ITEMS = new JSONObject();
        }
        
        //Process Metadata
        if (kit.has("METADATA")){
            this.METADATA = kit.getJSONObject("METADATA");
        } else {
            this.METADATA = new JSONObject();
        }
        assembleDetailMessage();
    }
    
    /** Create a new kit.
     * @param kitname The name for the kit
     * @param player The player object of the player creating the kit.
     */
    public Kit (String kitname, Player player) {
        //Create our blank internal objects
        this.FLAGS = new JSONObject();
        this.FLAGS.put("ENABLED", false);
        
        this.USERS = new JSONObject();
        this.ITEMS = new JSONObject();
        
        //Create the blank METADATA object and populate it with metadata
        this.METADATA = new JSONObject();
        this.METADATA.put("CREATEDBY", player.getUniqueId().toString());
        this.METADATA.put("CREATEDON", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        
        this.METADATA.put("LASTEDITEDBY", player.getUniqueId().toString());
        this.METADATA.put("LASTEDITEDON", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        
        this.METADATA.put("MCVERSION", JawaCommands.getPlugin().getServer().getVersion());
        
        this.KITNAME = kitname;
        
        assembleDetailMessage();
    }
    
    
//==============================================================================
//                            Kit objectizations 
//==============================================================================
    /**Generate a JSONObject representation of this object.
     * @return 
     */
    public JSONObject jsonizeKit(){
        JSONObject kit = new JSONObject();
        
        kit.put("KITNAME", KITNAME);
        kit.put("FLAGS", FLAGS);
        kit.put("USERS", USERS);
        kit.put("ITEMS", ITEMS);
        kit.put("METADATA", METADATA);
        
        return kit;
    }
    
    private void assembleDetailMessage(){
        detailsMessage = new ArrayList();
        adminDetailMessage = new ArrayList();
        detailsMessage.add(new ComponentBuilder("> Details for " )
                .color(ChatColor.GREEN)
                .append(KITNAME).color(ChatColor.GOLD).create());
        
        //Generate description line
        if (FLAGS.has("DESCRIPTION")) {
            detailsMessage.add(new ComponentBuilder(" > ").color(ChatColor.GREEN)
                    .append(FLAGS.getString("DESCRIPTION")).color(ChatColor.BLUE)
                .create());
            adminDetailMessage.add(new ComponentBuilder(" > ").color(ChatColor.GREEN)
                    .append(FLAGS.getString("DESCRIPTION")).color(ChatColor.BLUE)
                .create());
        } else {
            adminDetailMessage.add(new ComponentBuilder(" > ").color(ChatColor.GREEN)
                    .append("No description is set.").color(ChatColor.YELLOW)
                .create());
        }
        
        //Generate cooldown description
        if (FLAGS.has("COOLDOWN")) {
            detailsMessage.add(new ComponentBuilder(" > You may use this kit once every ").color(ChatColor.GREEN)
                    .append(String.valueOf(FLAGS.getInt("COOLDOWN"))).color(ChatColor.BLUE)
                    .append(" minutes").color(ChatColor.GREEN)
                .create());
            adminDetailMessage.add(new ComponentBuilder(" > COOLDOWN: ").color(ChatColor.GREEN)
                    .append(String.valueOf(FLAGS.getInt("COOLDOWN"))).color(ChatColor.BLUE)
                    .append(" minutes").color(ChatColor.GREEN)
                .create());
        } else {
            adminDetailMessage.add(new ComponentBuilder(" > ").color(ChatColor.GREEN)
                    .append("No COOLDOWN is set").color(ChatColor.YELLOW)
                .create());
        }
        
        //Generate single use line
        if (FLAGS.has("SINGLEUSE")) {
            if (FLAGS.getString("SINGLEUSE").equalsIgnoreCase("ONCE")) {
                detailsMessage.add(new ComponentBuilder(" > You may use this kit once.").color(ChatColor.GREEN)
                .create());
            } else {
                detailsMessage.add(new ComponentBuilder(" > You may use this kit once per life.").color(ChatColor.GREEN)
                .create());
            }
            adminDetailMessage.add(new ComponentBuilder(" > SINGLEUSE: ").color(ChatColor.GREEN)
                    .append(FLAGS.getString(flags.SINGLEUSE.toString())).color(ChatColor.BLUE)
                .create());
        } else {
            adminDetailMessage.add(new ComponentBuilder(" > ").color(ChatColor.GREEN)
                    .append("No SINGLEUSE is set").color(ChatColor.YELLOW)
                .create());
        }
        
        //Generate Permission line
        if (FLAGS.has("PERMISSION")) {
            detailsMessage.add(new ComponentBuilder(" > ").color(ChatColor.GREEN)
                    .append("You must have permission to use this kit").color(ChatColor.BLUE)
                .create());
            adminDetailMessage.add(new ComponentBuilder(" > Permission: ").color(ChatColor.GREEN)
                    .append(FLAGS.getString(flags.PERMISSION.toString())).color(ChatColor.BLUE)
                .create());
        } else {
            adminDetailMessage.add(new ComponentBuilder(" > ").color(ChatColor.GREEN)
                    .append("No PERMISSION is set").color(ChatColor.YELLOW)
                .create());
        }
        
    }
    
//==============================================================================
//                            Get Kit Information
//==============================================================================
    
    /** Return the kit name.
     * @return 
     */
    public String getKitName(){
        return this.KITNAME;
    }
    
    /** Return if the kit is empty
     * @return 
     */
    public boolean isKitEmpty(){
        return ITEMS.isEmpty();
    }
    
    /** Return the kit description. If one is not set this returns null.
     * @return 
     */
    public String getDescription(){
        if (FLAGS.has("DESCRIPTION")) return FLAGS.getString("DESCRIPTION");
        return null;
    }
    
    /** Returns the length of the cooldown in minutes. If there is no cooldown set
     * then this will return null
     * @return 
     */
    public Integer getCooldown() {
        if (FLAGS.has("COOLDOWN")) return FLAGS.getInt("COOLDOWN");
        return null;
    }
    
    /** Returns the announcement if it is set, if not, this returns null.
     * @return 
     */
    public String getAnnounce() {
        if (FLAGS.has("ANNOUNCE")) return FLAGS.getString("ANNOUNCE");
        return null;
    }
    
    /** Return the player message if it is set. If not this returns null.
     * @return 
     */
    public String getMessage(){
        if (FLAGS.has("MESSAGE")) return FLAGS.getString("MESSAGE");
        return null;
    }
    
    /** Return the string type of single use. If it is not set this returns null.
     * ONCE - Only usable once, ever.
     * LIFE - Once per life
     * @return 
     */
    public String getSingleUse(){
        if (FLAGS.has("SINGLEUSE")) return FLAGS.getString("SINGLEUSE");
        return null;
    }
    
    public boolean isEnabled(){
        return FLAGS.getBoolean("ENABLED");
    }
    
//==============================================================================
//                            Set Kit Parameters
//==============================================================================
    
    /** Enable this kit for players.
     */
    public void enable(){
        FLAGS.put("ENABLED", true);
    }
    
    /** Disable this kit for players.
     */
    public void disable() {
        FLAGS.put("ENABLED", false);
    }
    
    /** Sets the cooldown Flag in the kit's FLAG configuration. If the integer cooldown
     * is 0 or less this will remove the kit's cooldown paramater. This returns false
     * if the cooldown paramater was removed 
     * @param cooldown
     * @return 
     */
    public boolean setCoolDown(int cooldown) {
        if (cooldown <= 0){
            FLAGS.remove("COOLDOWN");
            return false;
        } else {
            FLAGS.put("COOLDOWN", cooldown);
            return true;
        }
    }
    
    /** Sets the single use parameter for the kit. If the parameter is not ONCE or LIFE,
     * then any existing SINGLEUSE parameter is removed and FALSE is returned. If
     * the paramter is ONCE or LIFE then it is set and returns true;
     * @param singleuse
     * @return 
     */
    public boolean setSingleUse(String singleuse){
        
        switch (singleuse.toUpperCase()){
            case "ONCE":
                FLAGS.put("SINGLEUSE", "ONCE");
                return true;
            case "LIFE":
                FLAGS.put("SINGLEUSE", "LIFE");
                return true;
            default:
                FLAGS.remove("SINGLEUSE");
                return false;
        }
    }
    
    /** Remove the permission configuration for the kit.
     * @return 
     */
    public String removePermission(){
        return setPermission("");
    }
    
    /** Set the permission to default for this kit.
     * @return 
     */
    public String setPermission(){
        return setPermission(KITNAME);
    }
    
    /** Sets the permission that the kit will check for. This will append permission
     * to the kit base permission "jawacommands.kit" so that is becomces jawacommands.kit.permission
     * If an empty string is passed this will remove the permission.
     * @param permission
     * @return 
     */
    public String setPermission(String permission){
        if (permission.equals("")){
            FLAGS.remove("PERMISSION");
            return null;
        } else {
            String perm = (new StringBuilder(KitHandler.KITBASEPERM)).append(".").append(permission).toString();
            FLAGS.put("PERMISSION", perm);
            return perm;
        }
    }
    
    /** Sets the description of the kit. If description is an empty string then the
     * DESCRIPTION flag is removed, otherwise it is set to the contents of the string.
     * This returns TRUE when a description is set, this returns FALSE when it is removed.
     * @param description
     * @return 
     */
    public boolean setDescription(String description){
        if (description.equals("")) {
            FLAGS.remove("DESCRIPTION");
            return false;
        } else {
            FLAGS.put("DESCRIPTION", description);
            return true;
        }
    }
    
    /** Sets the player message of the kit. If message is an empty string then the
     * MESSAGE flag is removed, otherwise it is set to the contents of the string.
     * This returns TRUE when a message is set, this returns FALSE when it is removed.
     * @param message
     * @return 
     */
    public boolean setMessage(String message){
        if (message.equals("")){
            FLAGS.remove("MESSAGE");
            return false;
        } else {
            FLAGS.put("MESSAGE", message);
            return true;
        }
    }
    
    /** Sets the announcement of the kit. If announce is an empty string then the
     * ANNOUNCE flag is removed, otherwise it is set to the contents of the string.
     * This returns TRUE when a announce is set, this returns FALSE when it is removed.
     * @param announce
     * @return 
     */
    public boolean setAnnounce(String announce){
        if (announce.equals("")){
            FLAGS.remove("ANNOUNCE");
            return false;
        } else {
            FLAGS.put("ANNOUNCE", announce);
            return true;
        }
    }
    
//==============================================================================
//                            User Information
//==============================================================================
    /** Track player usage statistics. This will increment the player's usage counter,
     * record the local data-time, and get the player's death counter.
     * @param player 
     */
    private void trackPlayerUse(Player player){
        if (!USERS.has(player.getUniqueId().toString())){
            USERS.put(player.getUniqueId().toString(), new JSONObject());
        }
        
        //Number of times used
        USERS.getJSONObject(player.getUniqueId().toString()).increment("COUNT");
        //Date-time of last use (for cool down)
        USERS.getJSONObject(player.getUniqueId().toString()).put("DATETIME", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        //Life count (for per-life mode)
        USERS.getJSONObject(player.getUniqueId().toString()).put("LIVES", player.getStatistic(Statistic.DEATHS));
    }
    
    /** Check if the kit has cooled down and a player can use it. If there is no cooldown
     * then this will return true, otherwise this will check the last time a player used it
     * and evaluate the local date-time vs the saved local date-time.
     * @param player
     * @return 
     */
    private boolean hasCooledDown(Player player){
        if (FLAGS.has("COOLDOWN") && USERS.has(player.getUniqueId().toString())) {
            LocalDateTime used = LocalDateTime.parse(USERS.getJSONObject(player.getUniqueId().toString()).getString("DATETIME"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            return !LocalDateTime.now().isBefore(used.plusMinutes(FLAGS.optInt("COOLDOWN", 0)));
        } else
            return true;
    }
    
    /** Check if a player has used this kit with respect to the kit's SINGLEUSE 
     * setting. If the SINGLEUSE is set to ONCE and the player has used this ever,
     * then this will return TRUE. If SINGLEUSE is set to LIFE then this will check
     * if the user has died since using this last, if they have this will return TRUE.
     * If SINGLEUSE is not set this will return FALSE.
     * @param player
     * @return 
     */
    private boolean usedOnce(Player player) {
        if (FLAGS.has("SINGLEUSE")) {
            switch (FLAGS.getString("SINGLEUSE")) {
                case "ONCE":
                    return USERS.has(player.getUniqueId().toString());
                case "LIFE":
                    if (USERS.has(player.getUniqueId().toString())) {
                        return (USERS.getInt("LIVES") < player.getStatistic(Statistic.DEATHS));
                    } else {
                        return true;
                    }
                default:
                    return false;
            }
        } else {
            return false;
        }
    }
    
    /** If PERMISSION is defined this will check if the player has permission. If they do
     * this will return TRUE. If they do not this will return FALSE. If PERMISSION is not
     * defined then this returns TRUE (always permissible).
     * @param player
     * @return 
     */
    private boolean isPermissible(Player player){
        if (FLAGS.has("PERMISSION")) {
            return player.hasPermission(FLAGS.getString("PERMISSION"));
        } else {
            return true;
        }
    }
    
    /** If the player has permission (if applicable) and kit is enabled. 
     * This will take into consideration admin rights.
     * @param player Player being assessed.
     * @return True or False
     */
    public boolean canUse(Player player){
        return player.hasPermission(KitHandler.KITBASEPERM + ".admin") || isPermissible(player) && isEnabled();
    }

//==============================================================================
//                            Handle kit actions
//==============================================================================
    
    /** Have a player use a kit.
     * @param player
     * @return 
     */
    public boolean useKit(Player player){
        return useKit(player, false);
    }
    
    /** This allows a player to use a kit. This is backed by useKit(Player player, boolean overridePermission).
     * Given boolean parameters allow the kit to be given without respect to kit configuration rules. Stats are
     * tracked for the player no matter what.
     * @param player
     * @param overridePermission If this is set to TRUE then permission requirements in the config are ignored.
     * @param overrideConfig If this is set to TRUE then all config restrictions are ignored and the kit is given. 
     * @return 
     */
    public boolean useKit(Player player, boolean overridePermission, boolean overrideConfig){
        if (overrideConfig){
            //give kit to player no matter what
            giveKitToPlayer(player);
            trackPlayerUse(player);
            return true;
        } else {
            return useKit(player, overridePermission);
            
        }
    }
    
    /** This allows a player to use a kit. This is backed by useKit(Player player, boolean overridePermission).
     * Giveing a boolean parameter allow the kit to be given without respect to kit permission rules. Stats are
     * tracked for the player no matter what.
     * @param player
     * @param overridePermission If this is set to TRUE then permission requirements in the config are ignored.
     * @return 
     */
    public boolean useKit(Player player, boolean overridePermission) {
        //If ignore permission or player has permission or player has admin permission
        if (overridePermission || isPermissible(player) || player.hasPermission(KitHandler.KITBASEPERM + ".admin")) {
            if (hasCooledDown(player)) { //Access kit cooldown
                if (!usedOnce(player)) { //Access if used once
                    giveKitToPlayer(player);
                    trackPlayerUse(player);
                    return true;
                } else {
                    if (FLAGS.getString("USEONCE").equals("LIFE")) 
                        player.sendMessage(ChatColor.RED + "> That kit can only be used once per life!");
                    else
                        player.sendMessage(ChatColor.RED + "> That kit can only be used once!");
                    return false;
                }
            } else {
                player.sendMessage(ChatColor.RED + "> That kit is still cooling down!");
                return false;
            }
        } else {
            player.sendMessage(ChatColor.RED + "> You don't have permission to use that kit!");
            return false;
        }
    }
    
    /**
     * Give items to a player. The items that will not fit into a player's
     * inventory are returned as an ItemStack[].
     * @param items
     * @param target
     * @return
     */
    private static ItemStack[] giveItemsToPlayer(ItemStack[] items, Player target) {
        HashMap<Integer, ItemStack> notGiven = target.getInventory().addItem(items);
        return notGiven.values().toArray(new ItemStack[notGiven.size()]);
    }
    
    /**
     * Will spawn items that won't fit in a player's inventory at their feet.
     * @param notGiven
     * @param target
     */
    private static void handleNotGivenItems(ItemStack[] notGiven, Player target) {
        Location loc = target.getLocation();
        for (ItemStack stack : notGiven) {
            target.getWorld().dropItem(loc, stack);
        }
    }
    
    /** Creates an item stack from a JSONObject containing item names as the keys
     * and quantities as the values. This returns an ItemStack[]
     * @param items
     * @return
     */
    private void generateItemStack(JSONObject items) {
        HashSet<ItemStack> itemsArray = new HashSet();
        ItemStack[] itemStack = new ItemStack[items.length()];
        items.keySet().forEach((item) -> {
            itemsArray.add(new ItemStack(Material.matchMaterial(item), items.getInt(item)));
        });
        itemStacks = itemsArray.toArray(itemStack);
    }
    
    /** Give a player a specific kit.
     * @param kitName
     * @param commandSender
     * @param target
     * @param overrideSingleUse
     * @return
     */
    private void giveKitToPlayer(Player target) {
        ItemStack[] notGiven = giveItemsToPlayer(itemStacks, target);
        handleNotGivenItems(notGiven, target);
    }

//==============================================================================
//                            Kit Modifications
//==============================================================================

    /** This will modify the items within the internal JSON ITEM storage and rebuild
     * the item stack for this kit. If the quantity is 0 or less the item is removed
     * from the JSON and stack. If the item is removed this returns FALSE if the item
     * is added this returns TRUE.
     * @param material The Bukkit material. This will be resolved to the enum name.
     * @param quantity The integer quantity of items. 0 or less will remove the item.
     * @return 
     */
    public boolean modifyItems(Material material, int quantity) {
        if (quantity <= 0) {
            ITEMS.remove(material.toString());
            generateItemStack(ITEMS);
            return false;
        } else {
            ITEMS.put(material.toString(), quantity);
            generateItemStack(ITEMS);
            return true;
        }
    }
    
}
