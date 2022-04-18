package br.com.pucrs.client;

import br.com.pucrs.remote.api.PeerConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Optional;

public class SocketClientRequest {
    private int port = 5001;
    private Socket socket;
    private Optional<PeerConnection> connection;


    public SocketClientRequest(Optional<PeerConnection> connection) {
        this.connection = connection;
    }

    public void getArchieve(String hashcode, String resourceName) {
        boolean aux = true;

        while (aux) {
            aux = false;
            try {
                System.out.println("Will try to connect on port " + port );
                socket = new Socket(connection.get().getAddress(), port);
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
            String hash = inputStream.readLine(); // <archieveContent> <- pattern

            //@Todo salvar o arquivo ou so ler ele, talvez tenha que passar o nome do arquivo sozinho



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
