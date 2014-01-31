/**file: TCPSender.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/
package cs455.overlay.transport;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class TCPSender {

	private Socket socket;	
	private DataOutputStream dout;	

	public TCPSender(Socket socket) throws IOException {	
		this.socket = socket;	
		dout = new DataOutputStream(socket.getOutputStream());	
	}	

	public void sendData(byte[] dataToSend) throws IOException {	
		int dataLength = dataToSend.length;	
		dout.writeInt(dataLength);	
		dout.write(dataToSend, 0, dataLength);	
		dout.flush();	
	}	
}

