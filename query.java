/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kknn;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author jagonzalez
 */
public class query {

    ArrayList<sample> dset = new ArrayList<>();     // The query samples
    ArrayList<Double> sMinVal = new ArrayList<>();  // The minimum value for each attribute
    ArrayList<Double> sMaxVal = new ArrayList<>();  // The maximum value for each attribute
    ArrayList<String> sNames = new ArrayList();    // The names of the attributes
    ArrayList<compVec> sDist = new ArrayList<>();   // Array to store a list of distances to the neighbors of each query sample
    //String testClasses = "";
    String fpath;           // The path of the query file
    String fname;           // The name of the query file
    String fdname;          // The path + name of the query file
    int qSamples = 0;       // The number of query samples
    int qAttributes = 0;    // The number of query attributes
    int distance = 3;       // The distance measure to be used for kNN
                            // 0 - Euclidean distance
                            // 1 - Linear kernel distance
                            // 2 - Polynomial kernel
                            // 3 - Radial basis kernel

    // Constructor to receive the test data in an array list of samples structure
    public query(ArrayList<sample> QS, ArrayList<Double> vecMinVal, ArrayList<Double> vecMaxVal, ArrayList<String> vecNames) {
        dset = QS;
        sMinVal = vecMinVal;
        sMaxVal = vecMaxVal;
        sNames = vecNames;
        qSamples = QS.size();
        //System.out.println("Members of QS: " + qSamples);
        qAttributes = dset.get(0).sValues.size();
    }

    // Constructor to read the query data from a csv text file
    public query(String mypath, String myfname, ArrayList<Double> vecMinVal, ArrayList<Double> vecMaxVal, ArrayList<String> vecNames) {
        fpath = mypath;
        fname = myfname;
        fdname = mypath + "/" + fname;
        sMinVal = vecMinVal;
        sMaxVal = vecMaxVal;
        sNames = vecNames;
    }// End query Constructor
    
    
    // Sets the distance measure to be used
    // 0: Euclidean
    // 1: Linear Kernel
    // 2: Polynomial Kernel
    // 3: Radial basis function
    public void setDistance(int myDist)
    {
      if(myDist >= 0 && myDist < 3)
      {
        this.distance = myDist;
      }
    }

    // Read the query file from a csv text file
    public void ReadQueryFile() {
        try {
            // Open Input Data File
            FileInputStream fstream;
            fstream = new FileInputStream(fdname);
            DataInputStream fin;
            fin = new DataInputStream(fstream);
            BufferedReader fb;
            fb = new BufferedReader(new InputStreamReader(fin));
            String fLine;
            String[] tmpLine;
            String delimeter = ",";

            int nSamples = 0;
            int nAttributes = 0;

            // Read the attributes names, the first line of the file
            fLine = fb.readLine();
            tmpLine = fLine.split(delimeter);
            for (int i = 0; i < tmpLine.length - 1; i++) {
                sNames.add(tmpLine[i]);
                nAttributes++;
            }
            this.qAttributes = nAttributes;
            int l = 0;
            // Read the attributes values and the class of each sample
            while ((fLine = fb.readLine()) != null) {
                ArrayList<Double> myv = new ArrayList<Double>();
                nSamples++;
                tmpLine = fLine.split(delimeter);
                myv.clear();

                for (int i = 0; i < tmpLine.length - 1; i++) {
                    myv.add(Double.parseDouble(tmpLine[i]));
                }
                sample mysample = new sample();
                mysample.setSample(myv, tmpLine[tmpLine.length - 1]);
                l++;
                dset.add(mysample);
            }
            fin.close();
            this.qSamples = nSamples;
        } catch (IOException | NumberFormatException e) {
            System.err.println("Errorsito: " + e.getMessage());
        }
    }// End ReadQueryFile

    // Print the attributes of a sample
    public void printCValues(compVec mySample) {
        for (int i = 0; i < mySample.cValues.size(); i++) {
            System.out.print(mySample.cValues.get(i) + ", ");
        }
    }

    // Print the attributes and the class of the samples of the query set
    public void printQuery() {
        for (int i = 0; i < this.dset.size(); i++) {
            for (int j = 0; j < this.dset.get(i).sValues.size(); j++) {
                System.out.print(this.dset.get(i).sValues.get(j) + ",");
            }
            System.out.println(this.dset.get(i).sClass);
            System.out.println("");
        }
    }// End printQuery

