package br.com.pucrs.server;

import br.com.pucrs.remote.api.PeerConnection;

import java.util.HashMap;
import java.util.Map;

public class ServerHeartbeat extends Thread {

    private Map<PeerConnection, Integer> heartBeats;


    public ServerHeartbeat() {
        this.heartBeats = new HashMap<>();
    }

    @Override
    public void run() {
        while (true) {
            heartBeats.forEach((peerConnection, integer) -> {
                heartBeats.put(peerConnection, integer-1);
            });
        }
    }
}
