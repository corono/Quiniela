/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package database;


import conf.Conf;
import static database.BBDD.getConexionBBDD;
import java.io.File;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;



/**
 * Clase utilizada para generar fichero XML relacionando todos los alias
 * con el ID de la BBDD del equipo, ademas de albergar todos los métodos 
 * necesarios para obtener el ID de un equipo a partir del nombre
 * @author francisco
 */
public class TeamAlias {
    private static Conf configuration = null;
    
    static{
        configuration = Conf.getConfiguration();
    }
    
    private static final Hashtable<String, Integer> teams = new Hashtable<>();
    //public static String aliasFile = "conf/teams.xml";
    

    
    /**
     * extract all name teams and alias from the database
     * and relate with their id
     * @throws Exception 
     */
    public static void teamNames() throws Exception{
        
        int teamid;
        String query, name;
        String[] alias;
        Connection conexion = getConexionBBDD();
        
        query = "SELECT idEquipos, Nombre, Alias FROM LIGA.Equipos;";
        Statement st = conexion.createStatement();
        ResultSet result = st.executeQuery(query);
        
         while (result.next()){
            teamid = result.getInt("idEquipos");
            name = result.getString("Nombre");
            alias = result.getString("Alias").split(",");
            teams.put(name.trim().toLowerCase(), teamid);
            for( String team: alias){
                teams.put(team.trim().toLowerCase(), teamid);
            }

        }
    }
    /**
     * returns a hashtable with all team alias and their id
     * @return
     * @throws Exception 
     */
    public static Hashtable<String, Integer> getTeamNames() throws Exception{
        
        teamNames();
        return teams;
    }
    
    /**
     * create xml file with all team alias and their id
     * @throws ParserConfigurationException
     * @throws TransformerConfigurationException
     * @throws Exception 
     */
    public static void fileTeamAlias() throws ParserConfigurationException, TransformerConfigurationException, Exception{
        
        teamNames();
        
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        
        Element rootElement = doc.createElement("teams");
        doc.appendChild(rootElement);
        
        //iterate through the hashtable        
        Enumeration <String> keys = teams.keys();
        
        while(keys.hasMoreElements()){
            String k = keys.nextElement();
            
            Element team = doc.createElement("team");
            rootElement.appendChild(team);
            Element id = doc.createElement("id");
            id.appendChild(doc.createTextNode(String.valueOf(teams.get(k))));
            team.appendChild(id);
            Element name = doc.createElement("name");
            name.appendChild(doc.createTextNode(k));
            team.appendChild(name);
        }
        
        //saves the file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(configuration.getAliasFile()));
        transformer.transform(source, result);
        
    }
    
//    public static void main(String[] args) throws TransformerConfigurationException, Exception {
//        fileTeamAlias();
//    }
    
}
