/*
 * Copyright (C) 2020 alexander
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

import java.util.HashMap;
import jawamaster.jawacommands.JawaCommands;
import jawamaster.jawacommands.commands.playeraugmentation.FullBright;
import jawamaster.jawacommands.commands.ChangeGameMode;
import jawamaster.jawacommands.commands.Colors;
import jawamaster.jawacommands.commands.ComeHere;
import jawamaster.jawacommands.commands.playeraugmentation.FlySpeed;
import jawamaster.jawacommands.commands.GoThere;
import jawamaster.jawacommands.commands.RepairCommand;
import jawamaster.jawacommands.commands.playeraugmentation.PlayerTime;
import jawamaster.jawacommands.commands.playeraugmentation.PlayerWeather;
import jawamaster.jawacommands.commands.playeraugmentation.SurvivalFly;
import jawamaster.jawacommands.commands.TPAccept;
import jawamaster.jawacommands.commands.playeraugmentation.WalkSpeed;
import jawamaster.jawacommands.commands.admin.Freeze;
import jawamaster.jawacommands.commands.admin.SudoAs;
import jawamaster.jawacommands.commands.development.BackCommand;
import jawamaster.jawacommands.kit.KitCommand;
import jawamaster.jawacommands.commands.development.RandomTP;
import jawamaster.jawacommands.commands.home.Home;
import jawamaster.jawacommands.commands.home.HomeInfo;
import jawamaster.jawacommands.commands.home.OtherHome;
import jawamaster.jawacommands.commands.spawn.RemoveSpawn;
import jawamaster.jawacommands.commands.spawn.SetSpawn;
import jawamaster.jawacommands.commands.spawn.Spawn;
import jawamaster.jawacommands.commands.warps.DelWarp;
import jawamaster.jawacommands.commands.warps.MakeWarp;
import jawamaster.jawacommands.commands.warps.ModWarp;
import jawamaster.jawacommands.commands.warps.WarpCommand;
import jawamaster.jawacommands.commands.warps.WarpBlackList;
import jawamaster.jawacommands.commands.warps.WarpInfo;
import jawamaster.jawacommands.commands.warps.WarpWhitelist;
import jawamaster.jawacommands.commands.warps.YeetPort;
import jawamaster.jawacommands.kit.GiveKit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

/**
 *
 * @author alexander
 */
public class CommandControlHandler {

    private static JavaPlugin plugin;
    private static final HashMap<String, CommandExecutor> COMMANDMAP = new HashMap();
    public static final HashMap<String, JSONObject> COMMANDTABS = new HashMap();

    public CommandControlHandler(JavaPlugin plugin) {
        CommandControlHandler.plugin = plugin;

        COMMANDMAP.put("colors", new Colors());
        
        COMMANDMAP.put("kit", new KitCommand());
        COMMANDMAP.put("givekit", new GiveKit());

        if (JawaCommands.homesEnabled()) {
            COMMANDMAP.put("home", new Home());
            COMMANDMAP.put("homeinfo", new HomeInfo());
            COMMANDMAP.put("otherhome", new OtherHome());
        }
        
        COMMANDMAP.put("sudo", new SudoAs());

        if (JawaCommands.warpsEnabled()) {
            COMMANDMAP.put("makewarp", new MakeWarp());
            COMMANDMAP.put("warp", new WarpCommand());
            COMMANDMAP.put("modwarp", new ModWarp());
            COMMANDMAP.put("delwarp", new DelWarp());
            COMMANDMAP.put("wlwarp", new WarpWhitelist());
            COMMANDMAP.put("blwarp", new WarpBlackList());
            COMMANDMAP.put("warpinfo", new WarpInfo());
        }
        
        COMMANDMAP.put("gm", new ChangeGameMode());

        COMMANDMAP.put("spawn", new Spawn());
        COMMANDMAP.put("setspawn", new SetSpawn());
        COMMANDMAP.put("removespawn", new RemoveSpawn());

        COMMANDMAP.put("comehere", new ComeHere());
        COMMANDMAP.put("gothere", new GoThere());
        COMMANDMAP.put("accept", new TPAccept());

        COMMANDMAP.put("fullbright", new FullBright());

        COMMANDMAP.put("yeetport", new YeetPort());
        
        COMMANDMAP.put("back", new BackCommand());
        
        COMMANDMAP.put("tpr", new RandomTP());
        
        COMMANDMAP.put("freeze", new Freeze());
        
        COMMANDMAP.put("pweather", new PlayerWeather());
        COMMANDMAP.put("ptime", new PlayerTime());
        
        COMMANDMAP.put("sfly", new SurvivalFly());
        COMMANDMAP.put("flyspeed", new FlySpeed());
        COMMANDMAP.put("walkspeed", new WalkSpeed());
        
        COMMANDMAP.put("repair", new RepairCommand());

        registerCommands();
        buildTabCompletionTable();
    }

    private static void registerCommands() {
        for (String command : COMMANDMAP.keySet()) {
            registerCommand(command, COMMANDMAP.get(command));
        }
    }

    private static boolean registerCommand(String command, CommandExecutor exec) {
        plugin.getCommand(command).setExecutor(exec);
        return true;
    }
    
    public static void buildTabCompletionTable(){
        //System.out.println(commandMap.get("ptime").getClass());
    }

}
