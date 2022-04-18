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

    /*@Todo Terminar o tratamento aqui
    //Todo o 1 e 2 deixa que eu faço..
    //todo ideia do fluxo ideial:
    *    Me conecto com o servidor, mando meus arquivos pra la
    *    Pergunto pra fazer a pesquisa do arquivo x
    *    Servidor me devolve todos os arquivos que ele encontrou com o padrão x
    *    Pergunto pro servidor todos que tem todos os peers que tem o hash
    *    Servidor devolve o username dos peer que tem o hash
    *    Escolho um peer,
            solicito ao servidor os dados de coneção do peer escolhido.
            servidor devolve peerConection com host e port.
            Abro socket com o peer.
            Mando o hash por parametro
            recebo arquivo.
         Fim
    *
    */
    public void menu() throws RemoteException {
        int opt;

        do {
            System.out.print("##--               Menu              --##\n\n");
            System.out.print("|---------------------------------------|\n");
            System.out.print("| 1 - Request resource list             |\n");
            System.out.print("| 2 - Request resource list             |\n");
            System.out.print("| 3 - Request resource from server      |\n");
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
                    requestResources();
                    break;
                case 3:
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

    private void disconnect() {

    }

    public void requestResourcesList() throws RemoteException {
        String allResources = remoteServerApi.listResources(peerConnection).stream()
                .map(it -> it.getFileName() + " " + it.getHash())
                .collect(Collectors.joining(",\n"));

        System.out.println("lista de todos os recursos disponível em rede: ");
        System.out.println(allResources);
    }

    public void requestResources() throws RemoteException {
        System.out.println("Enter the resource name: ");
        String resourceName = input.next();
        List<String> peerUserNames = remoteServerApi.getPeerNamesForResource(resourceName);
        System.out.println("Segue lista de usuários que possui o recurso :" + resourceName);
        String users = String.join(",\n", peerUserNames);
        System.out.println(users);
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

        Optional<PeerConnection> peer = remoteServerApi.getConnection(username);

        if (peer.isPresent()) {
            SocketClientRequest socketClientRequest = new SocketClientRequest(peer.get());
            socketClientRequest.getArchive(hashcode,resouceName);
        } else {
            System.out.println("Client " + username + " does not exists");
        }
        /*@Todo contatar outro peer, deve ser passado o ip e a porta do peer e o hashcode do recurso que desejamos receber
        / passo é... dizer quem é o peer vai contatar (não o ip o username mesmo) Comando pode ser (3 julia hash)
        / buscar no servidor a connection pra aquele peer.
        / Abrir conexão pro peer e solicitar o hash
         */
    }

}