    // Normalize the values of the attributes of each sample
    public void normalize() {
        double tmpVal = 0.0;
        int mySize = 0;
        ArrayList<Double> myv = new ArrayList<>();

        // Normalize Values
        for (int i = 0; i < this.dset.size(); i++) {
            mySize = this.dset.get(i).sValues.size();
            myv = this.dset.get(i).sValues;
            System.out.println("mySize: " + mySize);
            for (int j = 0; j < mySize; j++) {
                System.out.println("j: " + j);
                System.out.println("myv.get(j): " + myv.get(j));
                System.out.println("MinVal: " + this.sMinVal.get(j));
                System.out.println("MaxVal: " + this.sMaxVal.get(j));
                tmpVal = (myv.get(j) - this.sMinVal.get(j)) / (this.sMaxVal.get(j) - this.sMinVal.get(j));
                myv.set(j, tmpVal);
                this.dset.get(i).sValues.set(j, tmpVal);
            }
        }
    } // End Normalize

    
    // Computes the distance from sample x to sample y using "distance"
    public double computeDistance(sample x, sample y, int distance)
    {
        double myDist = 0.0;    // The distance value for the Euclidean distance
        double xx = 0.0;        // The xx term for linear and polynomial kernel
        double yy = 0.0;        // The yy term for linear and polynomial kernel
        double xy = 0.0;        // The xy term for linear and polynomial kernel
        double xMy = 0.0;       // The xMy term for Radial Basis Function distance
        double xMx = 0.0;       // The xMx term for Radial Basis Function distance
        double yMy = 0.0;       // The yMy term for Radial Basis Function distance
        double sigma = 0.90;
        // Distance id:
        // 0: Euclidean distancce
        // 1: Polinomial kernel distance, squared dot product
        for (int attribute = 0; attribute < this.qAttributes; attribute++)
            {
                switch(distance)
                {
                    // Euclidean distance
                    case 0:
                            myDist = myDist + Math.pow(x.sValues.get(attribute) - y.sValues.get(attribute), 2);
                            break;
                    // Polinomial and linear kernel distance, dot products   
                    case 1:
                    case 2:
                            xy = xy + x.sValues.get(attribute) * y.sValues.get(attribute);
                            yy = yy + y.sValues.get(attribute) * y.sValues.get(attribute);
                            xx = xx + x.sValues.get(attribute) * x.sValues.get(attribute);
                           break;
                    // Term for the RBF kernel
                    case 3:
                            xMy = xMy + Math.abs(x.sValues.get(attribute) - y.sValues.get(attribute));
                            break;
                           
                }
            }
        
        switch(distance)
        {
            case 0:  // Euclidean
                myDist = Math.sqrt(myDist);
                break;
            case 1:  // Not anymore the Linear Kernel
                myDist = Math.pow((1 + xx),3) - 2*Math.pow((1 + xy), 3) + Math.pow((1 + yy), 3);
                break;
            case 2:  // Polinomial Kernel
                //myDist = Math.sqrt(Math.pow((1 + xx),3) - 2*Math.pow((1 + xy), 3) + Math.pow((1 + yy), 3));
                myDist = Math.pow(1 + Math.sqrt(xx -2*xy + yy),13);
                break;
            case 3: // Radial Basis Function Kernel
                myDist = 2-2*Math.exp(-(Math.pow(xMy,2)/Math.pow(sigma,2))) ;
        }
        return myDist;
    }
    
    
    // Here we can change the distance to be used to calculate the nearest neigbours
    // trainSet is the trainingset and dSamples is just the number of samples in trainSet
    public void getDistances(ArrayList<sample> trainSet, int dSamples) {
        // For each instance in the test set
        for (int i = 0; i < this.qSamples; i++)
        {
            compVec mycomp = new compVec();
            // For each instance in the training set
            for (int k = 0; k < dSamples; k++)
            {
                neighbour myNeig = new neighbour(); // Create a neighbor for this training sample
                myNeig.number = k;  // Assign a number to the neighbor
                myNeig.dist = computeDistance(this.dset.get(i), trainSet.get(k), distance); // Distance from querysample to this neighbor
                //System.out.println("Distance: " + distance);
                //myNeig.dist = Math.exp(-(myDist/variance));   // Radial basis kernel
                myNeig.sclass = trainSet.get(k).sClass; // Class of the neighbor, from training set
                mycomp.cValues.add(myNeig); // Adding this neighbor to mycomp, which is the compVec
            }
            
            // Store in sDist a vector of the distances from the current sample to each neighbor
            // in the trainingset
            // Now we sort the neighbors of this query sample by its distance to the query sample
            Collections.sort(mycomp.cValues, new neighbour.sorter());
            // We add the sorted neighbors
            this.sDist.add(mycomp);
        }
    }

