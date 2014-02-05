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
    // Receiving host we're listen on behalf of
    private cs455.overlay.node.Node node;

    // Our main socket
    private ServerSocket serve;

    public TCPServerThread(cs455.overlay.node.Node n) throws IOException{
        this.node = n;
        serve = new ServerSocket(n.getPort());
    }

    public String toString(){
        return "TCPServerThread class";
    }

    public void run(){
        System.out.println(this); //TODO remove after debugging
        while(true){
            try{
                Socket s = serve.accept();
                new Connection(s, this.node);
            }
            catch(IOException e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
