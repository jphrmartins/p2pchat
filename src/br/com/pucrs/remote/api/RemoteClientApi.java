package br.com.pucrs.remote.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteClientApi extends Remote {

    boolean receiveMessage(String message) throws RemoteException;
}