    /*public void printClosest(int i)
     {
     System.out.println("Closest: ");
     for(int k = 0; k < 10; k++)
     {
     System.out.println(k + ": " + this.sDist.get(i).ks[k]);
     }
     }*/
    // Prints the distances to the instances, for debugging purposes
    public void printDistances(ArrayList<sample> trainSet) {
        // For each instance of the Test Set
        for (int i = 0; i < this.sDist.size(); i++) {
            // For each distance of a test instance with a training set instance
            for (int j = 0; j < trainSet.size(); j++) {
                System.out.println("Distance: " + this.sDist.get(i).cValues.get(j).dist + "," + "k: " + this.sDist.get(i).cValues.get(j).number + " Clase: " + this.sDist.get(i).cValues.get(j).sclass);
            }
            System.out.println("Clase: " + this.sDist.get(i).cClass);
            //this.printClosest(i);
            System.out.println("");
        }
    }// End printDistances

    // Perform the classification of the query samples
    public void classify(int k) {
        String qClass = "";

        for (int i = 0; i < qSamples; i++) {
            qClass = assignClass(this.sDist.get(i), k);
            this.dset.get(i).setPredClass(qClass);
            if (this.dset.get(i).pClass.equals(this.dset.get(i).sClass)) {
                this.dset.get(i).error = false;
            }
        }

    }

    // Assigns the class of one instance
    public String assignClass(compVec mycv, int nNeighbours) {
        // mycv contains the distances to the k nearest neighbours
        // nNeighbours are the number of neighbours that will be considered to assign the class
        String mys = "";
        String word = "";
        String maxClass = "";
        int maxIndex = 0;
        int myCounter = 0;
        int classIndex = 0;

        ArrayList<String> preWord = new ArrayList<String>();
        ArrayList<classAssignment> wordList = new ArrayList<classAssignment>();
        //classAssignment tmpWList = new classAssignment();

        // Create the string containing the samples classes
        // Only for the first k=10 neighbors, to save processor time
        for (int i = 0; i < 10 /*mycv.cValues.size()*/; i++) {
            mys = mys + mycv.cValues.get(i).sclass;
            if (i < 10 /*mycv.cValues.size()*/) {
                mys = mys + " ";
            }
        }

        // Create a string array containing separated words for the classes
        // of the first 10 neighbors of the query sample with NO REPEATS
        StringTokenizer token = new StringTokenizer(mys);
        for (int i = 0; i < nNeighbours; i++) {
            word = token.nextToken();
            if (!preWord.contains(word)) {
                preWord.add(word);
            }
        }

        // Adding all the classes to wordList
        // Prepares the classes names and counters of the classes
        // Classes counters set to 0
        // All possible classes stored in wordList
        for (int i = 0; i < preWord.size(); i++) {
            classAssignment newAssg = new classAssignment();
            newAssg.counter = 0;
            newAssg.word = preWord.get(i);
            wordList.add(newAssg);
        }

        // Sum number of class assignments for the number of neighbours
        // For each class and each neighbour
        for (int i = 0; i < nNeighbours; i++) {
            // For each class from the available classes for the k neighbours
            for (int j = 0; j < wordList.size(); j++) {
                if (wordList.get(j).word.equals(mycv.cValues.get(i).sclass)) {
                    wordList.get(j).counter++;
                }
            }
        }

        // Get the class with more votes counting the k nearest neighbours
        maxClass = wordList.get(0).word;
        maxIndex = 0;
        for (int i = 0; i < wordList.size(); i++) {
            // Switching classes
            if (wordList.get(i).counter > maxIndex) {
                maxClass = wordList.get(i).word;
                maxIndex = wordList.get(i).counter;
                classIndex = i;
            }
        }
        String newMaxClass = new String(maxClass);
        // Returns the class with more votes for the query instance
        return wordList.get(classIndex).word;
    }
}
