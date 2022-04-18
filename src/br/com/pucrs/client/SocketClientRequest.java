package br.com.pucrs.client;

import br.com.pucrs.remote.api.PeerConnection;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
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
                System.out.println("Will try to connect on port " + connection );
                socket = new Socket(connection.getAddress(), Integer.parseInt(connection.getPort()));
            } catch (Exception ignore) {
                System.out.println("Could not connect on connection " + connection);
                System.out.println("Return fail");
                return;
            }
        }
        System.out.println("Connect on connection " + connection);
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());

            if (!hashcode.isEmpty()) {
                outputStream.writeObject(hashcode);
            }

            byte[] archive = inputStream.readAllBytes(); // <archiveContent> <- pattern

            try {
                System.out.println("Received from input with size of: " + archive.length);
                socket.close();
                String fileDirectory = archiveRpository.getDirectory() + "/" + resourceName;
                File fileToWrite = new File(fileDirectory);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToWrite)));
                writer.write(new String(archive));
                System.out.println("File received and saved successfully");
                writer.flush();
                writer.close();
            }catch (Exception ignored){
                System.out.println("There were problems receiving the file");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public int getPort() {
        return port;
    }

}
