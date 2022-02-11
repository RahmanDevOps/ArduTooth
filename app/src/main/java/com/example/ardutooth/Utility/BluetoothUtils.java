package com.example.ardutooth.Utility;

import static com.example.ardutooth.Utility.Utility.btModuleUUID;
import static com.example.ardutooth.Utility.Utility.message_read;
import static com.example.ardutooth.Utility.Utility.message_state_changed;
import static com.example.ardutooth.Utility.Utility.message_write;
import static com.example.ardutooth.Utility.Utility.state_connected;
import static com.example.ardutooth.Utility.Utility.state_connecting;
import static com.example.ardutooth.Utility.Utility.state_connection_failed;
import static com.example.ardutooth.Utility.Utility.state_listening;
import static com.example.ardutooth.Utility.Utility.state_none;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;

import com.example.ardutooth.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BluetoothUtils {
    private final Handler handler;
    private int state;
    private ConnectThread connectThread;
    private AcceptThread acceptThread;
    private Communication communication;
    private final BluetoothAdapter bluetoothAdapter;
    private final Context context;

    public BluetoothUtils(Context context,Handler handler) {
        this.handler = handler;
        this.context = context;
        state = state_none;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public int getState() {
        return state;
    }

    public synchronized void setState(int state) {
        this.state = state;
        handler.obtainMessage(message_state_changed, state, -1).sendToTarget();
    }

    public synchronized void start() {
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        if (communication != null) {
            communication.cancel();
            communication = null;
        }

        setState(state_listening);
    }

    public synchronized void stop() {
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }

        if (communication != null) {
            communication.cancel();
            communication = null;
        }

        setState(state_none);
    }

    public void connect(BluetoothDevice device) {
        // Cancel any thread attempting to make a connection
        if (state == state_connecting) {
            if (connectThread != null) {
                connectThread.cancel();
                connectThread = null;
            }
        }

        // Start the thread to connect with the given device
        connectThread = new ConnectThread(device);
        connectThread.start();

        // Cancel any thread currently running a connection
        if (communication != null) {
            communication.cancel();
            communication = null;
        }

        setState(state_connecting);
    }

    @SuppressLint("MissingPermission")
    private synchronized void connected(BluetoothSocket socket) {
        // Cancel the thread that completed the connection
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        // Cancel any thread currently running a connection
        if (communication != null) {
            communication.cancel();
            communication = null;
        }

        communication = new Communication(socket);
        communication.start();

        setState(state_connected);
    }

    public void write(byte[] buffer) {

        Communication comm;
        synchronized (this) {
            if (state != state_connected) {
                return;
            }
            comm = communication;
        }
        comm.write(buffer);
    }

    private synchronized void connectionFailed() {
        BluetoothUtils.this.start();
        setState(state_connection_failed);
    }

    @SuppressLint("MissingPermission")
    private class ConnectThread extends Thread {
        private final BluetoothSocket socket;
        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tempSocket = null;
            try {
                tempSocket = device.createRfcommSocketToServiceRecord(btModuleUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }

            socket = tempSocket;
        }

        public void run() {
            bluetoothAdapter.cancelDiscovery();
            try {
                socket.connect();
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                connectionFailed();
                return;
            }

            synchronized (BluetoothUtils.this) {
                connectThread = null;
            }

            connected(socket);
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private class AcceptThread extends Thread {
        private final BluetoothServerSocket serverSocket;

        public AcceptThread() {
            BluetoothServerSocket tempSocket = null;
            try {
                tempSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(context.getString(R.string.app_name), btModuleUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }

            serverSocket = tempSocket;
        }

        public void run() {
            BluetoothSocket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    serverSocket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

            if (socket != null) {
                switch (state) {
                    case state_listening:
                    case state_connecting:
                        connect(socket.getRemoteDevice());
                        break;
                    case state_none:
                    case state_connected:
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }

        public void cancel() {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class Communication extends Thread {
        private final InputStream inputStream;
        private final OutputStream outputStream;
        private final BluetoothSocket socket;

        public Communication(BluetoothSocket socket) {
            this.socket = socket;
            InputStream tempIn = null;
            OutputStream tempOut = null;

            try {
                tempIn = socket.getInputStream();
                tempOut = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inputStream = tempIn;
            outputStream = tempOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            while (true) {
                try {
                    if (socket != null) {
                        bytes = inputStream.read(buffer);
                        handler.obtainMessage(message_read, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        public void write(byte[] buffer) {
            try {
                System.out.println("write function called ====222");
                outputStream.write(buffer);
                handler.obtainMessage(message_write,-1,-1,buffer).sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}