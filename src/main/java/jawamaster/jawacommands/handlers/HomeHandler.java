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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jawamaster.jawacommands.JawaCommands;
import jawamaster.jawapermissions.handlers.PermissionsHandler;
import net.jawasystems.jawacore.PlayerManager;
import net.jawasystems.jawacore.dataobjects.PlayerDataObject;
import net.jawasystems.jawacore.utils.TimeParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.json.JSONObject;

/**
 *
 * @author Jawamaster (Arthur Bulin)
 */
public class HomeHandler {
    
    private static final Logger LOGGER = Logger.getLogger("HomeHandler");
    
    public static final String HOMEPERMISSION = "jawacommands.home";
    public static final String ADDPERMISSION = HOMEPERMISSION + ".add";
    public static final String DELPERMISSION = HOMEPERMISSION + ".del";
    public static final String LISTPERMISSION = HOMEPERMISSION + ".list";
    public static final String OTHERPERMISSION = HOMEPERMISSION + ".other";
    public static final String ADMINPERMISSION = HOMEPERMISSION.concat(".admin");
    //System message (non-customizable)
    private static final TextComponent ERRORBED = Component.text("> Error: You can only set a bed home by sleeping in a bed!").color(NamedTextColor.RED);
    private static final TextComponent ERROREXISTS = Component.text("> Error: {h} already exists! Remove it first or replace it with 'replace'").color(NamedTextColor.RED);
    private static final TextComponent ERRORLIMIT = Component.text("> You have reached the home limit of {l}. You must either replace a home or delete one.").color(NamedTextColor.YELLOW);
    
//    private static final TextComponent NOPERMISSION = Component.text("> You do not have permission to do that").color(NamedTextColor.RED);
//    private static final String NOPERMISSION = ChatColor.RED + "> You do not have permission to do that.";
//    private static final String NODELPERMISSION = ChatColor.RED + "> You do not have permission to remove a home in this world.";
//    private static final String NOHOMES = ChatColor.RED + "> You do not have any homes set. Run /home help to see how.";
//    private static final String NOHOME = ChatColor.RED + "> {h} does not exist in your home list.";
    
    private static final HashMap<String, Integer> homeLimits = new HashMap();

