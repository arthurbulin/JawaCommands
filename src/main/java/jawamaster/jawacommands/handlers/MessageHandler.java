/*
 * Copyright (C) 2024 Arthur Bulin
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
import java.util.HashMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author Arthur Bulin
 */
public class MessageHandler {
    private static final HashMap<String, TextComponent> CUSTOMMESSAGES = new HashMap();
    private static final HashMap<String, TextComponent> DEFAULTMESSAGES = new HashMap();
    private static final String[] MESSAGES = {
        "safe-tp-freeze",
        "safe-tp-thaw",
        "freeze-public-freeze",
        "freeze-public-thaw",
        "home-no-addpermission",
        "home-no-delpermission",
        "home-no-permission",
        "home-add",
        "home-del",
        "home-tp",
        "home-tp-other",
        "home-no-home",
        "home-no-homes",
        "home-other-homes",
        "player-not-found",
        "no-bed"
        };
    
    /** Gets a standard message. This checks if the message has been customized. If it has not it gets
     * the default value and returns it.
     * @param messageName Plain text name of the message. This corresponds to the message name in the config file.
     * @return 
     */
    public static TextComponent getMessage(String messageName){
        if (CUSTOMMESSAGES.containsKey(messageName)){
            return CUSTOMMESSAGES.get(messageName);
        } else {
            return DEFAULTMESSAGES.get(messageName);
        }
    }
    
    /** Loads the default message TextComponents during startup. 
     * 
     */
    public static void loadDefaultMessages(){
        TextComponent safeTpFreezeMessage = Component.text("> You are being frozen for teleport").color(NamedTextColor.GREEN);
        DEFAULTMESSAGES.put("safe-tp-freeze", safeTpFreezeMessage);
        
        TextComponent safeTpThawMessage = Component.text("> You have been thawed").color(NamedTextColor.GREEN);
        DEFAULTMESSAGES.put("safe-tp-thaw", safeTpThawMessage);
        
        TextComponent freezePublicMessage = Component.text("> A sudden chill passes over {p}").color(NamedTextColor.DARK_AQUA);
        DEFAULTMESSAGES.put("freeze-public-freeze", freezePublicMessage);
        
        TextComponent thawPublicMessage = Component.text("> {p} is warmed by the light").color(NamedTextColor.GOLD);
        DEFAULTMESSAGES.put("freeze-public-thaw", thawPublicMessage);
        
        TextComponent homeNoAddPermissionMessage = Component.text("> You do not have permission to add a home in this world").color(NamedTextColor.DARK_RED);
        DEFAULTMESSAGES.put("home-no-addpermission", homeNoAddPermissionMessage);
        
        TextComponent homeAddMessage = Component.text("> {h} has been saved").color(NamedTextColor.GREEN);
        DEFAULTMESSAGES.put("home-add", homeAddMessage);
        
        TextComponent homeDelMessage = Component.text("> {h} has been deleted").color(NamedTextColor.GREEN);
        DEFAULTMESSAGES.put("home-del", homeDelMessage);
        
        TextComponent homeTpMessage = Component.text("> Welcome home").color(NamedTextColor.GREEN);
        DEFAULTMESSAGES.put("home-tp", homeTpMessage);
        
        TextComponent homeOtherTp = Component.text("> Sending you to {h}").color(NamedTextColor.GREEN);
        DEFAULTMESSAGES.put("home-tp-other", homeOtherTp);
        
        TextComponent homeNoPermissionMessage = Component.text("> You do not have permission to do that").color(NamedTextColor.RED);
        DEFAULTMESSAGES.put("home-no-permission", homeNoPermissionMessage);
        
        TextComponent homeNoDelPermissionMessage = Component.text("> You do not have permission to remove a home in this world").color(NamedTextColor.RED);
        DEFAULTMESSAGES.put("home-no-delpermission", homeNoDelPermissionMessage);
        
        TextComponent homeNoHomesMessage = Component.text("> You do not have any homes set. Run /home help to see how").color(NamedTextColor.RED);
        DEFAULTMESSAGES.put("home-no-homes", homeNoHomesMessage);
        
        TextComponent homeNoHomeMessage = Component.text("> {h} does not exist in your home list.").color(NamedTextColor.RED);
        DEFAULTMESSAGES.put("home-no-home", homeNoHomeMessage);
        
        TextComponent homeNoOtherHomeMessage = Component.text("> {h} does not exist in {p}'s home list.").color(NamedTextColor.RED);
        DEFAULTMESSAGES.put("home-no-other-home", homeNoOtherHomeMessage);
        
        TextComponent yourHomes = Component.text("> These are your homes, {p}:").color(NamedTextColor.RED);
        DEFAULTMESSAGES.put("home-your-homes", yourHomes);
        
        TextComponent otherHomes = Component.text("> These are {p}'s homes:").color(NamedTextColor.RED);
        DEFAULTMESSAGES.put("home-other-homes", otherHomes);
        
        TextComponent playerNotFoundError = Component.text(" > Error: That Player wasn't found either online or offline. Try using the player's actual minecraft name and not their nickname.", NamedTextColor.RED);
        DEFAULTMESSAGES.put("player-not-found", playerNotFoundError);
        
        TextComponent bedNotFound = Component.text("> Error: You do not have a valid bed location. Sleep in a bed to set your bed spawn.").color(NamedTextColor.RED);
        DEFAULTMESSAGES.put("no-bed", bedNotFound);
    }
    
