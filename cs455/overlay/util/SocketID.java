/**file: SocketID.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 
package cs455.overlay.util;

import java.net.*;

public class SocketID {

    public static String socketID(Socket socket){
        return new String(socket.getLocalAddress().toString() + ":" + socket.getPort());
    }

    public static String socketID(String IP, int port){
        return new String(IP + ":" + port);
    }

    public String toString(){
        return "SocketID class";
    }

}
