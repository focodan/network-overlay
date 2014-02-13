/**file: RegisterResponse.java
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

public class RegisterResponse implements Event, Protocol{
    private int type;
    private byte status;
    private String info;
    
    public RegisterResponse(byte status, String info){
        this.type = REGISTER_RESPONSE;
        this.status = status;
        this.info = info;
    }
    
    // byte[] format for marshalling is the following
    //     type
    //     info
    //     status
    
    public RegisterResponse(byte[] marshalledBytes) throws IOException { 
        ByteArrayInputStream baInputStream =	
				new ByteArrayInputStream(marshalledBytes);	
		DataInputStream din =	
				new DataInputStream(new BufferedInputStream(baInputStream));	

		this.type = din.readInt();		

		int identifierLength = din.readInt();	
		byte[] identifierBytes = new byte[identifierLength];	
		din.readFully(identifierBytes);	

		this.info = new String(identifierBytes);	

		this.status = din.readByte();	

		baInputStream.close();	
		din.close();
    }

    public byte[] getBytes() throws IOException { 
        byte[] marshalledBytes = null;	
		ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();	
		DataOutputStream dout =	
				new DataOutputStream(new BufferedOutputStream(baOutputStream));	

		dout.writeInt(this.type);		

		byte[] identifierBytes = (this.info).getBytes();	
		int elementLength = identifierBytes.length;	
		dout.writeInt(elementLength);	
		dout.write(identifierBytes);	

		dout.writeByte(this.status);	

		dout.flush();	
		marshalledBytes = baOutputStream.toByteArray();	

		baOutputStream.close();	
		dout.close();	
		return marshalledBytes;	
    }

    public int getType(){
        return this.type;
    }
    
    public byte getStatus(){
        return this.status;
    }
    
    public String getInfo(){
        return new String(this.info);
    }

    public String toString(){
        return "RegisterResponse class";
    }

}
