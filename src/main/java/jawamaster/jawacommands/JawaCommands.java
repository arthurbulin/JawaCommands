/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package jawamaster.jawacommands;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jawamaster.jawacommands.commands.Home;
import jawamaster.jawacommands.handlers.ESHandler;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 *
 * @author Arthur Bulin
 */
public class JawaCommands extends JavaPlugin {
    public static RestHighLevelClient restClient;
    public static ESHandler eshandler;
    public static JawaCommands plugin;
    public static Configuration config;
    
    public static String eshost;
    public static int esport;
    
    
    @Override
    public void onEnable(){
        try {
            loadConfig();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JawaCommands.class.getName()).log(Level.SEVERE, null, ex);
        }
        startESHandler();
        
        this.getCommand("home").setExecutor(new Home());
    }
    
    @Override
    public void onDisable(){
        
    }
    
    public void startESHandler(){
        //Initialize the restClient for global use
        restClient = new RestHighLevelClient(RestClient.builder(new HttpHost(eshost, esport, "http")).setRequestConfigCallback((RequestConfig.Builder requestConfigBuilder) -> requestConfigBuilder.setConnectTimeout(5000).setSocketTimeout(60000)).setMaxRetryTimeoutMillis(60000));
        
        eshandler = new ESHandler(this);
        
    }
    
    public void loadConfig() throws FileNotFoundException{
        //Handle the config generation and loading
        this.saveDefaultConfig();
        config = this.getConfig();
        eshost = (String) config.get("eshost");
        esport = (int) config.get("esport"); 
        

    }
}
