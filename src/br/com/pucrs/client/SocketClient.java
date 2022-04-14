package br.com.pucrs.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class SocketClient extends Thread {
    private int port = 5000;
    private Socket socket;
    private boolean connected;
    private final ArchiveRpository archiveRpository;


    public SocketClient(ArchiveRpository archiveRpository) {
        this.archiveRpository = archiveRpository;
        this.connected = false;
    }

    @Override
    public void run() {
        boolean aux = true;

        while (aux) {
            aux = false;
            try {
                System.out.println("Will try to connect on port " + port );
                socket = new Socket(InetAddress.getLocalHost().getHostAddress(), port);
            } catch (Exception ignore) {
                System.out.println("Port " + port + " Already on use, will try next");
                aux = true;
                port++;
            }
        }
        System.out.println("Connect on port " + port);
        connected = true;
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        try {
            String archiveContent = "";
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
            String hash = (String) inputStream.readObject(); // <hashcode> <- pattern

            try {
                archiveContent = getFile(hash);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (archiveContent != null) {
                outputStream.writeObject(archiveContent);
                outputStream.flush();
            } else {
                outputStream.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                socket.close();
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

    private String getFile(String hash) {
        return this.archiveRpository.getFileContent(hash);
    }
}
