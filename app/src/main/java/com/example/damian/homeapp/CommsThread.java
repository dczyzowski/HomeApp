package com.example.damian.homeapp;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.damian.homeapp.dodatki.MessageNotification;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Damian on 2017-04-16.
 */

public class CommsThread extends Thread {

    final BluetoothSocket bluetoothSocket;
    final InputStream inputStream;
    final OutputStream outputStream;

    public CommsThread(BluetoothSocket socket){
        bluetoothSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try{
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        }
        catch (IOException e){
            Log.d("CommsThread", e.getLocalizedMessage());
        }
        inputStream = tmpIn;
        outputStream = tmpOut;
    }

    public void run(){
        byte[] buffer = new byte[1024];

        int bytes;
        MainActivity.isConnected = true;


        // nasluchiwanie do czasu pojawienia sie wątku

        while (true){

            try{
                bytes = inputStream.read(buffer);
                MainActivity.UIupdater.obtainMessage(0, bytes, -1, buffer).sendToTarget();
            }
            catch (IOException e) {
                Log.d("CommsThread", e.getLocalizedMessage());
                break;
            }


        }


    }
    // to działanie bedzie wywolane z dzialania glownego aby wyslac
    // dane do zdalnego uzadzenia

    public void write(String str){

        try{
            outputStream.write(str.getBytes());
        }
        catch (IOException e){
            Log.d("CommsThread", e.getLocalizedMessage());
        }

    }

    //zamykanie polaczenia

    public void cancel(){
        try {
            bluetoothSocket.close();
        }
        catch (IOException e){
            Log.d("CommsThread", e.getLocalizedMessage());
        }
    }

}
