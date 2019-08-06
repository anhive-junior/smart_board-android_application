package com.example.upload;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import android.util.Log;



public class Client implements Runnable {
    @Override

    public void run() {

        // TODO Auto-generated method stub

        try {

            // Retrieve the ServerName

            InetAddress serverAddr = InetAddress.getByName("192.168.10.255");



            Log.d("UDP", "C: Connecting...");

            /* Create new UDP-Socket */

            DatagramSocket socket = new DatagramSocket();



            /* Prepare some data to be sent. */

            byte[] buf = ("TESTTESTTEST").getBytes();



            /* Create UDP-packet with

             * data & destination(url+port) */

            DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, 4000);

            Log.d("UDP", "C: Sending: '" + new String(buf) + "'");



            /* Send out the packet */

            socket.send(packet);

            Log.d("UDP", "C: Sent.");

            Log.d("UDP", "C: Done.");



            socket.receive(packet);

            Log.d("UDP", "C: Received: '" + new String(packet.getData()) + "'");



        } catch (Exception e) {

            Log.e("UDP", "C: Error", e);

        }

    }

}