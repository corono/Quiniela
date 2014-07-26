/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package predictor;

import java.io.*;

import weka.core.*;
import weka.core.Instances;
import weka.classifiers.Evaluation;
import weka.classifiers.*;
import weka.classifiers.functions.MultilayerPerceptron; 
import weka.core.converters.ConverterUtils.DataSource;

  

/**
 *
 * @author francisco
 */
public class Predictor {
   
    public static void main(String[] args) throws Exception{
        
        DataSource train = new DataSource("/home/francisco/Dropbox/TFG/data/wekafiles/train_2.csv");
        DataSource test = new DataSource("/home/francisco/Dropbox/TFG/data/wekafiles/test_2.csv");



        Instances trainInstances = train.getDataSet();
        Instances testInstances = test.getDataSet();

        //last three atributes out
        trainInstances.setClassIndex(trainInstances.numAttributes() - 1);
        testInstances.setClassIndex(testInstances.numAttributes() - 1);



        MultilayerPerceptron mlp = new MultilayerPerceptron();
        mlp.buildClassifier(trainInstances);
        mlp.setHiddenLayers("a");
        mlp.setLearningRate(0.3);       

        // train classifier
        Classifier cls = new MultilayerPerceptron();  
        cls.buildClassifier(trainInstances);
        



        // evaluate classifier and print some statistics
        Evaluation eval = new Evaluation(trainInstances);
        eval.evaluateModel(cls, testInstances);        

        System.out.println(eval.toSummaryString());

    }
}
