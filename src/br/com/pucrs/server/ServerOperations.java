package br.com.pucrs.server;

import br.com.pucrs.remote.api.RemoteServerApi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerOperations extends UnicastRemoteObject implements RemoteServerApi {

    private Map<ClientConnection, ResourceInfo> connections;
    private HeartbeatValidation validation;

    public ServerOperations(HeartbeatValidation validation) throws RemoteException {
        this.connections = new HashMap<>();
        this.validation = validation;
    }


    @Override
    public List<String> listResources() throws RemoteException {
        return new ArrayList<>();
    }

    @Override
    public boolean heartbeat(String connection) throws RemoteException {
        return true;
    }
}
