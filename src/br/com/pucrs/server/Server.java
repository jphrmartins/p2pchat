package br.com.pucrs.server;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Server {

    public static void main(String[] args) throws RemoteException, MalformedURLException, AlreadyBoundException {
        ServerOperationsApi serverOperations = new ServerOperationsApi();

        Thread heartBeat = new Thread(serverOperations);
        heartBeat.start();

        LocateRegistry.createRegistry(1099);
        Naming.bind("Server", serverOperations);
        System.out.println("Server UP");
    }
}
