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
import java.util.ArrayList;

public class MessagingNodesList implements Event, Protocol {
    private int type;
    private int stringCount;
    private ArrayList infoList;

    public MessagingNodesList(ArrayList<String> infoList){
        this.type = MESSAGING_NODES_LIST;
        this.infoList = infoList;
        this.stringCount = (this.infoList).size();
    }

    public MessagingNodesList(byte[] marshalledBytes) throws IOException {
    	/*ByteArrayInputStream baInputStream =	
				new ByteArrayInputStream(marshalledBytes);	
		DataInputStream din =	
				new DataInputStream(new BufferedInputStream(baInputStream));	

		this.type = din.readInt();		

		int identifierLength = din.readInt();	
		byte[] identifierBytes = new byte[identifierLength];	
		din.readFully(identifierBytes);	

		this.IPaddr = new String(identifierBytes);	

		this.port = din.readInt();
		
		this.serverPort = din.readInt();	

		baInputStream.close();	
		din.close();*/
    }

    public byte[] getBytes() throws IOException { 
        /*byte[] marshalledBytes = null;	
		ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();	
		DataOutputStream dout =
				new DataOutputStream(new BufferedOutputStream(baOutputStream));

		dout.writeInt(this.type);

		byte[] identifierBytes = (this.IPaddr).getBytes();
		int elementLength = identifierBytes.length;
		dout.writeInt(elementLength);
		dout.write(identifierBytes);

		dout.writeInt(this.port);
		dout.writeInt(this.serverPort);

		dout.flush();
		marshalledBytes = baOutputStream.toByteArray();

		baOutputStream.close();
		dout.close();
		return marshalledBytes;*/
		return null;
    }

    public int getType(){
        return this.type;
    }

    public String toString(){
        return "MessagingNodesList class";
    }

}
