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
            System.out.print("| 2 - Request resource from server      |\n");
            System.out.print("| 3 - Contact other peer                |\n");
            System.out.print("| 4 - Exit                              |\n");
            System.out.print("|---------------------------------------|\n");

            opt = input.nextInt();

            switch (opt) {
                case 1:
                    requestResources();
                    System.out.println("");
                    break;
                case 2:
                    requestResources();
                    break;
                case 3:
                    break;
                case 4:
                    System.exit(0);
            }
        } while (opt != 4);
    }

    public String requestResourcesList() throws RemoteException {
        return remoteServerApi.listResources(peerConnection).stream()
                .map(it -> it.getFileName() + " " + it.getHash())
                .collect(Collectors.joining(",\n"));
    }

    public String requestResources() throws RemoteException {
        System.out.println("Enter the resource name: ");
        List<String> peerUserNames = remoteServerApi.getPeerNamesForResource(input.next());
        return String.join(",\n", peerUserNames);
    }

    public void contactOtherPeer() {
        String ip;
        String hashcode;
        System.out.println("Enter the IP :");
        ip = input.next();
        System.out.println("Enter the hash code of the resource: ");
        hashcode = input.next();


        /*@Todo contatar outro peer, deve ser passado o ip e a porta do peer e o hashcode do recurso que desejamos receber
        / passo é... dizer quem é o peer vai contatar (não o ip o username mesmo) Comando pode ser (3 julia hash)
        / buscar no servidor a connection pra aquele peer.
        / Abrir conexão pro peer e solicitar o hash
         */
    }


}
