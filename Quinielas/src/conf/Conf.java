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
    private XMLConfiguration config = null;
    
    private Conf() throws ConfigurationException {
        config = new XMLConfiguration("/home/francisco/Quiniela/Quinielas/conf/config.xml");
        
    }
    
    /**
     *
     * @return
     */
    public static Conf getConfiguration() {
        try{
            if (myInstance == null){
                myInstance = new Conf();
            }
            return myInstance;
        }catch (ConfigurationException ex){
             ex.printStackTrace();
             return null;
        }
    }
    
    public  String getAliasFile(){
        return config.getString("teamfile.path");
    }
    public  String getMatchsFile(){
        return config.getString("matchsfile.path");
    }    
    public  String getMysqlHost() {       
        return config.getString("database.host");
    }
    public String getMysqlDatabase(){
        return config.getString("database.databaseName");        
    }
    public String getMysqlUser(){
        return config.getString("database.user");
    }
    public String getMysqlPass(){
        return config.getString("database.pass");
    }
    public Integer getStreak(){
        return config.getInteger("streak",5);
    }
    public String getPredictionsFile(){
        return config.getString("predictionsfile.path");
    }
    
}
