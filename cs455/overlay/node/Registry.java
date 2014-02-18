/**file: Registry.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 
package cs455.overlay.node;

import cs455.overlay.transport.*;
import cs455.overlay.wireformats.*;
import cs455.overlay.connection.*;
import cs455.overlay.util.*;
import java.io.*;
import java.util.*;

public class Registry implements Node{
    // Registry's personal info
    private int port;
    private int serverPort;
    
    // Registry's networking services
    private cs455.overlay.transport.TCPServerThread serverThread;

    // Data structures for managing messaging nodes
    private HashMap<String,Connection> incomingConnections; //all connections
    private LinkedHashMap<String,Connection> messagingNodes; //registered connections
    private int degree; // the size of the neighborhood for each messaging node
    
    // Adjacency-List Graph for overlay and link weights
    private ArrayList<ArrayList<Edge>> adjList;
    
    // For making events
    EventFactory factory;

    public Registry(int port) throws IOException{
        this.port = port;
        this.serverPort = -1; // TODO, at this point, my serverPort scheme is only
                            // needed by MessagingNode. B/C port == serverPort
                            // after serverThread initialization is complete.
        this.degree = 4; //default degree is 4 as specified in handout
        this.incomingConnections = new HashMap<String,Connection>();
        this.messagingNodes = new LinkedHashMap<String,Connection>();
        this.factory = EventFactory.getInstance();
        this.serverThread = new TCPServerThread(this,port);
        this.serverThread.start(); // TODO move out of constructor
        ArrayList<ArrayList<Edge>> adjList = null; // initialized in setupOverlay
    }
    
    public int getPort(){
        return this.port;
    }

    public String toString(){
        return "Registry class";
    }
    
    public void setServerPort(int p){
        if(this.serverPort == -1){ 
            this.serverPort = p;
        }
    }

    public synchronized void onEvent(Event e, String connectID){
        System.out.println("onEvent in MessagingNode receiving:"+e.getType()+" from: "+connectID);
        int eventType = e.getType();
        String ID;
        switch(eventType){
            case Protocol.REGISTER_REQUEST: {
                //call handleRegRequest to get response
                // if response is non-null, send it via connectID's Connection
                Event response = handleRegRequest(e, connectID);
                if(response != null){
                    try{
                        incomingConnections.get(connectID).sendData(response.getBytes());
                    }catch(IOException ie){
                        ie.printStackTrace();
                    }
                }
                break;
            }
            default: /* TODO add error handling */ break;
        }
    }

    /** ---------- REGISTRATION -------------------------*/

    //Adds connection c to the list of connections, NOT to the list of MessagingNodes
    public synchronized void registerConnection(Connection c) {
        System.out.println("registerConnection on "+c.getID()+" in Registry");
        
        if(!(incomingConnections.containsKey(incomingConnections.containsKey(c.getID())))){
            incomingConnections.put(c.getID(),c);
        }
        else{ //TODO change error handling, if any should be used
            //throw new IOException("Cannot register duplicate connection: "+c.getID());
        }
    }

    //Removes connection c from list of connections, NOT from list of MessagingNodes
    public synchronized void deregisterConnection(Connection c){
        System.out.println("deregisterConnection on "+c.getID());
        incomingConnections.remove(c.getID()); //TODO fails silently, perhaps change
    }

    //handles registration request, returns generated response event.
    private Event handleRegRequest(Event regRequest, String connectID){
        Event response = null;
        String additionalInfo = ""; //to avoid passing null references
        byte status = 0; //assumed valid 'til proven otherwise
        try{
            // create Register object from event and get its socket ID
            Register reg = new Register(regRequest.getBytes());
            String regID = SocketID.socketID(reg.getIPAddr(),reg.getPort());

            // check if its connection is valid
            if(!incomingConnections.containsKey(regID)){
                status += 1;
                additionalInfo += regID + " is not a valid connection. ";
            }
            //check if it is not already registered
            if(messagingNodes.containsKey(regID)){
                status += 2;
                additionalInfo += regID + " is already registered. ";
            }
            // check if its connection matches connectID
            if(!regID.equals(connectID)){
                status += 4;
                additionalInfo += regID + 
                    " does not match its connection info:"+connectID+". ";
            }
            // if good status --> copy connection to message node list
            if(status == 0){
                // A node's ID should always be host:serverPort in registry
                String regIDKey = SocketID.socketID(reg.getIPAddr(),reg.getServerPort());
                System.out.println("Adding "+regID+ /* TODO debug */
                    " to registry. Its serverPort is:"+reg.getServerPort());
                (incomingConnections.get(regID)).setInetServerPort(reg.getServerPort());
                messagingNodes.put(regIDKey,incomingConnections.get(regID));
                additionalInfo += "This is node "+(messagingNodes.size())+" in the registry";
            }
            // create Response object to return
            //TODO perhaps better style to use factory
            response = new RegisterResponse(status,additionalInfo);
        }
        /*catch(SocketException se){
        }
        catch(IOException ie){
        }*/
        catch(Exception e){
            e.printStackTrace();
        }

        return response;
    }
    
    /** ------- OVERLAY -----------------------------------*/

    // For a k-regular graph on n nodes, these two methods provide k and n:
    private int getNumberMessagingNodes(){
        return messagingNodes.size(); //n
    }
    private int getDegree(){
        return this.degree; //k
    }

    // neccessary and sufficient conditions for overlay to exist
    private boolean overlayExistence(int n, int k){
        if( (n >= (k+1)) && ( (n*k)%2==0 )) return true;
        else return false;
    }

    private String[] getMessagingNodeKeys(){
        return (messagingNodes.keySet()).toArray(new String[getNumberMessagingNodes()]);
    }

    // looks up host:port in registry, and returns host:ServerSocketPort
    private String getPeerInfo(String key){
        Connection c = messagingNodes.get(key);
        return SocketID.socketID(c.getInetIP(),c.getInetServerPort());
    }

    private void setupOverlay(int degree) throws Exception {
        this.degree = degree;
        setupOverlay();
    }

    // Because my overlay isn't really random or generic, I may want to read 
    // up on the following page about generating random k-regular graphs
    // http://egtheory.wordpress.com/2012/03/29/random-regular-graphs/
    private void setupOverlay() throws Exception { //TODO perhaps set adjList as class attribute
        System.out.println("setupOverlay("+getDegree()+")");
        
        //for random generation of link weights
        Random rand = new Random();
        
        // Overlay is a k-regular graph on n nodes
        int N = getNumberMessagingNodes();
        int K = getDegree();
        String[] nodeIDs = getMessagingNodeKeys(); // IDs for nodes

        // Stored as adjacency list -- array list of array lists
        /*ArrayList<ArrayList<Edge>>*/ this.adjList = new ArrayList<ArrayList<Edge>>(N);
        for(int i=0;i<N;i++){ adjList.add( new ArrayList<Edge>(K)); }
        
        // Can overlay exist?
        if(!(overlayExistence(N,K))){
            throw new Exception("Overlay cannot be created. At least one of "+
                "these properties fails:\n 1. Nodes > Connections +1\n 2. "+
                "Nodes*Connections is even");
        }
        
        // Constructed as ...
        for(int i=0;i<N;i++){
            (adjList.get(i)).add( new Edge(getPeerInfo(nodeIDs[i]),
                                getPeerInfo(nodeIDs[(i+1)%N]) )); //forward 1
            (adjList.get(i)).add( new Edge(getPeerInfo(nodeIDs[i]),
                                getPeerInfo(nodeIDs[(i+(N-1))%N]) )); //back 1
            (adjList.get(i)).add( new Edge(getPeerInfo(nodeIDs[i]),
                                    getPeerInfo(nodeIDs[(i+2)%N]) )); // forward 2
            (adjList.get(i)).add( new Edge(getPeerInfo(nodeIDs[i]),
                                    getPeerInfo(nodeIDs[(i+(N-2))%N]) )); // back 2
            //TODO remove after debugging
            System.out.println("Adding the following connections for node:"+
                                getPeerInfo(nodeIDs[(i)]));
            System.out.println("\t"+getPeerInfo(nodeIDs[(i+1)%N]));
            System.out.println("\t"+getPeerInfo(nodeIDs[(i+(N-1))%N]));
            System.out.println("\t"+getPeerInfo(nodeIDs[(i+2)%N]));
            System.out.println("\t"+getPeerInfo(nodeIDs[(i+(N-2))%N]));
            System.out.println();
        }
        
        // Add weights to edges, randomly in range [1,10]
        int max = 10;
        int min = 1;
        for(int i=0;i<N;i++){
            for(int j=0;j<K;j++){
                // if node hasn't been dealt with
                if(!((adjList.get(i)).get(j)).isWeightSet()){
                    //for testing, this can be 1
                    int r = rand.nextInt((max - min) + 1) + min; 
                    ((adjList.get(i)).get(j)).setWeight(r);
                    System.out.println("Adding Link "+((adjList.get(i)).get(j)));
                    //Add its friend
                    for(int a=0;a<N;a++){
                        // We've found our dest
                        //System.out.println("\tIs this a winner destination?"
                        //    +nodeIDs[a]+" "+/*.equals*/(((adjList.get(i)).get(j)).getDest()));
                        if(nodeIDs[a].equals(((adjList.get(i)).get(j)).getDest())){
                            //System.out.println("\tdest:"+((adjList.get(i)).get(j)).getDest());
                            // 'a' is our magic index
                            for(int b=0;b<K;b++){
                                // We've found our source
                                if(adjList.get(a).get(b).getDest().equals(((adjList.get(i)).get(j)).getSrc())){
                                    adjList.get(a).get(b).setWeight(r);
                                    System.out.println("Adding Link: "+adjList.get(a).get(b));
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }

        // Send messageNodeLists
        for(int i=0;i<N;i++){
            String[] list = new String[K];
            for(int j=0;j<K;j++){
                list[j] = new String(((adjList.get(i)).get(j)).getDest());
            }
            MessagingNodesList message = 
                new MessagingNodesList(list);
            (messagingNodes.get(nodeIDs[i])).sendData(message.getBytes());
        }
    }
    
    private void sendLinkWeights(){
        // Send linkWeights to each of the messaging nodes
        int N = getNumberMessagingNodes();
        int K = getDegree();
        Edge[] list = new Edge[N*K]; // Pseudo-2D array, bby!
        for(int i=0;i<N;i++){
            for(int j=0;j<K;j++){
                list[i*K+j] = (adjList.get(i)).get(j);
            }
        }
        LinkWeights message = new LinkWeights(list);
        for(String key : messagingNodes.keySet()){ //for(int i=0;i<N;i++){
            try{
                (messagingNodes.get(key)).sendData(message.getBytes());
            }
            catch(IOException uhOh){
                System.out.println(uhOh.getMessage());
                uhOh.printStackTrace();
            }
        }
    }
    
    private void listWeights(){
        //TODO provide errorhandling for adjList as null or incomplete
        int N = getNumberMessagingNodes();
        int K = getDegree();
        for(int i=0;i<N;i++){
            for(int j=0;j<K;j++){
                System.out.println((adjList.get(i)).get(j));
            }
        }
    }
    
    private void start(){
        //TODO implement
    }


    /**-------- MAIN ---------------------------------*/

    public static void main(String[] args){
        Registry reg=null;
        int port;
        String [] options = {"list-messaging-nodes","list-weights",
            "setup-overlay","send-overlay-link-weights","start"};
        Scanner scan = new Scanner(System.in);
        /*
          do any setups and commandline parsing here
        */
        System.out.println("Registry main()");
        port = 5000;
        try{
            reg = new Registry(port);
        }
        catch(Exception e){ 
            e.printStackTrace(); 
            System.exit(0); 
        };

        System.out.println("Registry accepting commands ...");
        while(true){
            System.out.println("Enter command: ");
            String response = scan.nextLine();
            System.out.println("You typed: "+response);
            String [] responseArgs = response.split(" ");
            
            if(responseArgs[0].equals(options[0])){
                ;
            }
            else if(responseArgs[0].equals(options[1])){
                System.out.println("Listing links of messaging nodes:");
                reg.listWeights();
            }
            else if(responseArgs[0].equals(options[2])){
                try{
                    if(responseArgs.length>1){
                        Integer nodeDegree = new Integer(responseArgs[1]);
                        reg.setupOverlay(nodeDegree);
                    }
                    else{
                        reg.setupOverlay();
                    }
                }
                catch(Exception o){
                    System.out.println(o.getMessage());
                    o.printStackTrace();
                }
            }
            else if(responseArgs[0].equals(options[3])){
                System.out.println("Sending link-weights to messaging nodes");
                reg.sendLinkWeights();
            }
            else if(responseArgs[0].equals(options[4])){
                System.out.println("Starting rounds");
                reg.start();
            }
            else{
                System.out.println("Command unrecognized");
            }
        }
    }
}
