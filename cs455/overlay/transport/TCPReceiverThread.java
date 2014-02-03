/**file: TCPReceiverThread.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 
package cs455.overlay.transport;

import cs455.overlay.node.*;
import cs455.overlay.wireformats.*;
import java.io.*; //TODO remove/reduce after debugging
import java.net.*; //TODO reduce after debugging

public class TCPReceiverThread extends Thread {

    //node info? ...
    cs455.overlay.node.Node node;
    Socket receive;
    
    public TCPReceiverThread(Socket recieve, Node n){
        
    }

    public String toString(){
        return "TCPReceiverThread class";
    }
    
    public void run(){
        System.out.println(this);
        /* 
         Listen for appropriate communications ... 
        */
        //JUST for testing ...
        (this.node).onEvent(new cs455.overlay.wireformats.Register("bob",5));
    }

}
