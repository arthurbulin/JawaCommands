/*
 * Copyright (C) 2019 alexander
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

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jawamaster.jawacommands.JawaCommands;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author alexander
 */
public class YAMLHandler {

    /**
     * Retrieves a file located down the filePath from inside the plugin's
     * configuration directory.That file is then returned as a FileConfiguration
     * object. If the file does not exist or is a directory it returns null. The
     * filePath should start with "/". If createFile is true then an empty file
     * is generated and the empty configuration is returned. If file creation
     * fails then null is returned.
     * @param filePath
     * @param createFile
     * @return
     */
    public static FileConfiguration GetYAMLConfiguration(String filePath, boolean createFile) {
        //Get the locations files
        System.out.println(filePath);
        
        System.out.println(JawaCommands.getPlugin().getDataFolder().exists());
        
        File yamlFile = new File(JawaCommands.getPlugin().getDataFolder() + filePath);

        // Generate file and directories if requested
        if (createFile) {
            yamlFile.mkdirs();
            try {
                yamlFile.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(YAMLHandler.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }

        // Return something
        if (yamlFile.exists() && yamlFile.isFile()) {
            FileConfiguration yaml = YamlConfiguration.loadConfiguration(yamlFile);
            return yaml;
        } else {
            return null;
        }

    }

    /**
     * Retrieves a file located down the filePath from inside the plugin's
     * configuration directory.That file is then returned as a FileConfiguration
     * object. If the file does not exist or is a directory it returns null. The
     * filePath should start with "/". This is backed by 
     * GetYAMLConfiguration(String filePath, boolean createFile)
     * @param filePath
     * @return
     */
    public static FileConfiguration GetYAMLConfiguration(String filePath) {
        return GetYAMLConfiguration(filePath, false);
    }

}
