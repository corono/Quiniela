/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package process;

import conf.Conf;
import static database.BBDD.getConexionBBDD;
import static database.BBDD.getSeasons;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import static process.ProcessedFiles.arffHeader;

/**
 *
 * @author francisco
 */
public class ProcessSeason {
    private static Conf configuration = null;
    /**
     * 
     * @param season
     * @param pw
     * @param header
     * @throws Exception 
     */
     static{
         configuration = Conf.getConfiguration();
    }
    
    public static void procesSeason(String season, PrintWriter pw, boolean header) throws Exception{
        
        Connection conexion = getConexionBBDD();
        
        StringBuilder query = new StringBuilder();
        query.append("Select p.Local as Local, p.Visitante as Visitante, p.Jornada as Jornada, ");
        query.append("p.Temporada as Temporada, c.PJ as LocalPJ, c.PG as LocalPG, c.PE as LocalPE, ");
        query.append("p.VictoriaLocal as VL, p.Empate as E, p.VictoriaVisitante as VV, ");
        query.append("c.PP as LocalPP, c.PGL as LocalPGL , c.PEL as LocalPEL, c.PPL as LocalPPL, ");
        query.append("c.PGV as LocalPGV, c.PEV as LocalPEV, c.PPV as LocalPPV, c.GF as LocalGF, ");
        query.append("c.GC as LocalGC, c.GFL as LocalGFL, c.GCL as LocalGCL, c.GFV as LocalGFV, ");
        query.append("c.GCV as LocalGCV, c.Puntos as LocalPuntos, c2.PJ as VisitantePJ, ");
        query.append("c2.PG as VisitantePG, c2.PE as VisitantePE, c2.PP as VisitantePP, ");
        query.append("c2.PGL as VisitantePGL , c2.PEL as VisitantePEL, c2.PPL as VisitantePPL, ");
        query.append("c2.PGV as VisitantePGV, c2.PEV as VisitantePEV, c2.PPV as VisitantePPV, ");
        query.append("c2.GF as VisitanteGF, c2.GC as VisitanteGC, c2.GFL as VisitanteGFL, ");
        query.append("c2.GCL as VisitanteGCL, c2.GFV as VisitanteGFV, c2.GCV as VisitanteGCV, ");
        query.append("c2.Puntos as VisitantePuntos ");
        query.append("FROM Partidos p ");
        query.append("JOIN Clasificacion c ");
        query.append("ON p.Local = c.Equipos_idEquipos AND p.Temporada = c.Temporada AND p.Jornada = c.Jornada ");
        query.append("AND p.Division = c.Division ");
        query.append("JOIN Clasificacion c2 ");
        query.append("ON p.Visitante = c2.Equipos_idEquipos AND p.Temporada = c2.Temporada AND p.Jornada = c2.Jornada ");
        query.append("AND p.Division = c2.Division WHERE p.Temporada = \"");
        query.append(season);
        query.append("\" ;");
        
        Statement st = conexion.createStatement();
        ResultSet result = st.executeQuery(query.toString());
        
        if(header){
            arffHeader(pw);
        }
        
        int[] vector = new int[6];
        Integer[] localData = new Integer[17];
        Integer[] visitData = new Integer[17];
        double[] entrada = new double[27], valPlayers = new double[2];
        Integer local, visitante; //streak = 5;
        String  temp;
        
        while (result.next()){
            local = result.getInt("Local");
            visitante = result.getInt("Visitante");
            temp = result.getString("Temporada");
            
            localData[0] = result.getInt("LocalPJ");
            localData[1] = result.getInt("LocalPG");
            localData[2] = result.getInt("LocalPE");
            localData[3] = result.getInt("LocalPP");
            localData[4] = result.getInt("LocalPGL");
            localData[5] = result.getInt("LocalPEL");
            localData[6] = result.getInt("LocalPPL");
            localData[7] = result.getInt("LocalPGV");
            localData[8] = result.getInt("LocalPEV");
            localData[9] = result.getInt("LocalPPV");
            localData[10] = result.getInt("LocalGF");
            localData[11] = result.getInt("LocalGC");
            localData[12] = result.getInt("LocalGFL");
            localData[13] = result.getInt("LocalGCL");
            localData[14] = result.getInt("LocalGFV");
            localData[15] = result.getInt("LocalGCV");
            localData[16] = result.getInt("LocalPuntos");
            
            visitData[0] = result.getInt("VisitantePJ");
            visitData[1] = result.getInt("VisitantePG");
            visitData[2] = result.getInt("VisitantePE");
            visitData[3] = result.getInt("VisitantePP");
            visitData[4] = result.getInt("VisitantePGL");
            visitData[5] = result.getInt("VisitantePEL");
            visitData[6] = result.getInt("VisitantePPL");
            visitData[7] = result.getInt("VisitantePGV");
            visitData[8] = result.getInt("VisitantePEV");
            visitData[9] = result.getInt("VisitantePPV");
            visitData[10] = result.getInt("VisitanteGF");
            visitData[11] = result.getInt("VisitanteGC");
            visitData[12] = result.getInt("VisitanteGFL");
            visitData[13] = result.getInt("VisitanteGCL");
            visitData[14] = result.getInt("VisitanteGFV");
            visitData[15] = result.getInt("VisitanteGCV");
            visitData[16] = result.getInt("VisitantePuntos");
          
            System.out.println(result.getString("Temporada")+" "+result.getInt("Jornada")+" "+ local+" "+visitante);
            vector = getMatchesBetween(local, visitante, temp, conexion);
            
            entrada[0] = (double)local;
            entrada[1] = (double)visitante;            
            if((vector[5]+vector[0]+vector[1]+vector[2]+vector[3]+vector[4]) > 0){
                entrada[2] = (double)(vector[0]+vector[3]) / (vector[5]+vector[0]+vector[1]+vector[2]+vector[3]+vector[4]);   //porcentaje enfrentamientos victoria local
                entrada[3] = (double)(vector[2]+vector[5]) / (vector[5]+vector[0]+vector[1]+vector[2]+vector[3]+vector[4]);   //porcentaje enfrentamientos victoria visitante
                entrada[4] = (double)(vector[1]+vector[4]) / (vector[5]+vector[0]+vector[1]+vector[2]+vector[3]+vector[4]);   //porcentaje enfrentamientos empatados
                entrada[5] = (double)(vector[0]+vector[5]) / (vector[5]+vector[0]+vector[1]+vector[2]+vector[3]+vector[4]);   //porcentaje enfrentamientos ganados por local
                entrada[6] = (double)(vector[2]+vector[3]) / (vector[5]+vector[0]+vector[1]+vector[2]+vector[3]+vector[4]);   //porcentaje enfrentamientos ganados por visitante
            }else{
                entrada[2] = 0.0;
                entrada[3] = 0.0;
                entrada[4] = 0.0;
                entrada[5] = 0.0;
                entrada[6] = 0.0;
            }
            entrada[7] = (double)localData[1] / localData[0]; //porcentaje partidos ganados hasta la fecha
            entrada[8] = (double)localData[4] / (localData[4] + localData[5] + localData[6]); //Porcentaje partidos ganados como local hasta la fecha
            entrada[9] = (double)localData[5] / (localData[4] + localData[5] + localData[6]); //porcentaje partidos empatados como local
            entrada[10] =(double) localData[6] / (localData[4]+localData[5] + localData[6]); //porcentaje partidos perdidos como local
            if((localData[10] + localData[11]) > 0){
                entrada[11] = (double)(localData[10] - localData[11]) / (localData[10] + localData[11]); //promedio de la diferencia de goles partidos hasta la fecha
            }else{
                entrada[11] = 0.0;
            }
            if((localData[12] + localData[13]) > 0){
                entrada[12] = (double)(localData[12] - localData[13]) / (localData[12] + localData[13]); //promedio diferencia goles partidos como local
            }else{
                entrada[12] = 0.0;
            }            
            entrada[13] = localData[16] / localData[0]; //promedio puntos obtenidos por partido
            entrada[14] = (double)streak(conexion, local,configuration.getStreak(),result.getInt("Jornada"), result.getString("Temporada"));//factor racha
            valPlayers = addPlayersValue(conexion, local,result.getString("Temporada"));
            entrada[15] = valPlayers[0];//suma de la valoración de los jugadores
            entrada[16] = valPlayers[0]/valPlayers[1]; //promedio valoracion jugadores
            
            entrada[17] = (double)visitData[1] / visitData[0]; //porcentaje partidos ganados hasta la fecha
            entrada[18] = (double)visitData[7] / (visitData[7] + visitData[8] + visitData[9]); //Porcentaje partidos ganados como visitante hasta la fecha
            entrada[19] = (double)visitData[8] / (visitData[7] + visitData[8] + visitData[9]); //porcentaje partidos empatados como visitante hasta la fecha
            entrada[20] = (double)visitData[9] / (visitData[7] + visitData[8] + visitData[9]); //porcentaje partidos perdidos como visitante hata la fecha
            if((visitData[10] + visitData[11])>0){
                entrada[21] = (double)(visitData[10] - visitData[11]) / (visitData[10] + visitData[11]); //promedio difrenecia goles hasta la fecha
            }else{
                entrada[21] = 0.0;
            }
            
            if((visitData[12] + visitData[13])>0){
                entrada[22] = (double)(visitData[12] - visitData[13]) / (visitData[12] + visitData[13]); //promedio diferencia goles como visitante
            }else{
                entrada[22] = 0.0;
            }
            entrada[23] = (double)visitData[16] / visitData[0]; //promedio puntos por partido
            entrada[24] = (double)streak(conexion, visitante, configuration.getStreak(), result.getInt("Jornada"), result.getString("Temporada"));
            valPlayers = addPlayersValue(conexion, visitante ,result.getString("Temporada"));
            entrada[25] = valPlayers[0];//suma de la valoración de los jugadores
            entrada[26] = valPlayers[0]/valPlayers[1]; //promedio valoracion jugadores
            
//            if(result.getBoolean("VL")){
//                entrada[21] = 0.0;
//                //entrada[21] = 1.0;
//                //entrada[22] = 0.0;
//                //entrada[23] = 0.0;
//            }else if(result.getBoolean("E")){
//                entrada[21] = 1.0;
//                //entrada[21] = 0.0;
//                //entrada[22] = 1.0;
//                //entrada[23] = 0.0;
//            }else if(result.getBoolean("VV")){
//                entrada[21] = 2.0;
//                //entrada[21] = 0.0;
//                //entrada[22] = 0.0;
//                //entrada[23] = 1.0;
//            }
//            
//            System.out.println(result.getString("Temporada")+" "+result.getInt("Jornada")+" : ");
//             for(int i = 0; i<entrada.length-4; i++){                
//                pw.print(entrada[i] +", ");
//            }
//             pw.print(entrada[entrada.length-3]+"\n");
             
             for(int i = 0; i<27; i++){                
                pw.print(entrada[i] +", ");
            }
             if(result.getBoolean("VL")){
                 pw.print("v\n");
             }
             if(result.getBoolean("E")){
                 pw.print("e\n");
             }
             if(result.getBoolean("VV")){
                 pw.print("d\n");                 
             }
             
            
            
        }
        st.close();
        result.close();
        conexion.close();
    }
    /**
     * obtiene historico enfrentamientos entre 2 equipos
     * @param team1
     * @param team2
     * @param temporada
     * @param conexion
     * @return devuelve un array que contiene:
     * pos 0: partidos ganados  team1 como local
     * pos 1: partidos empatados team1 como local
     * pos 2: partidos perdidos team1 como local
     * pos 3: partidos ganados team2 como local
     * pos 4: partidos ganados team2 como local
     * pos 5: partidos ganados team2 como local
     * @throws SQLException 
     */
    public static int[] getMatchesBetween(Integer team1, Integer team2, String temporada, Connection conexion) throws SQLException{
        
        StringBuilder query = new StringBuilder();
         
        query.append("SELECT Empate, VictoriaLocal, VictoriaVisitante  FROM Partidos ");
        query.append("WHERE Local = ");
        query.append(team1);
        query.append(" AND Visitante = ");
        query.append(team2);
        query.append(" AND Temporada < \"");
        query.append(temporada);
        query.append("\"");
        
        Statement st = conexion.createStatement();
        ResultSet rs = st.executeQuery(query.toString());
        int[] historico = new int[6];
        while(rs.next()){

           if(rs.getBoolean(1)){
               historico[0]+= 1;
           }
           if(rs.getBoolean(2)){
               historico[1]+= 1;
           }
           if(rs.getBoolean(3)){
               historico[2]+= 1;
           }
        }
        st.close();
        rs.close();
        
        query.delete(0, query.length());
        
        query.append("SELECT Empate, VictoriaLocal, VictoriaVisitante  FROM Partidos ");
        query.append("WHERE Local = ");
        query.append(team2);
        query.append(" AND Visitante = ");
        query.append(team1);
        query.append(" AND Temporada < \"");
        query.append(temporada);
        query.append("\"");
        st = conexion.createStatement();
        rs = st.executeQuery(query.toString());
        
       while(rs.next()){

           if(rs.getBoolean(1)){
               historico[3]+= 1;
           }
           if(rs.getBoolean(2)){
               historico[4]+= 1;
           }
           if(rs.getBoolean(3)){
               historico[5]+= 1;
           }
        }
        st.close();
        rs.close();
        
        return historico;
        

    }
    /**
     * return the streak of a team in a determined season 
     * @param conexion
     * @param equipo
     * @param racha
     * @param jornada
     * @param temporada
     * @return
     * @throws Exception 
     */
    public static int streak(Connection conexion, int equipo, int racha, int jornada, String temporada) throws Exception{
        
            int ji;
            Statement st = conexion.createStatement();

            if (jornada <= racha){
                ResultSet result = st.executeQuery("SELECT * FROM LIGA.Clasificacion WHERE Jornada in (1,"+jornada+") AND Equipos_idEquipos = "+equipo+" AND Temporada = '"+temporada+"'");
                return processRacha(result,1,jornada);
               
                
            }else{
                ji = jornada-racha;
                ResultSet result = st.executeQuery("SELECT * FROM LIGA.Clasificacion WHERE Jornada in ("+ji+","+jornada+") AND Equipos_idEquipos = "+equipo+" AND Temporada = '"+temporada+"' ORDER BY Jornada DESC");
                return processRacha(result,ji,jornada);
            }
            
        
    }
    /**
     * returns the streak of the team in the last matches
     * @param rs
     * @param ji 
     * @param jf
     * @return
     * @throws SQLException 
     */
    public static int processRacha (ResultSet rs, int ji, int jf) throws SQLException{
        
        int racha = 0;
        ArrayList<Integer> vector = new ArrayList<>();      
       
            
            while (rs.next()) {
                vector.add(rs.getInt("PG"));
                vector.add(rs.getInt("PP"));
                               
            }
            
            if(vector.isEmpty()){
                racha = 0;
            }
            else if(vector.size() > 2){
                racha = (vector.get(0) - vector.get(2)) - (vector.get(1) - vector.get(3));
            }else {
                racha = vector.get(0)-vector.get(1);
            }
            rs.close();
          System.out.println(racha);
            return racha;
    }
    /**
     * returns the roaster value for a team in a season, and the number of players 
     * @param conexion
     * @param teamID 
     * @param temporada 
     * @return  
     * @throws java.sql.SQLException 
     */
    public static double[] addPlayersValue(Connection conexion, int teamID, String temporada ) throws SQLException{
            
            double totalValue = 0, numJugadores = 0;
            double[] vector = new double[2];
            
            Statement st = conexion.createStatement();
            ResultSet result = st.executeQuery("SELECT h.ValPos as ValPos FROM LIGA.Historico_Jugador h JOIN Equipos e ON e.idEquipos = h.idEquipo WHERE e.idEquipos = "+ teamID + " AND h.Temporada = '"+ temporada +"'");
                
          
            while (result.next()){
                numJugadores ++;
                totalValue += result.getDouble("ValPos");
            }
            vector[0] = totalValue;
            vector[1] = numJugadores;
            
            return vector;
    }
    
