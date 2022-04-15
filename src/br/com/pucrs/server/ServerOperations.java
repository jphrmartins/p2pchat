package br.com.pucrs.server;

import java.util.HashMap;
import java.util.Map;

public class ServerOperations extends Thread {

    private Map<PeerConnection, Integer> heartBeats;


    public ServerOperations() {
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
