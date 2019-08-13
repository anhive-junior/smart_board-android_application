package com.example.upload;

import java.net.DatagramPacket;

import java.net.DatagramSocket;

import java.net.InetAddress;



import android.util.Log;



public class Server implements Runnable {
    public static final String SERVERIP = "192.168.10.224"; // 'Within' the emulator!

    public static final int SERVERPORT = 4001;

    public static String result;


    @Override

    public void run() {

        // TODO Auto-generated method stub

        try {

            /* Retrieve the ServerName */

            Log.d("UDP", "S: Connecting...");

            /* Create new UDP-Socket */

            DatagramSocket socket = new DatagramSocket(SERVERPORT);


            /* By magic we know, how much data will be waiting for us */

            byte[] buf = new byte[17];

            /* Prepare a UDP-Packet that can

             * contain the data we want to receive */

            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            Log.d("UDP", "S: Receiving...");


            while(true){
                /* Receive the UDP-Packet */
                socket.receive(packet);

                Log.d("UDP", "S: Received: '" + new String(packet.getData()) + "'");

                Log.d("UDP", "S: Done.");

                InetAddress clientAddr = packet.getAddress();
                result = packet.getAddress().toString();
                System.out.println(result);
                String[] temp = result.split("/");
                result = temp.toString();
                System.out.println(result);

                /*
                int clientPort = packet.getPort();

                System.out.println(clientAddr);
                System.out.println(clientPort);

                String s = "Thanks";

                buf = s.getBytes();

                packet = new DatagramPacket(buf, buf.length, clientAddr, SERVERPORT);



                Log.d("UDP", "S: Sending: '" + new String(buf) + "'");

                socket.send(packet);*/

                System.out.println("end");
            }
        } catch (Exception e) {

            Log.e("UDP", "S: Error", e);

        }

    }

}
