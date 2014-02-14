/**file: Connection.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 
package cs455.overlay.connection;

import cs455.overlay.transport.*;
import cs455.overlay.wireformats.*;
import cs455.overlay.node.*;
import cs455.overlay.util.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class Connection {
    //Reference to this connection's owner
    private Node node;

    //ID is of the format "IPAddress:port" for Inet, used as datastructure key
    private String ID;
    private int localServerPort; // a new node connects with this.node using this port
    private int inetServerPort; // a new node connects with remote node using this port
    
    //Network links
    private Socket socket;
    private cs455.overlay.transport.TCPReceiverThread receiver;
    private cs455.overlay.transport.TCPSender sender;
    
    public Connection(Socket s, Node n) throws IOException{
        this(s,n,0);
    }
    
    public Connection(Socket s, Node n, int localServerPort) throws IOException{
        this.localServerPort = localServerPort;
        this.inetServerPort = 0;
        this.node = n;
        this.socket = s;
        this.ID = SocketID.socketInetID(this.socket);
        this.sender = new cs455.overlay.transport.TCPSender(this.socket);
        this.receiver= new cs455.overlay.transport.TCPReceiverThread(
                                                this.socket, this.node);
        this.receiver.start();
        (this.node).registerConnection(this);
    }
    
    //return key for this connection
    public String getID(){ //TODO maybe change to getInetID
        return new String(this.ID);
    }
    
    public String getLocalID(){
        return new String(this.ID);
    }
    
    public String getInetIP(){
        return socket.getInetAddress().toString();
    }
    
    public String getLocalIP(){
        return socket.getLocalAddress().toString();
    }
    
    public int getLocalPort(){
        return socket.getLocalPort();
    }
    
    public int getInetPort(){
        return socket.getPort();
    }
    
    public int getLocalServerPort(){
        return this.localServerPort;
    }
    
    public int getInetServerPort(){
        return this.inetServerPort;
    }
    
    public void setInetServerPort(int p){
        return this.inetServerPort = p;
    }
    
    //send Event data through TCPSender
    public boolean sendData(byte[] bytes){
        boolean status = true;
        try{
            sender.sendData(bytes);
        }
        catch(IOException e){ status = false; }
        return status;
    }

}
