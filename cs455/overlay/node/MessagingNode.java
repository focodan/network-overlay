/**file: MessagingNode.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 
package cs455.overlay.node;

import cs455.overlay.wireformats.*;

public class MessagingNode implements Node{

    public void onEvent(Event e){}
    public int getPort(){ return 0; }

    public String toString(){
        return "MessagingNode class";
    }

}
