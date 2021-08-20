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
    private static HashMap<String, CommandExecutor> commandMap;
    public static HashMap<String, JSONObject> commandTabs;

    public CommandControlHandler(JavaPlugin plugin) {
        CommandControlHandler.plugin = plugin;
        commandMap = new HashMap();
        commandTabs = new HashMap();
        commandMap.put("colors", new Colors());
        commandMap.put("kit", new KitCommand());
        commandMap.put("givekit", new GiveKit());

        commandMap.put("home", new Home());
//        commandMap.put("sethome", new setHome());
//        commandMap.put("delhome", new delHome());
//        commandMap.put("listhome", new listHomes());
        commandMap.put("homeinfo", new HomeInfo());

        commandMap.put("sudo", new SudoAs());

        commandMap.put("makewarp", new MakeWarp());
        commandMap.put("warp", new WarpCommand());
        commandMap.put("modwarp", new ModWarp());
        commandMap.put("delwarp", new DelWarp());
        commandMap.put("wlwarp", new WarpWhitelist());
        commandMap.put("blwarp", new WarpBlackList());

        commandMap.put("gm", new ChangeGameMode());

        commandMap.put("spawn", new Spawn());
        commandMap.put("setspawn", new SetSpawn());
        commandMap.put("removespawn", new RemoveSpawn());

        commandMap.put("comehere", new ComeHere());
        commandMap.put("gothere", new GoThere());
        commandMap.put("accept", new TPAccept());

        commandMap.put("fullbright", new FullBright());

        commandMap.put("yeetport", new YeetPort());
        
        commandMap.put("back", new BackCommand());
        
        commandMap.put("tpr", new RandomTP());
        
        commandMap.put("freeze", new Freeze());
        
        commandMap.put("pweather", new PlayerWeather());
        commandMap.put("ptime", new PlayerTime());
        
        commandMap.put("sfly", new SurvivalFly());
        commandMap.put("flyspeed", new FlySpeed());
        commandMap.put("walkspeed", new WalkSpeed());
        
        commandMap.put("warpinfo", new WarpInfo());
        
        commandMap.put("otherhome", new OtherHome());
        
        commandMap.put("repair", new RepairCommand());

        registerCommands();
        buildTabCompletionTable();
    }

    private static void registerCommands() {
        for (String command : commandMap.keySet()) {
            registerCommand(command, commandMap.get(command));

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
