/**file: Protocol.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 
package cs455.overlay.wireformats;

public interface Protocol {
    // The message types
    public static final int REGISTER_REQUEST     = 4000;
    public static final int REGISTER_RESPONSE    = 4001;
    public static final int DEREGISTER_REQUEST   = 4002;
    public static final int MESSAGING_NODES_LIST = 4003;
    public static final int Link_Weights         = 4004;
    public static final int TASK_INITIATE        = 4005;
    public static final int TASK_COMPLETE        = 4006;
    public static final int PULL_TRAFIC_SUMMARY  = 4007;
    public static final int TRAFFIC_SUMMARY      = 4008;
}
