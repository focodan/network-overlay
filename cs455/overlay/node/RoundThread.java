/**file: RoundThread.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 
package cs455.overlay.node;

import cs455.overlay.node.*;
import cs455.overlay.connection.*;
import cs455.overlay.dijkstra.*;
import cs455.overlay.wireformats.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundThread extends Thread {
    private Connection registryConnection;
    private HashMap<String,Connection> peerConnections;
    private Dijkstra dijk;
    private AtomicInteger sendTracker;
    private AtomicLong sendSummation;
    private String myID;
    
    public RoundThread(Connection registryConnection,HashMap<String,Connection> peerConnections,
            Dijkstra dijk,AtomicInteger sendTracker,AtomicLong sendSummation) throws IOException{
        this.registryConnection = registryConnection;
        this.peerConnections = peerConnections;
        this.dijk = dijk;
        this.sendTracker = sendTracker;
        this.sendSummation = sendSummation;

    }

    public String toString(){
        return "RoundThread class";
    }
    
    public void round(){
        Random rand = new Random();
        //int min = -2147483648;
        //int max = 2147483647;
        // get random and valid shortest path
        String[] path = dijk.getRandomPath();
        /*System.out.println("Connecting to neighbor "+path[0]);
        for( String k : peerConnections.keySet() ){
            System.out.print(k+" ");
        }*/
        //if(peerConnections.containsKey(path[0]))System.out.println("It has this connection");
        Connection neighbor = peerConnections.get(path[0]); //problem line
        if(neighbor==null)System.out.println("neighbor is broken, doesn't have: "+path[0]);
        /*System.out.println("Neighbor is: "+neighbor.getID());*/
        
        // make 5 random payloads
        // turn these into messages, send to appropriate neighbor
        for(int i=0;i<5;i++){
            int r = rand.nextInt(/*(max - min) + 1*/);// + min; //check to make sure negs are OK
            Message m = new Message(r,path);
            //System.out.println("Attempting to print: "+m);
            try{
                neighbor.sendData(m.getBytes());
            }catch(Exception e){ System.out.println(e.getMessage()); e.printStackTrace(); }
        }
    }

    public void run(){
        // perform 5,000 rounds
        for(int i=0;i<5000;i++){
            if(i%500==0){
                System.out.println("Beginning round: "+i);
            }
            round();
            try{
                Thread.sleep(10);
            }catch(Exception e){ e.printStackTrace(); }
        }
        //send TaskComplete to Registry
        TaskComplete message = new TaskComplete(
                (this.registryConnection).getLocalIP(),
                (this.registryConnection).getLocalServerPort()); //Perhaps change to LocalPort?
        try{
            (this.registryConnection).sendData(message.getBytes());
        }catch(Exception e){ e.printStackTrace(); }
    }
}
