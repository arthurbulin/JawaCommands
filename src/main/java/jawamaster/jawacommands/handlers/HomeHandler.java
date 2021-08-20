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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import jawamaster.jawacommands.JawaCommands;
import net.jawasystems.jawacore.PlayerManager;
import net.jawasystems.jawacore.dataobjects.PlayerDataObject;
import org.bukkit.entity.Player;
import org.json.JSONObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;

/**
 *
 * @author Jawamaster (Arthur Bulin)
 */
public class HomeHandler {
    public static final String HOMEPERMISSION = "jawacommands.home";
    public static final String ADDPERMISSION = HOMEPERMISSION + ".add";
    public static final String DELPERMISSION = HOMEPERMISSION + ".del";
    public static final String LISTPERMISSION = HOMEPERMISSION + ".list";
    public static final String ADMINPERMISSION = HOMEPERMISSION.concat(".admin");
    private static final String NOPERMISSION = ChatColor.RED + "> You do not have permission to do that.";
    private static final String NOADDPERMISSION = ChatColor.RED + "> You do not have permission to add a home in this world.";
    private static final String NODELPERMISSION = ChatColor.RED + "> You do not have permission to remove a home in this world.";
    private static final String NOHOMES = ChatColor.RED + "> You do not have any homes set. Run /home help to see how.";
    private static final String NOHOME = ChatColor.RED + "> {h} does not exist in your home list.";

    /** Adds a home entry to the user's "homes" index entry. If replace == true
     * an existing home will be overwritten. Otherwise the player will be warned
     * and the home will not be overwritten.
     * @param player
     * @param homeName
     * @param replace
     * @return 
     */
    public static boolean addHome(Player player, String homeName, boolean replace) {
        //if has admin permission skip other checks
        //else if homes implicitly true allowed then check permission ONLY if the world is in a non-allowed list.
        //else if homes implicitly allowed false check for individual world permissions
        if (player.hasPermission(ADMINPERMISSION) || 
                (player.hasPermission(ADDPERMISSION) && !JawaCommands.getConfiguration().getStringList("homes-prohibited-worlds").contains(player.getWorld().getName())) 
                || player.hasPermission(ADDPERMISSION.concat(".").concat(player.getWorld().getName().toLowerCase()))){
            PlayerDataObject pdObject = PlayerManager.getPlayerDataObject(player);
            //Check that name doesnt exist
            if (homeName.equalsIgnoreCase("bed")){
                player.sendMessage(ChatColor.RED + "> Error: You can only set a bed home by sleeping in a bed!");
                return false;
            } else if (pdObject.containsHomeData() && pdObject.containsHome(homeName) && !replace) {
                player.sendMessage(ChatColor.RED + "> Error: " + homeName +" already exists! Remove it first or replace it with 'replace'");
                return false;
            } else {
                pdObject.setHome(player, homeName);
                //pdObject.setHome(homeName, LocationDataHandler.packLocation(player.getLocation()));
                String homeMessage = JawaCommands.getConfiguration().getConfigurationSection("messages").getString("home-add", ChatColor.GREEN + "> {h} has been saved").replace("{h}", homeName);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', homeMessage));
                return true;
            }
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', JawaCommands.getConfiguration().getConfigurationSection("messages").getString("home-no-addpermission", NOADDPERMISSION)));
            return false;
        }
    }

    /** Removes an entry from the user's "homes" index entry. If the home does not
     * exist the user will be notified.
     * @param player
     * @param homeName
     * @return 
     */
    public static boolean removeHome(Player player, String homeName) {
        //TODO allow the creation of non-deletable homes based on world or admin created
        if (player.hasPermission(DELPERMISSION)) { //Check if user has permission
            PlayerDataObject pdObject = PlayerManager.getPlayerDataObject(player);

            if (!pdObject.containsHome(homeName)) {
                player.sendMessage(NOHOME.replace("{h}", homeName));
                return false;
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', JawaCommands.getConfiguration().getConfigurationSection("messages").getString("home-del", ChatColor.GREEN + "> " + homeName + " has been deleted").replace("{h}", homeName)));
                pdObject.removeHome(homeName);
                return true;
            }

        } else {
            player.sendMessage(NODELPERMISSION);
            return false;
        }
    }