    /** Will load the custom messages from the config file.
     * @param messageConfig The messages configuration section from the plugin config file.
     */
    public static void loadCustomMessages(ConfigurationSection messageConfig){
        TextComponent temp;
        for (String message : MESSAGES){
            if (!"default".equals(messageConfig.getString(message, "default"))){
                temp = LegacyComponentSerializer.legacyAmpersand().deserialize(messageConfig.getString(message));
                CUSTOMMESSAGES.put(message, temp);
            }
        }
    }
    
    /** Replaces a string value from a message with a specified TextComponent.
     * @param placeHolder The string value to replace
     * @param message The message to alter
     * @param replacement The item to insert
     * @return The modified textComponent
     */
    public static TextComponent runReplace(String placeHolder, TextComponent message, TextComponent replacement){
        TextReplacementConfig replacerConfig = TextReplacementConfig.builder().matchLiteral(placeHolder).replacement(replacement).build();
        return (TextComponent) message.replaceText(replacerConfig);
    }
    
    /** Gets a message and replaces portions of it. This relies on {@link #getMessage(java.lang.String) getMessage} and {@link #runReplace(java.lang.String, net.kyori.adventure.text.TextComponent, net.kyori.adventure.text.TextComponent) runReplace}
     * @param messageName Plain text name of the message. This corresponds to the message name in the config file.
     * @param placeHolder The string value to replace
     * @param replacement The item to insert
     * @return 
     */
    public static TextComponent getAndReplaceMessage(String messageName, String placeHolder, TextComponent replacement){
        TextComponent message =  getMessage(messageName);
        return runReplace(placeHolder, message, replacement);
    }
    
    /** Gets a message and replaces portions of it. This relies on {@link #getMessage(java.lang.String) getMessage} and {@link #runReplace(java.lang.String, net.kyori.adventure.text.TextComponent, net.kyori.adventure.text.TextComponent) runReplace}
     * @param messageName Plain text name of the message. This corresponds to the message name in the config file.
     * @param placeHolder The string value to replace
     * @param replacementString The item to insert
     * @return 
     */
    public static TextComponent getAndReplaceMessage(String messageName, String placeHolder, String replacementString){
        TextComponent replacement = Component.text(replacementString);
        TextComponent message =  getMessage(messageName);
        return runReplace(placeHolder, message, replacement);
    }
    
    public static TextComponent getAndReplaceMessage(String messageName, ArrayList<String> placeHolders, ArrayList<TextComponent> replacements){
        TextComponent message =  getMessage(messageName);
        for (int i = 0; i < placeHolders.size(); i++){
            message = runReplace(placeHolders.get(i), message, replacements.get(i));
        }
        return message;
    }
}
