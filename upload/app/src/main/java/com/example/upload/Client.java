package com.example.upload;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import android.util.Log;



public class Client implements Runnable {

    private static String ip;

    public static final int port = 4001;
    public static final int recieveport = 4002;

    public static String result = null;

    Client(String ip){
        this.ip = ip;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

        while (result == null) {
            try {
                // Retrieve the ServerName

                InetAddress serverAddr = InetAddress.getByName(ip);

                //Log.d("UDP", "C: Connecting...");

                /* Create new UDP-Socket */

                DatagramSocket socket = new DatagramSocket();
                DatagramSocket socket1 = new DatagramSocket(recieveport);

                /* Prepare some data to be sent. */

                byte[] buf = ("TESTTESTTEST").getBytes();

                /* Create UDP-packet with

                 * data & destination(url+port) */

                DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, port);

                //Log.d("UDP", "C: Sending: '" + new String(buf) + "'");

                /* Send out the packet */

                socket.send(packet);

                Log.d("UDP", "C: Sent.");

                //Log.d("UDP", "C: Done.");
                socket.close();
                socket1.setSoTimeout(3000);
                while(true){
                    try{
                        socket1.receive(packet);
                        System.out.println(packet.getAddress());

                        Log.d("UDP", "C: Received: '" + new String(packet.getData()) + "'");

                        InetAddress clientAddr = packet.getAddress();
                        result = packet.getAddress().toString();
                        System.out.println(result);
                        String temp = result.substring(1);
                        result = temp;
                        System.out.println(result);

                        int clientPort = packet.getPort();

                        System.out.println(clientAddr);
                        System.out.println(clientPort);

                    }catch(SocketTimeoutException e){
                        System.out.println(e);
                        socket1.close();
                    }
                }
            } catch (Exception e) {
                System.out.println("out2");

                //Log.e("UDP", "C: Error", e);

            }
            System.out.println("out");
        }

    }

    public static void test() throws IOException{
        throw new IOException();
    }

}