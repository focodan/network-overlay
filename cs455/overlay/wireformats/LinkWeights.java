/**file: LinkWeights.java
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

public class LinkWeights implements Event {
    private int type;

    public LinkWeights(byte[] marshalledBytes) throws IOException { 
    }

    public byte[] getBytes() throws IOException { 
        return null; 
    }

    public int getType(){
        return this.type;
    }

    public String toString(){
        return "LinkWeights class";
    }

}
