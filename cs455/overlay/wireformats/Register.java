/**file: Register.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
   description: Implements the registration request message. 
*/ 

package cs455.overlay.wireformats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Register implements Event, Protocol {

    private int type;
    private String IPaddr;
    private int port;

    public Register(String _IPaddr, int _port){
        IPaddr = _IPaddr;
        port = _port;
        type = REGISTER_REQUEST;
    }
    
    // byte[] format for marshalling is the following
    //     type
    //     IPaddr
    //     port

	public Register(byte[] marshalledBytes) throws IOException {	
		ByteArrayInputStream baInputStream =	
				new ByteArrayInputStream(marshalledBytes);	
		DataInputStream din =	
				new DataInputStream(new BufferedInputStream(baInputStream));	

		this.type = din.readInt();		

		int identifierLength = din.readInt();	
		byte[] identifierBytes = new byte[identifierLength];	
		din.readFully(identifierBytes);	

		this.IPaddr = new String(identifierBytes);	

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

		byte[] identifierBytes = (this.IPaddr).getBytes();	
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
        public String getIPAddr(){
            return this.IPaddr;
        }
        public int getPort(){
            return this.port;
        }

	public String toString(){
		return "Register class";
	}

}
