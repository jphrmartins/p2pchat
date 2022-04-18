package br.com.pucrs.client;

import br.com.pucrs.remote.api.PeerConnection;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SocketClientRequest {
    private int port = 5001;
    private Socket socket;
    private final PeerConnection connection;
    private ArchiveRpository archiveRpository;

    public SocketClientRequest(PeerConnection connection, ArchiveRpository archiveRpository) {
        this.connection = connection;
        this.archiveRpository = archiveRpository;
    }

    public void getArchive(String hashcode, String resourceName) {
        boolean aux = true;

        while (aux) {
            aux = false;
            try {
                System.out.println("Will try to connect on port " + port );
                socket = new Socket(connection.getAddress(), port);
            } catch (Exception ignore) {
                System.out.println("Port " + port + " Already on use, will try next");
                aux = true;
                port++;
            }
        }
        System.out.println("Connect on port " + port);
        OutputStream outputStream = null;
        BufferedReader inputStream = null;
        try {
            byte[] hashcodes = new byte[0];
            hashcodes = hashcode.getBytes();
            outputStream = socket.getOutputStream();
            outputStream.flush();

            if (hashcodes.length > 0) {
                outputStream.write(hashcodes);
                outputStream.flush();
            } else {
                outputStream.flush();
            }

            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String archive = inputStream.readLine(); // <archiveContent> <- pattern

            try {
                Writer writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(archiveRpository.getDirectory() + "\\" + resourceName + ".txt"), "utf-8"));
                writer.write(archive);
                System.out.println("File received and saved successfully");

            }catch (Exception ignored){
                System.out.println("There were problems receiving the file");
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

}
