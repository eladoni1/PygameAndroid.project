package com.example.myapplication;

import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class JoystickCommunication implements JoystickView.JoystickListener {
    private String ipAddress;
    private int port;
    private InetSocketAddress inetSocketAddress;
    private Thread t;
    private BlockingQueue<byte[]> queue = new LinkedBlockingQueue<>();
    

    public JoystickCommunication(String ip, int port) throws UnknownHostException {
        ipAddress = ip;
        this.port = port;
        try {
            connect();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void connect() {
        t = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    DatagramSocket socket = new DatagramSocket();
                    while (true) {
                        byte[] result = queue.take();

                        byte[] buffer = result;

                        DatagramPacket dp = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ipAddress), port);

                        socket.send(dp);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int source) {
        // Switching between X and Y values - application used while sideways.
        float converted_x = -1 * yPercent, converted_y = xPercent;


        byte[] float_bytes1 = new byte[4];
        byte[] float_bytes2 = new byte[4];
        byte[] result = new byte[float_bytes1.length + float_bytes2.length + 1];

        // Old
        // toByteArray(xPercent, float_bytes1);
        // toByteArray(yPercent, float_bytes2);

        // New
        toByteArray(converted_x, float_bytes1);
         toByteArray(converted_y, float_bytes2);
        System.arraycopy(float_bytes1, 0, result, 0, float_bytes1.length);
        System.arraycopy(float_bytes2, 0, result, float_bytes1.length, float_bytes2.length);

        switch(source)
        {

            case R.id.joystickRight:
                Log.d("Right Joystick", " X: " + xPercent + " Y: " + yPercent);
                result[result.length - 1] = (byte) 0;
                break;
            case R.id.joystickLeft:
                Log.d("Left Joystick", " X: " + xPercent + " Y: " + yPercent);
                result[result.length - 1] = (byte) 1;
                break;
        }
        queue.add(result);

    }

    public byte[] toByteArray(float value, byte[] bytes) {
        ByteBuffer.wrap(bytes).putFloat(value);
        return bytes;
    }
}
