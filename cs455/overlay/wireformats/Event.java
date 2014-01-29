/**file: Event.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 
package cs455.overlay.wireformats;

import java.io.IOException;

public interface Event {
    
    public byte[] getBytes() throws IOException;
    
    public int getType();

}
