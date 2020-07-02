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
package jawamaster.jawacommands.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.jawasystems.jawacore.PlayerManager;
import net.jawasystems.jawacore.dataobjects.PlayerDataObject;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONObject;

/**
 *
 * @author Jawamaster (Arthur Bulin)
 */
public class PlayerTime implements CommandExecutor {

    private final Long MORNING = 0L;
    private final Long NOON = 6000L;
    private final Long SUNSET = 12000L;
    private final Long NIGHT = 13000L;
    private final Long MIDNIGHT = 18000L;
    private final Long SUNRISE = 23000L;
    //private final String PERMISSION = "jawacommands.ptime";
    private final String OTHERPERMISSION = "jawacommands.ptime.other";
    private final String USAGE = "/ptime [morning|noon|sunset|night|midnight|sunrise|reset] [player]. Run without arguments to reset your own weather.";
    public static final List<String> TABCOMPLETES = Arrays.asList("morning","noon","sunset","night","midnight","sunrise","reset");
//    public static final String COMMANDREGEX = "/ptime\\s";
//    public static final List<List<String>> REGEXAREAS = Arrays.asList(Arrays.asList("/ptime\\s(morning|noon|sunset|night|midnight|sunrise|reset)\\s"));
//    public static final List<List<String>> BUFFERAREAS = Arrays.asList(Arrays.asList("morning","noon","sunset","night","midnight","sunrise","reset"), Arrays.asList());

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        ((Player) commandSender).setPlayerTime(0, true);
        if (args == null || args.length == 0) {
            ((Player) commandSender).resetPlayerTime();
            commandSender.sendMessage(ChatColor.GREEN + "> Your time has been reset.");
        } else if (args.length >= 1) {
            PlayerDataObject target;
            String who;
            if (args.length >= 2 && commandSender.hasPermission(OTHERPERMISSION)) {
                target = PlayerManager.getPlayerDataObject(args[1]);
                if (target == null || !target.isOnline()) {
                    commandSender.sendMessage(PlayerManager.NOPLAYERERROR);
                    return true;
                }
                who = target.getFriendlyName() + "'s" + ChatColor.GREEN;
            } else {
                target = PlayerManager.getPlayerDataObject((Player) commandSender);
                who = "Your";
            }

            if (args[0].matches("^mor?n?i?n?g?")) {
                target.getPlayer().setPlayerTime(MORNING, false);
                commandSender.sendMessage(ChatColor.GREEN + "> " + who + " time has been set to morning");
            } else if (args[0].matches("^no?o?n?")) {
                target.getPlayer().setPlayerTime(NOON, false);
                commandSender.sendMessage(ChatColor.GREEN + "> " + who + " time has been set to noon");
            } else if (args[0].matches("^sunse?t?")) {
                target.getPlayer().setPlayerTime(SUNSET, false);
                commandSender.sendMessage(ChatColor.GREEN + "> " + who + " time has been set to sunset");
            } else if (args[0].matches("^ni?g?h?t?")) {
                target.getPlayer().setPlayerTime(NIGHT, false);
                commandSender.sendMessage(ChatColor.GREEN + "> " + who + " time has been set to night");
            } else if (args[0].matches("^mi?d?n?i?g?h?t?")) {
                target.getPlayer().setPlayerTime(MIDNIGHT, false);
                commandSender.sendMessage(ChatColor.GREEN + "> " + who + " time has been set to midnight");
            } else if (args[0].matches("^sunri?s?e?")) {
                target.getPlayer().setPlayerTime(SUNRISE, false);
                commandSender.sendMessage(ChatColor.GREEN + "> " + who + " time has been set to sunrise");
            } else if (args[0].matches("^res?e?t?")) {
                target.getPlayer().resetPlayerTime();
                commandSender.sendMessage(ChatColor.GREEN + "> " + who + " time has been reset");
            } else {
                commandSender.sendMessage(ChatColor.RED + "> " + USAGE);
            }
        }
        return true;
    }

}
