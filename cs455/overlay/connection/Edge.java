/**file: Edge.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 
package cs455.overlay.connection;

/*
import cs455.overlay.transport.*;
import cs455.overlay.wireformats.*;
import cs455.overlay.node.*;
import cs455.overlay.util.*;
import java.io.*;
import java.util.*;
import java.net.*;
*/

/* Represents a directed edge between two messaging nodes together with 
   its positive link weight.
 */

public class Edge {
    private String source;
    private String dest;
    private int weight;

    public Edge(String src, String dst, int w){
        this.source = src;
        this.dest = dst;
        this.weight = w;
    }

    public Edge(String src, String dst){
        this(src,dst,-1);
    }
    
    // Takes "hostA:portA hostB:portB Weight" and turns it into a proper
    // edge
    public Edge(String compressed){
        //TODO implement
        this("a","b",1);
    }

    public String getSrc(){
        return this.source;
    }
    public String getDest(){
        return this.dest;
    }
    public int getWeight(){
        return this.weight;
    }
    public void setWeight(int w){
        if(getWeight()<0 && w >= 0) this.weight = w;
    }
    
    public boolean isWeightSet(){
        return (this.weight >= 0);
    }

    public String toString(){
        return new String(this.source+" "+this.dest+" "+this.weight);
    }
}
