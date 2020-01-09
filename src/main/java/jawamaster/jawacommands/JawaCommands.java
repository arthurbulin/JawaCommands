/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package jawamaster.jawacommands;

import java.util.HashMap;
import java.util.UUID;
import jawamaster.jawacommands.commands.ChangeGameMode;
import jawamaster.jawacommands.commands.ComeHere;
import jawamaster.jawacommands.commands.GoThere;
import jawamaster.jawacommands.commands.TPAccept;
import jawamaster.jawacommands.commands.home.Home;
import jawamaster.jawacommands.commands.home.HomeInfo;
import jawamaster.jawacommands.commands.admin.SudoAs;
import jawamaster.jawacommands.commands.development.BackCommand;
import jawamaster.jawacommands.commands.development.Kit;
import jawamaster.jawacommands.commands.development.TestCommand;
import jawamaster.jawacommands.commands.home.delHome;
import jawamaster.jawacommands.commands.home.listHomes;
import jawamaster.jawacommands.commands.home.setHome;
import jawamaster.jawacommands.commands.spawn.SetSpawn;
import jawamaster.jawacommands.commands.spawn.Spawn;
import jawamaster.jawacommands.commands.spawn.RemoveSpawn;
import jawamaster.jawacommands.commands.warps.DelWarp;
import jawamaster.jawacommands.commands.warps.MakeWarp;
import jawamaster.jawacommands.commands.warps.ModWarp;
import jawamaster.jawacommands.commands.warps.Warp;
import jawamaster.jawacommands.commands.warps.WarpBlackList;
import jawamaster.jawacommands.commands.warps.WarpWhitelist;
import jawamaster.jawacommands.commands.warps.YeetPort;
import jawamaster.jawacommands.handlers.KitHandler;
import jawamaster.jawacommands.handlers.WarpHandler;
import jawamaster.jawacommands.handlers.WorldHandler;
import jawamaster.jawacommands.listeners.FirstSpawnListener;
import jawamaster.jawacommands.listeners.PlayerDeath;
import jawamaster.jawacommands.listeners.PlayerQuit;
import jawamaster.jawacommands.listeners.SpawnListener;
import jawamaster.jawacommands.listeners.TeleportListener;
import jawamaster.jawapermissions.handlers.ESHandler;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Arthur Bulin
 */
public class JawaCommands extends JavaPlugin {
    private static RestHighLevelClient restClient;
    private static ESHandler eshandler;
    private static Configuration config;
    private static JawaCommands plugin;
    
    private static String eshost;
    private static int esport;
    
    private static HashMap<String, WarpObject> warpIndex;
    private static JSONObject worldConfigurations;
    
    public static HashMap<UUID, JSONArray> backStack;
    
    public static JSONObject worldSpawns;
    
    public static JSONObject kits;
    
    
    @Override
    public void onEnable(){
        plugin = this;
        loadConfig();
        worldConfigurations = WorldHandler.LoadWorldConfigs();

        startESHandler();
        
        warpIndex = WarpHandler.loadWarpObjects();
        
        backStack = new HashMap();
        worldSpawns = WorldHandler.loadWorldSpawns();
        
        kits = KitHandler.loadKits();
        
        this.getCommand("home").setExecutor(new Home());
        this.getCommand("sethome").setExecutor(new setHome());
        this.getCommand("commandtest").setExecutor(new TestCommand());
        this.getCommand("delhome").setExecutor(new delHome());
        this.getCommand("listhome").setExecutor(new listHomes());
        this.getCommand("homeinfo").setExecutor(new HomeInfo());
        this.getCommand("sudo").setExecutor(new SudoAs());
        
        this.getCommand("warp").setExecutor(new Warp());
        this.getCommand("makewarp").setExecutor(new MakeWarp());
        this.getCommand("modwarp").setExecutor(new ModWarp());
        this.getCommand("delwarp").setExecutor(new DelWarp());
        this.getCommand("wlwarp").setExecutor(new WarpWhitelist());
        this.getCommand("blwarp").setExecutor(new WarpBlackList());
        
        this.getCommand("gm").setExecutor(new ChangeGameMode());
        
        this.getCommand("spawn").setExecutor(new Spawn());
        this.getCommand("setspawn").setExecutor(new SetSpawn());
        
        this.getCommand("comehere").setExecutor(new ComeHere());
        this.getCommand("gothere").setExecutor(new GoThere());
        this.getCommand("accept").setExecutor(new TPAccept());
        
        this.getCommand("fullbright").setExecutor(new FullBright());
        
        this.getCommand("removespawn").setExecutor(new RemoveSpawn());
        
        this.getCommand("yeetport").setExecutor(new YeetPort());
        
        //This should later be moved to tool box
        this.getCommand("kit").setExecutor(new Kit());
        
        this.getCommand("back").setExecutor(new BackCommand());
        
        //Register the custom spawn points listener, this should really get moved to toolbox later the same time things are migrated into Core
        getServer().getPluginManager().registerEvents(new SpawnListener(), this);
        getServer().getPluginManager().registerEvents(new FirstSpawnListener(), this);
        
        //For back command
        getServer().getPluginManager().registerEvents(new TeleportListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        
    }
    
    @Override
    public void onDisable(){
        
    }
    
    public void startESHandler(){
        //Initialize the restClient for global use
        restClient = new RestHighLevelClient(RestClient.builder(new HttpHost(eshost, esport, "http"))
                .setRequestConfigCallback((RequestConfig.Builder requestConfigBuilder) -> 
                        requestConfigBuilder.setConnectTimeout(5000).setSocketTimeout(60000)));
        
        eshandler = new jawamaster.jawapermissions.handlers.ESHandler(restClient);
        
    }
    
    public void loadConfig() {
        //Handle the config generation and loading
        this.saveDefaultConfig();
        config = this.getConfig();
        eshost = (String) config.get("eshost");
        esport = (int) config.get("esport"); 

    }
    
    public static JSONObject getWorldConfiguration(){
        return worldConfigurations;
    }
    
//    public static JSONObject getBackStack(){
//        return backStack;
//    }
//    
//    public static void addToBackStack(UUID uuid, JSONArray obj){
//        backStack.put(uuid.toString(), obj);
//    }
    
    public static JawaCommands getPlugin(){
        return plugin;
    }
    
    public ESHandler getESHandler(){
        return eshandler;
    }
    
    public RestHighLevelClient getRestClient(){
        return restClient;
    }
    
    public static HashMap<String, WarpObject> getWarpIndex(){
        return warpIndex;
    }
    
    public static void addWarpToIndex(WarpObject obj){
        warpIndex.put(obj.getWarpName(), obj);
    }
    
    

}
