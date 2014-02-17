/**file: LinkWeights.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 

package cs455.overlay.wireformats;

import cs455.overlay.connection.Edge;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LinkWeights implements Event, Protocol {
    private int type;
    private Edge[] links;
    private int stringCount;

    public LinkWeights(Edge[] links){
        this.type = Link_Weights;
        this.links = links;
        this.stringCount = links.length;
    }

    public LinkWeights(byte[] marshalledBytes) throws IOException { 
        ByteArrayInputStream baInputStream =    
            new ByteArrayInputStream(marshalledBytes);      
        DataInputStream din =   
            new DataInputStream(new BufferedInputStream(baInputStream));    
        
        this.type = din.readInt();              
        this.stringCount = din.readInt();
        this.links = new Edge[ this.stringCount ];

        for(int i=0; i < this.stringCount; i++){
            int identifierLength = din.readInt();       
            byte[] identifierBytes = new byte[identifierLength];        
            din.readFully(identifierBytes);     
            links[i] = new Edge(new String(identifierBytes));
        }

        baInputStream.close();  
        din.close();
    }

    public byte[] getBytes() throws IOException { 
        byte[] marshalledBytes = null;  
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();     
        DataOutputStream dout =
            new DataOutputStream(new BufferedOutputStream(baOutputStream));

        dout.writeInt(this.type);
        dout.writeInt(this.stringCount);

        for(int i=0; i<this.stringCount; i++){
            byte[] identifierBytes = (links[i].toString()).getBytes();
            int elementLength = identifierBytes.length;
            dout.writeInt(elementLength);
            dout.write(identifierBytes);
        }

        dout.flush();
        marshalledBytes = baOutputStream.toByteArray();

        baOutputStream.close();
        dout.close();
        return marshalledBytes;
    }

    public int getType(){
        return this.type;
    }

    public String toString(){
        return "LinkWeights class";
    }
}
