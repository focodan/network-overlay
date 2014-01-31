/**file: Node.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 
package cs455.overlay.node;

import cs455.overlay.wireformats.*;

public interface Node {

    public void onEvent(Event e);
    public int getPort();

}
