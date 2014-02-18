/**file: Vertex.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 
package cs455.overlay.connection;

import java.io.*;
/*
import cs455.overlay.transport.*;
import cs455.overlay.wireformats.*;
import cs455.overlay.node.*;
import cs455.overlay.util.*;
import java.io.*;
import java.util.*;
import java.net.*;
*/

/* Represents a node for Dijkstra's algorithm.
 */

public class Vertex implements Comparable<Vertex> {
    private int minDist; // From source to this
    private String ID; // host:port
    String prev;
    
    private Vertex(){}
    
    public Vertex(String id){
        this.ID = id;
        this.minDist = 0;
        this.prev = "undefined";
    }
    
    public void setMinDist(int minDist){
        this.minDist = minDist;
    }
    public void setPrev(String prev){
        this.prev = prev;
    }
    public int getMinDist(){
        return minDist;
    }
    
    public int compareTo(Vertex other) {
        if( this.getMinDist() < other.getMinDist()){
            return -1;
        }
        else if( this.getMinDist() > other.getMinDist()){
            return 1;
        }
        else{
            return 0;
        }
    }

    public String toString(){
        return new String("Vertex class");
    }
}
