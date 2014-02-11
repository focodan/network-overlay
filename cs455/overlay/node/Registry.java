/**file: Registry.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 
package cs455.overlay.node;

import cs455.overlay.transport.*;
import cs455.overlay.wireformats.*;
import cs455.overlay.connection.*;
import java.io.*; //TODO remove/reduce after debugging is finished
import java.util.*; //TODO remove/reduce after debugging is finished

public class Registry implements Node{
    // Registry's personal info
    private int port;
    
    // Registry's networking services
    private cs455.overlay.transport.TCPServerThread serverThread;
    //private cs455.overlay.transport.TCPSender sender; //handled in Connection-level 

    // Data structures for managing messaging nodes
    HashMap<String,Connection> incomingConnections;
    HashMap<String,Connection> messagingNodes;

    
    public Registry(int port) throws IOException{
        this.port = port;
        incomingConnections = new HashMap<String,Connection>();
        messagingNodes = new HashMap<String,Connection>();
        serverThread = new TCPServerThread(this);
        serverThread.start();
    }
    
    public int getPort(){
        return this.port;
    }

    public String toString(){
        return "Registry class";
    }
    
    public synchronized void onEvent(Event e){
        System.out.println("onEvent in MessagingNode receiving:"+e.getType());
        int eventType = e.getType();
        String ID;
        switch(eventType){
            case Protocol.REGISTER_REQUEST: {
                System.out.println("   event is a registry request");
                //TODO add error handling
                // move the connection associated with the e from incoming to messagingNodes
                try{
                Register event = new Register(e.getBytes());
                ID = Connection.makeID(event.getIPAddr(),event.getPort());
                if(incomingConnections.containsKey(ID)){}
                else{
                    System.out.println("Adding "+ID+" to Registry");
                    messagingNodes.put(ID,incomingConnections.get(ID));
                    incomingConnections.remove(ID);
                }
                }catch(Exception io){}
                break;
            }
            default: /* add error handling, possibly throw*/ break;
        }
    }

    public synchronized void registerConnection(Connection c){
        //System.out.println("registerConnection unimplemented in MessagingNode");
        System.out.println("registerConnection on "+c.getID()+" in Registry");
        //TODO decide how to handle duplicates and check for them
        incomingConnections.put(c.getID(),c);
    }
    public synchronized void deregisterConnection(Connection c){
        System.out.println("deregisterConnection unimplemented in MessagingNode");
    }
    
    /**-------- MAIN ---------------------------------*/
    
    public static void main(String[] args){
        int port;
        Scanner scan = new Scanner(System.in);
        /*
          do any setups and commandline parsing here
        */
        System.out.println("Registry main()");
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
