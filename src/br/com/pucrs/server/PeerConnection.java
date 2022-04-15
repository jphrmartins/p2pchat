package br.com.pucrs.server;

public class PeerConnection {
    private final String address;
    private final String port;

    private final String userName;

    public PeerConnection(String address, String port, String userName) {
        this.address = address;
        this.port = port;
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public String getPort() {
        return port;
    }

    public String getUserName() {
        return userName;
    }
}
