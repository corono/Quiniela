/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wikifycategoryclustering;

import weka.core.*;
import weka.core.converters.ConverterUtils.*;
import weka.filters.unsupervised.attribute.*;
import weka.filters.Filter;
import weka.core.converters.*;

import java.io.*;

/**
 *
 * @author BRTE\fperez
 */
public class ClusterFilter {
        /**    
        * Add an unsupervised cluster filter of your choice
        * @param opFilter opciones del Filtro
                * -W Cluster a implementar
                * -I atributos a ignorar
        * @param opCluster opciones del Cluster
        * opciones que podemos añadir a simpleKMeans
                * -I numero de iteraciones
                * -N numero de clusters (no siempre valida)
                * -A Algoritmo para calcular la distancia EuclideanDistance/ManhattanDistance
        * @param inputInstances instancias a las que aplicar el filtro
        * @return
        * @throws Exception
        */
        public static Instances clusterFilterInstances(String opFilter, String opCluster, Instances inputInstances) throws Exception {

		AddCluster clusterFilter = new AddCluster();
		
		String[] optionsFilter = Utils.splitOptions(opFilter);
		optionsFilter[1] +=" "+opCluster;
		
		clusterFilter.setOptions(optionsFilter);
		clusterFilter.setInputFormat(inputInstances);
		
		Instances instancesClusterFilter= Filter.useFilter(inputInstances, clusterFilter);
		return instancesClusterFilter;
        }

	
        /**
         * Create a new file with the new Instances
         * @param filterInstances instancias a guardar en fichero
         * @param fOut fOut ruta del fichero de salida
         * @throws Exception 
         */
	public static void newArffFile(Instances filterInstances, String fOut)throws Exception{
		ArffSaver saver = new ArffSaver();
		saver.setInstances(filterInstances);
		saver.setFile(new File(fOut));
		saver.writeBatch();
        }
        /**Establish the last attribute as Class
         * @param instance instancias a las que indicar el atributo clase
         */
	public static void indexClass(Instances instance){

		if (instance.classIndex() == -1)
		  instance.setClassIndex(instance.numAttributes() - 1);
	}
        
        /**
        * Filter which allow to know the probability for each each cluster an instance gets assigned 
        * @param opFilter opciones del Filtro instances instancias a las que aplicar el filtro
        * 
            * "-W weka.clusterers.EM/MakeDensityBasedClusterer -- aditional options"
            * addtional options
            * -N numero de clusters
            * -I iterations
            * ...
        * 
        * @param inputInstances
        * @return
        * @throws Exception 
        */
        
        public static Instances clusterMemberFilter(String opFilter, Instances inputInstances) throws Exception {
		
		ClusterMembership clusterMember = new ClusterMembership();
		
		String[] optionsFilter = Utils.splitOptions(opFilter);
		
		
		clusterMember .setOptions(optionsFilter);
		clusterMember .setInputFormat(inputInstances);
		
		Instances instancesClusterFilter= Filter.useFilter(inputInstances, clusterMember );
		return instancesClusterFilter;
        }
        
        /**
           * Expects the first parameter to point to the directory with the text files.
           * In that directory, each sub-directory represents a class and the text
           * files in these sub-directories will be labeled as such.
           *
           * @param File       the directory or the file to convert
           * @throws Exception  if something goes wrong
           */
          public static void dataArffDirectory(String directory) throws Exception {
                       // convert the directory into a dataset
                       TextDirectoryLoader loader = new TextDirectoryLoader();
                       loader.setDirectory(new File(directory));
                       Instances dataRaw = loader.getDataSet();
                       //System.out.println("\n\nImported data:\n\n" + dataRaw);

                       // apply the StringToWordVector
                       // (see the source code of setOptions(String[]) method of the filter
                       // if you want to know which command-line option corresponds to which
                       // bean property)
                       StringToWordVector filter = new StringToWordVector();
                       filter.setInputFormat(dataRaw);
                       Instances dataFiltered = Filter.useFilter(dataRaw, filter);
                       //System.out.println("\n\nFiltered data:\n\n" + dataFiltered);
          }    
          
           /**
         * Cluster using the EM algorithm
         * @param inFile
         * @param outFile
         * @throws Exception 
         */
        public static void clusterEMFileResult (String inFile, String outFile) throws Exception {
            
                        DataSource source = new DataSource(inFile);
                        Instances data = source.getDataSet();             

                        //clusterizamos y guardamos los resultados
                        Instances instancesFilter = ClusterFilter.clusterFilterInstances("-W weka.clusterers.EM ", "-I 100 -N -1 -M 1.0E-6 -S 100", data);
                        ClusterFilter.newArffFile(instancesFilter, outFile);

        }
           
}
