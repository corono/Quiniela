/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package database;

import static database.BBDD.getConexionBBDD;
import static database.BBDD.getSeasons;
import static database.BBDD.insertClasificacion;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import process.ClasRow;

/**
 * Clase que genera la tabla Clasificación de la BBDD a partir de la tabla
 * Partidos
 * @author francisco
 */
public class Clasificacion {
    
    private static StringBuilder sb = new StringBuilder();
    private static Statement stmt;
    
    private static StringBuilder reuseForPerformance(StringBuilder stringBuilder){
        return stringBuilder.delete(0, stringBuilder.length());
        
    }
    /**
     * 
     * @param season
     * @param division
     * @throws Exception 
     */
    public static void selectSeason(String season, String division) throws Exception{
        HashMap <Integer, ArrayList<ClasRow>> temporada = new HashMap<>();
       
        sb = reuseForPerformance(sb);
        sb.append("SELECT * FROM LIGA.Partidos WHERE Temporada = \"");
        sb.append(season);
        sb.append("\" AND Division = \"");
        sb.append(division);
        sb.append("\"");
                    
        Connection conexion = getConexionBBDD();
        stmt = conexion.createStatement();
        ResultSet result = stmt.executeQuery(sb.toString());
        int[] goles = new int[3];
        
        while (result.next()){
            if(result.getBoolean("VictoriaLocal")){
                goles[0] = 2;
            }else if(result.getBoolean("Empate")){
                goles[0] = 1;
            }else if (result.getBoolean("VictoriaVisitante")){
                goles[0] = 0;
            }
            goles[1] = result.getInt("GolesLocal");
            goles[2] = result.getInt("GolesVisitante");
            
            processLocal(temporada, result.getInt("Local"), result.getString("Jornada"), result.getString("Temporada"), goles, division);
            processVisitante(temporada, result.getInt("Visitante"), result.getString("Jornada"), result.getString("Temporada"), goles, division);
            
        }
        result.close();
        conexion.close();
        insertClasificacion(temporada);
        
    }
    /**
     * 
     * @param clasificacion
     * @param local
     * @param jornada
     * @param temporada
     * @param res 
     */
    public static void processLocal(HashMap <Integer, ArrayList<ClasRow>> clasificacion, Integer local, String jornada, String temporada, int[] res, String div){
        //se crea el objeto que contiene la información de la jornada
        ClasRow homeTeam = new ClasRow(local, temporada, Integer.valueOf(jornada), div);
        //introducimos la información del partido
        homeTeam.setLocalMatch(res);
        
        
        ArrayList<ClasRow> historicSeason;
        if(clasificacion.containsKey(local)){
            historicSeason = clasificacion.get(local);
            //actualizamos los valores con el acumulado hasta la fecha
            homeTeam.acumulado(historicSeason.get(historicSeason.size()-1));
            historicSeason.add(homeTeam);
            clasificacion.put(local, historicSeason);
            
        }else{
            historicSeason = new ArrayList<>();
            historicSeason.add(homeTeam);
            clasificacion.put(local, historicSeason);
        }
        
    }
    /**
     * 
     * @param clasificacion
     * @param visitante
     * @param jornada
     * @param temporada
     * @param res 
     */
    public static void processVisitante(HashMap <Integer, ArrayList<ClasRow>> clasificacion, Integer visitante, String jornada, String temporada, int[] res, String div){
         //se crea el objeto que contiene la información de la jornada
        ClasRow homeTeam = new ClasRow(visitante, temporada, Integer.valueOf(jornada), div);
        //introducimos la información del partido
        homeTeam.setVisitorMatch(res);
        
        
        ArrayList<ClasRow> historicSeason;
        if(clasificacion.containsKey(visitante)){
            historicSeason = clasificacion.get(visitante);
            //actualizamos los valores con el acumulado hasta la fecha
            homeTeam.acumulado(historicSeason.get(historicSeason.size()-1));
            historicSeason.add(homeTeam);
            clasificacion.put(visitante, historicSeason);
            
        }else{
            historicSeason = new ArrayList<>();
            historicSeason.add(homeTeam);
            clasificacion.put(visitante, historicSeason);
        }
        
    }
    
    public static void main(String[] args) throws Exception {

        ArrayList<String> temporadas = getSeasons();
        for (String s : temporadas){
            selectSeason(s,"1ª");
            selectSeason(s,"2ª");
        }
    }
    
}
