package br.com.pucrs.client;

import br.com.pucrs.remote.api.RemoteClientApi;
import br.com.pucrs.remote.api.RemoteServerApi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteClient extends UnicastRemoteObject implements RemoteClientApi {
    private static final long serialVersion = 1L;
    private RemoteServerApi remoteServerApi;

    protected RemoteClient(RemoteServerApi remoteServerApi) throws RemoteException {
        this.remoteServerApi = remoteServerApi;
    }


    @Override
    public boolean receiveMessage(String message) throws RemoteException {
        return false;
    }

    public static void main(String[] args) {
        
    }
}
