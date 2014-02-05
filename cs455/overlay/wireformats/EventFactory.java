/**file: EventFactory.java
   author: Dan Elliott
   course: CS 455
   assignment: HW 1 Network Overlay
*/ 
package cs455.overlay.wireformats;

public class EventFactory {
    //singleton
    private static final EventFactory factory = new EventFactory();
    private EventFactory() { }
    public static EventFactory getInstance(){ return factory; }
    public String toString(){ return "EventFactory class"; }

}