    /** Sends a list of homes to the player in a viewable format.
     * @param player
     */
    public static void sendHomeList(Player player) {
        if (player.hasPermission(LISTPERMISSION)) {
            PlayerDataObject pdObject = PlayerManager.getPlayerDataObject(player);
            if (pdObject.containsHomeData() && !getOtherHomeList(pdObject, true).isEmpty()) {
                
                player.sendMessage(ChatColor.GREEN + "> These are your homes:");
                for (BaseComponent[] baseComp : getOtherHomeList(pdObject, true)){
                    player.spigot().sendMessage(baseComp);
                }
            } else {
                player.sendMessage(NOHOMES);
            }

        } else {
            player.sendMessage(NOPERMISSION);
        }
    }
    
    /** Sends a list of homes to the player in a viewable format for the PlayerDataObject. 
     * Must not be the same player.
     * @param player
     * @param pdObject
     */
    public static void sendHomeList(Player player, PlayerDataObject pdObject) {
        if (player.hasPermission(LISTPERMISSION)) {
            if (pdObject.containsHomeData()) {
                player.sendMessage(ChatColor.GREEN + "> These are your homes:");
                for (BaseComponent[] baseComp : getOtherHomeList(pdObject, false)){
                    player.spigot().sendMessage(baseComp);
                }
            } else {
                player.sendMessage(NOHOMES);
            }
        } else {
            player.sendMessage(NOPERMISSION);
        }
    }
    
