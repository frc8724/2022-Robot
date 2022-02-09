package org.mayheminc.util.EventServer;

import java.util.ArrayList;
import java.util.List;

/**
 * The Event Server has a TCP Server. Every 100 ms it loops through the events
 * and lets them send messages to the TCP Server.
 */
public class EventServer extends Thread {

    List<Event> EventList = new ArrayList<Event>();
    TCPServer tcpServer = new TCPServer();

    /**
     * add an event object to the event list that will be polled every 100ms.
     * 
     * @param E
     */
    public void add(Event E) {
        EventList.add(E);
    }

    /**
     * output a string to the TCP Server. This can be used by the events to signal
     * their events or single events can fire strings directly to the TCP Server
     * 
     * @param str
     */
    public void output(String str) {
        tcpServer.add(str);
    }

    /**
     * Run the event server. Run the TCP Server. Loop through the events every
     * 100ms.
     */
    @Override
    public void run() {
        tcpServer.start();

        while (true) {
            try {
                Thread.sleep(100);

                // execute each event
                EventList.forEach((n) -> n.Execute());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}