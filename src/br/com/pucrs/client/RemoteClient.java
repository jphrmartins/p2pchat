package br.com.pucrs.client;

import br.com.pucrs.remote.api.RemoteServerApi;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class RemoteClient extends UnicastRemoteObject implements RemoteClientApi {
    private static final long serialVersion = 1L;
    private RemoteServerApi remoteServerApi;

    protected RemoteClient(RemoteServerApi remoteServerApi) throws RemoteException {
        this.remoteServerApi = remoteServerApi;
    }


    @Override
    public boolean receiveMessage(String message) throws RemoteException {
        return false;
    }

    public HashMap<Integer,String> readArchives(String directory){
        File dir = new File(directory);
        File[] files = Objects.requireNonNull(dir.listFiles());
        String message = "";
        HashMap<Integer,String> hashCodes = new HashMap<>();
        for (File file : files){
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    message = message + line;
                }
            } catch (IOException e) {
                System.err.format("Error while reading archive. ", e);
            }
            hashCodes.put(message.hashCode(),message);
            message = "";
        }
        return hashCodes;
    }

    public static void main(String[] args) {
        
    }
}
