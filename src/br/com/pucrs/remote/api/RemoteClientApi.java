package br.com.pucrs.remote.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Objects;

public interface RemoteClientApi extends Remote {

    boolean receiveMessage(String message) throws RemoteException ;

}
