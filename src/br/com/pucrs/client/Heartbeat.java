import br.com.pucrs.remote.api.RemoteServerApi;

import java.io.*;
import java.net.*;
import java.util.*;

public class Heartbeat extends Thread {
    protected String addr = null;
    protected byte[] data = new byte[1024];
    protected int port;
    private RemoteServerApi remoteServerApi;

    public Heartbeat(RemoteServerApi remoteServerApi, String addr, int port) throws UnknownHostException {
        this.remoteServerApi = remoteServerApi;
        data = ("heartbeat " + InetAddress.getLocalHost().getHostAddress()).getBytes();
        this.addr = addr;
        this.port = port;
    }

    public void run() {
        while (true) {
            try {

            } catch (Exception e) {
            }

            try {
                Thread.sleep(3000);
            } catch(InterruptedException e) {
            }
//			System.out.println("\npulse!");
        }
    }
}
