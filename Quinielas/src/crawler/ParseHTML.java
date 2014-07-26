/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package crawler;



import static database.BBDD.getConexionBBDD;
import static database.BBDD.saveCoach;
import static database.BBDD.saveCoachHistoric;
import static database.BBDD.savePlayer;
import static database.BBDD.savePlayerHistoric;
import static database.BBDD.saveTeam;
import java.text.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Pattern;
/**
 *Clase encargada de detectar el tipo de página a parsear o extaer 
 * información y de extraer dicha información
 * @author francisco
 */
public class ParseHTML {
    
    private final static Pattern PLAYER = Pattern.compile("^http://www.bdfutbol.com/es/j/+.*");
    private final static Pattern TEAM = Pattern.compile("^http://www.bdfutbol.com/es/e/+.*");
    private final static Pattern SEASON = Pattern.compile("^http://www.bdfutbol.com/es/t/+.*");
    private final static Pattern REFEREE = Pattern.compile("^http://www.bdfutbol.com/es/r/+.*");
    private final static Pattern COACH = Pattern.compile("^http://www.bdfutbol.com/es/l/+.*");
    
    public ParseHTML(){
        
    }
    
    /**
     * classify the url of the webpage and call the right method to
     * extract the info
     * @param html
     * @param url
     * @throws ParseException 
     */
    public void parseHTML (String html, String url) throws Exception{
        Document doc = Jsoup.parse(html, url);
//        String[] key = null;
//        String description = doc.select("meta[name=description]").get(0).attr("content");
        Connection conexion = getConexionBBDD();
        
        
        
        if(PLAYER.matcher(url).matches()){
            System.out.println("player found");
            parsePlayer(doc, url, conexion);
//        }else if(REFEREE.matcher(url).matches()){
//            System.out.println("referee found");
//            parseReferee(doc, url);
        }else if(TEAM.matcher(url).matches()){
            System.out.println("team found");
            parseTeam(doc, url, conexion);    
//        }else if (SEASON.matcher(url).matches()){
//            System.out.println("season found");
//            parseSeason(doc);
        }else if (COACH.matcher(url).matches()){
            System.out.println("coach found");
            parseCoach(doc, url, conexion);
        }
        conexion.close();
        
        
    }
    /**
     * extracts the info from a player and saves it in the database
     * @param doc
     * @param url
     * @param conexion
     * @throws ParseException 
     */
    public void parsePlayer(Document doc, String url, Connection conexion) throws Exception{
        
        String[] playerInfo = new String[8];
        Integer id = Integer.valueOf(url.replaceAll("\\D+", ""));
        //get the nickname of the player
        playerInfo[3] = doc.select("title").text().split(",")[0];
        
        Element e = doc.select("table[class=tabledades]").get(0);
        Elements rows = e.select("tr");
        //playerInfo[0] = rows.get(0).select("td.dades2").text();
        //playerInfo[1] = rows.get(1).select("td.dades2").text();
        //playerInfo[2] = rows.get(rows.size()-1).select("td.dades2").text();
        for(Element t: rows){
             
              switch(t.select("td.dades1").text()){
                  case "Nombre:":
                      playerInfo[0] = t.select("td.dades2").text();
                      break;
                  case "Demarcación:":
                      playerInfo[2] = t.select("td.dades2").text();
                      break;
                  case "País:":
                      playerInfo[1] = t.select("td.dades2").text();
                      break;
                  case "Fecha de nacimiento:":
                      playerInfo[4] = t.select("td.dades2").text();
                      break;
                  case "Lugar de nacimiento:":
                      playerInfo[5] = t.select("td.dades2").text();
                      break;
                  case "Altura / peso:":
                      playerInfo[6] = t.select("td.dades2").text();
                      if(playerInfo[6]!=null){
                           playerInfo[7] = playerInfo[6].split("/")[1];
                           playerInfo[6] = playerInfo[6].split("/")[0];
                       }
                      break;
                  
              }
            
        }
       
       
        
        savePlayer(conexion, playerInfo, id);

        //insertar en la BBDD al jugador
        //Jugador player = new Jugador();
        //player.setInfo(playerInfo);
        
        //extrae la información del histórico del jugador
        //Equipo   Temporada Div Edad PJ PT PC PS Min *TA TR Goles* 
       
        e = doc.select("table[class=taulabdf traject]").get(0);
        String[] historicInfo = new String[12];
        
        rows = e.select("tr");
        for (int i = 1; i< rows.size()-2; i++){
            Elements data = rows.get(i).select("td");
            historicInfo[0] = data.get(1).text();
            historicInfo[1] = data.get(3).text();
            if(historicInfo[1] == null)
                historicInfo[1] = "-1";
            historicInfo[2] = data.get(4).text();
            historicInfo[3] = data.get(5).text();
            historicInfo[4] = data.get(6).text();
            historicInfo[5] = data.get(7).text();
            historicInfo[6] = data.get(8).text();
            historicInfo[7] = data.get(9).text();
            historicInfo[8] = data.get(10).text();
            historicInfo[9] = data.get(11).text();
            historicInfo[10] = data.get(12).text();            
            historicInfo[11] = data.get(13).text();
            if(historicInfo[11] == null){
                historicInfo[11] = "0";
            }
            savePlayerHistoric(conexion, historicInfo, id);
        }
        //insertar historico en la bbdd
        
        
    }
    /**
     * extracts the info from a coach page and saves it in the database
     * @param doc
     * @param url 
     * @param conexion 
     * @throws java.sql.SQLException 
     * @throws java.text.ParseException 
     */
    public void parseCoach(Document doc, String url, Connection conexion) throws SQLException, ParseException{
        String[] coachInfo = new String[7];
        Integer id = Integer.valueOf(url.replaceAll("\\D+", ""));
        //get the nickname 
        coachInfo[2] = doc.select("title").text().split(",")[0];
       
        Element e = doc.select("table[class=tabledades]").get(0);
        Elements rows = e.select("tr");
        
        for(Element t: rows){
             System.out.println(t.select("td").text());
              switch(t.select("td.dades1").text()){
                  case "Nombre:":
                      coachInfo[0] = t.select("td.dades2").text();
                      break;
                  case "País:":
                      coachInfo[1] = t.select("td.dades2").text();
                      break;
                  case "Fecha de nacimiento:":
                      coachInfo[3] = t.select("td.dades2").text();
                      break;
                  case "Lugar de nacimiento:":
                      coachInfo[4] = t.select("td.dades2").text();
                      break;
                  case "Altura / peso:":
                      coachInfo[5] = t.select("td.dades2").text();
                      if(coachInfo[5]!=null){
                           coachInfo[6] = coachInfo[5].split("/")[1];
                           coachInfo[5] = coachInfo[5].split("/")[0];
                       }
                      break;
                  
              }
            
        }
        

        //insertar en la BBDD al entrenador
        saveCoach(conexion, coachInfo, id);
        
        //extrae la información del histórico 
       
        e = doc.select("table[class=taulabdf traject]").get(0);
        String[] historicInfo = new String[5];
        System.out.println(e.select("tr").size());
        rows = e.select("tr");
        for (int i = 1; i< rows.size()-2; i++){
            Elements data = rows.get(i).select("td");
            historicInfo[0] = data.get(1).text();//equipo
            historicInfo[1] = data.get(3).text();//temporada
            historicInfo[2] = data.get(4).text();//division
            historicInfo[3] = data.get(5).text();//edad
            historicInfo[4] = data.get(6).text();//partidos
            saveCoachHistoric(conexion, historicInfo, id);
        }
        //insertar historico en la bbdd
    }
    /**
     * 
     * @param doc
     * @param url 
     */
    public void parseReferee(Document doc, String url){
        String[] refereeInfo = new String[2];
        Integer id = Integer.valueOf(url.replaceAll("\\D+", ""));
        //get the nickname 
        refereeInfo[1] = doc.select("title").text().split(",")[0];
       
        Element e = doc.select("table[class=tabledades]").get(0);
        Elements rows = e.select("tr");
        refereeInfo[0] = rows.get(0).select("td.dades2").text();
        //insertar en la BBDD al arbitro
        
        //extrae la información del histórico 
        e = doc.select("table[class=taulabdf traject]").get(0);
        String[] historicInfo = new String[8];
        System.out.println(e.select("tr").size());
        rows = e.select("tr");
        for (int i = 1; i< rows.size()-3; i++){
            Elements data = rows.get(i).select("td");
            historicInfo[0] = data.get(1).text();
            historicInfo[1] = data.get(2).text();
            historicInfo[2] = data.get(3).text();
            historicInfo[3] = data.get(4).text();
            historicInfo[4] = data.get(5).text();
            historicInfo[5] = data.get(6).text();
            historicInfo[6] = data.get(7).text();
            historicInfo[7] = data.get(8).text();
        }
        
        //insertar historico en la bbdd
        
    }
    
