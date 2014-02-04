/**file: TCPServerThread.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 
package cs455.overlay.transport;

import cs455.overlay.node.*;
import cs455.overlay.connection.*;
import java.net.*; //TODO reduce from * to specific packages
import java.io.*; //TODO remove after debugging

public class TCPServerThread extends Thread {
    // Receiving host
    cs455.overlay.node.Node node;
    
    // Our main socket
    ServerSocket serve;

    public TCPServerThread(cs455.overlay.node.Node n) throws IOException{
        this.node = n;
        serve = new ServerSocket(n.getPort());
    }

    public String toString(){
        return "TCPServerThread class";
    }
    
    public void run(){
        System.out.println(this); //TODO remove after debugging
        while(true){ //QUESTION for TA: Will my receiver thread go out of scope?
                    // How do I manage these threads?
            try{ //TODO better error handling -- should I escape from loop or keep going though???
                Socket s = serve.accept();
                //TCPReceiverThread r = new TCPReceiverThread(s,this.node);
                //r.start();
                new Connection(s, this.node);
            }
            catch(Exception e){ } //TODO refine Exception type
        }
    }

}
