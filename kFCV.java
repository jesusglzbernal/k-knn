/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kknn;

import java.util.Date;
import java.util.Random;
import java.lang.Math;
import java.util.ArrayList;

/**
 *
 * @author jagonzalez
 * Class to perform the actual k-Fold Cross Validation
 */
public class kFCV 
{
  int numElem;
  int samples;
  int numFolds;
  int numNeighbours;
  int kernel=0;
  double avgError;
  dataSet tDataSet = new dataSet();
  ArrayList<fold> mFold = new ArrayList<fold>();
  ArrayList<Integer> mErrors = new ArrayList<Integer>();
  
  
  // kFCV Constructor
  public kFCV(dataSet myDs, int nFolds, int myKernel, int myNeighbors)
  {
      this.tDataSet = myDs;          // The training dataset is received by KFCV, myDs
      this.tDataSet.normalize();     // Normalize since we receive the dataset, not in the query object
      this.tDataSet.setMinMax();     // Compute min and max values for attributes since we receive the dataset
      this.samples = myDs.dSamples;
      this.numFolds = nFolds;
      this.numElem = getNumElem(this.samples, numFolds);
      if(myKernel >= 0 && myKernel <=3)
      {
          this.kernel = myKernel;
      }
      this.numNeighbours = myNeighbors;
      this.kernel = myKernel;
  }
  
  // Run the kFCV task and output result to console
  public void runKFCV()
  {
      System.out.println("Input File:    " + tDataSet.fname);
      System.out.println("Num. Elements: " + numElem);
      System.out.println("Num. Folds:    " + numFolds);
      System.out.println("Samples:       " + samples);
      
      fillFolds(this.tDataSet);
      kfClassify();
      
      System.out.println("Avg Error:    " + (avgError*100));
      System.out.println("Avg Accuracy: " + ((1 - avgError)*100));
  }
  
  // Compute the number of elements for each partition
  // Still need to put the remaining elements to each fold, less than k
  public int getNumElem(int size, int k)
  {
      int mynum;
      double mydouble;
      
      mydouble = Math.floor(size/k);
      mynum = (int)mydouble;
      return(mynum);
  }
  
  public void setNumNeighbours(int k)
  {
      this.numNeighbours = k;
  }
  
  // Randomly fill the folds to train and test
  public void fillFolds(dataSet myData)
  {
    Date date = new Date();
    Random Generator = new Random(date.getTime());
    ArrayList<Integer> generated = new ArrayList<Integer>();
    
    for(int i = 0; i < numFolds; i++)
    {
      fold newFold = new fold();
      for(int j = 0; j < numElem; j++)
      {
        Integer myNumber = Generator.nextInt(this.samples);
        while(generated.contains(myNumber))
        {
            myNumber = Generator.nextInt(this.samples);
        }
        generated.add(myNumber);
        sample newSample = new sample();
        newSample.sValues = myData.dset.get(myNumber).sValues;
        newSample.sClass = myData.dset.get(myNumber).sClass;
        newFold.dset.add(newSample);
      }
      mFold.add(newFold);
    }
  }
  
  
  // Creates a training set with k-1 partitions
  public ArrayList<sample> createTS(int myFold)
  {
      ArrayList<sample> myNewSet = new ArrayList<sample>();
      
      for(int i = 0; i < numFolds; i++)
      {
          if(i != myFold)
          {
              for(int j = 0; j < mFold.get(i).dset.size(); j++)
              {
                  sample myNewSample = new sample();
                  myNewSample.sValues = mFold.get(i).dset.get(j).sValues;
                  myNewSample.sClass = mFold.get(i).dset.get(j).sClass;
                  myNewSet.add(myNewSample);
              }
          }
      }
      return myNewSet;
  }
  
  
  // Create a test set with 1 partition
  public ArrayList<sample> createQS(int myFold)
  {
      ArrayList<sample> myNewSet = new ArrayList<sample>();
      int i = myFold;

      for(int j = 0; j < mFold.get(myFold).dset.size(); j++)
      {
          sample myNewSample = new sample();
          myNewSample.sValues = mFold.get(i).dset.get(j).sValues;
          myNewSample.sClass = mFold.get(i).dset.get(j).sClass;
          myNewSet.add(myNewSample);
      }
      return myNewSet;
  }
  
  // Perform the classification task for all the folds
  // Stores the result
  public void kfClassify()
  {
      for(int i = 0; i < numFolds; i++)
      {
          ArrayList<sample> myKFoldTS = new ArrayList<sample>();
          ArrayList<sample> myKFoldQS = new ArrayList<sample>();
          myKFoldQS = createQS(i);
          myKFoldTS = createTS(i);
          dataSet myTS = new dataSet(myKFoldTS);
          myTS.dSamples = myTS.dset.size();
          query myQS = new query(myKFoldQS, tDataSet.sMinVal, tDataSet.sMaxVal, tDataSet.sNames);
          myQS.setDistance(this.kernel);
          myQS.getDistances(myTS.dset, myTS.dSamples);
          myQS.classify(numNeighbours);
          countErrors(myQS, i);
      }
      setAverageError();
  }
  
  // Counts the errors in one fold
  public void countErrors(query qS, int myfold)
  {
         Integer numErr = 0;
         for(int j = 0; j < qS.dset.size(); j++)
         {
             if(qS.dset.get(j).error )
             {
                 numErr++;
             }
         }
         mErrors.add(numErr);
         System.out.println("Fold " + (myfold + 1) + " errors: " + numErr);
  }
  
  
  // Calculates the average error for all folds and stores the result in avgError
  public void setAverageError()
  {
      double numErr = 0;
      
      for(int i = 0; i < numFolds; i++)
      {
          numErr = numErr + mErrors.get(i);
      }
      avgError = numErr/samples;
  }
  
  
}
