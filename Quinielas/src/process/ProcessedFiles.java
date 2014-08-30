/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package process;


import java.io.FileWriter;
import java.io.PrintWriter;
/**
 *
 * @author francisco
 */
public class ProcessedFiles {
    /**
     * Print the arff header file, the output is a char
     * v local win
     * e draw
     * d visitor win
     * @param pw 
     */
    public static void arffHeader(PrintWriter pw){
        
        pw.println("@relation Quiniela");
        pw.println("@attribute team1-id numeric");
        pw.println("@attribute team2-id numeric");
        pw.println("@attribute percent-match-local-win numeric");
        pw.println("@attribute percent-match-local-defeat numeric");
        pw.println("@attribute percent-match-draw numeric");
        pw.println("@attribute percent-team1-win numeric");
        pw.println("@attribute percent-team2-win numeric");
        pw.println("@attribute percent-team1-seasonWins numeric");
        pw.println("@attribute percent-team1-seasonLocalWins numeric");
        pw.println("@attribute percent-team1-seasonLocalDraws numeric");
        pw.println("@attribute percent-team1-seasonLocalDefeats numeric");
        pw.println("@attribute team1-average numeric");
        pw.println("@attribute team1-local-average numeric");
        pw.println("@attribute team1-average-ppm numeric");
        pw.println("@attribute team1-streak numeric");
        pw.println("@attribute team1-players-value numeric");
        pw.println("@attribute team1-average-player-value numeric");
        pw.println("@attribute percent-team2-seasonWins numeric");
        pw.println("@attribute percent-team2-seasonVisitorWins numeric");
        pw.println("@attribute percent-team2-seasonVisitorDraws numeric");
        pw.println("@attribute percent-team2-seasonVisiorDefeats numeric");
        pw.println("@attribute team2-average numeric");
        pw.println("@attribute team2-local-average numeric");
        pw.println("@attribute team2-average-ppm numeric");
        pw.println("@attribute team2-streak numeric");
        pw.println("@attribute team2-players-value numeric");
        pw.println("@attribute team2-average-player-value numeric");
        pw.println("@attribute type {'v','e','d'}");
        pw.println("@data");
    }
    
    public static void pattHeader(PrintWriter pw){
        
    }
    
}
