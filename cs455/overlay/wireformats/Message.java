/**file: Message.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 
package cs455.overlay.wireformats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

// only to be sent from messaging node to messaging node
// contains the path as generate dy Dijkstra's algorithm
public class Message implements Event{
    private int type;

    public Message(byte[] marshalledBytes) throws IOException { 
    }

    public byte[] getBytes() throws IOException { 
        return null; 
    }

    public int getType(){
        return this.type;
    }

    public String toString(){
        return "Message class";
    }

}
