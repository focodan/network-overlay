/**file: TaskComplete.java
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

public class TaskComplete implements Event, Protocol {
    /*
    Message Type: TASK_COMPLETE
    Node IP address:
    Node Port number:
    */
    private int type;
    private String IP;
    private int port; // Is this serverPort?

    public TaskComplete(String IP, int port){
        this.type = TASK_COMPLETE;
        this.IP = IP;
        this.port = port;
    }
    
    public TaskComplete(byte[] marshalledBytes) throws IOException {
        ByteArrayInputStream baInputStream =	
				new ByteArrayInputStream(marshalledBytes);	
		DataInputStream din =	
				new DataInputStream(new BufferedInputStream(baInputStream));	

		this.type = din.readInt();		

		int identifierLength = din.readInt();	
		byte[] identifierBytes = new byte[identifierLength];	
		din.readFully(identifierBytes);	

		this.IP = new String(identifierBytes);	

		this.port = din.readInt();	

		baInputStream.close();	
		din.close();	
    }

    public byte[] getBytes() throws IOException { 
        byte[] marshalledBytes = null;	
		ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();	
		DataOutputStream dout =
				new DataOutputStream(new BufferedOutputStream(baOutputStream));

		dout.writeInt(this.type);

		byte[] identifierBytes = (this.IP).getBytes();
		int elementLength = identifierBytes.length;
		dout.writeInt(elementLength);
		dout.write(identifierBytes);

		dout.writeInt(this.port);

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
        return "TaskComplete class";
    }

}
