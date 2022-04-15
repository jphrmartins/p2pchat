package br.com.pucrs.remote.api;

import br.com.pucrs.server.PeerConnection;
import br.com.pucrs.server.ResourceInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RemoteServerApi extends Remote {

    Set<ResourceInfo> listResources() throws RemoteException;

    List<ResourceInfo> search(String name) throws RemoteException;

    Optional<PeerConnection> getConnection(String peerName) throws RemoteException;

    List<String> getPeerNamesForResource(String resourceName) throws RemoteException;

    boolean heartbeat(PeerConnection connection) throws RemoteException;

    boolean connect(PeerConnection connection, List<ResourceInfo> resourceInfo) throws RemoteException;

}
