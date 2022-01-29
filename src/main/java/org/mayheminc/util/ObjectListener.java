package org.mayheminc.util;

import java.util.*;
import java.io.*;
import java.net.*;
import java.nio.*;

public class ObjectListener extends Thread {
    protected static final int MAX_OBJECTS_PER_FRAME = 20;
    protected static final int MAYHEM_MAGIC = 0x1519B0B4;
    protected static final int MAX_BUFFER = 1500;
    protected static final int DEFAULT_PORT = 5810;

    private DatagramSocket socket;
    private DatagramPacket packet;
    private ByteBuffer buffer;
    private int lastFrame = 0;
    private ArrayList<ObjectLocation> objList;
    private Callback callback = null;

    public interface Callback {
        public void objectListenerCallback(int frame, ArrayList<ObjectLocation> objList);
    }

    public ObjectListener() throws SocketException {
        this(DEFAULT_PORT);
    }

    public ObjectListener(int port) throws SocketException {
        super("ObjectListener-" + port);

        socket = new DatagramSocket(port);

        byte[] byteBuffer = new byte[MAX_BUFFER];
        packet = new DatagramPacket(byteBuffer, byteBuffer.length);
        buffer = ByteBuffer.wrap(byteBuffer);
        
        objList = new ArrayList<ObjectLocation>();
    }

    public int getLastFrame() {
        return lastFrame;
    }

    public List<ObjectLocation> getObjectList() {
        return objList;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void run() {
        String name = super.getName();
        long lastTimestamp = 0;

        while (true) {
            // Receive new datagram
            try {
                socket.receive(packet);
                buffer.rewind();
            } catch (IOException e) {
                System.err.println(super.getName() + " encountered an error");
                e.printStackTrace();
                System.err.println(super.getName() + " aborting");
                break;
            }

            // Abort if told to do so
            if (Thread.interrupted())
                break;

            // Validate packet
            int magic = buffer.getInt();
            if (magic != MAYHEM_MAGIC) {
                System.err.println(name + ": invalid packet received (magic == 0x" + Integer.toHexString(magic) + ")");
                continue;
            }

            // Get information about the update
            int frame = buffer.getInt();
            long timestamp = buffer.getLong();

            // Check for out-of-date data
            if (timestamp <= lastTimestamp) {
                System.err.println(name + ": timestamp for new frame #" + frame + " (" + timestamp + ") is not newer than that for previous frame #" + lastFrame + " (" + lastTimestamp + "); rejecting out-of-date data");
                lastTimestamp = timestamp;
                continue;
            }
            if (frame <= lastFrame) {
                System.err.println(name + ": frame #" + frame + " is earlier than existing frame #" + lastFrame + "; did object detection service restart?");
            }

            // Get list of all objects involved
            ArrayList<ObjectLocation> objList = new ArrayList<ObjectLocation>();
            for (int i = 0; i < MAX_OBJECTS_PER_FRAME; i++) {
                // Pull the next object from the buffer
                ObjectLocation loc = new ObjectLocation(buffer);

                // As soon as one object is none, so are the rest
                if (loc.type == ObjectLocation.ObjectTypes.OBJ_NONE)
                    break;

                // Add object to our list
                objList.add(loc);
            }

            // Update the list of objects
            this.objList = objList;
            lastFrame = frame;
            lastTimestamp = timestamp;

            // Invoke callback, if applicable
            if (callback != null) {
                callback.objectListenerCallback(frame, objList);
            }
        }

        // Clean up
        socket.close();
    }

    // Sample implementation for testing and demonstration purposes
    public static void main(String[] args) {
        ObjectListener listener;
        Callback callback = new ObjectListener.Callback() {
            public void objectListenerCallback(int frame, ArrayList<ObjectLocation> objList) {
                System.out.println("Received notification about objects in frame #" + frame);
                for (ObjectLocation loc: objList) {
                    System.out.println("  " + loc);
                }
            }
        };

        // Create the listener
        try {
            listener = new ObjectListener();
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }

        // Use the callback implementation
        listener.setCallback(callback);

        // Begin listening
        System.out.println("Starting object listener...\n");
        listener.start();

        // Wait forever -- notifications will come from callback
        while (true) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
