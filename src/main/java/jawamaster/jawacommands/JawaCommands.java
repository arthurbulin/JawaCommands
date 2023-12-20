/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package jawamaster.jawacommands;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import jawamaster.jawacommands.commands.development.TestCommand;
import jawamaster.jawacommands.handlers.CommandControlHandler;
import jawamaster.jawacommands.handlers.HomeHandler;
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
import net.jawasystems.jawacore.JawaCore;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Arthur Bulin
 */
public class JawaCommands extends JavaPlugin {
    private static final Logger LOGGER = Logger.getLogger("JawaCommands][JawaCommands");
    
    private static JawaCommands plugin;
    private static String serverName;
    private static Configuration config;
    
    private static boolean warpsEnabled;
    private static boolean homesEnabled;
    private static List<String> homesProhibitedWorlds;
    private static boolean debug;
    
    private static CommandControlHandler commandControlHandler;
    
    private static WarpHandler warpHandler;
    public static KitHandler kitHandler;
    
    public static HashMap<UUID, JSONArray> backStack;
    
    @Override
    public void onEnable(){
        plugin = this;
        
        loadConfig();

            //TODO Make the warp handler into its own static instance
            backStack = new HashMap();

            //TODO Make the world Handler into its own static instance
            WorldHandler.loadWorldSpawns();

            //TODO make the kithandler into its own static instance
            //kitHandler = new KitHandler(this);
            //Initialize warp feature
            if (!JawaCore.isFirstRun()){
                if (warpsEnabled && validateWarpIndex()) {
                    warpHandler = new WarpHandler();
                    LOGGER.log(Level.INFO, "Warp commands enabled");
                } else {
                    LOGGER.log(Level.INFO, "The warps index is not available or warps are not enabled. Warp commands will be disabled.");
                    warpsEnabled = false;
                }

                if (homesEnabled && validateHomeIndex()) {
                    LOGGER.log(Level.INFO, "Home commands enabled.");
                } else {
                    homesEnabled = false;
                    LOGGER.log(Level.INFO, "The homes index is not available or homes are not enabled. Home commands will be disabled.");
                }
            }

            //commandControlHandler
            commandControlHandler = new CommandControlHandler(plugin);

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
        LOGGER.log(Level.INFO,"Loading configuration from file.");
        //Handle the config generation and loading
        this.saveDefaultConfig();
        config = this.getConfig();
        debug = config.getBoolean("debug", false);
        serverName = JawaCore.getServerName();
        
        if (config.contains("homes-settings")){
            homesEnabled = config.getConfigurationSection("homes-settings").getBoolean("homes-enabled", false);
            homesProhibitedWorlds = config.getConfigurationSection("homes-settings").getStringList("homes-prohibited-worlds");
            ConfigurationSection homeLimits = config.getConfigurationSection("homes-settings.homes-limits");
            for (String key : homeLimits.getKeys(true)){
                HomeHandler.setHomeLimit(key, homeLimits.getInt(key));
            }
            
            JawaCore.registerIndexLiteral("homes", config.getString("index-customization.homes", "homes"), JawaCommands.getPlugin().getName());
        }
        
        if (config.contains("warp-settings")){
            warpsEnabled = config.getConfigurationSection("warp-settings").getBoolean("warps-enabled", false);
            JawaCore.registerIndexLiteral("warps", config.getString("index-customization.warps", "warps"), JawaCommands.getPlugin().getName());
        }
        
        JawaCore.receiveConfigurations(JawaCommands.plugin.getName(), config);
    }
    
    public static Configuration getConfiguration() {
        return config;
    }
    
    public static boolean isDebug(){
        return debug;
    }
    
    /** Validates the existence of the warps index with JawaCore. If the index does not exist it 
     * will request that JawaCore create the Index. If some reason the index is not created it will
     * disable warping and warp commands.
     * @return True if the index exists at the end of evaluation. False if the index does not exist at the end.
     */
    private static boolean validateWarpIndex(){
        
        LOGGER.log(Level.INFO, "Requsting index validation from JawaCore for the warps index...");
        if (!JawaCore.validateIndex("warps")){
            LOGGER.log(Level.INFO, "Requesting JawaCore create the warp index...");
            boolean created = JawaCore.createIndex("warps", null);
            if (!created) {
                LOGGER.log(Level.INFO, "Index returned non-createed. Attempting to revalidate with JawaCore...");
                created = JawaCore.validateIndex("warps");
            }
            return created;
        } else {
            return true;
        }
    }
    
    /** Validates the existence of the warps index with JawaCore. If the index does not exist it 
     * will request that JawaCore create the Index. If some reason the index is not created it will
     * disable warping and warp commands.
     * @return True if the index exists at the end of evaluation. False if the index does not exist at the end.
     */
    private static boolean validateHomeIndex(){
        LOGGER.log(Level.INFO, "Requsting index validation from JawaCore for the homes index...");
        if (!JawaCore.validateIndex("homes")){
            LOGGER.log(Level.INFO, "Requesting JawaCore create the homes index...");
            boolean created = JawaCore.createIndex("homes", null);
            if (!created) {
                LOGGER.log(Level.INFO, "Index returned non-createed. Attempting to revalidate with JawaCore...");
                created = JawaCore.validateIndex("homes");
            }
            return created;
        } else {
            return true;
        }
    }
    
    /** Returns if warps are enabled. Either config disabled or from lack of an index.
     * @return True if warps are enabled. False if they are not.
     */
    public static boolean warpsEnabled(){
        return warpsEnabled;
    }
    
    public static boolean homesEnabled(){
        return homesEnabled;
    }
    

}
