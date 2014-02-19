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

//TODO consider adding a getDest() method.
public class Message implements Event, Protocol{
    private int type;
    private int payload;
    private String[] route;
    private int stringCount;
    
    public Message(int _payload, String[] _route){
        this.type = MESSAGE_PAYLOAD;
        this.payload = _payload;
        this.route = _route;
        this.stringCount = this.route.length;
    }

    public Message(byte[] marshalledBytes) throws IOException {
        ByteArrayInputStream baInputStream =    
            new ByteArrayInputStream(marshalledBytes);      
        DataInputStream din =   
            new DataInputStream(new BufferedInputStream(baInputStream));    
        
        this.type = din.readInt();
        this.payload = din.readInt();            
        this.stringCount = din.readInt();
        this.route = new String[ this.stringCount ];

        for(int i=0; i < this.stringCount; i++){
            int identifierLength = din.readInt();       
            byte[] identifierBytes = new byte[identifierLength];        
            din.readFully(identifierBytes);     
            this.route[i] = new String(identifierBytes);
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
        dout.writeInt(this.payload);
        dout.writeInt(this.stringCount);

        for(int i=0; i<this.stringCount; i++){
            byte[] identifierBytes = (route[i]).getBytes();
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
    
    public String[] getRoute(){
        return (String[]) (this.route).clone();
    }
    
    public String getDest(){
        if(this.route.length > 0){
            return this.route[ this.route.length - 1 ];
        }
        else{
            return "undefined";
        }
    }

    public String toString(){
        String routeString="";
        for(int i=0;i<route.length;i++){ routeString += route[i]+" "; }
        return new String(type+" "+payload+" "+routeString);
    }

}
