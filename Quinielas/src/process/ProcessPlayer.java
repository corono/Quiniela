/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package process;

import static database.BBDD.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author francisco
 */
public class ProcessPlayer {
    /**
     * extract the players from a season and evaluates their team influence
     * @param team
     * @param season
     * @param conexion
     * @throws Exception 
     */
    public static void evaluationTeamPlayer() throws Exception{
        
        Connection conexion = getConexionBBDD();
        double mxp, val, aux0, aux1, aux2, aux3, aux4, aux5;
        int n = 6; //número de variables usadas para determinar la valoración del jugador
        String tmp; 
        
        String query =  "SELECT * FROM LIGA.Historico_Jugador";
        Statement st = conexion.createStatement();
        ResultSet result = st.executeQuery(query);
        while (result.next()){
            if(result.getInt("PJugados") == 0){
                mxp = 0;
            }else{
                mxp = result.getInt("Minutos") / result.getInt("PJugados");
            }
            
            
            aux0 = result.getInt("PJugados") * result.getInt("PTitular");
            aux1 = result.getInt("PTitular") * result.getInt("PCompletos");
            aux2 = result.getInt("PCompletos") * result.getInt("PSuplente");
            aux3 = result.getInt("PSuplente") * mxp;
            aux4 = mxp * result.getInt("Goles");
            aux5 = result.getInt("Goles") * result.getInt("PJugados");
            //System.out.println(Math.sin(360/6)+" "+aux0 + " "+aux1 + " "+aux2 + " "+aux3 + " "+aux4 + " "+aux5 + " ");
                
            tmp = result.getString("Temporada");
            val = 0.5 * Math.sin(360/n) * (aux0 + aux1 + aux2 + aux3 + aux4 + aux5)* -1;
            try{
                Statement st2 = conexion.createStatement();
                String query2 = "UPDATE LIGA.Historico_Jugador SET ValPos = '" + val + "' WHERE Jugadores_idJugadores = '" + result.getString("Jugadores_idJugadores") + "' AND Temporada = '" + tmp +"'";
                //System.out.println(query2);
                st2.executeUpdate(query2);
            }
            catch (SQLException e){
                System.out.println (e.getMessage());
                e.printStackTrace();
                break;
                
            }
            
            
        }
    }    
   
    
    public static void main(String[] args) throws Exception {
        evaluationTeamPlayer();
    }
    
    
}
