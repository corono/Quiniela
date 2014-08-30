/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package file;

import conf.Conf;
import static database.BBDD.getConexionBBDD;
import static database.BBDD.saveMatch;
import database.TeamAlias;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import static process.LevenshteinDistance.computeLevenshteinDistance;

/**
 *Clase que carga en la base de datos los partidos almacenados en un fichero 
 * con tamaño de campo fijo:
 * TEMPORADA  DIVISION    JORNADA      PARTIDO     RESULTADO
 * yyyy-yyyy    xª         j     local-visitante     0-0
 * @author francisco
 */
public class MatchsFile {
    
     //private static final String matchsFile = "../data/1-2div";
     private static Conf configuration = null;
     
     static{
        configuration = Conf.getConfiguration();
    }
    
    /**
       * método que abre el fichero que contiene los partidos
       * @return
       * @throws Exception 
       */
      public static FileReader openMatchesFile () throws Exception{
                    FileReader fr = new FileReader (new File (configuration.getMatchsFile()));
                    
                    return fr;
      }
    
     
        /**
         * read the file with the matches and stores them in the database
         * @throws Exception 
         */
        public static void readMatchesFile() throws Exception {
                   
                   FileReader fr = openMatchesFile();
                   BufferedReader br = new BufferedReader (fr);
                   String linea;
                   String[] aux = new String[8];  
                   Integer g;
                   Connection conexion = getConexionBBDD();
                   StringBuilder sb = new StringBuilder();


                   //se lee el fichero de los partidos yyyy-yyyy division jornada local visitante gl-gv
                   while ((linea = br.readLine())!= null){
                       if(linea.length()>60){
                           sb.append(linea.substring(0, 5));
                           sb.append(linea.substring(7,11));
                           aux[0] = sb.toString();//temporada
                           sb.delete(0, sb.length());
                           aux[1] = linea.substring(11, 21);//division
                           aux[2] = linea.substring(21, 27);//jornada
                           aux[3] = linea.substring(27, 44);//equipo local
                           aux[4] = linea.substring(44, 61);//equipo visitante
                           aux[5] = linea.substring(62, 63);//goles local
                           aux[6] = linea.substring(64, 65);//goles visitante
                           aux[7] = "0";

                           //clean spaces
                           for (int j = 0; j < aux.length; j++ ){
                                 aux[j] = aux[j].trim().toLowerCase();
                           }
                           //detect who winned the match
                           g = Integer.parseInt(aux[5])-Integer.parseInt(aux[6]);

                           if(g == 0){
                               aux[7] = "0";
                           }else if(g < 0){
                               aux[7] = "-1";
                           }else if (g>0){
                               aux[7] = "1";
                           }
                           
                          //get team database id 
                          aux[3] = String.valueOf(getTeamID(aux[3]));
                          aux[4] = String.valueOf(getTeamID(aux[4]));
                          saveMatch(conexion, aux);
                          
                         }//if
                       }//while              

                   fr.close();
                   conexion.close();
        }
        /**
         * 
         * @param team
         * @return
         * @throws ParserConfigurationException
         * @throws SAXException
         * @throws IOException 
         */
        public static Integer getTeamID(String team) throws ParserConfigurationException, SAXException, IOException{
            
            boolean found = Boolean.FALSE;
            int temp = 0, id = -1, distance = 100;
            String name;
            HashMap <Integer, Integer>  dLevenshtein = new HashMap<>();
            
            
            File fXmlFile = new File(configuration.getAliasFile());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
 
            NodeList nList = doc.getElementsByTagName("team");
            
            while((!found)&&(temp<nList.getLength())){

                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                        Element eElement = (Element) nNode;
                        id = Integer.parseInt(eElement.getElementsByTagName("id").item(0).getTextContent());
                        name = eElement.getElementsByTagName("name").item(0).getTextContent();
                        //calculate levenshteind distance
                        distance = computeLevenshteinDistance(team.toLowerCase(),name);
                        //check if the distanc is lower than the previous                       
                        if(!dLevenshtein.containsKey(id)){
                            dLevenshtein.put(id,distance);
                            
                        }else{
                            if(dLevenshtein.get(id) > distance){
                                dLevenshtein.put(id, distance);                                
                            }
                        }

                        if (team.toLowerCase().equals(name)){
                            found = Boolean.TRUE; 
                        }
                }
                temp++;
            }//while
            //if the team is new, return the id
            //whit the lowest distance
            if(!found){
                for (Integer k: dLevenshtein.keySet()){
                    if(distance > dLevenshtein.get(k)){
                        distance = dLevenshtein.get(k);
                        id = k;                        
                    }
                }
            }
            return id;
            
        }
        
      
    
}
