package br.com.pucrs.client;

import br.com.pucrs.remote.api.RemoteServerApi;
import br.com.pucrs.remote.api.PeerConnection;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ClientApp {

    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException, InterruptedException {
        if (args.length != 5) {
            System.out.println("Must pass the current name, localId, ServerIp and ServerPort");
            System.out.println("Java ClientApp <userName> <LocalId> <ServerIp> <ServerPort> <directoryOfFile>");
        }
        RemoteServerApi remoteServerApi = (RemoteServerApi) Naming.lookup("//" + args[2] + ":" + args[3] + "/Server");
        ArchiveRpository archiveRpository = new ArchiveRpository(args[4]);
        SocketClientListener socketClientListener = new SocketClientListener(archiveRpository);
//        socketClientListener.start();
//        while (!socketClientListener.isConnected()) {
//            Thread.sleep(2000);
//            System.out.println("Still not connect will sleep a little");
//        }
        String username = args[0];
        String currentIpAddress = args[1];
        int port = socketClientListener.getPort();
        PeerConnection peerConnection = new PeerConnection(currentIpAddress, Integer.toString(port), username);
        Heartbeat heartbeat = new Heartbeat(remoteServerApi, peerConnection);
        PeerClient client = new PeerClient(remoteServerApi, peerConnection, heartbeat, archiveRpository);

        client.start(); // <- Not a thread. Just main loop on while...
        socketClientListener.interrupt();

    }
}
