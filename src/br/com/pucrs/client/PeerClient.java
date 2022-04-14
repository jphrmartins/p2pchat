package br.com.pucrs.client;

import br.com.pucrs.remote.api.RemoteServerApi;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PeerClient extends Thread {
    private RemoteServerApi remoteServerApi;
    private int port;

    protected PeerClient(RemoteServerApi remoteServerApi, int port) {
        this.remoteServerApi = remoteServerApi;
        this.port = port;
    }

    @Override
    public void run() {
        System.out.println("RODANDO");
        //@Todo Fazer os metodos pra mostrar o menuzinho e tudo mais + thread de heartBeat..
    }


}
