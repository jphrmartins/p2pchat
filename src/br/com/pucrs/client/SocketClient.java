package br.com.pucrs.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class SocketClient extends Thread {
    private static int port = 5000;
    private Socket socket;
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;
    String hash = "";
    String archive;

    @lombok.SneakyThrows
    @Override
    public void run() {
        boolean aux=true;
        while (aux){
            aux=false;
            try{
                socket = new Socket(InetAddress.getLocalHost().getHostAddress(),port);
            }catch(Exception ignore){
                aux = true;
                port++;
            }
        }
        try{
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
            hash = (String) inputStream.readObject(); // <hashcode> <- pattern

            try{
                archive = hashCodes.get(hash);
            }catch (Exception e){
                System.out.println(e);
            }

            outputStream.writeObject(archive);
            outputStream.flush();

        }catch (Exception e){
            System.out.println(e);
        }
        outputStream.close();
        inputStream.close();
        socket.close();
    }
}
