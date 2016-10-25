package kknn;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author  Jesus A. Gonzalez
 */
public class dataSet {
    
    // Array list to load the data set without class
    // Sample contains attribute values (double) plus the class (string)
    ArrayList<sample> dset = new ArrayList<>();
    
    // Array lists of minimum and maximum values for each attribute
    ArrayList<Double> sMinVal = new ArrayList<>();
    ArrayList<Double> sMaxVal = new ArrayList<>();
    
    // Array list to load the class of each sample
    // ArrayList<String> sClass = new ArrayList();
    
    // Array list to load the attributes names
    ArrayList<String> sNames  = new ArrayList();
    
    String fpath;   // File path
    String fname;   // File name
    String fdname;  // File path + name
    int dSamples;   // Number of samples
    int dAttributes;// Number of attributes
    
    // Constructor, initializing file name to empty
    public dataSet()
    {
        fpath = "";
        fname = "";
        fdname = "";
    }
    
    // Constructor, initializing the training set to TS, without reading the file
    public dataSet(ArrayList<sample> TS)
    {
        fpath = "";
        fname = "";
        fdname = "";
        
        dset = TS;
    }
    
    // Constructor, initializing the file path and name
    public dataSet(String mypath, String myfname)
    {
      fpath = mypath;
      fname = myfname;
      fdname = mypath + "/" + fname;
    }
    
    // Function to read the input file in csv format
    public void ReadInputFile()
    {
        try
        {
          // Open Input Data File
          FileInputStream fstream;
          fstream = new FileInputStream(fdname);
          DataInputStream fin;
          fin = new DataInputStream(fstream);
          BufferedReader fb;
          fb = new BufferedReader(new InputStreamReader(fin));
          String fLine; // To get each line of the file
          String[] tmpLine; // To get each attribute value as a string token
          String delimeter = ","; // Set the delimiter to "," for the csv format

          // Initialize the number of samples and attributes to 0
          int nSamples = 0;
          int nAttributes = 0;
          
          // Read the attributes names, in the first line
          fLine = fb.readLine();
          tmpLine = fLine.split(delimeter);
          for(int i = 0; i < tmpLine.length - 1; i++)
          {
              sNames.add(tmpLine[i]); // Add another attribute name
              nAttributes++; // Increase the number of attributes
          }
          
          // Read the attributes values and the class of each sample
          while ((fLine = fb.readLine()) != null)
          {
              ArrayList<Double> myv = new ArrayList<Double>(); // List to store the attribute values
              nSamples++;
              tmpLine = fLine.split(delimeter);
              myv.clear();

              // Get a vector, myv, with the values of the attributes
              for(int i = 0; i < tmpLine.length - 1; i++)
              { 
                  myv.add(Double.parseDouble(tmpLine[i])); //Obtaining the attribute value pair
              }
              sample mysample = new sample(); // Create a new sample 
              mysample.setSample(myv,tmpLine[tmpLine.length - 1]); // myv has the attribute values
                                                                   // tmpLine[tmpLine.length -1] corresponds the class value/name
              dset.add(mysample); // Add the sample to the dataset "dset"
          }
          
          fin.close(); // Close the file
          this.dSamples = nSamples; // Assign the number of samples of the dataset
          this.dAttributes = nAttributes; // Assign the number of attributes of the dataset
        }
        catch (IOException | NumberFormatException e)
        {
          System.err.println("Errorsito: " + e.getMessage()); 
        }
    }
    
    // Print the dataset for testing
    public void printData()
    {
      for(int i = 0; i < this.dset.size(); i++)
      {
        // Print the attribute values of a sample
        for(int j = 0; j < this.dset.get(i).sValues.size(); j++)
        {
          System.out.print(this.dset.get(i).sValues.get(j) + ","); // Print an attribute/value pair
        }
        System.out.println(this.dset.get(i).sClass); // Print the class of the sample
        System.out.println("");
      }
    }
    
    
    // Set the minimum and maximum values of an attribute
    // Used during the normalization process
    public void setMinMax()
    {
      
      ArrayList<Double> myv = new ArrayList<>();
      int mySize = 0;
      
      // Initialize Min and Max Values for each attribute
      for(int k = 0; k < this.dAttributes; k++)
      {
          this.sMinVal.add(10000.00);
          this.sMaxVal.add(-10000.00);
      }
      
      // Set the Min and Max Values for each attribute
      for(int i = 0; i < this.dAttributes; i++)
      {
        for(int j = 0; j < this.dSamples; j++)
        {   
          myv = this.dset.get(j).sValues;
          if(myv.get(i) < this.sMinVal.get(i))
          {
              // Exchange Min...
              this.sMinVal.set(i, myv.get(i));
          }
          if(myv.get(i) > this.sMaxVal.get(i))
          {
              // Exchange Max...
              this.sMaxVal.set(i, myv.get(i));
          }
        }
      }
    }
    
    // Normalize the values of each attribute
    public void normalize()
    {
      double tmpVal = 0.0;
      int mySize = 0;
      int j = 0;
      ArrayList<Double> myv = new ArrayList<>();
      // Normalize Values
      for(int i = 0; i < this.dSamples;i++)
      {
        for(j = 0; j < this.dAttributes; j++)
        {
          myv = this.dset.get(i).sValues;
          tmpVal = (myv.get(j) - this.sMinVal.get(j))/(this.sMaxVal.get(j)-this.sMinVal.get(j));
          myv.set(j, tmpVal);
          this.dset.get(i).sValues.set(j, tmpVal);
        }
      }
    }
}