    /** Returns true if the player has any homes at all for this server
     * @param player
     * @return 
     */
    public static boolean hasHomes(Player player){
        PlayerDataObject pdObject = PlayerManager.getPlayerDataObject(player);
        return pdObject.containsHomeData();
    }
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
                player.sendMessage(ERRORBED);
                return false;
            } else if (pdObject.containsHomeData() && pdObject.containsHome(homeName) && !replace) {
                TextComponent existsMessage = MessageHandler.runReplace("{h}", ERROREXISTS, Component.text(homeName));
                player.sendMessage(existsMessage);
                return false;
            } else {
                if (homeLimits.containsKey(pdObject.getRank()) && (pdObject.getHomeCount() >= homeLimits.get(pdObject.getRank()))){
                    TextComponent limitMessage = MessageHandler.runReplace("{l}", ERRORLIMIT, Component.text(homeLimits.get(pdObject.getRank())));
                    player.sendMessage(limitMessage);
//                    player.sendMessage(ChatColor.YELLOW + "> You have reached the home limit of " + homeLimits.get(pdObject.getRank()) + ". You must either replace a home or delete one.");
                    
                    return true;
                } else {
                    pdObject.setHome(player, homeName);
                    //pdObject.setHome(homeName, LocationDataHandler.packLocation(player.getLocation()));
//                    String homeMessage = JawaCommands.getConfiguration().getConfigurationSection("messages").getString("home-add", ChatColor.GREEN + "> {h} has been saved").replace("{h}", homeName);
                    TextComponent homeMessage = MessageHandler.runReplace("{h}", MessageHandler.getMessage("home-add"), Component.text(homeName));
                    player.sendMessage(homeMessage);
                    return true;
                }
            }
        } else {
            player.sendMessage(MessageHandler.getMessage("home-no-addpermission"));
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
//                player.sendMessage(NOHOME.replace("{h}", homeName));
                player.sendMessage(MessageHandler.getAndReplaceMessage("home-no-home", "{h}", homeName));
                return false;
            } else {
                //TextComponent homeMessage = MessageHandler.runReplace("{h}", MessageHandler.getMessage("home-del"), Component.text(homeName));
                player.sendMessage(MessageHandler.getAndReplaceMessage("home-del", "{h}", homeName));
                //player.sendMessage(homeMessage);
                pdObject.removeHome(homeName);
                return true;
            }

        } else {
            player.sendMessage(MessageHandler.getAndReplaceMessage("home-no-delpermission", "{h}", homeName));
//            player.sendMessage(NODELPERMISSION);
            return false;
        }
    }

    /** Sends a list of homes to the player in a viewable format.
     * @param player
     */
    public static void sendHomeList(Player player) {
        if (player.hasPermission(LISTPERMISSION)) {
            PlayerDataObject pdObject = PlayerManager.getPlayerDataObject(player);
            if (pdObject.containsHomeData()) {
                List<TextComponent> homesList = getHomeList(pdObject, false);
                player.sendMessage(MessageHandler.getAndReplaceMessage("home-your-homes", "{p}", pdObject.getFriendlyName()));
                for (TextComponent line : homesList){
                    player.sendMessage(line);
                }
            } else {
                player.sendMessage(MessageHandler.getMessage("home-no-homes"));
            }
        } else {
            player.sendMessage(MessageHandler.getMessage("home-no-permission"));
        }
    }
    
    /** Sends a list of homes to the player in a viewable format for the PlayerDataObject. 
     * Must not be the same player.
     * @param player
     * @param pdObject
     */
    public static void sendOtherHomeList(Player player, PlayerDataObject pdObject) {
        if (player.hasPermission(OTHERPERMISSION)) {   
            if (pdObject.containsHomeData()) {
                List<TextComponent> homesList = getHomeList(pdObject, false);
                player.sendMessage(MessageHandler.getAndReplaceMessage("home-other-homes", "{p}", pdObject.getFriendlyName()));
                for (TextComponent line : homesList){
                    player.sendMessage(line);
                }
            } else {
                player.sendMessage(MessageHandler.getMessage("home-other-homes"));
            }
        } else {
            player.sendMessage(MessageHandler.getMessage("home-no-permission"));
        }
        
    }
    
    
    /** Returns a List of BaseComponents for the player homes.
     * @param pdo PlayerDataObject containing the homedata to send. Must not be the same player.
     * @param same
     * @return 
     */
    private static List<TextComponent> getHomeList(PlayerDataObject pdo, boolean same) {
        List<String> homeList = pdo.getHomeList();
//        List<BaseComponent[]> compList = new ArrayList();
        List<TextComponent> compList = new ArrayList();
        
        int partitionSize = 4;
        List<List<String>> partitions = new LinkedList();
        for (int i = 0; i < homeList.size(); i += partitionSize) {
            partitions.add(homeList.subList(i, Math.min(i + partitionSize, homeList.size())));
        }

        for (List<String> homeLine : partitions) {
//            ComponentBuilder compBuilder = new ComponentBuilder(" > ").color(ChatColor.GREEN);
            TextComponent compBuilder = Component.text(" > ").color(NamedTextColor.GREEN);
            for (String home : homeLine) {
//                compBuilder.append("[" + home + "]").color(ChatColor.BLUE);
                String command;
                String commandInfo;
                if (same) {
                    command = "/home ".concat(home);
                    commandInfo = "/home info ".concat(home);
                } else {
                    command = "/otherhome ".concat(home);
                    commandInfo = "/otherhome info ".concat(home);
                }
                compBuilder.append(Component.text("[".concat(home).concat("]")).color(NamedTextColor.BLUE)
                                .clickEvent(ClickEvent.runCommand(command))
                                .hoverEvent(HoverEvent.showText(Component.text("Go to".concat(home)))))
                            .append(Component.text("[i]").color(NamedTextColor.YELLOW)
                                .clickEvent(ClickEvent.runCommand(commandInfo))
                                .hoverEvent(HoverEvent.showText(Component.text(home.concat(" info")))));
            };
            compList.add(compBuilder);
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
            TextComponent homeINFO = Component.text("> Home info for ".concat(homeName)).color(NamedTextColor.GREEN);
//            BaseComponent[] homeInfo = new ComponentBuilder("> Home info for " + homeName).color(ChatColor.GREEN).create();
            TextComponent position = Component.text(" > World: ").color(NamedTextColor.GREEN)
                    .append(Component.text(home.getString("world")).color(NamedTextColor.BLUE))
                    .append(Component.text(" (X,Y,Z): ").color(NamedTextColor.GREEN))
                    .append(Component.text((int) Math.round(home.getDouble("X")) + "," + (int) Math.round(home.getDouble("Y")) + "," + (int) Math.round(home.getDouble("Z"))).color(NamedTextColor.GOLD))
                    .append(Component.text(" Created: ").color(NamedTextColor.GREEN))
                    .append(Component.text(TimeParser.getHumanReadableDateTime(home.getString("date"),2)).color(NamedTextColor.DARK_AQUA));
//            BaseComponent[] homePosition = new ComponentBuilder(" > World: ").color(ChatColor.GREEN)
//                    .append(home.getString("world")).color(ChatColor.BLUE)
//                    .append(" (X,Y,Z): ").color(ChatColor.GREEN)
//                    .append((int) Math.round(home.getDouble("X")) + "," + (int) Math.round(home.getDouble("Y")) + "," + (int) Math.round(home.getDouble("Z"))).color(ChatColor.GOLD)
//                    .append(" Created: ").color(ChatColor.GREEN)
//                    .append(TimeParser.getHumanReadableDateTime(home.getString("date"),2)).color(ChatColor.DARK_AQUA)
//                    .create();
            TextComponent options = Component.text(" > Home Options: ").color(NamedTextColor.GREEN)
                    .append(Component.text(" [Delete] ").color(NamedTextColor.DARK_RED)
                            .clickEvent(ClickEvent.suggestCommand("/home delete ".concat(homeName)))
                            .hoverEvent(HoverEvent.showText(Component.text("!!Delete Thsi Home!!"))))
                    .append(Component.text(" [Share] ").color(NamedTextColor.YELLOW)
                            .clickEvent(ClickEvent.suggestCommand("Not Yet Implimented"))
                            .hoverEvent(HoverEvent.showText(Component.text("Share"))))
                    .append(Component.text(" [Go To] ").color(NamedTextColor.GREEN)
                            .clickEvent(ClickEvent.runCommand("/home ".concat(homeName)))
                            .hoverEvent(HoverEvent.showText(Component.text("Go To"))));
                    ;
//            BaseComponent[] homeOptions = new ComponentBuilder(" > Home Options: ").color(ChatColor.GREEN)
//                    .append(" [Delete]").color(ChatColor.DARK_RED)
//                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/home delete " + homeName))
//                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("!!Delete This Home!!")))
//                    .append(" [Share]").color(ChatColor.YELLOW)
//                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "Not Yet Implimented"))
//                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Share")))
//                    .append(" [Go To]").color(ChatColor.GREEN)
//                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home " + homeName))
//                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Go To")))
//                    .create();
            player.sendMessage(homeINFO);
            player.sendMessage(position);
            player.sendMessage(options);
            
        } else {
            player.sendMessage(MessageHandler.getAndReplaceMessage("home-no-home", "{h}", homeName));
//            player.sendMessage(NOHOME.replace("{h}", homeName));
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
            TextComponent homeINFO = Component.text("> Home info for ".concat(homeName).concat(" for ")).color(NamedTextColor.GREEN).append(pdObject.getFriendlyName());
//            BaseComponent[] homeInfo = new ComponentBuilder("> Home info for " + homeName + " for ").color(ChatColor.GREEN).append(pdObject.getFriendlyName()).create();
            TextComponent position = Component.text(" > World: ").color(NamedTextColor.GREEN)
                    .append(Component.text(home.getString("world")).color(NamedTextColor.BLUE))
                    .append(Component.text(" (X,Y,Z): ").color(NamedTextColor.GREEN))
                    .append(Component.text((int) Math.round(home.getDouble("X")) + "," + (int) Math.round(home.getDouble("Y")) + "," + (int) Math.round(home.getDouble("Z"))).color(NamedTextColor.GOLD))
                    .append(Component.text(" Created: ").color(NamedTextColor.GREEN))
                    .append(Component.text(TimeParser.getHumanReadableDateTime(home.getString("date"),2)).color(NamedTextColor.DARK_AQUA));
            TextComponent options = Component.text(" > Home Options: ").color(NamedTextColor.GREEN)
                    .append(Component.text(" [Go To] ").color(NamedTextColor.GREEN)
                            .clickEvent(ClickEvent.runCommand("/otherhome ".concat(pdObject.getName()).concat(" ").concat(homeName)))
                            .hoverEvent(HoverEvent.showText(Component.text("Go To"))));    
//            BaseComponent[] homePosition = new ComponentBuilder(" > World: ").color(ChatColor.GREEN)
//                    .append(home.getString("world")).color(ChatColor.BLUE)
//                    .append(" (X,Y,Z): ").color(ChatColor.GREEN)
//                    .append((int) Math.round(home.getDouble("X")) + "," + (int) Math.round(home.getDouble("Y")) + "," + (int) Math.round(home.getDouble("Z"))).color(ChatColor.GOLD)
//                    .append(" [Go To]").color(ChatColor.GREEN)
//                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/otherhome " + pdObject.getName() + " " + homeName))
//                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Go To")))
//                    .create();
            player.sendMessage(homeINFO);
            player.sendMessage(position);
            player.sendMessage(options);
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
            if (player.getRespawnLocation()!= null){
                TPHandler.performSafeTeleport(player, player.getRespawnLocation());
                return true;
            } else {
                player.sendMessage(MessageHandler.getMessage("no-bed"));
                return false;
            }
        } else if (pdObject.containsHome(homeName)) {
            player.sendMessage(MessageHandler.getMessage("home-tp"));
            TPHandler.performSafeTeleport(player, pdObject.getHomeLocation(homeName));
            return true;
        } else {
            player.sendMessage(MessageHandler.getAndReplaceMessage("home-no-home", "{h}", homeName));
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
            player.sendMessage(MessageHandler.getAndReplaceMessage("home-tp-other", "{h}", homeName));
//            player.sendMessage(ChatColor.GREEN + "> Sending you to " + homeName);
            TPHandler.performSafeTeleport(player, pdObject.getHomeLocation(homeName));
            return true;
        } else {
            ArrayList<String> holders = new ArrayList<String>(Arrays.asList("{h}","{p}"));
            ArrayList<TextComponent> replace = new ArrayList<TextComponent>(Arrays.asList(Component.text(homeName),pdObject.getFriendlyName()));
            player.sendMessage(MessageHandler.getAndReplaceMessage("home-no-other-home", holders, replace));
            return false;
        }
    }
    
    /** Sets the home limit for specific rank.
     * @param rank The rank as a string
     * @param limit The home count or anything less than 0 to remove the limit
     */
    public static void setHomeLimit(String rank, int limit){
        if (PermissionsHandler.rankExists(rank)) {
            if (limit < 0){
                homeLimits.remove(rank);
                LOGGER.log(Level.INFO, "Home limits removed for {0}", rank);
            } else {
                homeLimits.put(rank, limit);
                LOGGER.log(Level.INFO, "Home limit for {0} set to {1}", new Object[]{rank, limit});
            }
        } else {
            LOGGER.log(Level.INFO, "Cannot set home limit for {0} as it is not a valid rank", rank);
        }
    }
}
