/**file: MessagingNode.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 
package cs455.overlay.node;

import cs455.overlay.wireformats.*;
import cs455.overlay.transport.*;
import cs455.overlay.connection.*;
import java.io.*; //TODO remove/reduce after debugging
import java.util.*; //TODO remove/reduce after debugging
import java.net.*; //TODO remove/reduce after debugging


public class MessagingNode implements Node{ //TODO remember to keep track of ServerSocket for Registry
    // Personal info
    private int port;
    private String IPAddr;
    
    // Registry info
    private int registryPort; // its serversocket
    private String registryIPAddr; // its serversocket
    private Connection registryConnection; // for regular message-passing
    //TODO special attribute for Connection to Registry
    
    // Networking services
    private cs455.overlay.transport.TCPServerThread serverThread;
    
    // Dijkstra's
    // TODO ...
    HashMap<String,Connection> messagingNodes;
    
    // Tracking info
    private int sendTracker;
    private int receiveTracker;
    private int relayTracker;
    private long sendSummation;
    private long receiveSummation;
    
    public MessagingNode(String registryIP, int registryPort) throws IOException{
        this.registryIPAddr = registryIP;
        this.registryPort = registryPort;
        
        initializeTrackingInfo();

        // Data structure for peer messaging nodes
        messagingNodes = new HashMap<String,Connection>();  
        
        serverThread = new TCPServerThread(this);
        serverThread.start();
        
        initializeRegistryConnection();
    }
    
    public int getPort(){ 
        return this.port;
    }
    
    private void initializeTrackingInfo(){
        this.sendTracker = 0;
        this.receiveTracker = 0;
        this.relayTracker = 0;
        this.sendSummation = 0;
        this.receiveSummation = 0;
    }
    
    // Sets up connection from this node to the registry node, sends registration requests
    private void initializeRegistryConnection() throws IOException {
        // Create socket connection to Registry
        Socket s = new Socket(this.registryIPAddr,this.registryPort);
        // Create registry connection as a local abstraction
        this.registryConnection = new Connection(s,this);
        // Send registration request
        Event registryRequest = new Register((this.registryConnection).getLocalIP(),
                            (this.registryConnection).getLocalPort());
        (this.registryConnection).sendData(registryRequest.getBytes());
                
    }

    public String toString(){
        return "MessagingNode class";
    }
    
    public synchronized void onEvent(Event e, String connectID){
        System.out.println("onEvent in MessagingNode receiving:"+e.getType()+" from: "+connectID);
        int eventType = e.getType();
        String ID;
        switch(eventType){
            // TODO write out appropriate cases
            case Protocol.REGISTER_RESPONSE: {

                //
                try{
                    RegisterResponse response = new RegisterResponse(e.getBytes());
                    byte status = response.getStatus();
                    if(status == 0){
                        System.out.println("Now registered at Registry. Response: "+response.getInfo());
                    }
                    else{
                        System.out.println("Unable to register at Registry. Response: "+response.getInfo());
                    }
                } catch(Exception er){ er.printStackTrace(); }
                /*if(response != null){
                    try{
                        incomingConnections.get(connectID).sendData(response.getBytes());
                    }catch(IOException ie){
                        ie.printStackTrace();
                    }
                }*/
                break;
            }
            default: /* TODO add error handling */ break;
        }
    }

    public synchronized void registerConnection(Connection c){
        System.out.println("registerConnection unimplemented in MessagingNode");
    }
    public synchronized void deregisterConnection(Connection c){
        System.out.println("deregisterConnection unimplemented in MessagingNode");
    }

    /** -------- main -------------------------------------*/
    public static void main(String[] args){
        System.out.println("Messaging node main()");
        try{
            MessagingNode m = new MessagingNode("sacramento",5000);
        }catch(Exception e){}
    }
}
