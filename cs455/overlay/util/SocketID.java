/**file: SocketID.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 
package cs455.overlay.util;

import java.net.*;

public class SocketID {

    public static String socketInetID(Socket socket){ // ID for remote connection
        return new String(socket.getInetAddress().toString() + ":" + socket.getPort());
    }
    
    public static String socketLocalID(Socket socket){ // ID for local connection
        return new String(socket.getLocalAddress().toString() + ":" + socket.getLocalPort());
    }

    public static String socketID(String IP, int port){
        return new String(IP + ":" + port);
    }

    public static String getIP(String socketID){
        String ip = (socketID.split(":"))[0];
        String[] ipParts = ip.split("/");
        if(ipParts.length > 1){ ip = ipParts[1]; }
        else{ ip = ipParts[0]; } 
        return ip;
    }
    
    public static Integer getPort(String socketID){
        return new Integer((socketID.split(":"))[1]);
    }

    public String toString(){
        return "SocketID class";
    }

}
