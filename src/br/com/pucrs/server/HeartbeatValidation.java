package br.com.pucrs.server;

import java.util.HashMap;
import java.util.Map;

public class HeartbeatValidation extends Thread {

    private Map<ClientConnection, Integer> heartBeats;


    public HeartbeatValidation() {
        this.heartBeats = new HashMap<>();
    }

    @Override
    public void run() {
        while (true) {
            heartBeats.forEach((clientConnection, integer) -> {
                heartBeats.put(clientConnection, integer-1);
            });
        }
    }
}
