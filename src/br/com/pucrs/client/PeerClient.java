package br.com.pucrs.client;

import br.com.pucrs.remote.api.RemoteServerApi;
import br.com.pucrs.remote.api.PeerConnection;
import br.com.pucrs.remote.api.ResourceInfo;

import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

public class PeerClient {
    private final RemoteServerApi remoteServerApi;
    private final Heartbeat heartbeat;
    private final PeerConnection peerConnection;
    private final Scanner input;
    private final ArchiveRpository archiveRpository;

    protected PeerClient(RemoteServerApi remoteServerApi, PeerConnection peerConnection,
                         Heartbeat heartbeat, ArchiveRpository archiveRpository) {
        this.remoteServerApi = remoteServerApi;
        this.input = new Scanner(System.in);
        this.heartbeat = heartbeat;
        this.archiveRpository = archiveRpository;
        this.peerConnection = peerConnection;
    }

    //Só aviso mesmo -> não é thread, roda na main.
    public void start() throws RemoteException {
        System.out.println("Client will start running on " + peerConnection);
        boolean connected = connectOnRemoteServer();
        if (connected) {
            heartbeat.start();
            menu();
        } else {
            throw new RuntimeException("Error trying to connect on remote server");
        }
    }

    private boolean connectOnRemoteServer() throws RemoteException {
        List<ResourceInfo> resourceInfos = archiveRpository.getAllResources();
        return remoteServerApi.connect(peerConnection, resourceInfos);
    }

    public void menu() throws RemoteException {
        int opt;

        do {
            System.out.print("##--               Menu              --##\n\n");
            System.out.print("|---------------------------------------|\n");
            System.out.print("| 1 - Request all resource list         |\n");
            System.out.print("| 2 - Search resource on server         |\n");
            System.out.print("| 3 - Get peer for resource             |\n");
            System.out.print("| 4 - Contact other peer                |\n");
            System.out.print("| 5 - Exit                              |\n");
            System.out.print("|---------------------------------------|\n");

            opt = input.nextInt();

            switch (opt) {
                case 1:
                    requestResourcesList();
                    System.out.println("");
                    break;
                case 2:
                    searchResourceOnServer();
                    break;
                case 3:
                    searchPeersForResource();
                    break;
                case 4:
                    contactOtherPeer();
                    break;
                case 5:
                    System.out.println("Disconectando cliente");
                    disconnect();
                    break;
                default:
                    System.out.println();
                    break;
            }
        } while (opt != 5);
    }

    private void searchResourceOnServer() throws RemoteException {
        System.out.println("Escreva o termo para fazer a pesquisa do seu arquivo: ");
        String fileSearch = input.next();
        List<ResourceInfo> resourceInfo = remoteServerApi.search(peerConnection, fileSearch);
        if (!resourceInfo.isEmpty()) {
            String resources = resourceInfo.stream()
                    .map(it -> it.getFileName() + " " + it.getHash())
                    .collect(Collectors.joining(", \n"));
            System.out.println("Lista de recursos encontrados: ");
            System.out.println(resources);
        } else {
            System.out.println("Nenhum recurso encontrado.");
        }
    }

    private void disconnect() throws RemoteException {
        boolean disconnected = remoteServerApi.disconnect(peerConnection);
        if (disconnected) {
            heartbeat.disconnect();
            System.out.println("user disconnected");
        } else {
            System.out.println("User already disconnected");
        }
    }

    public void requestResourcesList() throws RemoteException {
        Set<ResourceInfo> resourceInfo = remoteServerApi.listResources(peerConnection);
        if (!resourceInfo.isEmpty()) {
            String allResources = remoteServerApi.listResources(peerConnection).stream()
                    .map(it -> it.getFileName() + " " + it.getHash())
                    .collect(Collectors.joining(",\n"));

            System.out.println("lista de todos os recursos disponível em rede: ");
            System.out.println(allResources);
        } else {
            System.out.println("Nenhum recurso disponível");
        }
    }

    public void searchPeersForResource() throws RemoteException {
        System.out.println("Enter the resource name: ");
        String resourceName = input.next();
        List<String> peerUserNames = remoteServerApi.getPeerNamesForResource(peerConnection, resourceName);

        if (!peerUserNames.isEmpty()) {
            System.out.println("Segue lista de usuários que possui o recurso: " + resourceName);
            String users = String.join(",\n", peerUserNames);
            System.out.println(users);
        } else {
            System.out.println("Nenhum peer disponível para esse recurso");
        }
    }

    public void contactOtherPeer() throws RemoteException {
        String username;
        String hashcode;
        System.out.println("Enter the peer username :");
        username = input.next();
        System.out.println("Enter the resource name: ");
        String resouceName = input.next();
        System.out.println("Enter the hash code of the resource: ");
        hashcode = input.next();

        PeerConnection peer = remoteServerApi.getConnection(username);

        if (peer != null) {
            SocketClientRequest socketClientRequest = new SocketClientRequest(peer, archiveRpository);
            socketClientRequest.getArchive(hashcode,resouceName);
        } else {
            System.out.println("Client " + username + " does not exists");
        }
    }

}
