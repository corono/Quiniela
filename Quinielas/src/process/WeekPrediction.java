/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package process;

import conf.Conf;
import static database.BBDD.getConexionBBDD;
import static file.MatchsFile.getTeamID;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.configuration.ConfigurationException;
import org.xml.sax.SAXException;
import static process.ProcessSeason.getMatchesBetween;
import static process.ProcessSeason.streak;

/**
 * This class reads the week matchs and return the prediction for that matchs
 * the file format is:
 * #Primera Division Temporada yyyy-yy 
 * 1 local-visitor
 * 2 local-visitor
 * ...
 *#Segunda Division
 * 1 local-visitor 
 * ...
 * @author francisco
 */
public class WeekPrediction {
    private static Conf configuration = null;
    
     static{
         configuration = Conf.getConfiguration();
    }
    
    public static void weekArffFile(ArrayList<String> matchs1, ArrayList<String> matchs2, String season, String jornada) throws ParserConfigurationException, SAXException, IOException, SQLException, Exception{
        //vector = getMatchesBetween(local, visitante, temp, conexion);
        for(int i = 0; i < matchs1.size(); i++){
            getMatchsTeams(matchs1.get(i), season, jornada);
        }
        
    }
    /**
     * Extract the teams in the match, and find their id
     * @param match localTeam-VisitorTeam
     * @param season
     * @param jornada
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public static void getMatchsTeams(String match, String season, String jornada) throws ParserConfigurationException, SAXException, IOException, SQLException, Exception{
        
        String[] matchs = match.split("-");
        int team1 = getTeamID(matchs[0]);
        int team2 = getTeamID(matchs[1]);
        System.out.println(matchs[0]+" "+team1+" "+team2+" "+matchs[1]);
        //consulta para generar entrada NN
        Connection conexion = getConexionBBDD();
        int[] historic = getMatchesBetween(team1, team2, season, conexion);
        streak(conexion, team1, configuration.getStreak(), Integer.valueOf(jornada), season);
        
    }
    public static void getSeasonPerformance(int team, String season, int jornada, Connection conexion){
        
        
        
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException, ParserConfigurationException, SAXException, SQLException, Exception {
        String line, season = null, jornada = null;
        ArrayList<String> matchs1 = new ArrayList<>();
        ArrayList<String> matchs2 = new ArrayList<>();
        Boolean primera = false, segunda = false;
        
        FileReader fr = new FileReader(new File("/home/francisco/Dropbox/TFG/data/jornadas/jornada.txt"));
        BufferedReader br = new BufferedReader(fr);
        
        while ((line = br.readLine()) != null ){
            System.out.println(line);
            if (line.contains("Primera")){
                primera = true;
                line = br.readLine();
                season = line.split(" ")[line.split(" ").length-1];
                System.out.println(season);
                line = br.readLine();
                jornada = line.split(" ")[line.split(" ").length-1];
                line = br.readLine();
                
            }
            if(line.contains("Segunda")){
                primera = false;
                segunda = true;
                line = br.readLine();
                season = line.split(" ")[line.split(" ").length-1];
                System.out.println(season);
                line = br.readLine();
                jornada = line.split(" ")[line.split(" ").length-1];
                line = br.readLine();
            }
            
            if(primera){
                matchs1.add(line);
            }
            
            if(segunda){
                matchs2.add(line);
            }
                
        }
        br.close();
        
        weekArffFile(matchs1,matchs2, season, jornada);
        
        
        
        
    }
   
}
