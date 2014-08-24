/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package conf;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

/**
 * Configuration class
 * Singleton pattern to make sure that there is no conf issues
 *
 * @author francisco
 */
public class Conf {
    
    
    private static Conf myInstance = null;
    private XMLConfiguration xmlConf = null;
    
    private Conf() throws ConfigurationException {
        xmlConf = new XMLConfiguration("conf/config.xml");
        
    }
    
    /**
     *
     * @return
     * @throws ConfigurationException
     */
    public static Conf getConfiguration() throws ConfigurationException {
        if (myInstance == null){
            myInstance = new Conf();
        }
        return myInstance;
    }
    
    public  String getAliasFile(){
        return xmlConf.getString("teamfile.path");
    }
    
    public  String getMysqlUrl() {
       
        return xmlConf.getString("MySqlUrl");
    }
    
}
