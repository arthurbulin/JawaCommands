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
package jawamaster.jawacommands.commands.playeraugmentation;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * @author Jawamaster (Arthur Bulin)
 */
public class FlySpeed implements CommandExecutor {

    /*
    normal|impulse|light|warp|ridiculous|ludicrus|plaid
    normal .1
    impulse .2
    light .3
    warp .4
    ridiculous .5
    ludicrus .6
    plaid 1
    */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (((Player)commandSender).getAllowFlight()) {
            if (args == null | args.length == 0) {
                ((Player) commandSender).setFlySpeed(.1f);
                ((Player) commandSender).removePotionEffect(PotionEffectType.HUNGER);
                commandSender.sendMessage(ChatColor.GREEN + "> Flight speed reset to normal.");
            } else if (args.length == 1) {
                boolean adverseEffect = !(((Player) commandSender).getGameMode().equals(GameMode.CREATIVE) || ((Player) commandSender).getGameMode().equals(GameMode.SPECTATOR));
                switch (args[0]) {
                    case "1":
                        ((Player) commandSender).setFlySpeed(.1f);
                        ((Player) commandSender).removePotionEffect(PotionEffectType.HUNGER);
                        commandSender.sendMessage(ChatColor.GREEN + "> Flight speed reset to normal.");
                        break;
                    case "2":
                        ((Player) commandSender).removePotionEffect(PotionEffectType.HUNGER);
                        ((Player) commandSender).setFlySpeed(.2f);
                        if (adverseEffect) ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 2, false, false, false));
                        commandSender.sendMessage(ChatColor.GREEN + "> Flight speed set to impulse speed.");
                        break;
                    case "3":
                        ((Player) commandSender).removePotionEffect(PotionEffectType.HUNGER);
                        ((Player) commandSender).setFlySpeed(.3f);
                        if (adverseEffect) ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 5, false, false, false));
                        commandSender.sendMessage(ChatColor.GREEN + "> Flight speed set to light speed.");
                        break;
                    case "4":
                        ((Player) commandSender).removePotionEffect(PotionEffectType.HUNGER);
                        ((Player) commandSender).setFlySpeed(.4f);
                        if (adverseEffect) ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 7, false, false, false));
                        commandSender.sendMessage(ChatColor.GREEN + "> Flight speed set to warp speed.");
                        break;
                    case "6":
                        ((Player) commandSender).removePotionEffect(PotionEffectType.HUNGER);
                        ((Player) commandSender).setFlySpeed(.6f);
                        if (adverseEffect) ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 14, false, false, false));
                        commandSender.sendMessage(ChatColor.GREEN + "> Flight speed set to ridiculous speed.");
                        break;
                    case "8":
                        ((Player) commandSender).removePotionEffect(PotionEffectType.HUNGER);
                        ((Player) commandSender).setFlySpeed(.8f);
                        if (adverseEffect) ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 30, false, false, false));
                        commandSender.sendMessage(ChatColor.GREEN + "> Flight speed set to ludicrus speed.");
                        break;
                    case "10":
                        ((Player) commandSender).removePotionEffect(PotionEffectType.HUNGER);
                        ((Player) commandSender).setFlySpeed(1f);
                        if (adverseEffect) ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 50, false, false, false));
                        commandSender.sendMessage(ChatColor.GREEN + "> Flight speed set to " 
                                + ChatColor.AQUA + "p" 
                                + ChatColor.BLUE + "l" 
                                + ChatColor.GOLD + "a" 
                                + ChatColor.GREEN + "i" 
                                + ChatColor.LIGHT_PURPLE + "d"
                                + ChatColor.GREEN + " speed.");
                        break;
                    default:
                        commandSender.sendMessage(ChatColor.RED + "> Error: How fast is that?");
                        break;
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + "> Error: Too many arguments.");
            }
        } else {
            commandSender.sendMessage(ChatColor.RED + "> Error: You can't fly right now.");
        }
        return true;
    }
    
}
