package br.com.pucrs.server;

import br.com.pucrs.remote.api.RemoteServerApi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Collectors;

public class ServerOperationsApi extends UnicastRemoteObject implements RemoteServerApi, Runnable {
    private static final int TIME_TO_LEAVE_SECONDS = 10;
    private final Map<PeerConnection, List<ResourceInfo>> connections;
    private final Map<PeerConnection, Integer> heartBeat;

    public ServerOperationsApi() throws RemoteException {
        this.connections = new HashMap<>();
        this.heartBeat = new HashMap<>();
    }

    @Override
    public Set<ResourceInfo> listResources() throws RemoteException {
        return getUniqueResources();

    }

    @Override
    public List<ResourceInfo> search(String name) throws RemoteException {
        return getUniqueResources().stream()
                .filter(it -> it.getFileName().startsWith(name))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PeerConnection> getConnection(String peerName) throws RemoteException {
        return connections.keySet().stream()
                .filter(connection -> connection.getUserName().equals(peerName))
                .findFirst();
    }

    @Override
    public List<String> getPeerNamesForResource(String resourceName) throws RemoteException {
        return connections.entrySet()
                .stream()
                .filter(entry -> {
                    List<ResourceInfo> resourceInfo = entry.getValue();
                    return resourceInfo.stream().anyMatch(it -> it.getFileName().equals(resourceName));
                })
                .map(Map.Entry::getKey)
                .map(PeerConnection::getUserName)
                .collect(Collectors.toList());
    }

    @Override
    public boolean heartbeat(PeerConnection connection) throws RemoteException {
        Optional<Integer> ttl = Optional.ofNullable(heartBeat.get(connection));
        ttl.ifPresent(it -> heartBeat.put(connection, TIME_TO_LEAVE_SECONDS));
        return ttl.isPresent();
    }

    @Override
    public boolean connect(PeerConnection connection, List<ResourceInfo> resourceInfo) throws RemoteException {
        if (connections.containsKey(connection) || heartBeat.containsKey(connection)) {
            return false;
        } else {
            heartBeat.put(connection, TIME_TO_LEAVE_SECONDS);
            connections.put(connection, resourceInfo);
            return true;
        }
    }

    private Set<ResourceInfo> getUniqueResources() {
        return connections.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
    }

    private void removeConnection(PeerConnection peerConnection) {
        heartBeat.remove(peerConnection);
        connections.remove(peerConnection);
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (!heartBeat.isEmpty()) {
                    heartBeat.forEach((peerConnection, ttl) -> {
                        if (ttl - 1 == 0) {
                            removeConnection(peerConnection);
                        } else {
                            heartBeat.put(peerConnection, ttl - 1);
                        }
                    });
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
