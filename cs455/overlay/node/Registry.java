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
    
    // Link weights and overlay ...
    // ...
    
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
        this.serverThread.start();
    }
    
    public int getPort(){
        return this.port;
    }

    public String toString(){
        return "Registry class";
    }
    
    public /*synchronized*/ void setServerPort(int p){
        // can only be changed *once* from its sentinel value
        // this is set in the 'this constructor', so the integrity of this
        // value is guarded.
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
                System.out.println("Adding "+regID+ /* TODO debug */
                    " to registry. Its serverPort is:"+reg.getServerPort());
                (incomingConnections.get(regID)).setInetServerPort(reg.getServerPort());
                messagingNodes.put(regID,incomingConnections.get(regID));
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
        // Overlay is a k-regular graph on n nodes
        int N = getNumberMessagingNodes();
        int K = getDegree();
        String[] nodeIDs = getMessagingNodeKeys(); // IDs for nodes

        // Stored as adjacency list -- array list of array lists
        ArrayList<ArrayList<String>> adjList = new ArrayList<ArrayList<String>>(N);
        for(int i=0;i<N;i++){ adjList.add( new ArrayList<String>(K)); }
        
        // Can overlay exist?
        if(!(overlayExistence(N,K))){
            throw new Exception("Overlay cannot be created. At least one of "+
                "these properties fails:\n 1. Nodes > Connections +1\n 2. "+
                "Nodes*Connections is even");
        }
        
        // Constructed as ...
        for(int i=0;i<N;i++){
            (adjList.get(i)).add( getPeerInfo(nodeIDs[(i+1)%N]) ); //forward 1
            (adjList.get(i)).add( getPeerInfo(nodeIDs[(i+(N-1))%N]) ); //back 1
            (adjList.get(i)).add( getPeerInfo(nodeIDs[(i+2)%N]) ); // forward 2
            (adjList.get(i)).add( getPeerInfo(nodeIDs[(i+(N-2))%N]) ); // back 2
            //TODO remove after debugging
            System.out.println("Adding the following connections for node:"+getPeerInfo(nodeIDs[(i)]));
            System.out.println("\t"+getPeerInfo(nodeIDs[(i+1)%N]));
            System.out.println("\t"+getPeerInfo(nodeIDs[(i+(N-1))%N]));
            System.out.println("\t"+getPeerInfo(nodeIDs[(i+2)%N]));
            System.out.println("\t"+getPeerInfo(nodeIDs[(i+(N-2))%N]));
        }
        
        // Nodes informed as ...
        // send messages here via each Connection's sendData method
        for(int i=0;i<N;i++){ // TODO test this
            MessagingNodesList message = 
                new MessagingNodesList(adjList.get(i).toArray(new String[K]));
            (messagingNodes.get(nodeIDs[i])).sendData(message.getBytes());
        }
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
                ;
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
                ;
            }
            else if(responseArgs[0].equals(options[4])){
                ;
            }
            else{
                System.out.println("Command unrecognized");
            }
        }
    }
}
