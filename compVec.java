/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kknn;

import java.util.ArrayList;

/**
 *
 * @author jagonzalez
 * - Values of the distances of a query sample to the neighbors in the training set
 *     - cValues, distance to this neighbor
 *     - cClass, class of this neighbor
 */
public class compVec {

  ArrayList<neighbour> cValues = new ArrayList<>();
  String cClass = "";

  /*public void setSample(ArrayList<neighbour> myv, String myc)
  {
      cValues = myv;
      cClass = myc;
  }*/
}
