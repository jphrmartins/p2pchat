package br.com.pucrs.client;

import br.com.pucrs.remote.api.RemoteServerApi;

import java.rmi.RemoteException;
import java.util.*;

public class PeerClient extends Thread {
    private static RemoteServerApi remoteServerApi;
    private int port;

    private static Scanner input = new Scanner(System.in);

    protected PeerClient(RemoteServerApi remoteServerApi, int port) {
        this.remoteServerApi = remoteServerApi;
        this.port = port;
    }

    @Override
    public void run() {
        System.out.println("RODANDO");
        //@Todo Fazer os metodos pra mostrar o menuzinho e tudo mais + thread de heartBeat..

    }

    public static void menu() throws RemoteException {

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

            switch (opt){
                case 1: requestResources();
                    System.out.println("");
                    break;
                case 2: requestResources();
                    break;
                case 3:
                    break;
                case 4: System.exit(0);
            }
        }while (opt!=4);
    }

    public static String requestResourcesList() throws RemoteException {
        return remoteServerApi.listResources().toString();
        //@Todo Os peers podem solicitar uma lista de recursos (nomes dos arquivos strings de identificacao, IPs dos
        // peers que contem os recursos e hashes)
        //ao servidor ou um recurso espececifico.
    }
    public static String requestResources() throws RemoteException {
        System.out.println("Enter the resource name: ");
        return remoteServerApi.getPeerNamesForResource(input.next()).toString();
    }

    public static void contactOtherPeer(){
        String ip;
        String hashcode;
        System.out.println("Enter the IP :");
        ip = input.next();
        System.out.println("Enter the hash code of the resource: ");
        hashcode = input.next();


        //@Todo contatar outro peer, deve ser passado o ip e a porta do peer e o hashcode do recurso que desejamos receber
    }


}