    public static void main(String[] args) throws Exception {
        
        ArrayList<String> seasons = getSeasons();
        FileWriter fichero = new FileWriter("/home/francisco/Dropbox/TFG/data/wekafiles/train_borr.arff");
        PrintWriter pw = new PrintWriter(fichero);
        
        //train with las 15 seasons (1992-93/2007-08)
        for(int i = 20; i < seasons.size()-5; i++){
            //FileWriter fichero = new FileWriter("/home/francisco/Dropbox/TFG/data/wekafiles/train"+seasons.get(i)+".csv");
            //PrintWriter pw = new PrintWriter(fichero);
            if(i == 20){
                procesSeason(seasons.get(i), pw, true);
            }else{
                procesSeason(seasons.get(i), pw, false);
            }
            //pw.flush();
            //pw.close();
        }
        pw.flush();
        pw.close();
        

//        
        FileWriter ficheroTest = new FileWriter("/home/francisco/Dropbox/TFG/data/wekafiles/test_borr.arff");
        PrintWriter pwtest = new PrintWriter(ficheroTest);
        //test
        for(int i = seasons.size()-5; i < seasons.size()-1; i++){
            //FileWriter ficheroTest = new FileWriter("/home/francisco/Dropbox/TFG/data/wekafiles/test"+seasons.get(i)+".csv");
            //PrintWriter pwtest = new PrintWriter(ficheroTest);
            if(i == seasons.size()-5){
                procesSeason(seasons.get(i), pwtest, true);
            }else{
                procesSeason(seasons.get(i), pwtest, false);
            }
            
            //pwtest.flush();
            //pwtest.close();
        }
        pwtest.flush();
        pwtest.close();
        
        
    }
    
}
