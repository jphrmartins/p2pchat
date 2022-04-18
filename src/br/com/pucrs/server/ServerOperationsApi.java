package br.com.pucrs.server;

import br.com.pucrs.remote.api.PeerConnection;
import br.com.pucrs.remote.api.RemoteServerApi;
import br.com.pucrs.remote.api.ResourceInfo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Collectors;

public class ServerOperationsApi extends UnicastRemoteObject implements RemoteServerApi, Runnable {

    private static final long serialVersion = 1L;
    private static final int TIME_TO_LEAVE_SECONDS = 10;
    private final Map<PeerConnection, List<ResourceInfo>> connections;
    private final Map<PeerConnection, Integer> heartBeatConnections;

    public ServerOperationsApi() throws RemoteException {
        this.connections = new HashMap<>();
        this.heartBeatConnections = new HashMap<>();
    }

    @Override
    public Set<ResourceInfo> listResources(PeerConnection peerConnection) throws RemoteException {
        return getUniqueResources(peerConnection);

    }

    @Override
    public List<ResourceInfo> search(PeerConnection peerConnection,
                                     String name) throws RemoteException {
        return getUniqueResources(peerConnection).stream()
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
    public List<String> getPeerNamesForResource(PeerConnection requestConnection,
                                                String resourceName) throws RemoteException {
        return connections.entrySet()
                .stream()
                .filter(connections -> !connections.getKey().equals(requestConnection))
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
        System.out.println("Recevied heartbeat of " + connection.toString());
        Optional<Integer> ttl = Optional.ofNullable(heartBeatConnections.get(connection));
        ttl.ifPresent(it -> heartBeatConnections.put(connection, TIME_TO_LEAVE_SECONDS));
        return ttl.isPresent();
    }

    @Override
    public boolean connect(PeerConnection connection, List<ResourceInfo> resourceInfo) throws RemoteException {
        if (connections.containsKey(connection) || heartBeatConnections.containsKey(connection)) {
            return false;
        } else {
            heartBeatConnections.put(connection, TIME_TO_LEAVE_SECONDS);
            connections.put(connection, resourceInfo);
            return true;
        }
    }

    @Override
    public boolean disconnect(PeerConnection connection) throws RemoteException {
        if (!(connections.containsKey(connection) && heartBeatConnections.containsKey(connection))) {
            return false;
        }
        connections.remove(connection);
        heartBeatConnections.remove(connection);
        return true;
    }

    private Set<ResourceInfo> getUniqueResources(PeerConnection peerConnection) {
        return connections.entrySet()
                .stream()
                .filter(it -> !it.getKey().equals(peerConnection))
                .flatMap(it -> it.getValue().stream())
                .collect(Collectors.toSet());
    }

    private void removeConnection(PeerConnection peerConnection) {
        heartBeatConnections.remove(peerConnection);
        connections.remove(peerConnection);
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (!heartBeatConnections.isEmpty()) {
                    String connectionsToBeat = heartBeatConnections.entrySet()
                                    .stream().map(it -> "(" + it.getKey().getUserName() + " " + it.getValue() + ")")
                                    .collect(Collectors.joining(", "));
                    System.out.println("Running heartbeat for connections: " + connectionsToBeat);
                    Set<Map.Entry<PeerConnection, Integer>> heartbeats = heartBeatConnections.entrySet();
                    for (Map.Entry<PeerConnection, Integer> heartBeat :heartbeats) {
                        PeerConnection peerConnection = heartBeat.getKey();
                        Integer ttl = heartBeat.getValue();
                        if (ttl - 1 == 0) {
                            removeConnection(peerConnection);
                        } else {
                            heartBeatConnections.put(peerConnection, ttl - 1);
                        }
                    }
                }
                System.out.println("Will take a nap"); //@Todo remover
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
