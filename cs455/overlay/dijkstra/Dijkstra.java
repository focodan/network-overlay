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
        /*initialize();
        execute();*/
    }

    public String toString(){
        return "Dijkstra class";
    }

    public void initialize(){
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
 
    public void execute(){
        //TODO debug
        System.out.println("Executing Dijkstra");
        while(!queue.isEmpty()){ // this is a half-baked implementation
            Vertex u = queue.poll();
           
            ArrayList<Edge> neighbors = graph.get(u.getID());
            for(int i=0;i<neighbors.size();i++){
                // if its a neighbor not yet visited
                if(queue.contains(vertices.get( neighbors.get(i).getDest() ))){
                    Vertex v = vertices.get( neighbors.get(i).getDest() );
                    int alt = u.getMinDist() + neighbors.get(i).getWeight();
                    if(alt < v.getMinDist()){ //relax (u,v)
                       v.setMinDist(alt); // Maybe this does a reduce-key simply via reference?
                       v.setPrev(u.getID());
                       // Because Java doesn't have a *real* priority queue
                       // I have to remove and re-insert instead of reduce-key
                       queue.remove(v);
                       queue.add(v);
                   }
               }
           }
           
        }
    }
    
    //returns shortest path from src -> id, where
    // [src, v1, v2, ..., id] are ids of nodes
    public String[] getShortestPath(String id){
        ArrayList<String> path;
        String[] finalPath = null;
        Stack<String> pathStack = new Stack<String>();
        
        //TODO debug
        /*System.out.println("Generating shortest path from "+this.source+
            " to "+id);*/

        Vertex v = vertices.get(id);
        pathStack.push(v.getID());
        while(!v.getPrev().equals("undefined")){
            pathStack.push(v.getPrev());
            v = vertices.get(v.getPrev());
        }
        
        int len = pathStack.size();
        finalPath = new String[len];
        for(int i=0;i<len;i++){
            finalPath[i] = pathStack.pop();
            //System.out.print(" "+finalPath[i]); //TODO debug
        } //System.out.println(); //TODO debug

        return finalPath;
    }
    
    public String getFancyShortestPath(String id){ //TODO
        String[] path = getShortestPath(id);
        String finalPath = "";
        for(int i=0;i<path.length-1;i++){
            int w=-1; // weight of edge from path[i] to path[i+1]
            ArrayList<Edge> neighbors = graph.get(path[i]);
            for(int j=0;j<neighbors.size();j++){
                if(neighbors.get(j).getDest().equals(path[i+1])){
                    w = neighbors.get(j).getWeight();
                }
            }
            finalPath += (path[i]+"--"+w+"--");
        }
        finalPath += path[path.length-1]; //last node
        return finalPath;
    }
    
    public void printAllShortestPaths(){
        // for each vertex
        //    find its shortest path string array
        //    print the array
        for( String key : vertices.keySet() ){
            System.out.println("Shortest path for "+key);
            String[] p = getShortestPath(key);
            for(int i=0;i<p.length;i++){
                System.out.print(p[i]+" --> ");
            } System.out.println();
        }
    }
    public void printAllShortestPathsFancy(){
        // for each vertex
        //    find its shortest path string array
        //    print the array
        for( String key : vertices.keySet() ){
            System.out.println("Shortest path for "+key+":");
            System.out.println("\t"+getFancyShortestPath(key));
        }
    }

}
