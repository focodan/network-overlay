/**file: Connection.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 
package cs455.overlay.connection;

import cs455.overlay.transport.*;
import cs455.overlay.wireformats.*;
import cs455.overlay.node.*;
import java.io.*; //TODO remove/reduce after debugging is finished
import java.util.*; //TODO remove/reduce after debugging is finished
import java.net.*; //TODO remove/reduce after debugging is finished

public class Connection {
    //Reference to this connection's owner
    private Node node;

    //Use host info of peer as unique key for this connection
    //ID is of the format "IPAddress:port"
    private String ID;
    
    //Network links
    private Socket socket;
    private cs455.overlay.transport.TCPReceiverThread receiver;
    private cs455.overlay.transport.TCPSender sender;
    
    public Connection(Socket s, Node n) throws IOException{
        this.node = n;
        this.socket = s;
        this.ID = makeID(this.socket);
        this.sender = new cs455.overlay.transport.TCPSender(this.socket);
        this.receiver= new cs455.overlay.transport.TCPReceiverThread(
                                                this.socket, this.node);
        this.receiver.start();
        (this.node).registerConnection(this);
    }
    
    private String makeID(Socket socket){
        return new String(socket.getLocalAddress().toString() + ":" + socket.getPort());
    }
    
    //return key for this connection
    public String getID(){
        return new String(this.ID);
    }
    
    public String getLocalIP(){
        return socket.getLocalAddress().toString();
    }
    
    public int getLocalPort(){
        return socket.getPort();
    }
    
    //send Even data through TCPSender
    public boolean sendData(byte[] bytes){//TODO why does this return boolean?
        boolean status = true; //whether data could be sent successfuly 
        try{
            sender.sendData(bytes);
        }
        catch(IOException e){ status = false; } //TODO decide how to fail
        return status;
    }

}
