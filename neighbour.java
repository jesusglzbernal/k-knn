/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kknn;

import java.util.Comparator;

/**
 *
 * @author jagonzalez
 * Class to compare a query instance with its neighbors, a comparator
 */
class neighbour implements Comparable<neighbour>
{
int number; // Number of neighbor
double dist; // Distance to this neighbor
String sclass;  // Class of this neighbor


    // Constructor, sets the number, distance, and class to 0, 0.0 and ""
    public neighbour ()
    {
        this.number = 0;
        this.dist = 0.0;
        this.sclass = "";
    }
    
    // Constructor, stores the number, distance and class
    public neighbour(int mn, double md, String mc)
    {
        this.number = mn;
        this.dist = md;
        this.sclass = mc;
    }
    
    // Returns the distance of this neighbor
    public double getDistance()
    {
        return this.dist;
    }

    @Override
    public int compareTo(neighbour o)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    // Compares 2 instances, returns the result of the comparison
    public static class sorter implements Comparator<neighbour>
    {
       @Override
       public int compare(neighbour o1, neighbour o2)
      {
        return o1.dist > o2.dist ? 1 : (o1.dist < o2.dist ? -1 : 0);
      }
    }
}
