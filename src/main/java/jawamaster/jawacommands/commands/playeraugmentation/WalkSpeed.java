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
public class WalkSpeed implements CommandExecutor {
    /*
    normal|impulse|light|warp|ridiculous|ludicrus|plaid
    sneak .1
    normal .2
    scout .3
    lightning_mcqueen .4
    rainbow_dash .5
    saitama .6
    roadrunner .7
    dash .8
    sonic .9
    theflash 1
    */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args == null | args.length == 0) {
//            ((Player) commandSender).setWalkSpeed(.2f);
            ((Player) commandSender).removePotionEffect(PotionEffectType.SPEED);
            ((Player) commandSender).removePotionEffect(PotionEffectType.JUMP);
            ((Player) commandSender).removePotionEffect(PotionEffectType.HUNGER);
            commandSender.sendMessage(ChatColor.GREEN + "> Walk speed reset to normal.");
//            commandSender.sendMessage("Exhastuon:" + ((Player) commandSender).getExhaustion());
//            commandSender.sendMessage("Foodleve:" + ((Player) commandSender).getFoodLevel());
//            commandSender.sendMessage("SaturatedRegenRate:" + ((Player) commandSender).getSaturatedRegenRate());
//            commandSender.sendMessage("Saturation:" + ((Player) commandSender).getSaturation());
//            commandSender.sendMessage("StarvationRate:" + ((Player) commandSender).getStarvationRate());
        } else if (args.length == 1) {
            boolean adverseEffect = !(((Player) commandSender).getGameMode().equals(GameMode.CREATIVE) || ((Player) commandSender).getGameMode().equals(GameMode.SPECTATOR));
            switch (args[0]) {
                case "2":
//                    ((Player) commandSender).setWalkSpeed(.2f);
                    ((Player) commandSender).removePotionEffect(PotionEffectType.SPEED);
                    ((Player) commandSender).removePotionEffect(PotionEffectType.JUMP);
                    ((Player) commandSender).removePotionEffect(PotionEffectType.HUNGER);
                    ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2, false, false, false));
                    ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 2, false, false, false));
                    if (adverseEffect) ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 2, false, false, false));
                    if (adverseEffect) ((Player) commandSender).removePotionEffect(PotionEffectType.HUNGER);
                    commandSender.sendMessage(ChatColor.GREEN + "> Walk speed reset to normal.");
                    break;
                case "3":
                    ((Player) commandSender).removePotionEffect(PotionEffectType.SPEED);
                    ((Player) commandSender).removePotionEffect(PotionEffectType.JUMP);
                    ((Player) commandSender).removePotionEffect(PotionEffectType.HUNGER);
//                    ((Player) commandSender).setWalkSpeed(.3f);
                    ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 5, false, false, false));
                    ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 5, false, false, false));
                    if (adverseEffect) ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 5, false, false, false));
                    commandSender.sendMessage(ChatColor.GREEN + "> Walk speed set to Scout.");
                    break;
                case "4":
                    ((Player) commandSender).removePotionEffect(PotionEffectType.SPEED);
                    ((Player) commandSender).removePotionEffect(PotionEffectType.JUMP);
                    ((Player) commandSender).removePotionEffect(PotionEffectType.HUNGER);
//                    ((Player) commandSender).setWalkSpeed(.4f);
                    ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 7, false, false, false));
                    ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 5, false, false, false));
                    if (adverseEffect) ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 7, false, false, false));
                    commandSender.sendMessage(ChatColor.GREEN + "> Walk speed set to Lightning McQueen.");
                    break;
                case "5":
                    ((Player) commandSender).removePotionEffect(PotionEffectType.SPEED);
                    ((Player) commandSender).removePotionEffect(PotionEffectType.JUMP);
                    ((Player) commandSender).removePotionEffect(PotionEffectType.HUNGER);
