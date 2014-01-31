/**file: Registry.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 
package cs455.overlay.node;

import cs455.overlay.transport.*;
import cs455.overlay.wireformats.*;
import java.io.*; //TODO remove after debugging is finished

public class Registry implements Node{
    // Registry's personal info
    int port;
    String IPAddr; // TODO maybe unneccessary
    
    // Registry's networking services
    cs455.overlay.transport.TCPServerThread serverThread; // thread to handle incoming Events from Messaging nodes
    cs455.overlay.transport.TCPSender sender; // either 

    // Data structures for managing messaging nodes
    
    
    public Registry(int port){
        this.port = port;
        serverThread = new TCPServerThread(this);
        serverThread.start();
    }
    
    public int getPort(){
        return this.port;
    }
    
    public void onEvent(Event e){
        //case switch
        System.out.println("onEvent unimplemented in Registry");
    }

    public String toString(){
        return "Registry class";
    }
    
    /**-------- MAIN ---------------------------------*/
    
    public static void main(String[] args){
        /*
          
        */
        int port = 5000;
        
        Registry reg = new Registry(port);
        
        
        
    }

}
