package br.com.pucrs.remote.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteServerApi extends Remote {

    List<String> listConnections() throws RemoteException;

    boolean heartbeat(String connection) throws RemoteException;

}
//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