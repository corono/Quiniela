/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package database;




import conf.Conf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;  
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;

        
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.ConfigurationException;
import process.ClasRow;


/**
 * Clase principal para el control de la BBDD
 * @author francisco
 */
public class BBDD {
    private static Integer matchCounter = 0;
    private static final String urlBBDD = "jdbc:mysql://127.0.0.1:3306/LIGA";
    private static final String user = "root";
    private static final String pass = "b_a_sket88";
    public static ArrayList<String> teams = new ArrayList<>();
    private static Conf configuration = null;

    static{
        configuration = Conf.getConfiguration();
    }
    
     /**
      * Método que conecta con la BBDD y devuelve la conexión.
      * @return
      * @throws Exception 
      */
     public static Connection getConexionBBDD() throws Exception{
                    DriverManager.registerDriver(new org.gjt.mm.mysql.Driver());
                    //Connection conexion = DriverManager.getConnection(urlBBDD, user, pass);
                    
                    String url = "jdbc:mysql://"+configuration.getMysqlHost()+":3306/"+configuration.getMysqlDatabase();
                    System.out.println(url);
                    Connection conexion = DriverManager.getConnection(url, configuration.getMysqlUser(), configuration.getMysqlPass());
                    
                    
                    return conexion;

          }     
      
      /**
       *  match[0] = temporada
       *  match[1] = division
       *  match[2] = jornada
       *  match[3] = equipo local
       *  match[4] = equipo visitante
       *  match[5] = goles local
       *  match[6] = goles visitante
       *  match[7] = 0 draw, 1 local win, -1 visitor win
       * 
       * @param conexion
       * @param match
       * @throws Exception 
       */
      public static void saveMatch(Connection conexion, String[] match)throws Exception{
                       
                       PreparedStatement pstmt = conexion.prepareStatement(
                                    "INSERT INTO Partidos (idPartidos, Local, Visitante, Jornada, Temporada, VictoriaLocal, Empate, VictoriaVisitante, Division, Arbitros_idArbitros, GolesLocal, GolesVisitante) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                       pstmt.setInt(1, matchCounter);
                       pstmt.setInt(2, Integer.parseInt(match[3]));
                       pstmt.setInt(3, Integer.parseInt(match[4]));
                       pstmt.setString(4, match[2]);
                       pstmt.setString (5, match[0]);
                       
                       switch(Integer.parseInt(match[7])){
                           case 0:
                                pstmt.setBoolean(6, Boolean.FALSE);
                                pstmt.setBoolean(7, Boolean.TRUE);
                                pstmt.setBoolean(8, Boolean.FALSE);
                               break;
                           case 1:
                                pstmt.setBoolean(6, Boolean.TRUE);
                                pstmt.setBoolean(7, Boolean.FALSE);
                                pstmt.setBoolean(8, Boolean.FALSE);
                               break;
                           case -1:
                                pstmt.setBoolean(6, Boolean.FALSE);
                                pstmt.setBoolean(7, Boolean.FALSE);
                                pstmt.setBoolean(8, Boolean.TRUE);
                               break;
                           
                       }
                       pstmt.setString(9, match[1]);
                       pstmt.setInt(10, -1);
                       pstmt.setInt(11, Integer.parseInt(match[5]));
                       pstmt.setInt(12, Integer.parseInt(match[6]));
                       pstmt.executeUpdate();  
                       pstmt.close();
                       matchCounter++;
      }
      /**
       * 
       * @param conexion
       * @param player
       * @param id
       * @throws SQLException
       * @throws ParseException 
       */
      public static void savePlayer(Connection conexion, String[] player, Integer id) throws SQLException, ParseException{
                        
                        
                        PreparedStatement pstmt = conexion.prepareStatement(
                                    "INSERT INTO Jugadores (idJugadores, Nombre, Pais, Lugar_Nacimiento, Fecha_Nacimiento, Altura, Peso, Posicion, Apodo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                        pstmt.setInt(1, id);
                        pstmt.setString(2, player[0]);
                        pstmt.setString(3, player[1]);
                        pstmt.setString(4, player[5]);
                        pstmt.setString(5, player[4]);                       
                        pstmt.setString(6, player[6]);
                        pstmt.setString(7, player[7]);
                        pstmt.setString(8, player[2]);
                        pstmt.setString(9, player[3]);
                        pstmt.executeUpdate();
                        pstmt.close();
            
      }
      /**
       * 
       * @param conexion
       * @param playerHisto
       * @param id
       * @throws SQLException 
       */
      public static void savePlayerHistoric(Connection conexion, String[] playerHisto, Integer id) throws SQLException, ParseException{
                    
                        PreparedStatement pstmt = conexion.prepareStatement(
                                     "INSERT INTO Historico_Jugador(Jugadores_idJugadores, Temporada, Equipo, PJugados, PTitular, PCompletos, PSuplente, Minutos, Amarillas, Rojas, Goles, Division, Edad) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                        pstmt.setInt(1,id);
                        pstmt.setString(2, playerHisto[1]);
                        pstmt.setString(3, playerHisto[0]);
                        pstmt.setInt(4, Integer.parseInt(playerHisto[4]));
                        pstmt.setInt(5, Integer.parseInt(playerHisto[5]));
                        pstmt.setInt(6, Integer.parseInt(playerHisto[6]));
                        pstmt.setInt(7, Integer.parseInt(playerHisto[7]));
                        pstmt.setInt(8, Integer.parseInt(playerHisto[8]));
                        pstmt.setInt(9, Integer.parseInt(playerHisto[9]));
                        pstmt.setInt(10, Integer.parseInt(playerHisto[10]));
                        pstmt.setInt(11, Integer.parseInt(playerHisto[11]));
                        pstmt.setString(12, playerHisto[2]);
                        pstmt.setInt(13, Integer.parseInt(playerHisto[3]));
                        pstmt.executeUpdate();
                        pstmt.close();
                         
      }
      /**
       * coach[0] = name
       * coach[1] = country
       * coach[2] = nickname
       * coach[3] = birth date
       * coach[4] = birthplace
       * coach[5] = height
       * coach[6] = weight
       * @param conexion
       * @param coach
       * @param id
       * @throws SQLException 
       */
      public static void saveCoach(Connection conexion, String[] coach, Integer id) throws SQLException, ParseException{
          
                        PreparedStatement pstmt = conexion.prepareStatement(
                                    "INSERT INTO Entrenador (idEntrenador, Nombre, Lugar_Nacimiento, Fecha_Nacimiento, Altura, Peso, Apodo, Pais) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                        pstmt.setInt(1, id);
                        pstmt.setString(2, coach[0]);
                        pstmt.setString(3, coach[4]);
                        pstmt.setString(4, coach[3]);
                        pstmt.setString(5, coach[5]);
                        pstmt.setString(6, coach[6]);
                        pstmt.setString(7, coach[2]);
                        pstmt.setString(8, coach[1]);
                        pstmt.executeUpdate();
                        pstmt.close();
            
      }
      /**
       * coachHisto[0] = Equipo
       * coachHisto[1] = Temporada
       * coachHisto[2] = División
       * coachHisto[3] = Edad
       * coachHisto[4] = Partidos
       * @param conexion
       * @param coachHisto
       * @param id
       * @throws SQLException 
       */
      public static void saveCoachHistoric(Connection conexion, String[] coachHisto, Integer id) throws SQLException, ParseException{
                    
                        PreparedStatement pstmt = conexion.prepareStatement(
                                     "INSERT INTO Trayectoria_Entrenador( Temporada, Equipo, Division, Partidos, Edad, Entrenador_idEntrenador) VALUES(?, ?, ?, ?, ?, ?)");
                        pstmt.setInt(6, id);
                        pstmt.setString(1, coachHisto[1]);
                        pstmt.setString(2, coachHisto[0]);
                        pstmt.setString(3, coachHisto[2]);
                        pstmt.setInt(4, Integer.parseInt(coachHisto[4]));
                        pstmt.setInt(5, Integer.parseInt(coachHisto[3]));

                        pstmt.executeUpdate();
                        pstmt.close();
                         
      }     
      /**
       * 
       * @param conexion
       * @param team
       * @param id
       * @throws SQLException 
       */ 
      public static void saveTeam( Connection conexion, String[] team, Integer id) throws SQLException{
                        System.out.println(team[0]);
                        PreparedStatement pstmt = conexion.prepareStatement(
                                    "INSERT INTO Equipos (idEquipos, Nombre, Fundacion, Total_Temporadas_1, Total_Temporadas_2, Alias, Filial, Ciudad, Filial_de) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                        pstmt.setInt(1, id);
                        pstmt.setString(2, team[0]);
                        pstmt.setString(3, team[6]);
                        pstmt.setInt(4, Integer.parseInt(team[3]));
                        pstmt.setInt(5, Integer.parseInt(team[4]));
                        pstmt.setString(6, team[1]);
                        pstmt.setString(7, team[5]);
                        pstmt.setString(8, team[2]);
                        pstmt.setString(9, team[7]);
                        pstmt.executeUpdate();
                        pstmt.close();
      }
      /**
     * 
     * @param clasificacion
     * @throws Exception 
     */
        public static void insertClasificacion(HashMap <Integer, ArrayList<ClasRow>> clasificacion)throws Exception{

            Integer puntos, temporada;
            Connection conexion = getConexionBBDD();
            PreparedStatement pstmt = conexion.prepareStatement(
                    "INSERT INTO Clasificacion (Temporada, Equipos_idEquipos, PJ, PG, PE, PP, PGL, PEL, PPL, PGV, PEV, PPV, GF, GC, GFL, GCL, GFV, GCV, Jornada, Puntos, Division) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            for (Integer idEquipo : clasificacion.keySet()){

                for(ClasRow jornada : clasificacion.get(idEquipo)){
                    pstmt.setString(1, jornada.getTemporada());
                    pstmt.setInt(2, idEquipo);
                    pstmt.setInt(3, jornada.getPJ());
                    pstmt.setInt(4, jornada.getPG());
                    pstmt.setInt(5, jornada.getPE());
                    pstmt.setInt(6, jornada.getPP());
                    pstmt.setInt(7, jornada.getPGL());
                    pstmt.setInt(8, jornada.getPEL());
                    pstmt.setInt(9, jornada.getPPL());
                    pstmt.setInt(10, jornada.getPGV());
                    pstmt.setInt(11, jornada.getPEV());
                    pstmt.setInt(12, jornada.getPPV());
                    pstmt.setInt(13, jornada.getGF());
                    pstmt.setInt(14, jornada.getGC());
                    pstmt.setInt(15, jornada.getGFL());
                    pstmt.setInt(16, jornada.getGCL());
                    pstmt.setInt(17, jornada.getGFV());
                    pstmt.setInt(18, jornada.getGCV());
                    pstmt.setInt(19, jornada.getJornada());
                    temporada = Integer.valueOf(jornada.getTemporada().substring(0,4));
                    if(temporada>=1995){
                        puntos = jornada.getPG()*3+jornada.getPE();
                    }else{
                        puntos = jornada.getPG()*2+jornada.getPE();
                    }
                    pstmt.setInt(20, puntos);
                    pstmt.setString(21, jornada.getDivision());

                    pstmt.executeUpdate();
                }


            }

            pstmt.close();   
            conexion.close();


        }
      /**
       * get all season stored in database
       * @return
       * @throws Exception 
       */
      public static ArrayList<String> getSeasons() throws Exception{
          
        Connection conexion = getConexionBBDD();
        String query =  "SELECT Temporada FROM LIGA.Partidos Group BY Temporada";
        Statement st = conexion.createStatement();
        ResultSet result = st.executeQuery(query);
        ArrayList<String> seasons = new ArrayList<>();

        while (result.next()){
            seasons.add(result.getString("Temporada"));
        }
        return seasons;
          
      }
      /**
       * 
       * @throws Exception 
       */
      public static void selectTeamsFromPartidos() throws Exception{
          
          Connection conexion = getConexionBBDD();
          String query =  "SELECT Local FROM LIGA.Partidos Group BY Local";
          Statement st = conexion.createStatement();
          ResultSet result = st.executeQuery(query);
          while (result.next()){
              teams.add(result.getString("Local"));
          }
          
          
      }
      /* metodos usados para extraer alias de otras fuentes de información
      recopiladas previamente de forma manual
      public static void updateAlias(Connection conexion, String alias, Integer id)throws Exception{
          
          StringBuilder sb = new StringBuilder();
          sb.append("UPDATE Equipos");
          sb.append(" SET Alias = \"");
          sb.append(alias);
          sb.append("\" WHERE idEquipos = ");
          sb.append(id);
          Statement st = conexion.createStatement();
          st.executeUpdate(sb.toString());
          
      }
      public static int getIdTeam(Connection conexion, String team)throws Exception{
            String query = "SELECT idEquipos, Nombre, Alias FROM LIGA.Equipos WHERE Nombre = '"+team+"';";
            Statement st = conexion.createStatement();
            ResultSet result = st.executeQuery(query);
            Integer teamid = -1;
        
            while (result.next()){
                teamid = result.getInt("idEquipos");
                System.out.println( "id: "+teamid+" Nombre: "+result.getString("Nombre")); 
           }
            return teamid;
      }
     */
      
      
      
    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
    
//        
//        HashMap <String, ArrayList<Partido>> partidoMap = new HashMap<>();
//        
//         FileReader fr = new FileReader (new File ("../data/Alias"));
//         BufferedReader br = new BufferedReader (fr);
//         String linea;
//         Integer id;
//         
//         Connection conexion = getConexionBBDD();
//         while ((linea = br.readLine())!= null){
//             
//             id = getIdTeam(conexion, linea.split(",")[0]);
//             updateAlias(conexion, linea, id);
//         }
//         conexion.close();
        
//       readMatchesFile();
        
//        FileWriter fw = new FileWriter(new File("/home/francisco/Dropbox/TFG/data/equipos"));
//        selectTeamsFromPartidos();
//        for(String equipo: teams){
//            fw.write(equipo+"\n");
//            
//        }
//        fw.close();
        
    }
    
}
