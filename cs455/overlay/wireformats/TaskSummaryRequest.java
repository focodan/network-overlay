/**file: TaskSummaryRequest.java
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

public class TaskSummaryRequest implements Event, Protocol {
    private int type;
    
    public TaskSummaryRequest(){
        this.type = PULL_TRAFIC_SUMMARY;
    }

    public TaskSummaryRequest(byte[] marshalledBytes) throws IOException {
        ByteArrayInputStream baInputStream =	
				new ByteArrayInputStream(marshalledBytes);	
		DataInputStream din =	
				new DataInputStream(new BufferedInputStream(baInputStream));	

		this.type = din.readInt();		

		baInputStream.close();	
		din.close();
    }

    public byte[] getBytes() throws IOException { 
        byte[] marshalledBytes = null;	
		ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();	
		DataOutputStream dout =
				new DataOutputStream(new BufferedOutputStream(baOutputStream));

		dout.writeInt(this.type);

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
        return "TaskSummaryRequest class";
    }

}