    /** Returns a List of BaseComponents for the player homes.
     * @param pdo PlayerDataObject containing the homedata to send. Must not be the same player.
     * @param same
     * @return 
     */
    public static List<BaseComponent[]> getOtherHomeList(PlayerDataObject pdo, boolean same) {
        List<String> homeList = pdo.getHomeList();
        List<BaseComponent[]> compList = new ArrayList();
        
        int partitionSize = 4;
        List<List<String>> partitions = new LinkedList();
        for (int i = 0; i < homeList.size(); i += partitionSize) {
            partitions.add(homeList.subList(i, Math.min(i + partitionSize, homeList.size())));
        }

        for (List<String> homeLine : partitions) {
            ComponentBuilder compBuilder = new ComponentBuilder(" > ").color(ChatColor.GREEN);
            for (String home : homeLine) {
                compBuilder.append("[" + home + "]").color(ChatColor.BLUE);
                if (same) {
                    compBuilder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home " + home))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Go To " + home)))
                        .append("[i]").color(ChatColor.YELLOW)
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home info " + home))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(home + " info")))
                        .append(" ");
                } else {
                    compBuilder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/otherhome " + pdo.getName() + " " + home))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Go To " + home)))
                        .append("[i]").color(ChatColor.YELLOW)
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/otherhome " + pdo.getName() + " info " + home))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(home + " info")))
                        .append(" ");
                }
            };
            
            compList.add(compBuilder.create());
        }
        return compList;
        
    }

    /** Gets detailed and formatted home information for the player.
     * @param player
     * @param homeName 
     */
    public static void sendHomeInfo(Player player, String homeName) {
        
        PlayerDataObject pdObject = PlayerManager.getPlayerDataObject(player);
        if (pdObject.containsHome(homeName)) {
            JSONObject home = pdObject.getHome(homeName);
            BaseComponent[] homeInfo = new ComponentBuilder("> Home info for " + homeName).color(ChatColor.GREEN).create();
            BaseComponent[] homePosition = new ComponentBuilder(" > World: ").color(ChatColor.GREEN)
                    .append(home.getString("world")).color(ChatColor.BLUE)
                    .append(" (X,Y,Z): ").color(ChatColor.GREEN)
                    .append((int) Math.round(home.getDouble("X")) + "," + (int) Math.round(home.getDouble("Y")) + "," + (int) Math.round(home.getDouble("Z"))).color(ChatColor.GOLD)
                    .create();
            BaseComponent[] homeOptions = new ComponentBuilder(" > Home Options: ").color(ChatColor.GREEN)
                    .append(" [Delete]").color(ChatColor.DARK_RED)
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/home delete " + homeName))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("!!Delete This Home!!")))
                    .append(" [Share]").color(ChatColor.YELLOW)
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "Not Yet Implimented"))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Share")))
                    .append(" [Go To]").color(ChatColor.GREEN)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home " + homeName))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Go To")))
                    .create();
            player.spigot().sendMessage(homeInfo);
            player.spigot().sendMessage(homePosition);
            player.spigot().sendMessage(homeOptions);
            
        } else {
            player.sendMessage(NOHOME.replace("{h}", homeName));
        }
    }
    
    /** Gets detailed and formatted home information for the player from the other player's data.
     * @param player
     * @param homeName 
     * @param pdObject 
     */
    public static void sendOtherHomeInfo(Player player, String homeName, PlayerDataObject pdObject) {
        if (pdObject.containsHome(homeName)) {
            JSONObject home = pdObject.getHome(homeName);
            BaseComponent[] homeInfo = new ComponentBuilder("> Home info for " + homeName + " for ").color(ChatColor.GREEN).append(pdObject.getFriendlyName()).create();
            BaseComponent[] homePosition = new ComponentBuilder(" > World: ").color(ChatColor.GREEN)
                    .append(home.getString("world")).color(ChatColor.BLUE)
                    .append(" (X,Y,Z): ").color(ChatColor.GREEN)
                    .append((int) Math.round(home.getDouble("X")) + "," + (int) Math.round(home.getDouble("Y")) + "," + (int) Math.round(home.getDouble("Z"))).color(ChatColor.GOLD)
                    .append(" [Go To]").color(ChatColor.GREEN)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/otherhome " + pdObject.getName() + " " + homeName))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Go To")))
                    .create();
            player.spigot().sendMessage(homeInfo);
            player.spigot().sendMessage(homePosition);
        }
    }
    
    
    /** Sends a player to the specified home location. Returns true on a successful transport
     * otherwise returns false.
     * @param player
     * @param homeName
     * @return 
     */
    public static boolean sendToHome(Player player, String homeName){
        PlayerDataObject pdObject = PlayerManager.getPlayerDataObject(player);
        if (homeName.equalsIgnoreCase("bed")) {
            if (player.getBedSpawnLocation() != null){
                TPHandler.performSafeTeleport(player, player.getBedSpawnLocation());
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "> Error: You do not have a valid bed location. Sleep in a bed to set your bed spawn.");
                return false;
            }
        } else if (pdObject.containsHome(homeName)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', JawaCommands.getConfiguration().getConfigurationSection("messages").getString("home-tp", "&a> Welcome home")));
            TPHandler.performSafeTeleport(player, pdObject.getHomeLocation(homeName));
            return true;
        } else {
            player.sendMessage(NOHOME.replace("{h}", homeName));
            return false;
        }
    }
    
    /** Sends a player to the specified home location.Returns true on a successful transport
     * otherwise returns false.
     * @param player
     * @param homeName
     * @param pdObject
     * @return 
     */
    public static boolean sendToOtherHome(Player player, String homeName, PlayerDataObject pdObject){
        if (pdObject.containsHome(homeName)) {
            player.sendMessage(ChatColor.GREEN + "> Sending you to " + homeName);
            TPHandler.performSafeTeleport(player, pdObject.getHomeLocation(homeName));
            return true;
        } else {
            player.sendMessage(NOHOME.replace("{h}", homeName));
            return false;
        }
    }
}
