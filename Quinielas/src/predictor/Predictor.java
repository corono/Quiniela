/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package predictor;

import conf.Conf;
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
    private static Conf configuration;
    
     static{
        configuration = Conf.getConfiguration();
        }
     
     public static void multilayerPerceptron() throws Exception{
        
         
        DataSource train = new DataSource(configuration.getWorkspace()+"train_common.arff");
        DataSource test = new DataSource(configuration.getWorkspace()+"test_common.arff");



        Instances trainInstances = train.getDataSet();
        Instances testInstances = test.getDataSet();

        //last attribute classify
        trainInstances.setClassIndex(trainInstances.numAttributes() - 1);
        testInstances.setClassIndex(testInstances.numAttributes() - 1);
//        
//        Classifier cModel = (Classifier)new MultilayerPerceptron();  
//        cModel.buildClassifier(trainInstances);  
//
//        weka.core.SerializationHelper.write("/some/where/nBayes.model", cModel);
//
//        Classifier cls = (Classifier) weka.core.SerializationHelper.read("/some/where/nBayes.model");
//
//        // Test the model
//        Evaluation eTest = new Evaluation(trainInstances);
//        eTest.evaluateModel(cls, testInstances);



        MultilayerPerceptron mlp = new MultilayerPerceptron();
        mlp.buildClassifier(trainInstances);
        mlp.setHiddenLayers(configuration.getHiddenLayers());
        mlp.setLearningRate(configuration.getLearningRate());    
        mlp.setTrainingTime(configuration.getEpocs());
        mlp.setMomentum(configuration.getMomentum());

        // train classifier
        Classifier cls = new MultilayerPerceptron();  
        cls.buildClassifier(trainInstances);   
       

        // evaluate classifier and print some statistics
        Evaluation eval = new Evaluation(trainInstances);
        eval.evaluateModel(cls, testInstances);        
        
        System.out.println(eval.toSummaryString());
     }
   
   public static void main(String[] args) throws Exception{
       
       multilayerPerceptron();
//        System.out.println("TestPredictor");
//        DataSource train = new DataSource("/home/francisco/Dropbox/TFG/data/wekafiles/train_borr.arff");
//        DataSource test = new DataSource("/home/francisco/Dropbox/TFG/data/wekafiles/test_borr.arff");
//
//
//
//        Instances trainInstances = train.getDataSet();
//        Instances testInstances = test.getDataSet();
//
//        //last attribute classify
//        trainInstances.setClassIndex(trainInstances.numAttributes() - 1);
//        testInstances.setClassIndex(testInstances.numAttributes() - 1);
////        
////        Classifier cModel = (Classifier)new MultilayerPerceptron();  
////        cModel.buildClassifier(trainInstances);  
////
////        weka.core.SerializationHelper.write("/some/where/nBayes.model", cModel);
////
////        Classifier cls = (Classifier) weka.core.SerializationHelper.read("/some/where/nBayes.model");
////
////        // Test the model
////        Evaluation eTest = new Evaluation(trainInstances);
////        eTest.evaluateModel(cls, testInstances);
//
//
//
//        MultilayerPerceptron mlp = new MultilayerPerceptron();
//        mlp.buildClassifier(trainInstances);
//        mlp.setHiddenLayers("a");
//        mlp.setLearningRate(0.3);    
//        mlp.setTrainingTime(500);
//        mlp.setMomentum(0.2);
//
//        // train classifier
//        Classifier cls = new MultilayerPerceptron();  
//        cls.buildClassifier(trainInstances);   
//       
//
//        // evaluate classifier and print some statistics
//        Evaluation eval = new Evaluation(trainInstances);
//        eval.evaluateModel(cls, testInstances);        
//        
//        System.out.println(eval.toSummaryString());
//
   }
}
