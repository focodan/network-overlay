/**file: TCPReceiverThread.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 
package cs455.overlay.transport;

import cs455.overlay.node.*;
import cs455.overlay.wireformats.*;
import java.io.*; //TODO remove/reduce after debugging
import java.net.*; //TODO reduce after debugging

public class TCPReceiverThread extends Thread {

    // Our host node, socket, and input stream
    private cs455.overlay.node.Node node;
    private Socket socket;
    private DataInputStream din;
    private EventFactory factory; 
    
    public TCPReceiverThread(Socket receive, cs455.overlay.node.Node n)
                            throws IOException{
        this.node = n;
        this.socket = receive;
        this.din = new DataInputStream(socket.getInputStream());
        this.factory = EventFactory.getInstance();
    }
    
    public void run(){ //TODO figure out how to send byte[] as an Event
                        // to node
        int dataLength;
        while (socket != null){
            try {
                dataLength = din.readInt();
             	byte[] data = new byte[dataLength];
                din.readFully(data, 0, dataLength);
                // call the event factory and pass the new event to node
                node.onEvent(factory.makeEvent(data));
            } catch (SocketException se) {
                System.out.println(se.getMessage());
                break;
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
                break;
            }
        }

    }
    
    
    public String toString(){
        return "TCPReceiverThread class";
    }

}