    public void parseSeason(Document doc){
        //liga, copa del rey, pichichi y zamora en primera
        //2ª divison liga y pichichi
        //temporadas sin supercopa
        
        if(doc.title().contains("Primera")){
            System.out.println(doc.title());
        }
        
    }
    /**extract the necessary info from a team and saves it in the database
     * @param doc
     * @param url 
     * @param conexion 
     * @throws java.sql.SQLException 
     */
    public void parseTeam(Document doc, String url, Connection conexion) throws SQLException{
        String[] teamInfo = new String[8];
        Integer id = Integer.valueOf(url.replaceAll("\\D+", ""));
        
        teamInfo[1] = doc.select("title").text();
        teamInfo[3] = "0";
        teamInfo[4] = "0";
       
        Element e = doc.select("table[class=tabledades]").get(0);
        Elements rows = e.select("tr");
      
         for(Element t: rows){
             //System.out.println(t.select("td").text());
              switch(t.select("td.dades1").text()){
                  case "Nombre":
                      teamInfo[0] = t.select("td.dades2").text();
                      break;
                  case "Otros nombres":
                      teamInfo[1] += ",";
                      teamInfo[1] += t.select("td.dades2").text();
                      break;
                  case "Ciudad":
                      teamInfo[2] = t.select("td.dades2").text();
                      break;
                  case "Temporadas en 1ª":
                      teamInfo[3] = t.select("td.dades2").text();
                      break;
                  case "Temporadas en 2ª":
                      teamInfo[4] = t.select("td.dades2").text();
                      break;
                  case "Filial":
                      teamInfo[5] = t.select("td.dades2").text();
                      break;
                  case "Fecha Fundación":
                      teamInfo[6] = t.select("td.dades2").text();
                      break;
                  case "Equipo anterior":
                      teamInfo[1] += ",";
                      teamInfo[1] += doc.select("p[class=ceq2]").text();
                      break;
                  case "Equipo Filial De":
                      teamInfo[7] = t.select("td.dades2").text();
                      
                  
              }
            
        }
        //insertar en la BBDD al equipo
        saveTeam(conexion, teamInfo, id);
       
        
        
        //extrae la información del histórico 
       
//        Elements trofeos = doc.select("div[class=divtrof]");
//        if(trofeos.size()>0){
//            String[] trofeoInfo = new String[8];
//            String titulo;
//            for(Element t: trofeos){
//                titulo = t.select("div[class=etiqanys]").text();
//                if(titulo.contains("^Liga[*(s)]+(:)$")){
//                    
//                }else if(titulo.contains("Copa+s*+ del Rey:")){
//                    
//                }else if(titulo.contains("Liga+s*+ de Campeones:$")){
//                    
//                }else if (titulo.contains("Copa+s*+ UEFA:")){
//                    
//                }else if (titulo.contains("Copas Interncontinentales:")){
//                    
//                }else if(titulo.contains("Supercopas:")){
//                    
//                }else if(titulo.contains("Supercopas Europa:")){
//                    
//                }else if(titulo.contains("Mundiales de Clubes:")){
//                    
//                }
//            }
//        }
       
        //insertar historico en la bbdd
        
    }
    
}
