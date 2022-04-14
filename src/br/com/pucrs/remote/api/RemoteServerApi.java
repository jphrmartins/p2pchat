package br.com.pucrs.remote.api;

import br.com.pucrs.server.ClientConnection;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteServerApi extends Remote {

    List<String> listResources() throws RemoteException;

    List<String> search(String name) throws RemoteException;

    ClientConnection getConnection(String peerName)  throws RemoteException;


    boolean heartbeat(String connection) throws RemoteException;

}
//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