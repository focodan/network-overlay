/**file: MessagingNode.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 
package cs455.overlay.node;

import cs455.overlay.wireformats.*;
import cs455.overlay.transport.*;
import java.io.*; //TODO remove/reduce after debugging

public class MessagingNode implements Node{
    // Personal info
    private int port;
    private String IPAddr;
    
    // Registry info
    private int registryPort;
    private String registryIPAddr; 
    
    // Networking services
    private cs455.overlay.transport.TCPServerThread serverThread;
    private cs455.overlay.transport.TCPSender sender;
    
    // Dijkstra's
    // TODO ...
    
    // Tracking info
    private int sendTracker;
    private int receiveTracker;
    private int relayTracker;
    private long sendSummation;
    private long receiveSummation;
    

    public void onEvent(Event e){
        System.out.println("onEvent unimplemented in MessagingNode");
    }
    public int getPort(){ 
        return this.port;
    }

    public String toString(){
        return "MessagingNode class";
    }

}
