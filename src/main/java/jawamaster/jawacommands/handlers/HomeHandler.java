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

import java.util.LinkedList;
import net.jawasystems.jawacore.handlers.LocationDataHandler;
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
            } else if (pdObject.containsHome(homeName) && !replace) {
                player.sendMessage(ChatColor.RED + "> Error: " + homeName +" already exists! Remove it first or replace it with 'replace'");
                return false;
            } else {
                pdObject.setHome(homeName, LocationDataHandler.packLocation(player.getLocation()));
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
     * @return 
     */
    public static void sendHomeList(Player player) {
        if (player.hasPermission(LISTPERMISSION)) {
            PlayerDataObject pdObject = PlayerManager.getPlayerDataObject(player);
            if (pdObject.containsHomeData()) {
                List<String> homeList = pdObject.getHomeList();

                int partitionSize = 4;
                List<List<String>> partitions = new LinkedList<List<String>>();
                for (int i = 0; i < homeList.size(); i += partitionSize) {
                    partitions.add(homeList.subList(i, Math.min(i + partitionSize, homeList.size())));
                }
                
//                double iters = Math.ceil(homeList.size() / 7.0);
//                int total = homeList.size();
                player.sendMessage(ChatColor.GREEN + "> These are your homes:");
                for (List<String> homeLine : partitions){
                    ComponentBuilder compBuilder = new ComponentBuilder(" > ").color(ChatColor.GREEN);
                    for (String home : homeLine){
                        compBuilder.append("[" + home + "]").color(ChatColor.BLUE)
                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home " + home))
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Go To " + home).create()))
                                .append("[i]").color(ChatColor.YELLOW)
                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home info " + home))
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(home + " info").create()))
                                .append(" ");
                    }
                    BaseComponent[] baseComp = compBuilder.create();
                    player.spigot().sendMessage(baseComp);
                }
//                //int i = 1;
//                int fromIndex = 0;
//                int toIndex = 0;
//                for (int i = 0; i < iters; i++){
//                    ComponentBuilder compBuilder = new ComponentBuilder(" > ").color(ChatColor.GREEN);
//                    
//                    toIndex = 7 * (i + 1);
//                    if (toIndex > total) toIndex = total;
//                    for (String home : homeList.subList(fromIndex, toIndex)){
//                        compBuilder.append("[" + home + "]").color(ChatColor.BLUE)
//                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home " + home))
//                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Go To " + home).create()))
//                                .append("[i]").color(ChatColor.YELLOW)
//                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home info " + home))
//                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(home + " info").create()))
//                                .append(" ");
//                                
//                    }        
//                    fromIndex = (7 * (i+1)) + 1;
//                    BaseComponent[] baseComp = compBuilder.create();
//                    player.spigot().sendMessage(baseComp);
//                }
                
            } else {
                player.sendMessage(NOHOMES);
            }

        } else {
            player.sendMessage(NOPERMISSION);
        }
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
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder("!!Delete This Home!!").create())))
                    .append(" [Share]").color(ChatColor.YELLOW)
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "Not Yet Implimented"))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Share").create()))
                    .append(" [Go To]").color(ChatColor.GREEN)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home " + homeName))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Go To").create()))
                    .create();
            player.spigot().sendMessage(homeInfo);
            player.spigot().sendMessage(homePosition);
            player.spigot().sendMessage(homeOptions);
            
        } else {
            player.sendMessage(NOHOME.replace("{h}", homeName));
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
            TPHandler.performSafeTeleport(player, LocationDataHandler.unpackLocation(pdObject.getHome(homeName)));
            return true;
        } else {
            player.sendMessage(NOHOME.replace("{h}", homeName));
            return false;
        }
    }
}
