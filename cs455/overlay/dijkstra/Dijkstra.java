/**file: Dijkstra.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/

package cs455.overlay.dijkstra;

import java.util.*;
import cs455.overlay.connection.*;

public class Dijkstra {
    //Graph information of k-regular graph
    final int infin = Integer.MAX_VALUE;
    int N; // number of nodes
    int K; // degree of each node
    String source;
    Edge[] edges; //raw form of graph
    //String[] nodeID;
    /*HashMap<String,int> minDist; // node ID, shortest distance from source
    HashMap<String,String> prev; // <nodeA,nodeB> means nodeB precedes nodeA on 
     */                            // shortest path.
    HashMap<String,Vertex> vertices; // vertex set
    HashMap<String,ArrayList<Edge>> graph; //adjacency list
    PriorityQueue<Vertex> queue;

    //Shortest paths
    private Dijkstra(){} // Ban default construction
    
    public Dijkstra(int _n, int _k, Edge[] _edges, String _source){
        System.out.println("Dijkstra on:"+_n+" "+_k+" "+_source);
        N = _n;
        K = _k;
        edges = _edges;
        source = _source;
        vertices = new HashMap<String,Vertex>();
        graph = new HashMap<String,ArrayList<Edge>>(N);
        
        initialize();
        execute();
    }

    public String toString(){
        return "Dijkstra class";
    }

    private void initialize(){
        for(int i=0;i<N;i++){
            vertices.put(edges[i*K].getSrc(),new Vertex(edges[i*K].getSrc()));
            vertices.get(edges[i*K].getSrc()).setMinDist(infin);
            graph.put(edges[i*K].getSrc(),new ArrayList<Edge>(K));
            for(int j=0;j<K;j++){
                graph.get(edges[i*K].getSrc()).add(edges[i*K+j]);
            }
        }
        vertices.get(this.source).setMinDist(0);
        queue = new PriorityQueue<Vertex>(vertices.values());
    }
 
    private void execute(){
    }
    // function dist_between

}
