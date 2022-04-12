package br.com.pucrs.server;

public class ClientConnection {
    private final String address;
    private final String port;

    public ClientConnection(String address, String port) {
        this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public String getPort() {
        return port;
    }
}
