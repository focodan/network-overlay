/**file: TaskSummaryResponse.java
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

public class TaskSummaryResponse implements Event, Protocol {
    private int type;
    private String IP;
    private int port;
    private int sendTracker;
    private long sendSummation;
    private int receiveTracker;
    private long receiveSummation;    
    private int relayTracker;
    /*
    Message Type: TRAFFIC_SUMMARY
    Node IP address:
    Node Port number:
    Number of messages sent
    Summation of sent messages
    Number of messages received
    Summation of received messages
    Number of messages relayed
    */
    
    public TaskSummaryResponse(String IP,int port,int sendTracker,long sendSummation,
            int receiveTracker,long receiveSummation,int relayTracker){
        this.type = TRAFFIC_SUMMARY;
        this.IP = IP;
        this.port = port;
        this.sendTracker = sendTracker;
        this.sendSummation = sendSummation;
        this.receiveTracker = receiveTracker;
        this.receiveSummation = receiveSummation;    
        this.relayTracker = relayTracker;
    }

    public TaskSummaryResponse(byte[] marshalledBytes) throws IOException {
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
		this.sendTracker = din.readInt();
        this.sendSummation = din.readLong();
        this.receiveTracker = din.readInt();
        this.receiveSummation = din.readLong();
        this.relayTracker = din.readInt();

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
		
		dout.writeInt(this.sendTracker);
		dout.writeLong(this.sendSummation);
		dout.writeInt(this.receiveTracker);
		dout.writeLong(this.receiveSummation);
		dout.writeInt(this.relayTracker);

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
        return "TaskSummaryResponse class";
    }

}
