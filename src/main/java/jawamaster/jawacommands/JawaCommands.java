/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package jawamaster.jawacommands;

import java.util.HashMap;
import java.util.UUID;
import jawamaster.jawacommands.commands.development.TestCommand;
import jawamaster.jawacommands.handlers.CommandControlHandler;
import jawamaster.jawacommands.kit.KitHandler;
import jawamaster.jawacommands.handlers.WarpHandler;
import jawamaster.jawacommands.handlers.WorldHandler;
import jawamaster.jawacommands.listeners.FirstSpawnListener;
import jawamaster.jawacommands.listeners.OnPlayerJoin;
import jawamaster.jawacommands.listeners.PlayerDeath;
import jawamaster.jawacommands.listeners.PlayerMovementListener;
import jawamaster.jawacommands.listeners.PlayerQuit;
import jawamaster.jawacommands.listeners.SpawnListener;
import jawamaster.jawacommands.listeners.TabCompleteListener;
import jawamaster.jawacommands.listeners.TeleportListener;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Arthur Bulin
 */
public class JawaCommands extends JavaPlugin {
    private static JawaCommands plugin;
    private static CommandControlHandler commandControlHandler;
   
    private static WarpHandler warpHandler;
   
    public static HashMap<UUID, JSONArray> backStack;
    
    public static JSONObject worldSpawns;
    
    public static KitHandler kitHandler;
    
    public static JSONObject safeRandomTps;
    
    private static Configuration config;
    
    private static boolean debug;
    
    
    @Override
    public void onEnable(){
        plugin = this;
        loadConfig();
        
        //TODO Make the warp handler into its own static instance
        
        backStack = new HashMap();
        
        //TODO Make the world Handler into its own static instance
        worldSpawns = WorldHandler.loadWorldSpawns();
        
        //TODO make the kithandler into its own static instance
        //kitHandler = new KitHandler(this);
        
        //commandControlHandler
        commandControlHandler = new CommandControlHandler(plugin);
        
        warpHandler = new WarpHandler();
        KitHandler.loadKits();
        
        this.getCommand("testcommand").setExecutor(new TestCommand());

        //Register the custom spawn points listener
        getServer().getPluginManager().registerEvents(new SpawnListener(), this);
        getServer().getPluginManager().registerEvents(new FirstSpawnListener(), this);
        
        //For back command
        getServer().getPluginManager().registerEvents(new TeleportListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        
        getServer().getPluginManager().registerEvents(new PlayerMovementListener(), this);
        
        getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
        
        getServer().getPluginManager().registerEvents(new TabCompleteListener(), this);
        
    }
    
    @Override
    public void onDisable(){
        
    }
    
    public static JawaCommands getPlugin(){
        return plugin;
    }
    
    public void loadConfig(){
        System.out.print("Loading configuration from file.");
        //Handle the config generation and loading
        this.saveDefaultConfig();
        config = this.getConfig();
        debug = config.getBoolean("debug", false);
    }
    
    public static Configuration getConfiguration() {
        return config;
    }
    
    public static boolean isDebug(){
        return debug;
    }
    

}
