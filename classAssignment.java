/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kknn;

/**
 *
 * @author jagonzalez
 * - "classAssignment" is used as a structure to store the hits of each class at
 *   the time of performing classification.
 * - Used at "query" time
 */
public class classAssignment
{
    String word; // The class name
    int counter; // The counter or weight of this class in terms of the k neighbors
}
