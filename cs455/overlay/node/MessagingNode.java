/**file: MessagingNode.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/
package cs455.overlay.node;

import cs455.overlay.wireformats.*;
import cs455.overlay.transport.*;
import cs455.overlay.connection.*;
import java.io.*;
import java.util.*;
import java.net.*;


public class MessagingNode implements Node{
    // Personal info
    private int port;
    private int serverPort;
    private String IPAddr;

    // Registry info
    private int registryPort; // its serversocket
    private String registryIPAddr; // its serversocket
    private Connection registryConnection; // for regular message-passing

    // Networking services
    private cs455.overlay.transport.TCPServerThread serverThread;
    private HashMap<String,Connection> incomingConnections; //TODO phase-out according to notes
    
    // Peer messaging nodes
    private LinkedHashMap<String,Connection> messagingNodes;
    private String[] nodeList; //neighors provided by Registry

    // Dijkstra's
    // TODO ...
    //HashMap<String,Connection> messagingNodes;

    // Tracking info
    private int sendTracker;
    private int receiveTracker;
    private int relayTracker;
    private long sendSummation;
    private long receiveSummation;

    public MessagingNode(String registryIP, int registryPort) throws IOException{
        this.registryIPAddr = registryIP;
        this.registryPort = registryPort;
        this.serverPort = -1; //sentinel used by setServerPort()
        initializeTrackingInfo();

        // Data structure for peer messaging nodes
        incomingConnections = new HashMap<String,Connection>();
        messagingNodes = new LinkedHashMap<String,Connection>();  
        
        serverThread = new TCPServerThread(this);
        serverThread.start();
        
        System.out.println("my serverPort is: "+this.serverPort); //TODO debug
        
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
        System.out.println(s+" is my socket");
        // Create registry connection as a local abstraction
        this.registryConnection = new Connection(s,(this),this.serverPort);
        // Send registration request
        Event registryRequest = new Register((this.registryConnection).getLocalIP(),
                            (this.registryConnection).getLocalPort(),
                            (this.registryConnection).getLocalServerPort());
        System.out.println("made registry request event");//TODO debug
        (this.registryConnection).sendData(registryRequest.getBytes());
        System.out.println("sent registry request event");//TODO debug
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
                try{
                    RegisterResponse response = new RegisterResponse(e.getBytes());
                    byte status = response.getStatus();
                    if(status == 0){
                        System.out.println("Now registered at Registry. Response: "+response.getInfo());
                    }
                    else{ //TODO perhaps System.exit(0) upon failure to register, or send additional requests
                        System.out.println("Unable to register at Registry. Response: "+response.getInfo());
                    }
                } catch(Exception er){ er.printStackTrace(); }
                break;
            }
            case Protocol.MESSAGING_NODES_LIST: {
                try{
                    MessagingNodesList contents = new MessagingNodesList(e.getBytes());
                    
                    /*byte status = response.getStatus();
                    if(status == 0){
                        System.out.println("Now registered at Registry. Response: "+response.getInfo());
                    }
                    else{ //TODO perhaps System.exit(0) upon failure to register, or send additional requests
                        System.out.println("Unable to register at Registry. Response: "+response.getInfo());
                    }*/
                } catch(Exception er){ er.printStackTrace(); }
                
                break;
            }
            default: {
                System.out.println("Unable to handle event type: "+eventType);
            }
        }
    }

    public synchronized void registerConnection(Connection c){
        System.out.println("registerConnection on "+c.getID()+" in MessagingNode");
        
        if(!(incomingConnections.containsKey(incomingConnections.containsKey(c.getID())))){
            incomingConnections.put(c.getID(),c);
        }
        else{ //TODO change error handling, if any should be used
            //throw new IOException("Cannot register duplicate connection: "+c.getID());
        }
    }
    public synchronized void deregisterConnection(Connection c){
        System.out.println("deregisterConnection unimplemented in MessagingNode");
    }
    
    // This may be bad. This may be thread-unsafe. I may need to rethink my life ...
    // Shrideep, forgive me for my programming sins.
    // Allows TCPServerThread to set this node's serverPort;
    public /*synchronized*/ void setServerPort(int p){
        // can only be changed *once* from its sentinel value
        // this is set in the 'this constructor', so the integrity of this
        // value is guarded.
        if(this.serverPort == -1){ 
            this.serverPort = p;
        }
    }

    /** -------- main -------------------------------------*/
    public static void main(String[] args){
        System.out.println("Messaging node main()");
        try{
            MessagingNode m = new MessagingNode("dover",5000);
        }catch(Exception e){ System.out.println(e.getMessage()); e.printStackTrace(); }
    }
}
