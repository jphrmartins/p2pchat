package br.com.pucrs.client;

import br.com.pucrs.remote.api.RemoteServerApi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ClientApp {

    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException, InterruptedException {
        if (args.length <= 1) {
            System.out.println("Must pass the server host and port of the central server");
        }
        RemoteServerApi remoteServerApi = (RemoteServerApi) Naming.lookup("//" + args[0] + ":" + args[1] + "/Server");

        ArchiveRpository archiveRpository = new ArchiveRpository(".");
        SocketClient socketClient = new SocketClient(archiveRpository);
        socketClient.start();
        while (!socketClient.isConnected()) {
            Thread.sleep(2000);
            System.out.println("Still not connect will sleep a little");
        }
        PeerClient client = new PeerClient(remoteServerApi, socketClient.getPort());

        client.start();
    }
}
