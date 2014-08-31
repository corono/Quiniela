/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package conf;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static predictor.Predictor.multilayerPerceptron;
import static process.WeekPrediction.weekPrediction;

/**
 *
 * @author francisco
 */
public class Main {
    //cargar fichero de jornada por argumento
    //weekPrediction con él
    //weekPrediction genera 2 ficheros (primera y segunda dvision)
    //cargar el modelo seleccionado en el predictor
    //procesar los 2 ficheros
    //guardar los resultados de las predicciones
    //generar fichero de salida con los partidos y las predicciones
    public static void main(String[] args) throws Exception {
        
        
       switch (args.length){
           case 1 : 
                try {
                    //genera ficheros para predecir
                    weekPrediction(args[0]);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
               break;
           default :
               System.out.println("Wrong number of parameters");
               System.out.println("'java -jar jarfile jornadafile.txt'");
               System.exit(0);
               
       }
       //train the multilayerPerceptron
       multilayerPerceptron();
       //load the model, test and retrun the file with the result
    }
}
