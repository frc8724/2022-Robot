package org.mayheminc.util.EventServer;

import java.io.*;
import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;

import edu.wpi.first.wpilibj.DriverStation;

class TCPServer extends Thread {

   private static final int PORT = 5809;

   ArrayBlockingQueue<String> buffer = new ArrayBlockingQueue<>(10);

   public static void main(String argv[]) throws Exception {
      TCPServer server = new TCPServer();
      server.start();

      Thread.sleep(5000);
      server.add("bugs_answer.wav\n");

      Thread.sleep(5000);
      server.add("bugs_answer.wav\n");
      Thread.sleep(5000);
      server.add("bugs_answer.wav\n");
      Thread.sleep(5000);
      server.add("bugs_answer.wav\n");
   }

   public void add(String S) {
      // add S to the buffer, but if we can't fail silently
      buffer.offer(S);
   }

   public void run() {
      ServerSocket welcomeSocket;
      try {
         String wavfile;

         welcomeSocket = new ServerSocket(PORT);

         try {
            while (true) {
               Socket connectionSocket = welcomeSocket.accept();

               System.out.println("Opening socket");
               DriverStation.reportError("Opening Socket for Sound", false);

               DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

               while (true) {
                  wavfile = buffer.take(); // take from the buffer. Wait if nothing is available
                  outToClient.writeBytes(wavfile + "\n");
                  DriverStation.reportError("Sending Sound: " + wavfile, false);
               }
            }
         } catch (Exception ex) {
         } finally {
            welcomeSocket.close();
         }

      } catch (Exception ex) {
      }
   }
}