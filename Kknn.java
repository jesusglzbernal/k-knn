/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kknn;

import java.util.ArrayList;

/**
 *
 * @author Jesus A. Gonzalez
 * Main Class of the k Nearest Neighbors Algorithm implementation
 * In this implementation we require the following steps:
 *     1) Read a dataset using the "dataSet" class, this includes
 *        - Reading the data file
 *        - Setting the minimum and maximum values for each attribute
 *          in order to normalize
 *        - Normalize the dataset
 *     2) Perform a k-Fold cross validation using the kFCV class for this we:
 *        - Set the number of neighbors to use
 */
public class Kknn {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        //dataSet dS = new dataSet("/Users/jagonzalez/Documents/R/Telas/Firmas/RTI17", "RTI-17.csv");
        //dataSet dS = new dataSet("/Users/jagonzalez/Documents/R/Telas/Firmas/RTI14", "RTI14-0.csv");
        //dataSet dS = new dataSet("/Users/jagonzalez/Documents/R/Telas/Firmas/RTI14", "RTI14-1.csv");
        //dataSet dS = new dataSet("/Users/jagonzalez/Documents/R/Telas/Firmas/RTI16", "RTI16-4040-0.csv");
        //dataSet dS = new dataSet("/Users/jagonzalez/Documents/R/Telas/Firmas/RTI16", "RTI16-4040-1.csv");
        //dataSet dS = new dataSet("/Users/jagonzalez/Documents/R/Telas/Firmas/RTI16", "RTI16-4540-0.csv");
        //dataSet dS = new dataSet("/Users/jagonzalez/Documents/R/Telas/Firmas/RTI16", "RTI16-4540-1.csv");
        //dataSet dS = new dataSet("/Users/jagonzalez/Documents/R/Telas/Firmas/RTI17", "RTI17-0.csv");
        //dataSet dS = new dataSet("/Users/jagonzalez/Documents/R/Telas/Firmas/RTI17", "RTI17-1.csv");
        
        // Set the path of the input file and the file name
        //dataSet dS = new dataSet("/Users/jagonzalez/Documents/R/Telas/Firmas/RTI16", "RTI16-4040-0.csv");

        dataSet dS = new dataSet("/Users/jagonzalez/Documents/R/Telas/Firmas/RTI16", "ecoli.csv");
        //dataSet dS = new dataSet("/Users/jagonzalez/Documents/knn", "carTrain.csv");
        
        // Read the input file name
        dS.ReadInputFile();
        //dS.printData();
        
        // Find the minimum and maximum values for each attribute, preparing for normalization of data
        dS.setMinMax();
        // Normalizing the data
        dS.normalize();
        //dS.printData();
   
        /*
        query qS = new query("/Users/jagonzalez/Documents/R/Telas/Firmas/RTIA", "RTItest.csv", dS.sMinVal, dS.sMaxVal, dS.sNames);
        //qS = new query("/Users/jagonzalez/Documents/knn", "carTest.csv", dS.sMinVal, dS.sMaxVal, dS.sNames);
        qS.ReadQueryFile();
        qS.normalize();
        
        //qS.printQuery();
        qS.getDistances(dS.dset, dS.dSamples);
        //qS.printDistances(dS.dset);
        qS.classify(5); */
        
        // Setting the values of the kFCV class for the k fold cross validation
        // 1: the dataset
        // 2: Number of folds in the kfcv
        // 3: Distance measure or kernel 0-Euclidean 1-Linear-Kernel 2-Polynomial-Kernel 3-RBF-Kernel
        // 4: Number of neighbors
        kFCV mkf = new kFCV(dS,10,3,3);
        
        // Set the value of "k" for the kNN algorithm
        mkf.setNumNeighbours(7);
        // Run the kFCV for kNN
        mkf.runKFCV();
    }
}
