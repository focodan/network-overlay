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
import java.io.*; //TODO remove/reduce after debugging is finished
import java.util.*; //TODO remove/reduce after debugging is finished

public class Registry implements Node{
    // Registry's personal info
    private int port;
    
    // Registry's networking services
    private cs455.overlay.transport.TCPServerThread serverThread;

    // Data structures for managing messaging nodes
    HashMap<String,Connection> incomingConnections;
    HashMap<String,Connection> messagingNodes;
    
    // Link weights and overlay ...
    // ...
    
    // For making events
    EventFactory factory;

    public Registry(int port) throws IOException{
        this.port = port;
        incomingConnections = new HashMap<String,Connection>();
        messagingNodes = new HashMap<String,Connection>();
        factory = EventFactory.getInstance();
        serverThread = new TCPServerThread(this);
        serverThread.start();
    }
    
    public int getPort(){
        return this.port;
    }

    public String toString(){
        return "Registry class";
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
                System.out.println("Adding "+regID+" to registry");
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
    
    private void setupOverlay(int degree){
        System.out.println("setupOverlay("+degree+")");
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
                if(responseArgs.length>1){
                    Integer nodeDegree = new Integer(responseArgs[1]);
                    reg.setupOverlay(nodeDegree);
                }
                else{
                    System.out.println("Needs integer argument");
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
