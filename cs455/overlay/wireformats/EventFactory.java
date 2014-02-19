/**file: EventFactory.java
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

public class EventFactory {
    //singleton
    private static final EventFactory factory = new EventFactory();
    private EventFactory() { }
    public static EventFactory getInstance(){ return factory; }
    public String toString(){ return "EventFactory class"; }

    public Event makeEvent(byte[] data){
        // Given the first int in data which represents the message type
        // as specified in Protocol, call the proper event constructor.
        Event result = null;
        try{ //TODO add error handling, perhaps throw an Exception
       	ByteArrayInputStream baInputStream =
        new ByteArrayInputStream(data);
        DataInputStream din =	
        new DataInputStream(new BufferedInputStream(baInputStream));

        int type = din.readInt();
        
        baInputStream.close();
        din.close();

        switch(type){
            case Protocol.REGISTER_REQUEST: {
                result = new Register(data); break;
            }
            case Protocol.REGISTER_RESPONSE: {
                result = new RegisterResponse(data); break;
            }
            case Protocol.DEREGISTER_REQUEST: {
                result = new Deregister(data); break;
            }
            case Protocol.DEREGISTER_RESPONSE: {
                result = new DeregisterResponse(data); break;
            }
            case Protocol.MESSAGING_NODES_LIST: {
                result = new MessagingNodesList(data); break;
            }
            case Protocol.Link_Weights: {
                result = new LinkWeights(data); break;
            }
            case Protocol.TASK_INITIATE: {
                 result = new TaskInitiate(data); break;
            }
            case Protocol.TASK_COMPLETE: {
                 result = new TaskComplete(data); break;
            }
            case Protocol.PULL_TRAFIC_SUMMARY: {
                 result = new TaskSummaryRequest(data); break;
            }
            case Protocol.TRAFFIC_SUMMARY: {
                 result = new TaskSummaryResponse(data); break;
            }
            case Protocol.MESSAGE_PAYLOAD: {
                 result = new Message(data); break;
            }
            default: /* add error handling, possibly throw*/ break; 
        } 
        }catch(IOException e){} //TODO fill in...

        return result;
    }
}
