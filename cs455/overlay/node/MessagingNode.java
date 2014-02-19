/**file: MessagingNode.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/
package cs455.overlay.node;

import cs455.overlay.util.*;
import cs455.overlay.wireformats.*;
import cs455.overlay.transport.*;
import cs455.overlay.connection.*;
import cs455.overlay.dijkstra.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;


public class MessagingNode implements Node{
    // Personal info
    private int port;//TODO phase-out according to notes
    private int serverPort;
    private String IPAddr;
    private String ID; // IPAddr:serverPort

    // Registry info
    private int registryPort; // its serversocket
    private String registryIPAddr; // its serversocket
    private Connection registryConnection; // for regular message-passing

    // Networking services
    private cs455.overlay.transport.TCPServerThread serverThread;
    private HashMap<String,Connection> incomingConnections; //TODO phase-out according to notes
    
    // Peer messaging nodes
    private LinkedHashMap<String,Connection> messagingNodes;
    private String[] nodeList; //peer IDs of neighbors
    private Edge[] edgeList; //contains nodeIDs and weight for each edge in entire
                            // overlay graph

    // Dijkstra's
    private Dijkstra dijk;

    // Tracking info
    private AtomicInteger sendTracker;
    private AtomicInteger receiveTracker;
    private AtomicInteger relayTracker;
    private AtomicLong sendSummation;
    private AtomicLong receiveSummation;

    public MessagingNode(String registryIP, int registryPort) throws IOException{
        this.registryIPAddr = registryIP;
        this.registryPort = registryPort;
        this.serverPort = -1; //sentinel used by setServerPort()
        initializeTrackingInfo();

        // Data structure for peer messaging nodes
        incomingConnections = new HashMap<String,Connection>();
        messagingNodes = new LinkedHashMap<String,Connection>();  
        
        serverThread = new TCPServerThread(this);
        serverThread.start(); //TODO move out of constructor
        
        System.out.println("my serverPort is: "+this.serverPort); //TODO debug

        //TODO this.IPaddr set here, perhaps move to TCPServerThread 
        initializeRegistryConnection(); //TODO move out of constructor

        this.ID = SocketID.socketID(this.IPAddr,this.serverPort);
        
        dijk = null; // initialized by Link_Weight message in onEvent()
    }
    
    public int getPort(){ 
        return this.port;
    }
    
    private void initializeTrackingInfo(){
        this.sendTracker = new AtomicInteger();
        this.receiveTracker = new AtomicInteger();
        this.relayTracker = new AtomicInteger();
        this.sendSummation = new AtomicLong();
        this.receiveSummation = new AtomicLong();
    }
    
    // Sets up connection from this node to the registry node, sends registration requests
    private void initializeRegistryConnection() throws IOException {
        // Create socket connection to Registry
        Socket s = new Socket(this.registryIPAddr,this.registryPort);
        System.out.println(s+" is my socket");
        // Create registry connection as a local abstraction
        this.registryConnection = new Connection(s,(this),this.serverPort);
        this.IPAddr = this.registryConnection.getLocalIP();
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
                    this.nodeList = contents.getInfoList();
                    connectToNeighbors(); //Connect to neighbors
                } catch(Exception er){ er.printStackTrace(); }
                break;
            }
            case Protocol.Link_Weights: {
                try{
                    /*MessagingNodesList contents = new MessagingNodesList(e.getBytes());
                    this.nodeList = contents.getInfoList();
                    connectToNeighbors(); //Connect to neighbors*/
                    LinkWeights contents = new LinkWeights(e.getBytes());
                    this.edgeList = contents.getLinkWeights();
                    //TODO debug
                    /*System.out.println("My edge list:");
                    for(int i=0;i<this.edgeList.length;i++){
                        System.out.println(edgeList[i]);
                    }*/
                    // setup Dijkstra and pre-compute all shortest paths
                    dijk = new Dijkstra((edgeList.length/nodeList.length), //N
                            nodeList.length, // K
                            this.edgeList, // edges
                            this.ID); // source
                    dijk.initialize();
                    dijk.execute();
                    dijk.printAllShortestPathsFancy();
                    
                } catch(Exception er){ er.printStackTrace(); }
                break;
            }
            case Protocol.TASK_INITIATE: {
                try{
                     RoundThread t = new RoundThread(registryConnection,
                                    incomingConnections,dijk,sendTracker,
                                    sendSummation);
                     t.start();
                    
                } catch(Exception er){ er.printStackTrace(); }
                break;
            }
            case Protocol.MESSAGE_PAYLOAD: {
                try{
                    Message message = new Message(e.getBytes());
                    //String[] route = message.getRoute();
                    if((this.ID).equals(message.getDest())){
                        this.receiveTracker.incrementAndGet();
                        this.receiveSummation.incrementAndGet();
                    }
                    else{
                        //forward to next node
                        String[] route = message.getRoute();
                        String nextID = null;
                        for(int i=0;i<route.length;i++){
                            if(route[i].equals(this.ID)){
                                nextID = route[i+1];
                            }
                        }
                        incomingConnections.get(nextID).sendData(e.getBytes());
                        relayTracker.incrementAndGet();
                    }
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
    
    // Allows TCPServerThread to set this node's serverPort;
    public void setServerPort(int p){
        if(this.serverPort == -1){ 
            this.serverPort = p;
        }
    }

    /** -------- overlay ----------------------------------*/
    // Register Connections to neighbors in infoList 
    // Scheme is to only send requests to peers with an ID of lesser lexographic
    // value, so that I avoid duplicate or self connections
    private void connectToNeighbors(){
        for(int i=0;i<nodeList.length;i++){
            //if((this.ID).compareTo(nodeList[i]) > 0){ //JUST FOR GIGGLES -- PERHAPS KEEP THIS COMMENT
                 try{
                     System.out.println("Connecting to peer: "+SocketID.getIP(nodeList[i])
                         +" "+SocketID.getPort(nodeList[i]));
                     Socket s = new Socket(SocketID.getIP(nodeList[i]),SocketID.getPort(nodeList[i]));                     
                     System.out.println(s+" is my socket");
                     new Connection(s,(this),this.serverPort);
                 }catch(IOException uhOh){
                     System.out.println(uhOh.getMessage());
                     uhOh.printStackTrace();
                 }
            //}
        }
    }
    
    /** -------- rounds -----------------------------------*/
    

    /** -------- main -------------------------------------*/
    public static void main(String[] args){
        System.out.println("Messaging node main()");
        try{
            MessagingNode m = new MessagingNode("pikes",5000);
        }catch(Exception e){ System.out.println(e.getMessage()); e.printStackTrace(); }
    }
}
