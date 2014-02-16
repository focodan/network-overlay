/**file: MessagingNodesList.java
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


public class MessagingNodesList implements Event, Protocol {
    private int type;
    private int stringCount;
    private String[] infoList;

    public MessagingNodesList(String[] infoList){
        this.type = MESSAGING_NODES_LIST;
        this.infoList = infoList;
        this.stringCount = (this.infoList).length;
    }

    public MessagingNodesList(byte[] marshalledBytes) throws IOException {
    	ByteArrayInputStream baInputStream =	
				new ByteArrayInputStream(marshalledBytes);	
		DataInputStream din =	
				new DataInputStream(new BufferedInputStream(baInputStream));	

		this.type = din.readInt();		
        this.stringCount = din.readInt();
        this.infoList = new String[ this.stringCount ];

        for(int i=0; i < this.stringCount; i++){
            int identifierLength = din.readInt();	
		    byte[] identifierBytes = new byte[identifierLength];	
		    din.readFully(identifierBytes);	
		    infoList[i] = new String(identifierBytes);
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
            byte[] identifierBytes = (infoList[i]).getBytes();
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
    
    public String[] getInfoList(){ //TODO test
         return (String[]) (this.infoList).clone();
    }

    public String toString(){
        return "MessagingNodesList class";
    }

}
