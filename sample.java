/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kknn;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jagonzalez
 * Structure to store a sample data, an example
 */

// The sample class
public class sample
{
  ArrayList<Double> sValues = new ArrayList<Double>(); // The attribute values as "Double"
  String sClass = ""; // The sample class
  String pClass = ""; // The predicted class
  boolean error = true;  // Boolean to describe if this was an error or not

  // Set the values of a sample
  public void setSample(ArrayList<Double> myv, String myc)
  {
      sValues = myv;
      sClass = myc;
  }
  
  // Set the value of the predicted class
  public void setPredClass(String mypc)
  {
      pClass = mypc;
  }
  
  // Print the sample values
  public void printSample(ArrayList<Double> myv)
  {
    for(int i = 0; i < myv.size(); i++)
    {
      System.out.print(this.sValues.get(i) + ",");
    }
    System.out.println(this.sClass);
  }
}
