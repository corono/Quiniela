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
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import static process.ProcessSeason.addPlayersValue;
import static process.ProcessSeason.getMatchesBetween;
import static process.ProcessSeason.streak;
import static process.ProcessedFiles.arffHeader;

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
        
        double[] inputRow = new double[27];
        Connection conexion = getConexionBBDD();
        
        PrintWriter pw = new PrintWriter (new File("/home/francisco/Dropbox/TFG/data/jornadas/testFile1div.arff"));
        arffHeader(pw);        
        
        for(int i = 0; i < matchs1.size(); i++){
            System.out.println(matchs1.get(i));
            inputRow = getMatchsTeams(matchs1.get(i), season, conexion);
            for(int j = 0; j < inputRow.length; j++){
                pw.print(inputRow[j]+",");
            } 
            if (i == (matchs1.size()-1)){
                pw.print("v");
            }else{
                pw.print("v\n");
            }
        } 
        pw.close();
        
        PrintWriter pw2 = new PrintWriter (new File("/home/francisco/Dropbox/TFG/data/jornadas/testFile2div.arff"));
        arffHeader(pw2);
        for(int i = 0; i < matchs2.size(); i++){
            System.out.println(matchs2.get(i));
            inputRow = getMatchsTeams(matchs2.get(i), season, conexion);
            for(int j = 0; j < inputRow.length; j++){
                pw2.print(inputRow[j]+",");
            }
            if (i == (matchs2.size()-1)){
                pw2.print("v");
            }else{
                pw2.print("v\n");
            }
            
        }
        pw2.close();
        
        conexion.close();
        
    }
    /**
     * Extract the teams in the match, and find their id
     * @param match localTeam-VisitorTeam
     * @param season
     * @param conexion
     * @return 
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public static double[] getMatchsTeams(String match, String season, Connection conexion) throws ParserConfigurationException, SAXException, IOException, SQLException, Exception{
        
        String[] matchs = match.split("-");
        int team1 = getTeamID(matchs[0]);
        int team2 = getTeamID(matchs[1]);
        //System.out.println(matchs[0]+" "+team1+" "+team2+" "+matchs[1]);
        //consulta para generar entrada NN
        
        
        int jornada = getJornada(season,team1,team2,conexion);
        
        int[] historic = getMatchesBetween(team1, team2, season, conexion);
        int[] seasonData = getSeasonPerformance(team1, season,jornada, conexion);
        
        double[] entrada = new double[27];
        double[] aux = new double[12]; 
        
        entrada[0] = (double)team1;
        entrada[1] = (double)team2;  
        
        
        if((historic[5]+historic[0]+historic[1]+historic[2]+historic[3]+historic[4]) > 0){
            entrada[2] = (double)(historic[0]+historic[3]) / (historic[5]+historic[0]+historic[1]+historic[2]+historic[3]+historic[4]);   //porcentaje enfrentamientos victoria local
            entrada[3] = (double)(historic[2]+historic[5]) / (historic[5]+historic[0]+historic[1]+historic[2]+historic[3]+historic[4]);   //porcentaje enfrentamientos victoria visitante
            entrada[4] = (double)(historic[1]+historic[4]) / (historic[5]+historic[0]+historic[1]+historic[2]+historic[3]+historic[4]);   //porcentaje enfrentamientos empatados
            entrada[5] = (double)(historic[0]+historic[5]) / (historic[5]+historic[0]+historic[1]+historic[2]+historic[3]+historic[4]);   //porcentaje enfrentamientos ganados por local
            entrada[6] = (double)(historic[2]+historic[3]) / (historic[5]+historic[0]+historic[1]+historic[2]+historic[3]+historic[4]);   //porcentaje enfrentamientos ganados por visitante
        }else{
            entrada[2] = 0.0;
            entrada[3] = 0.0;
            entrada[4] = 0.0;
            entrada[5] = 0.0;
            entrada[6] = 0.0;
        }
        System.out.println(team1);
        aux = getTeamParams(seasonData);
        System.arraycopy(aux, 0, entrada, 7, aux.length);
        
        entrada[14] = (double)streak(conexion, team1, configuration.getStreak(), jornada, season);//factor racha
        
        double[] valPlayers = addPlayersValue(conexion, team1, season); 
        entrada[15] = valPlayers[0];//suma de la valoración de los jugadores
        entrada[16] = valPlayers[0]/valPlayers[1]; //promedio valoracion jugadores
        
        seasonData = getSeasonPerformance(team2, season,jornada, conexion);
        System.out.println(team2);
        aux = getTeamParams(seasonData);
        System.arraycopy(aux, 0, entrada, 17, aux.length);
        entrada[24] = (double)streak(conexion, team2, configuration.getStreak(),jornada, season);//factor racha
        
        valPlayers = addPlayersValue(conexion, team2, season); 
        entrada[25] = valPlayers[0];//suma de la valoración de los jugadores
        entrada[26] = valPlayers[0]/valPlayers[1]; //promedio valoracion jugadores        
        
        return entrada;
        
    }
    /**
     * 
     * @param season
     * @param local
     * @param visitante
     * @param conexion
     * @return
     * @throws SQLException 
     */
    public static int getJornada(String season, int local, int visitante, Connection conexion) throws SQLException{
        
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT Jornada FROM LIGA.Partidos WHERE Temporada = '");
        sb.append(season);
        sb.append("' AND Local = ");
        sb.append(local);
        sb.append(" AND Visitante = ");
        sb.append(visitante);
        
        int jornada = 0;
        System.out.println(sb.toString());
        Statement st = conexion.createStatement();
        ResultSet result = st.executeQuery(sb.toString());
        
        while (result.next()){       
            if(!result.getString("Jornada").equals("1ª/2ª")){
                jornada = Integer.valueOf(result.getString("Jornada"));
                System.out.println(result.getString("Jornada"));
            }
        }
        st.close();
        
        return jornada;
    }
    /**
     * 
     * @param team
     * @param season
     * @param jornada
     * @param conexion
     * @return
     * @throws SQLException 
     */
    public static int[] getSeasonPerformance(int team, String season, int jornada, Connection conexion) throws SQLException{
        
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM LIGA.Clasificacion where Temporada = '");
        sb.append(season);
        sb.append("' AND jornada = ");
        sb.append(jornada);
        sb.append(" AND Equipos_idEquipos = ");
        sb.append(team);
        
        int[] teamData = new int[17];
        
        System.out.println(sb.toString());
        
        Statement st = conexion.createStatement();
        ResultSet result = st.executeQuery(sb.toString());
        while (result.next()){
            teamData[0] = result.getInt("PJ");
            teamData[1] = result.getInt("PG");
            teamData[2] = result.getInt("PE");
            teamData[3] = result.getInt("PP");
            teamData[4] = result.getInt("PGL");
            teamData[5] = result.getInt("PEL");
            teamData[6] = result.getInt("PPL");
            teamData[7] = result.getInt("PGV");
            teamData[8] = result.getInt("PEV");
            teamData[9] = result.getInt("PPV");
            teamData[10] = result.getInt("GF");
            teamData[11] = result.getInt("GC");
            teamData[12] = result.getInt("GFL");
            teamData[13] = result.getInt("GCL");
            teamData[14] = result.getInt("GFV");
            teamData[15] = result.getInt("GCV");
            teamData[16] = result.getInt("Puntos");
        }
       
            
       st.close();
        
        return teamData;
        
    }
    /**
     * returns input params
     * 0 percentage of matches winned until now
     * 1 percentage of matches winned as local
     * 2 percentage of matches  drawn as local
     * 3 percentage of matches loosed as local
     * 4 goal average difference
     * 5 goal average difference as local
     * 6 average points obtained by match
     * @param teamData
     * @return 
     */
    public static double[] getTeamParams(int[] teamData){
        
        double[] entrada = new double[7];
        
         
        entrada[0] = (double)teamData[1] / teamData[0]; //porcentaje partidos ganados hasta la fecha
        entrada[1] = (double)teamData[4] / (teamData[4] + teamData[5] + teamData[6]); //Porcentaje partidos ganados como local hasta la fecha
        entrada[2] = (double)teamData[5] / (teamData[4] + teamData[5] + teamData[6]); //porcentaje partidos empatados como local
        entrada[3] =(double) teamData[6] / (teamData[4]+teamData[5] + teamData[6]); //porcentaje partidos perdidos como local
        if((teamData[10] + teamData[11]) > 0){
            entrada[4] = (double)(teamData[10] - teamData[11]) / (teamData[10] + teamData[11]); //promedio de la diferencia de goles partidos hasta la fecha
        }else{
            entrada[4] = 0.0;
        }
        if((teamData[12] + teamData[13]) > 0){
            entrada[5] = (double)(teamData[12] - teamData[13]) / (teamData[12] + teamData[13]); //promedio diferencia goles partidos como local
        }else{
            entrada[5] = 0.0;
        }            
        entrada[6] = teamData[16] / teamData[0]; //promedio puntos obtenidos por partido
        
        return entrada;
        
    }
    /**
     * read txt file with the matchs of the weekend, and create two arff files
     * one with the input for the 1 division neural network
     * and other with the input for the 2 division neural network
     * @param inputFile
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static void weekPrediction(String inputFile) throws FileNotFoundException, IOException{
        
        String line, season = null, jornada = null;
        ArrayList<String> matchs1 = new ArrayList<>();
        ArrayList<String> matchs2 = new ArrayList<>();
        Boolean primera = false, segunda = false;
        
        FileReader fr = new FileReader(new File(inputFile));
        BufferedReader br = new BufferedReader(fr);
        
        while ((line = br.readLine()) != null ){
            if (line.contains("Primera")){
                primera = true;
                line = br.readLine();
                season = line.split(" ")[line.split(" ").length-1];
                line = br.readLine();
                
            }
            if(line.contains("Segunda")){
                primera = false;
                segunda = true;
                line = br.readLine();
                season = line.split(" ")[line.split(" ").length-1];
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
        try {
            weekArffFile(matchs1,matchs2, season, jornada);
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        
    }
    
    public static void main(String[] args) {
        try {
            String file = "/home/francisco/Dropbox/TFG/data/jornadas/jornada10.txt";
            
            weekPrediction(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        
    }
   
}
