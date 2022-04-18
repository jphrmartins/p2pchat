package br.com.pucrs.client;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class SocketClientListener extends Thread {
    private int port = 5000;
    private ServerSocket serverSocket;
    private boolean connected;
    private final String address;
    private final ArchiveRpository archiveRpository;

    public SocketClientListener(ArchiveRpository archiveRpository, String address) {
        this.archiveRpository = archiveRpository;
        this.connected = false;
        this.address = address;
    }

    public void run() {
        boolean aux = true;

        while (aux) {
            aux = false;
            try {
                System.out.println("Will try to connect on port " + port );
                InetAddress inetAddress = InetAddress.getByName(address);
                serverSocket = new ServerSocket(port, 50, inetAddress);
            } catch (Exception ignore) {
                System.out.println("Port " + port + " Already on use, will try next");
                aux = true;
                port++;
            }
        }
        System.out.println("Connect on port " + port);
        OutputStream outputStream = null;
        BufferedReader inputStream = null;
        connected = true;
        try {
            while (connected) {
                Socket socket = serverSocket.accept();
                byte[] archiveContent = new byte[0];
                outputStream = socket.getOutputStream();
                outputStream.flush();
                inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String hash = inputStream.readLine(); // <hashcode> <- pattern

                try {
                    archiveContent = getFile(hash);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (archiveContent.length > 0) {
                    outputStream.write(archiveContent);
                    outputStream.flush();
                } else {
                    outputStream.flush();
                }
                System.out.println("Completed send the archive from the hash code " + hash);
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getPort() {
        return port;
    }

    public boolean isConnected() {
        return connected;
    }

    private byte[] getFile(String hash) {
        return this.archiveRpository.getFileContent(hash);
    }
}
