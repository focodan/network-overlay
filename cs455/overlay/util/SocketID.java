/**file: SocketID.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 
package cs455.overlay.util;

import java.net.*;

public class SocketID {

    public static String socketID(Socket socket){
        return new String(socket.getLocalAddress().toString() + ":" + socket.getPort()); //TODO change to Inet
    }

    public static String socketID(String IP, int port){
        return new String(IP + ":" + port);
    }
    /* possibly unneeded
    public static String getIP(String socketID){
        return (socketID.split(":")).get(0);
    }
    
    public static String getPort(String socketID){
        return (socketID.split(":")).get(0);
    }
    */
    public String toString(){
        return "SocketID class";
    }

}
