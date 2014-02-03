/**file: Registry.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 
package cs455.overlay.node;

import cs455.overlay.transport.*;
import cs455.overlay.wireformats.*;
import java.io.*; //TODO remove/reduce after debugging is finished
import java.util.*; //TODO remove/reduce after debugging is finished

public class Registry implements Node{
    // Registry's personal info
    private int port;
    
    // Registry's networking services
    private cs455.overlay.transport.TCPServerThread serverThread;
    private cs455.overlay.transport.TCPSender sender;

    // Data structures for managing messaging nodes
    // TODO fill-in
    
    public Registry(int port) throws IOException{
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
        int port;
        Scanner scan = new Scanner(System.in);
        /*
          do any setups and commandline parsing here
        */
        port = 5000;
        try{
            Registry reg = new Registry(port);
        } catch(Exception e){};
        
        System.out.println("Registry accepting commands ...");
        while(true){
            /*
              Allow commands to be given here
            */
            
            // Proof of concept for foreground process, thread independence ...
            System.out.println("enter command...");
            int response = scan.nextInt();
            System.out.println(response);
        }
        
    }

}
