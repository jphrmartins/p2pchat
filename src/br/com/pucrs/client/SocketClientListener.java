package br.com.pucrs.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class SocketClientListener extends Thread {
    private int port = 5000;
    private Socket socket;
    private boolean connected;
    private final ArchiveRpository archiveRpository;

    public SocketClientListener(ArchiveRpository archiveRpository) {
        this.archiveRpository = archiveRpository;
        this.connected = false;
    }

    @Override
    //@Todo @JU
    // Validar se isso aqui vai ficar esperando uma conexão...
    // da um bizu aqui https://www.codejava.net/java-se/networking/java-socket-client-examples-tcp-ip
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
        OutputStream outputStream = null;
        BufferedReader inputStream = null;
        connected = true;
        try {
            while (connected) {
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

    private byte[] getFile(String hash) {
        return this.archiveRpository.getFileContent(hash);
    }
}