//                    ((Player) commandSender).setWalkSpeed(.5f);
                    ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 10, false, false, false));
                    ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 5, false, false, false));
                    if (adverseEffect) ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 10, false, false, false));
                    commandSender.sendMessage(ChatColor.GREEN + "> Walk speed set to Rainbow Dash.");
                    break;
                case "6":
                    ((Player) commandSender).removePotionEffect(PotionEffectType.SPEED);
                    ((Player) commandSender).removePotionEffect(PotionEffectType.JUMP);
                    ((Player) commandSender).removePotionEffect(PotionEffectType.HUNGER);
//                    ((Player) commandSender).setWalkSpeed(.6f);
                    ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 14, false, false, false));
                    ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 6, false, false, false));
                    if (adverseEffect) ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 14, false, false, false));
                    commandSender.sendMessage(ChatColor.GREEN + "> Walk speed set to Saitama.");
                    break;
                case "7":
                    ((Player) commandSender).removePotionEffect(PotionEffectType.SPEED);
                    ((Player) commandSender).removePotionEffect(PotionEffectType.JUMP);
                    ((Player) commandSender).removePotionEffect(PotionEffectType.HUNGER);
//                    ((Player) commandSender).setWalkSpeed(.7f);
                    ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 20, false, false, false));
                    ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 7, false, false, false));
                    if (adverseEffect) ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 20, false, false, false));
                    commandSender.sendMessage(ChatColor.GREEN + "> Walk speed set to Road Runner.");
                    break;
                case "8":
                    ((Player) commandSender).removePotionEffect(PotionEffectType.SPEED);
                    ((Player) commandSender).removePotionEffect(PotionEffectType.JUMP);
                    ((Player) commandSender).removePotionEffect(PotionEffectType.HUNGER);
//                    ((Player) commandSender).setWalkSpeed(.8f);
                    ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 30, false, false, false));
                    ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 8, false, false, false));
                    if (adverseEffect) ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 30, false, false, false));
                    commandSender.sendMessage(ChatColor.GREEN + "> Walk speed set to Dash.");
                    break;
                case "9":
                    ((Player) commandSender).removePotionEffect(PotionEffectType.SPEED);
                    ((Player) commandSender).removePotionEffect(PotionEffectType.JUMP);
                    ((Player) commandSender).removePotionEffect(PotionEffectType.HUNGER);
//                    ((Player) commandSender).setWalkSpeed(.9f);
                    ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 40, false, false, false));
                    ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 9, false, false, false));
                    if (adverseEffect) ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 40, false, false, false));
                    commandSender.sendMessage(ChatColor.GREEN + "> Walk speed set to Sonic.");
                    break;
                case "10":
                    ((Player) commandSender).removePotionEffect(PotionEffectType.SPEED);
                    ((Player) commandSender).removePotionEffect(PotionEffectType.JUMP);
                    ((Player) commandSender).removePotionEffect(PotionEffectType.HUNGER);
//                    ((Player) commandSender).setWalkSpeed(1f);
//                    ((Player) commandSender).setExhaustion(1.0f);
                    ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 50, false, false, false));
                    ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 10, false, false, false));
                    if (adverseEffect) ((Player) commandSender).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 50, false, false, false));
                    commandSender.sendMessage(ChatColor.GREEN + "> Walk speed set to The Flash.");
                    break;
                default:
                    commandSender.sendMessage(ChatColor.RED + "> Error: How fast is that?");
                    break;
            }
//            commandSender.sendMessage("Exhastuon:" + ((Player) commandSender).getExhaustion());
//            commandSender.sendMessage("Foodleve:" + ((Player) commandSender).getFoodLevel());
//            commandSender.sendMessage("SaturatedRegenRate:" + ((Player) commandSender).getSaturatedRegenRate());
//            commandSender.sendMessage("Saturation:" + ((Player) commandSender).getSaturation());
//            commandSender.sendMessage("StarvationRate:" + ((Player) commandSender).getStarvationRate());
        } else {
            commandSender.sendMessage(ChatColor.RED + "> Error: Too many arguments.");
        }
    return true;
    }
}
