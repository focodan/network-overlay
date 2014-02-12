/**file: Node.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 
package cs455.overlay.node;

import cs455.overlay.wireformats.*;
import cs455.overlay.transport.*;
import cs455.overlay.connection.*;

public interface Node {

    public void onEvent(Event e, String connectID);
    public void registerConnection(Connection c);
    public void deregisterConnection(Connection c);
    public int  getPort();
}
