package br.com.pucrs.client;

import br.com.pucrs.remote.api.RemoteServerApi;
import br.com.pucrs.remote.api.PeerConnection;

import java.rmi.RemoteException;

public class Heartbeat extends Thread {
    private final PeerConnection peerConnection;
    private final RemoteServerApi remoteServerApi;

    public Heartbeat(RemoteServerApi remoteServerApi, PeerConnection peerConnection) {
        this.remoteServerApi = remoteServerApi;
        this.peerConnection = peerConnection;
    }

    public void run() {
        while (true) {
            try {
                System.out.println("pulse heartbeat");
                remoteServerApi.heartbeat(peerConnection);
                Thread.sleep(3000);
            } catch (RemoteException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
